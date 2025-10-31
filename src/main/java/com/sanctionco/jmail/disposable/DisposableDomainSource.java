package com.sanctionco.jmail.disposable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p>A {@code DisposableDomainSource} is used as a source of truth for which domains are considered
 * disposable or temporary.
 *
 * <p>It is required when adding the {@code disallowDisposableDomains()} rule to your
 * {@link com.sanctionco.jmail.EmailValidator} in order to consider email addresses that use a
 * disposable domain as invalid.
 *
 * <p>Currently, there are two types of {@code DisposableDomainSource}: {@link InputStreamSource}
 * and {@link IsTempMailAPISource}. Both of these can be instantiated via static methods on this
 * class: {@link #inputStream(InputStream)} and {@link #isTempMailAPI(String)} respectively. There
 * is an additional static method {@link #file(String)} used to instantiate a
 * {@code DisposableDomainSource} from a file, which uses an {@link InputStreamSource} underneath.
 *
 * <p>Additionally, you can easily create your own {@code DisposableDomainSource} by writing a
 * class that implements {@code DisposableDomainSource}.
 */
public interface DisposableDomainSource {

  /**
   * Determine if the given domain is a disposable domain. Domains are compared in a case
   * in-sensitive way, so calling this method with both {@code example-disposable-domain.com}
   * and {@code EXAMPLE-DISPOSABLE-DOMAIN.COM} should return the same result.
   *
   * @param domain the domain to check
   * @return {@code true} if the domain is a disposable domain, or {@code false} if not
   */
  boolean isDisposableDomain(String domain);

  /**
   * <p>Create and return a new {@code DisposableDomainSource} which uses a file as the source
   * of disposable domains.
   *
   * <p>The file should have each disposable domain on its own line in the file.
   *
   * @param path the path to the file containing disposable domains
   * @return a new instance of {@code DisposableDomainSource}
   * @throws IOException if the file at the given path does not exist or there is an issue reading
   *                     the file
   */
  static DisposableDomainSource file(String path) throws IOException {
    try (InputStream in = Files.newInputStream(Paths.get(path))) {
      return new InputStreamSource(in);
    }
  }

  /**
   * <p>Create and return a new {@link InputStreamSource}, which can be used as a
   * {@code DisposableDomainSource} that uses an input stream as the source of disposable domains.
   * <p>
   * The input stream is not closed by this method, so the caller is responsible for closing it.
   * Once the InputStreamSource instance is created, the input stream is no longer used by it.
   *
   * @param inputStream the input stream containing disposable domains. Will not be closed.
   * @return a new instance of {@link InputStreamSource}
   * @throws IOException if the input stream is already closed or cannot be read
   */
  static DisposableDomainSource inputStream(InputStream inputStream) throws IOException {
    return new InputStreamSource(inputStream);
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
