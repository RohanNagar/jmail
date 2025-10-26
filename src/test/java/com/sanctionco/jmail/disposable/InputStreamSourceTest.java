package com.sanctionco.jmail.disposable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputStreamSourceTest {
  private static final String PATH = "src/test/resources/disposable_email_blocklist.conf";

  @Test
  void shouldReadFromInputStream() throws IOException {
    try (InputStream inputStream = inputStreamFromFile(PATH)) {
      assertDoesNotThrow(
          () -> DisposableDomainSource.inputStream(inputStream),
          "InputStreamSource should not throw on open input stream");
    }
  }

  @Test
  void shouldNotCloseInputStream() throws IOException {
    try (TrackCloseInputStream inputStream = new TrackCloseInputStream(inputStreamFromFile(PATH))) {
      DisposableDomainSource.inputStream(inputStream);

      // The stream should still be readable
      assertFalse(inputStream.closed);
    }
  }

  @Test
  void shouldThrowWhenStreamIsClosedOnMissingFile() throws IOException {
    InputStream inputStream = inputStreamFromFile(PATH);
    inputStream.close();

    assertThrows(
        IOException.class,
        () -> DisposableDomainSource.inputStream(inputStream),
        "InputStreamSource should throw when stream is closed");
  }

  @Test
  void shouldIdentifyDisposableDomains() throws IOException {
    InputStream inputStream = inputStreamFromFile(PATH);
    DisposableDomainSource source = DisposableDomainSource.inputStream(inputStream);

    assertAll(
        () -> assertTrue(source.isDisposableDomain("disposableinbox.com")),
        () -> assertTrue(source.isDisposableDomain("10-minute-mail.com")),
        () -> assertTrue(source.isDisposableDomain("emailnow.net"))
    );
  }

  @Test
  void shouldNotBlockMissingDomains() throws IOException {
    InputStream inputStream = inputStreamFromFile(PATH);
    DisposableDomainSource source = DisposableDomainSource.inputStream(inputStream);

    assertAll(
        () -> assertFalse(source.isDisposableDomain("gmail.com")),
        () -> assertFalse(source.isDisposableDomain("yahoo.com")),
        () -> assertFalse(source.isDisposableDomain("utexas.edu"))
    );
  }

  @SuppressWarnings("SameParameterValue")
  private InputStream inputStreamFromFile(String path) {
    try {
      return Files.newInputStream(Paths.get(path));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  // Custom InputStream that tracks whether close() was called
  private static class TrackCloseInputStream extends InputStream {
    private final InputStream delegate;
    boolean closed = false;

    TrackCloseInputStream(InputStream delegate) {
      this.delegate = delegate;
    }

    @Override
    public int read() throws IOException {
      return delegate.read();
    }

    @Override
    public void close() throws IOException {
      closed = true;
      delegate.close();
    }
  }
}
