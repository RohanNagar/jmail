package com.sanctionco.jmail.disposable;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InputStreamSourceTest {
  private static final String PATH = "disposable_email_blocklist.conf";

  @Test
  void shouldReadFromInputStream() throws IOException {
    try (InputStream inputStream = getResourceAsStream(PATH)) {
      assertDoesNotThrow(
          () -> DisposableDomainSource.inputStream(inputStream),
          "InputStreamSource should not throw on open input stream");
    }
  }

  @Test
  void shouldNotCloseInputStream() throws IOException {
    try (TrackCloseInputStream inputStream = new TrackCloseInputStream(getResourceAsStream(PATH))) {
      DisposableDomainSource.inputStream(inputStream);

      // The stream should still be readable
      assertFalse(inputStream.closed);
    }
  }

  @Test
  void shouldAllowWhiteSpaceInInputStream() throws IOException {
    List<String> domains = List.of(
        "first.com",
        "second.com",
        " untrimmed.com ",
        " CAPS.com ",
        " ALLCAPS.COM ",
        "",
        "   "
    );

    DisposableDomainSource source = DisposableDomainSource.inputStream(getListAsStream(domains));

    assertAll(
        () -> assertTrue(source.isDisposableDomain("first.com")),
        () -> assertTrue(source.isDisposableDomain("second.com")),
        () -> assertTrue(source.isDisposableDomain("untrimmed.com")),
        () -> assertFalse(source.isDisposableDomain(""))
    );
  }

  @Test
  void shouldAllowMixedCaseInInputStream() throws IOException {
    List<String> domains = List.of(
        "CAPS.com",
        "ALLCAPS.COM"
    );

    DisposableDomainSource source = DisposableDomainSource.inputStream(getListAsStream(domains));

    assertAll(
        () -> assertTrue(source.isDisposableDomain("caps.com")),
        () -> assertTrue(source.isDisposableDomain("allcaps.com")),
        () -> assertFalse(source.isDisposableDomain(""))
    );
  }

  @Test
  void shouldThrowWhenStreamIsClosed() throws IOException {
    InputStream inputStream = getResourceAsStream(PATH);
    inputStream.close();

    assertThrows(
        IOException.class,
        () -> DisposableDomainSource.inputStream(inputStream),
        "InputStreamSource should throw when stream is closed");
  }

  @Test
  void shouldIdentifyDisposableDomains() throws IOException {
    InputStream inputStream = getResourceAsStream(PATH);
    DisposableDomainSource source = DisposableDomainSource.inputStream(inputStream);

    assertAll(
        () -> assertTrue(source.isDisposableDomain("disposableinbox.com")),
        () -> assertTrue(source.isDisposableDomain("10-minute-mail.com")),
        () -> assertTrue(source.isDisposableDomain("emailnow.net"))
    );
  }

  @Test
  void shouldMatchDomainsCaseInsensitively() throws IOException {
    InputStream inputStream = getResourceAsStream(PATH);
    DisposableDomainSource source = DisposableDomainSource.inputStream(inputStream);

    assertAll(
        () -> assertTrue(source.isDisposableDomain("disposableinbox.com")),
        () -> assertTrue(source.isDisposableDomain("DiSpoSaBlEiNbOX.com")),
        () -> assertTrue(source.isDisposableDomain("disposableinbox.COM"))
    );
  }

  @Test
  void shouldNotBlockMissingDomains() throws IOException {
    InputStream inputStream = getResourceAsStream(PATH);
    DisposableDomainSource source = DisposableDomainSource.inputStream(inputStream);

    assertAll(
        () -> assertFalse(source.isDisposableDomain("gmail.com")),
        () -> assertFalse(source.isDisposableDomain("yahoo.com")),
        () -> assertFalse(source.isDisposableDomain("utexas.edu"))
    );
  }

  @SuppressWarnings("SameParameterValue")
  private InputStream getResourceAsStream(String path) {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
    if (inputStream == null) {
      throw new IllegalArgumentException("classpath resource not found: " + path);
    }
    return inputStream;
  }

  private InputStream getListAsStream(List<String> domains) {
    return new ByteArrayInputStream(String.join("\n", domains).getBytes());
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
