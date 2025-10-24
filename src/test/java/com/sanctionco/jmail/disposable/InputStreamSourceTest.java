package com.sanctionco.jmail.disposable;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

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

  private InputStream inputStreamFromFile(String path) {
    try {
      return Files.newInputStream(Paths.get(path));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
