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
  void passesThroughDefaultValidateFailure() {
    String invalid = "test.@test.com";

    EmailValidationResult result = JMail.validator().validate(invalid);

    assertThat(result.isFailure()).isTrue();
    assertThat(result.getFailureReason()).isEqualTo(FailureReason.LOCAL_PART_ENDS_WITH_DOT);
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

  @Test
  void isInvalidCanValidate() {
    EmailValidator validator = JMail.validator().requireTopLevelDomain();
    String address = "test@test.com";

    assertThat(validator.isInvalid(address)).isFalse();
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
    @ValueSource(strings = {"admin@mailserver1", "test@example", "test@server"})
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
    @ValueSource(strings = {"John Smith <test@server.com>", "ABC <123t@abc.net>"})
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
  class DisallowObsoleteWhitespace {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "a@b .com", "a. b@c.com", "1234   @   local(blah)  .machine .example",
        "Test.\r\n Folding.\r\n Whitespace@test.org", "test. \r\n \r\n obs@syntax.com",
        "\r\n (\r\n x \r\n ) \r\n first\r\n ( \r\n x\r\n ) \r\n .\r\n ( \r\n x) \r\n "
            + "last \r\n (  x \r\n ) \r\n @test.org"})
    void rejectsObsoleteWhitespace(String email) {
      runInvalidTest(JMail.validator().disallowObsoleteWhitespace(), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@domain.test.org", "test@domain.exmple.com", "test@domain.invalid.net",
        "test@domain.localhost.hi", "test@sub.example.muesum", "test@example.co", "hello@world"})
    void allowsOtherAddresses(String email) {
      runValidTest(JMail.validator().disallowObsoleteWhitespace(), email);
    }
  }

  @Nested
  class RequireValidMXRecord {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@domain.test", "test@domain.example", "test@domain.invalid",
        "test@domain.localhost", "test@gmail.co"})
    void rejectsDomainsWithoutMXRecord(String email) {
      runInvalidTest(JMail.validator().requireValidMXRecord(), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@gmail.com", "test@hotmail.com", "test@yahoo.com", "test@utexas.edu",
        "test@gmail.(comment)com"})
    void allowsDomainsWithMXRecord(String email) {
      runValidTest(JMail.validator().requireValidMXRecord(100, 3), email);
    }

    @Test
    void correctlyCustomizesTimeoutAndRetries() {
      long startTime = System.currentTimeMillis();
      runInvalidTest(JMail.validator().requireValidMXRecord(10, 1), "test@coolio.com");
      long endTime = System.currentTimeMillis();

      assertThat(endTime - startTime).isLessThan(500);
    }
  }

  @Nested
  class RequireOnlyAsciiCharacters {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "jørn@test.com", "我買@屋企.香港", "Pelé@example.com", "xyz@🙏.kz"})
    void rejectsNonAsciiEmails(String email) {
      runInvalidTest(JMail.validator().requireAscii(), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@domain.test.org", "hello@world", "user%uucp!path@berkeley.edu",
        "\"John Michael\" <tester@test.net>", "(comment)test@test.org"})
    void allowsOtherAddresses(String email) {
      runValidTest(JMail.validator().requireAscii(), email);
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
    assertThat(validator.validate(email).isSuccess()).isTrue();
  }

  private static void runInvalidTest(EmailValidator validator, String email) {
    Condition<String> invalid = new Condition<>(validator::isInvalid, "invalid");

    assertThat(validator.tryParse(email)).isNotPresent();
    assertThat(email).is(invalid);
    assertThatExceptionOfType(InvalidEmailException.class)
        .isThrownBy(() -> validator.enforceValid(email));
    assertThat(validator.validate(email).isFailure()).isTrue();
    assertThat(validator.validate(email).getFailureReason())
        .isEqualTo(FailureReason.FAILED_CUSTOM_VALIDATION);
  }
}
