package com.sanctionco.jmail.disposable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>An implementation of {@link DisposableDomainSource} that uses a file as the source
 * for disposable domains.
 *
 * <p>The file should contain a list of disposable domains where each domain is on its own
 * line in the file.
 *
 * @see DisposableDomainSource
 */
public class FileSource implements DisposableDomainSource {
  private final Set<String> disposableDomains;

  /**
   * Create a new instance of {@code FileSource}.
   *
   * @param path the path to the file that contains disposable domains
   * @throws IOException if the file at the given path does not exist or there is an issue reading
   *                     the file
   */
  FileSource(String path) throws IOException {
    this.disposableDomains = Collections.unmodifiableSet(
        new HashSet<>(Files.readAllLines(Paths.get(path))));
  }

  @Override
  public boolean isDisposableDomain(String domain) {
    return this.disposableDomains.contains(domain);
  }
}
