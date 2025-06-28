package com.sanctionco.jmail.disposable;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileSourceTest {
  private static final String PATH = "src/test/resources/disposable_email_blocklist.conf";

  @Test
  void shouldReadFromFile() {
    assertDoesNotThrow(
        () -> DisposableDomainSource.file(PATH),
        "FileSource should not throw on an existent file");
  }

  @Test
  void shouldThrowOnMissingFile() {
    assertThrows(
        IOException.class,
        () -> DisposableDomainSource.file("src/test/resources/missing.txt"),
        "FileSource should throw on an missing file");
  }

  @Test
  void shouldIdentifyDisposableDomains() throws IOException {
    DisposableDomainSource source = DisposableDomainSource.file(PATH);

    assertAll(
        () -> assertTrue(source.isDisposableDomain("disposableinbox.com")),
        () -> assertTrue(source.isDisposableDomain("10-minute-mail.com")),
        () -> assertTrue(source.isDisposableDomain("emailnow.net"))
    );
  }

  @Test
  void shouldNotBlockMissingDomains() throws IOException {
    DisposableDomainSource source = DisposableDomainSource.file(PATH);

    assertAll(
        () -> assertFalse(source.isDisposableDomain("gmail.com")),
        () -> assertFalse(source.isDisposableDomain("yahoo.com")),
        () -> assertFalse(source.isDisposableDomain("utexas.edu"))
    );
  }
}
