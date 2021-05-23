package com.sanctionco.jmail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {

  @ParameterizedTest(name = "{0}")
  @CsvSource(value = {
      "test@123.123.123.com,test,123.123.123.com",
      "first.last@example.com,first.last,example.com",
      "email@[123.123.123.123],email,[123.123.123.123]",
      "first.last@[12.34.56.78],first.last,[12.34.56.78]"})
  void passesDefaultValidation(String email, String localPart, String domain) {
    // JMailTest.ensureValid is the real test. This test just makes sure that
    // the pass-through from EmailValidator works.

    // Set expected values based on if the domain is an IP address or not
    final List<String> expectedParts = domain.startsWith("[")
        ? Collections.singletonList(domain.substring(1, domain.length() - 1))
        : Arrays.stream(domain.split("\\.")).collect(Collectors.toList());

    final String expectedDomain = domain.startsWith("[")
        ? domain.substring(1, domain.length() - 1)
        : domain;

    final TopLevelDomain expectedTld = expectedParts.size() > 1
        ? TopLevelDomain.fromString(expectedParts.get(expectedParts.size() - 1))
        : TopLevelDomain.NONE;

    Optional<Email> parsed = JMail.validator().tryParse(email);

    assertTrue(parsed.isPresent());
    assertEquals(email, parsed.get().toString());
    assertEquals(localPart, parsed.get().localPart());
    assertEquals(expectedDomain, parsed.get().domain());
    assertEquals(expectedParts, parsed.get().domainParts());
    assertEquals(expectedTld, parsed.get().topLevelDomain());

    assertTrue(JMail.validator().isValid(email));
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {"email@[123.123.123.123]", "first.last@[12.34.56.78]"})
  void disallowIpAddressRejectsIpAddressEmails(String email) {
    EmailValidator validator = JMail.validator().disallowIpDomain();

    assertFalse(validator.isValid(email));
    assertFalse(validator.tryParse(email).isPresent());
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {"test@123.123.123.com", "first.last@example.com"})
  void disallowIpAddressAllowsOtherEmails(String email) {
    EmailValidator validator = JMail.validator().disallowIpDomain();

    assertTrue(validator.isValid(email));
    assertTrue(validator.tryParse(email).isPresent());
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {"admin@mailserver1", "test@example"})
  void requireTopLevelDomainRejectsDotlessAddresses(String email) {
    EmailValidator validator = JMail.validator().requireTopLevelDomain();

    assertFalse(validator.isValid(email));
    assertFalse(validator.tryParse(email).isPresent());
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {"test@123.123.123.com", "first.last@example.com"})
  void requireTopLevelDomainAllowsOtherAddresses(String email) {
    EmailValidator validator = JMail.validator().requireTopLevelDomain();

    assertTrue(validator.isValid(email));
    assertTrue(validator.tryParse(email).isPresent());
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {"test@123.123.123.org", "first.last@example.net"})
  void requireOnlyTopLevelDomainRejects(String email) {
    EmailValidator validator = JMail.validator()
        .requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM);

    assertFalse(validator.isValid(email));
    assertFalse(validator.tryParse(email).isPresent());
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {"test@123.123.123.com", "first.last@example.com", "x@test.edu"})
  void requireOnlyTopLevelDomainAllows(String email) {
    EmailValidator validator = JMail.validator()
        .requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM, TopLevelDomain.DOT_EDU);

    assertTrue(validator.isValid(email));
    assertTrue(validator.tryParse(email).isPresent());
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "test@domain.test", "test@domain.example", "test@domain.invalid", "test@domain.localhost",
      "test@example.com", "test@example.org", "test@example.net", "test@sub.example.com"})
  void disallowReservedDomainsRejects(String email) {
    EmailValidator validator = JMail.validator().disallowReservedDomains();

    assertFalse(validator.isValid(email));
    assertFalse(validator.tryParse(email).isPresent());
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "test@domain.test.org", "test@domain.exmple.com", "test@domain.invalid.net",
      "test@domain.localhost.hi", "test@sub.example.muesum", "test@example.co", "hello@world"})
  void disallowReservedDomainsAllows(String email) {
    EmailValidator validator = JMail.validator().disallowReservedDomains();

    assertTrue(validator.isValid(email));
    assertTrue(validator.tryParse(email).isPresent());
  }

  @Test
  void customRuleWorks() {
    EmailValidator validator = JMail.validator()
        .withRule(e -> e.domain().startsWith("test"));

    String valid1 = "first.last@test.com";
    String valid2 = "x@test.two.com";

    assertAll("Valid emails pass custom rule",
        () -> assertTrue(validator.isValid(valid1)),
        () -> assertTrue(validator.tryParse(valid1).isPresent()),
        () -> assertTrue(validator.isValid(valid2)),
        () -> assertTrue(validator.tryParse(valid2).isPresent()));

    String invalid1 = "first.last@tes.com";
    String invalid2 = "first.last@example.com";

    assertAll("Invalid emails fail custom rule",
        () -> assertFalse(validator.isValid(invalid1)),
        () -> assertFalse(validator.tryParse(invalid1).isPresent()),
        () -> assertFalse(validator.isValid(invalid2)),
        () -> assertFalse(validator.tryParse(invalid2).isPresent()));
  }

  @Test
  void enforceValidThrows() {
    EmailValidator validator = JMail.validator()
        .withRule(e -> e.domain().startsWith("test"));

    String valid1 = "first.last@test.com";
    String valid2 = "x@test.two.com";

    assertAll("Valid emails pass without throwing",
        () -> assertDoesNotThrow(() -> validator.enforceValid(valid1)),
        () -> assertDoesNotThrow(() -> validator.enforceValid(valid2)));

    String invalid1 = "first.last@tes.com";
    String invalid2 = "first.last@example.com";

    assertAll("Invalid emails fail with exception",
        () -> assertThrows(InvalidEmailException.class, () -> validator.enforceValid(invalid1)),
        () -> assertThrows(InvalidEmailException.class, () -> validator.enforceValid(invalid2)));
  }
}
