package com.sanctionco.jmail.normalization;

import com.sanctionco.jmail.Email;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NormalizationPropertiesTest {

  @AfterEach
  void cleanup() {
    System.clearProperty("jmail.normalize.strip.quotes");
    System.clearProperty("jmail.normalize.case");
    System.clearProperty("jmail.normalize.remove.dots");
    System.clearProperty("jmail.normalize.remove.subaddress");
    System.clearProperty("jmail.normalize.subaddress.separator");
  }

  @Test
  void ensureNormalizedStripsQuotesWhenPropertyIsSet() {
    System.setProperty("jmail.normalize.strip.quotes", "true");

    assertThat(Email.of("\"test.1\"@example.org"))
        .isPresent().get()
        .returns("test.1@example.org", Email::normalized);

    System.setProperty("jmail.normalize.strip.quotes", "false");

    assertThat(Email.of("\"test.1\"@example.org"))
        .isPresent().get()
        .returns("\"test.1\"@example.org", Email::normalized);
  }

  @Test
  void ensureNormalizedConvertsToUpperCaseWhenPropertyIsSet() {
    System.setProperty("jmail.normalize.case", "UPPERCASE");

    assertThat(Email.of("Test.1@example.org"))
        .isPresent().get()
        .returns("TEST.1@EXAMPLE.ORG", Email::normalized);

    System.setProperty("jmail.normalize.case", "NO_CHANGE");

    assertThat(Email.of("Test.1@example.org"))
        .isPresent().get()
        .returns("Test.1@example.org", Email::normalized);
  }

  @Test
  void ensureNormalizedLowerCasesBothLocalPartAndDomain() {
    System.setProperty("jmail.normalize.case", "LOWERCASE");

    assertThat(Email.of("TEST.1@EXAMPLE.ORG"))
        .isPresent().get()
        .returns("test.1@example.org", Email::normalized);

    System.setProperty("jmail.normalize.case", "NO_CHANGE");

    assertThat(Email.of("TEST.1@EXAMPLE.ORG"))
        .isPresent().get()
        .returns("TEST.1@EXAMPLE.ORG", Email::normalized);
  }

  @Test
  void ensureNormalizedLowerCasesCorrectPartBasedOnOptions() {
    System.setProperty("jmail.normalize.case", "LOWERCASE_LOCAL_PART_ONLY");

    assertThat(Email.of("TEST.1@EXAMPLE.ORG"))
        .isPresent().get()
        .returns("test.1@EXAMPLE.ORG", Email::normalized);

    System.setProperty("jmail.normalize.case", "LOWERCASE_DOMAIN_ONLY");

    assertThat(Email.of("TEST.1@EXAMPLE.ORG"))
        .isPresent().get()
        .returns("TEST.1@example.org", Email::normalized);

    System.setProperty("jmail.normalize.case", "NO_CHANGE");

    assertThat(Email.of("TEST.1@EXAMPLE.ORG"))
        .isPresent().get()
        .returns("TEST.1@EXAMPLE.ORG", Email::normalized);
  }

  @Test
  void ensureNormalizedRemovesDotsWhenPropertyIsSet() {
    System.setProperty("jmail.normalize.remove.dots", "true");

    assertThat(Email.of("t.e.s.t.1@example.org"))
        .isPresent().get()
        .returns("test1@example.org", Email::normalized);

    System.setProperty("jmail.normalize.remove.dots", "false");

    assertThat(Email.of("t.e.s.t.1@example.org"))
        .isPresent().get()
        .returns("t.e.s.t.1@example.org", Email::normalized);
  }

  @Test
  void ensureNormalizedRemovesSubAddressBasedOnOptions() {
    System.setProperty("jmail.normalize.remove.subaddress", "true");

    assertThat(Email.of("test+example@tt.edu"))
        .isPresent().get()
        .returns("test@tt.edu", Email::normalized);

    assertThat(Email.of("test-example@tt.edu"))
        .isPresent().get()
        .returns("test-example@tt.edu", Email::normalized);

    System.setProperty("jmail.normalize.subaddress.separator", "-");

    assertThat(Email.of("test+example@tt.edu"))
        .isPresent().get()
        .returns("test+example@tt.edu", Email::normalized);

    assertThat(Email.of("test-example@tt.edu"))
        .isPresent().get()
        .returns("test@tt.edu", Email::normalized);
  }
}
