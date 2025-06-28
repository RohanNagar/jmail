package com.sanctionco.jmail.disposable;

/**
 * <p>An implementation of {@link DisposableDomainSource} that uses the
 * <a href="https://www.istempmail.com">IsTempMail API</a> as the source for disposable domains.
 *
 * <p>You must register with IsTempMail to get an API key that you would use for your requests
 * to the API.
 *
 * <p>Note that usage of this {@link DisposableDomainSource} in your
 * {@link com.sanctionco.jmail.EmailValidator} implies that every single address being validated
 * will make an API request to the IsTempMail API. In production, you will likely want to have
 * some caching of results from the API to reduce network calls and time spend making HTTP requests.
 * This class does not do any caching of its own so that control of any cache can remain within
 * your own codebase.
 *
 * <p>To do such caching, the recommended approach is to create your own
 * {@link DisposableDomainSource} that wraps this source and performs caching, and then use that
 * class in your {@link com.sanctionco.jmail.EmailValidator}. An example using a simple
 * {@link java.util.concurrent.ConcurrentHashMap} is given below.
 *
 * <pre>
 * {@code
 * public class CacheWrappedDisposableDomainSource implements DisposableDomainSource {
 *   private final DisposableDomainSource wrappedSource;
 *   private final Map<String, Boolean> cache;
 *
 *   CacheWrappedDisposableDomainSource(DisposableDomainSource wrappedSource) {
 *     this.wrappedSource = wrappedSource;
 *     this.cache = new ConcurrentHashMap<>();
 *   }
 *
 *   public boolean isDisposableDomain(String domain) {
 *     return cache.computeIfAbsent(domain, wrappedSource::isDisposableDomain);
 *   }
 * }
 * }
 * </pre>
 *
 * @see DisposableDomainSource
 */
public class IsTempMailAPISource implements DisposableDomainSource {
  private static final String BASE_URL = "https://istempmail.com/api/check/";

  private final HttpClient httpClient;
  private final String apiKey;

  /**
   * Create a new instance of {@code IsTempMailAPISource}.
   *
   * @param httpClient the client used to make HTTP requests
   * @param apiKey     the IsTempMail API key to use when making requests to the API
   */
  IsTempMailAPISource(HttpClient httpClient, String apiKey) {
    this.httpClient = httpClient;
    this.apiKey = apiKey;
  }

  @Override
  public boolean isDisposableDomain(String domain) {
    String url = BASE_URL + apiKey + "/" + domain;
    String httpResponse = httpClient.get(url);

    return parseBlockedValue(httpResponse);
  }

  private boolean parseBlockedValue(String s) {
    // Assume the response is false by default
    boolean resp = false;

    // Parse the value with string manipulation since it's faster than regex (by up to 10x!)
    if (s.contains("blocked")) {
      int keyIndex = s.indexOf("blocked");

      // Find the first colon after "blocked"
      int colonIndex = s.indexOf(":", keyIndex);

      if (colonIndex != -1) {
        // Find the start of the value
        int valueStart = colonIndex + 1;

        // Skip whitespace
        while (valueStart < s.length() && Character.isWhitespace(s.charAt(valueStart))) {
          valueStart++;
        }

        // Read until next comma or closing brace
        int valueEnd = valueStart;
        while (valueEnd < s.length()
            && s.charAt(valueEnd) != ','
            && s.charAt(valueEnd) != '}') {
          valueEnd++;
        }

        String rawValue = s.substring(valueStart, valueEnd).trim();
        resp = "true".equals(rawValue);
      }
    }

    return resp;
  }
}
