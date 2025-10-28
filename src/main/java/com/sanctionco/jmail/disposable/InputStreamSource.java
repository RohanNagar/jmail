package com.sanctionco.jmail.disposable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;

/**
 * An implementation of {@link DisposableDomainSource} that loads domains from an input stream.
 * <p>
 * The input stream must contain lowercase domains, one per line.
 * All domains are read into memory and stored in an unmodifiable set for fast lookup.
 *
 * @see DisposableDomainSource
 */
public class InputStreamSource implements DisposableDomainSource {
  private final Set<String> disposableDomains;

  /**
   * Reads all domains (one per line) from the given input stream into memory.
   * <p>
   * The input stream is not closed by this constructor,
   * so the caller is responsible for closing it.
   * Once the instance is created, the input stream is no longer used.
   *
   * @param inputStream the input stream containing disposable domains
   * @throws IOException if the input stream is closed or cannot be read
   */
  InputStreamSource(InputStream inputStream) throws IOException {
    this.disposableDomains = readAllLines(inputStream);
  }

  @Override
  public boolean isDisposableDomain(String domain) {
    return this.disposableDomains.contains(domain);
  }

  private static Set<String> readAllLines(InputStream inputStream) throws IOException {
    // not closing the InputStreamReader nor BufferedReader - they will be garbage collected later
    Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    BufferedReader bufferedReader = new BufferedReader(reader);

    try {
      return bufferedReader.lines()
          .map(String::trim)
          .filter(s -> !s.isEmpty())
          .collect(toUnmodifiableSet());
    } catch (UncheckedIOException e) {
      // As documented by BufferedReader.lines(), IOException is wrapped in UncheckedIOException.
      // Rethrow the original IOException.
      throw e.getCause();
    }
  }

  // As JMail supports java 8, then having this copy of Collectors.toUnmodifiableSet() from java 10+
  private static <T> Collector<T, HashSet<T>, Set<T>> toUnmodifiableSet() {
    return Collector.of(
        HashSet::new,
        Set::add,
        (left, right) -> {
          left.addAll(right);
          return left;
        },
        Collections::unmodifiableSet
    );
  }
}
