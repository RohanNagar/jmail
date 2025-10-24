package com.sanctionco.jmail.disposable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>An implementation of {@link DisposableDomainSource} that uses an input stream as the source
 * for disposable domains.
 *
 * <p>The input stream should contain a list of disposable lowercase domains where each domain is on its own
 * line.
 *
 * @see DisposableDomainSource
 */
public class InputStreamSource implements DisposableDomainSource {
  private final Set<String> disposableDomains;

  /**
   * Create a new instance of {@code InputStreamSource} from input stream.
   * <p>
   * Closes the input stream.
   *
   * @param inputStream the input stream that contains disposable domains. Will be closed by this method
   * @throws IOException if the input stream was closed or any other issue occurred reading the input stream
   */
  InputStreamSource(InputStream inputStream) throws IOException {
    this.disposableDomains = Collections.unmodifiableSet(
        new HashSet<>(readAllLines(inputStream)));
  }

  @Override
  public boolean isDisposableDomain(String domain) {
    return this.disposableDomains.contains(domain);
  }

  private static List<String> readAllLines(InputStream inputStream) throws IOException {
    try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
         BufferedReader bufferedReader = new BufferedReader(reader)
    ) {
      return bufferedReader.lines()
          .map(String::trim)
          .filter(s -> !s.isEmpty())
          .collect(Collectors.toList());
    } catch (UncheckedIOException e) {
      // operating on bufferedReader.lines() wraps underlying IOException in UncheckedIOException
      throw e.getCause();
    }
  }
}
