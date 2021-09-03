package com.sanctionco.jmail;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

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

  @Test
  void toStringIsCorrect() {
    EmailValidator validator = JMail.validator().requireTopLevelDomain();

    assertThat(validator).hasToString("EmailValidator[validationRuleCount=1]");
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
    @ValueSource(strings = {"admin@mailserver1", "test@example", "test@-server"})
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
  class DisallowExplicitSourceRouting {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"@1st.relay,@2nd.relay:user@final.domain", "@a:user@final.domain"})
    void rejectsAddressesWithExplicitSourceRouting(String email) {
      runInvalidTest(JMail.validator().disallowExplicitSourceRouting(), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.com", "first.last@example.com"})
    void allowsOtherAddresses(String email) {
      runValidTest(JMail.validator().disallowExplicitSourceRouting(), email);
    }
  }

  @Nested
  class DisallowQuotedIdentifiers {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"John Smith <test@server.com>", "ABC <123t@abc.net"})
    void rejectsAddressesWithQuotedIdentifiers(String email) {
      runInvalidTest(JMail.validator().disallowQuotedIdentifiers(), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.com", "first.last@example.com"})
    void allowsOtherAddresses(String email) {
      runValidTest(JMail.validator().disallowQuotedIdentifiers(), email);
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

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"first.last@test.com", "x@test.two.com"})
    void validatesCorrectlyWithCollection(String email) {
      Predicate<Email> rule = e -> e.domain().startsWith("test");
      runValidTest(JMail.validator().withRules(Collections.singletonList(rule)), email);
    }
  }

  private static void runValidTest(EmailValidator validator, String email) {
    Condition<String> valid = new Condition<>(validator::isValid, "valid");

    assertThat(validator.tryParse(email)).isPresent();
    assertThat(email).is(valid);
    assertThatNoException().isThrownBy(() -> validator.enforceValid(email));
  }

  private static void runInvalidTest(EmailValidator validator, String email) {
    Condition<String> invalid = new Condition<>(e -> !validator.isValid(e), "invalid");

    assertThat(validator.tryParse(email)).isNotPresent();
    assertThat(email).is(invalid);
    assertThatExceptionOfType(InvalidEmailException.class)
        .isThrownBy(() -> validator.enforceValid(email));
  }
}
