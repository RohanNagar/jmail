package com.sanctionco.jmail;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.sanctionco.jmail.helpers.EmailValidatorAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailValidatorTest {

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "test@123.123.123.com", "first.last@example.com", "first@host",
      "email@[123.123.123.123]", "first.last@[12.34.56.78]" })
  void passesThroughDefaultValidation(String email) {
    runValidTest(JMail.validator(), email);
  }

  @Test
  @SuppressWarnings("unchecked")
  void duplicateRuleCallsOnlyAddsOnePredicate() throws Exception {
    EmailValidator validator = JMail.validator()
        .disallowIpDomain()
        .disallowIpDomain();

    Field predicatesField = EmailValidator.class
        .getDeclaredField("validationPredicates");
    predicatesField.setAccessible(true);

    Set<Predicate<String>> predicates = (Set<Predicate<String>>) predicatesField.get(validator);

    assertThat(predicates).hasSize(1);
  }

  @Nested
  class DisallowIpAddress {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = { "email@[123.123.123.123]", "first.last@[12.34.56.78]" })
    void rejectsIpAddressEmails(String email) {
      runInvalidTest(JMail.validator().disallowIpDomain(), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = { "test@123.123.123.com", "first.last@example.com" })
    void allowsOtherEmails(String email) {
      runValidTest(JMail.validator().disallowIpDomain(), email);
    }
  }

  @Nested
  class RequireTopLevelDomain {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"admin@mailserver1", "test@example"})
    void rejectsDotlessAddresses(String email) {
      runInvalidTest(JMail.validator().requireTopLevelDomain(), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.com", "first.last@example.com"})
    void allowsOtherAddresses(String email) {
      runValidTest(JMail.validator().requireTopLevelDomain(), email);
    }
  }

  @Nested
  class RequireOnlyTopLevelDomains {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.org", "first.last@example.net"})
    void rejects(String email) {
      runInvalidTest(JMail.validator().requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.com", "first.last@example.com", "x@test.edu"})
    void allows(String email) {
      runValidTest(JMail.validator()
          .requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM, TopLevelDomain.DOT_EDU), email);
    }
  }

  @Nested
  class DisallowReservedDomains {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@domain.test", "test@domain.example", "test@domain.invalid", "test@domain.localhost",
        "test@example.com", "test@example.org", "test@example.net", "test@sub.example.com"})
    void rejectsReservedDomains(String email) {
      runInvalidTest(JMail.validator().disallowReservedDomains(), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@domain.test.org", "test@domain.exmple.com", "test@domain.invalid.net",
        "test@domain.localhost.hi", "test@sub.example.muesum", "test@example.co", "hello@world"})
    void allowsOtherAddresses(String email) {
      runValidTest(JMail.validator().disallowReservedDomains(), email);
    }
  }

  @Nested
  class CustomRule {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"first.last@test.com", "x@test.two.com"})
    void validatesCorrectly(String email) {
      runValidTest(JMail.validator().withRule(e -> e.domain().startsWith("test")), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"first.last@tes.com", "first.last@example.com"})
    void invalidatesCorrectly(String email) {
      runInvalidTest(JMail.validator().withRule(e -> e.domain().startsWith("test")), email);
    }
  }

  private static void runValidTest(EmailValidator validator, String email) {
    assertThat(validator).finds(email)
        .valid() // and
        .parsable();
  }

  private static void runInvalidTest(EmailValidator validator, String email) {
    assertThat(validator).finds(email)
        .invalid() // and
        .notParsable();
  }

  @Test
  void throwsAppropriately() {
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
