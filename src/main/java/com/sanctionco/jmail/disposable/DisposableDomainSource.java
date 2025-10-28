package com.sanctionco.jmail.disposable;

import java.io.IOException;

/**
 * <p>A {@code DisposableDomainSource} is used as a source of truth for which domains are considered
 * disposable or temporary.
 *
 * <p>It is required when adding the {@code disallowDisposableDomains()} rule to your
 * {@link com.sanctionco.jmail.EmailValidator} in order to consider email addresses that use a
 * disposable domain as invalid.
 *
 * <p>Currently, there are two types of {@code DisposableDomainSource}: {@link FileSource} and
 * {@link IsTempMailAPISource}. Both of these can be instantiated via static methods on this class:
 * {@link #file(String)} and {@link #isTempMailAPI(String)} respectively.
 *
 * <p>Additionally, you can easily create your own {@code DisposableDomainSource} by writing a
 * class that implements {@code DisposableDomainSource}.
 */
public interface DisposableDomainSource {

  /**
   * Determine if the given domain is a disposable domain.
   *
   * @param domain the domain to check
   * @return {@code true} if the domain is a disposable domain, or {@code false} if not
   */
  boolean isDisposableDomain(String domain);

  /**
   * <p>Create and return a new {@link FileSource}, which can be used as a
   * {@code DisposableDomainSource} that uses a file as the source of disposable domains.
   *
   * <p>The file should have each disposable domain on its own line in the file.
   *
   * @param path the path to the file containing disposable domains
   * @return a new instance of {@link FileSource}
   * @throws IOException if the file at the given path does not exist or there is an issue reading
   *                     the file
   */
  static DisposableDomainSource file(String path) throws IOException {
    return new FileSource(path);
  }

  /**
   * <p>Create and return a new {@link IsTempMailAPISource}, which can be used as a
   * {@code DisposableDomainSource} that uses the
   * <a href="https://www.istempmail.com">IsTempMail API</a> as the source of disposable domains.
   *
   * @param apiKey the API key to use when making requests to the IsTempMail API
   * @return a new instance of {@link IsTempMailAPISource}
   */
  static DisposableDomainSource isTempMailAPI(String apiKey) {
    return new IsTempMailAPISource(new HttpClient(), apiKey);
  }
}
