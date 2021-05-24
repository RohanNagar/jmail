package com.sanctionco.jmail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JMailTest {

  @ParameterizedTest(name = "{0}")
  @MethodSource({
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideValidEmails",
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideValidWhitespaceEmails"})
  @CsvFileSource(resources = "/valid-addresses.csv", numLinesToSkip = 1)
  void ensureValidPasses(String email, String localPart, String domain) {
    // Set expected values based on if the domain is an IP address or not
    final List<String> expectedParts = domain.startsWith("[")
        ? Collections.singletonList(domain.substring(1, domain.length() - 1))
        : Arrays.stream(
            domain
                .replaceAll("\\s*\\([^\\)]*\\)\\s*", "") // no comments in parts
                .split("\\.")).map(String::trim).collect(Collectors.toList());

    final String expectedDomain = domain.startsWith("[")
        ? domain.substring(1, domain.length() - 1)
        : domain;

    final TopLevelDomain expectedTld = expectedParts.size() > 1
        ? TopLevelDomain.fromString(expectedParts.get(expectedParts.size() - 1))
        : TopLevelDomain.NONE;

    Optional<Email> parsed = JMail.tryParse(email);

    assertTrue(parsed.isPresent());

    assertAll("Created Email object has correct properties",
        () -> assertEquals(email, parsed.get().toString()),
        () -> assertEquals(localPart, parsed.get().localPart()),
        () -> assertEquals(expectedDomain, parsed.get().domain()),
        () -> assertEquals(expectedParts, parsed.get().domainParts()),
        () -> assertEquals(expectedTld, parsed.get().topLevelDomain()));

    assertAll("Helper methods are correct",
        () -> assertTrue(JMail.isValid(email)),
        () -> assertDoesNotThrow(() -> JMail.enforceValid(email)));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource({
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidEmails",
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidWhitespaceEmails"})
  @CsvFileSource(resources = "/invalid-addresses.csv", delimiter = '\u007F')
  void ensureInvalidFails(String email) {
    Optional<Email> parsed = JMail.tryParse(email);

    assertFalse(parsed.isPresent());

    assertAll("Helper methods are correct",
        () -> assertFalse(JMail.isValid(email)),
        () -> assertThrows(InvalidEmailException.class, () -> JMail.enforceValid(email)));
  }

  @Test
  void tryParseSetsCommentFields() {
    String email = "test(hello)@(world)example.com";

    Optional<Email> parsed = JMail.tryParse(email);

    assertTrue(parsed.isPresent());

    assertAll("The email with comment is correct",
        () -> assertEquals("test(hello)", parsed.get().localPart()),
        () -> assertEquals("test", parsed.get().localPartWithoutComments()),
        () -> assertEquals("(world)example.com", parsed.get().domain()),
        () -> assertEquals("example.com", parsed.get().domainWithoutComments()),
        () -> assertEquals(Arrays.asList("hello", "world"), parsed.get().comments()),
        () -> assertEquals(Arrays.asList("example", "com"), parsed.get().domainParts()),
        () -> assertEquals(TopLevelDomain.DOT_COM, parsed.get().topLevelDomain()));
  }

  @Test
  void strictValidatorRejects() {
    String dotlessEmail = "test@example";
    String ipEmail = "test@[1.2.3.4]";
    String acceptedEmail = "test@example.com";

    assertAll("Strict validator works correctly",
        () -> assertTrue(JMail.strictValidator().isValid(acceptedEmail)),
        () -> assertFalse(JMail.strictValidator().isValid(dotlessEmail)),
        () -> assertFalse(JMail.strictValidator().isValid(ipEmail)));
  }
}
