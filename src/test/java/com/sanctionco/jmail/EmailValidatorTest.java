package com.sanctionco.jmail;

import com.sanctionco.jmail.disposable.DisposableDomainSource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
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

    Map<Predicate<Email>, FailureReason> predicates
        = (Map<Predicate<Email>, FailureReason>) predicatesField.get(validator);

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
      runInvalidTest(JMail.validator()
          .disallowIpDomain(), email, FailureReason.CONTAINS_IP_DOMAIN);
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
      runInvalidTest(JMail.validator()
          .requireTopLevelDomain(), email, FailureReason.MISSING_TOP_LEVEL_DOMAIN);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@123.123.123.com",
        "first.last@example.com",
        "user@[123.123.123.123]",
        "first.last@[IPv6:::12.34.56.78]"
    })
    void allowsOtherAddresses(String email) {
      runValidTest(JMail.validator().requireTopLevelDomain(), email);
    }
  }

  @Nested
  class DisallowExplicitSourceRouting {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"@1st.relay,@2nd.relay:user@final.domain", "@a:user@final.domain"})
    void rejectsAddressesWithExplicitSourceRouting(String email) {
      runInvalidTest(JMail.validator()
          .disallowExplicitSourceRouting(), email, FailureReason.CONTAINS_EXPLICIT_SOURCE_ROUTING);
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
      runInvalidTest(JMail.validator()
          .disallowQuotedIdentifiers(), email, FailureReason.CONTAINS_QUOTED_IDENTIFIER);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.com", "first.last@example.com"})
    void allowsOtherAddresses(String email) {
      runValidTest(JMail.validator().disallowQuotedIdentifiers(), email);
    }
  }

  @Nested
  class DisallowReservedDomains {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@domain.test", "test@domain.example", "test@domain.invalid", "test@domain.localhost",
        "test@example.com", "test@example.org", "test@example.net", "test@sub.example.com"})
    void rejectsReservedDomains(String email) {
      runInvalidTest(JMail.validator()
          .disallowReservedDomains(), email, FailureReason.CONTAINS_RESERVED_DOMAIN);
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
  class RequireOnlyTopLevelDomains {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.org", "first.last@example.net"})
    void rejects(String email) {
      runInvalidTest(
          JMail.validator().requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM),
          email,
          FailureReason.INVALID_TOP_LEVEL_DOMAIN);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.com", "first.last@example.com", "x@test.edu"})
    void allows(String email) {
      runValidTest(JMail.validator()
          .requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM, TopLevelDomain.DOT_EDU), email);
    }
  }

  @Nested
  class DisallowSingleCharacterTopLevelDomains {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.o", "first.last@example.n", "test@test.c"})
    void rejects(String email) {
      runInvalidTest(
          JMail.validator().disallowSingleCharacterTopLevelDomains(),
          email,
          FailureReason.SINGLE_CHARACTER_TOP_LEVEL_DOMAIN);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"test@123.123.123.co", "first.last@example.com", "x@test"})
    void allows(String email) {
      runValidTest(JMail.validator().disallowSingleCharacterTopLevelDomains(), email);
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
      runInvalidTest(JMail.validator()
          .disallowObsoleteWhitespace(), email, FailureReason.CONTAINS_OBSOLETE_WHITESPACE);
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
      runInvalidTest(JMail.validator()
          .requireValidMXRecord(), email, FailureReason.INVALID_MX_RECORD);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@gmail.com", "test@hotmail.com", "test@yahoo.com", "test@utexas.edu",
        "test@gmail.(comment)com"})
    void allowsDomainsWithMXRecord(String email) {
      runValidTest(JMail.validator().requireValidMXRecord(100, 5), email);
    }

    @Test
    void correctlyCustomizesTimeoutAndRetries() {
      long startTime = System.currentTimeMillis();
      runInvalidTest(JMail.validator()
          .requireValidMXRecord(10, 1), "test@coolio.com", FailureReason.INVALID_MX_RECORD);
      long endTime = System.currentTimeMillis();

      assertThat(endTime - startTime).isLessThan(500);
    }
  }

  @Nested
  class DisallowDisposableDomains {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "mailbox@10-minute-mail.com", "john@emailnow.net", "test@disposableinbox.com",
        "john@emailnow.(comment)net"})
    void rejectsWithDisposableDomains(String email) throws IOException {
      DisposableDomainSource source
          = DisposableDomainSource.file("src/test/resources/disposable_email_blocklist.conf");

      runInvalidTest(JMail.validator()
          .disallowDisposableDomains(source), email, FailureReason.CONTAINS_DISPOSABLE_DOMAIN);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@gmail.com", "test@hotmail.com", "test@yahoo.com", "test@utexas.edu",
        "test@gmail.(comment)com", "test@[1.2.3.4]"})
    void allowsWithoutDisposableDomain(String email) throws IOException {
      DisposableDomainSource source
          = DisposableDomainSource.file("src/test/resources/disposable_email_blocklist.conf");

      runValidTest(JMail.validator().disallowDisposableDomains(source), email);
    }
  }

  @Nested
  class RequireOnlyAsciiCharacters {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "jørn@test.com", "我買@屋企.香港", "Pelé@example.com", "xyz@🙏.kz"})
    void rejectsNonAsciiEmails(String email) {
      runInvalidTest(JMail.validator()
          .requireAscii(), email, FailureReason.NON_ASCII_ADDRESS);
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
      runInvalidTest(JMail.validator().withRule(e -> e.domain().startsWith("test")), email,
          FailureReason.FAILED_CUSTOM_VALIDATION);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"first.last@tes.com", "first.last@example.com"})
    void invalidatesCorrectlyWithCustomFailureReason(String email) {
      FailureReason failureReason = new FailureReason("DOES_NOT_START_WITH_TEST");

      runInvalidTest(
          JMail.validator().withRule(e -> e.domain().startsWith("test"), failureReason),
          email,
          failureReason);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"first.last@tes.com", "first.last@example.com"})
    void invalidatesCorrectlyWithCustomStringFailureReason(String email) {
      FailureReason failureReason = new FailureReason("MY_REASON");

      runInvalidTest(
          JMail.validator().withRule(e -> e.domain().startsWith("test"), "MY_REASON"),
          email,
          failureReason);

      EmailValidator validator = JMail.strictValidator()
          .requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM);

      EmailValidationResult result = validator.validate("test@test.org");

      result.getFailureReason().equals(FailureReason.INVALID_TOP_LEVEL_DOMAIN);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"first.last@test.com", "x@test.two.com"})
    void validatesCorrectlyWithCollection(String email) {
      Predicate<Email> rule = e -> e.domain().startsWith("test");
      runValidTest(JMail.validator().withRules(Collections.singletonList(rule)), email);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"first.last@tes.com", "first.last@example.com"})
    void invalidatesCorrectlyWithMap(String email) {
      FailureReason failureReason = new FailureReason("DOES_NOT_START_WITH_TEST");

      runInvalidTest(
          JMail.validator()
              .withRules(Collections.singletonMap(
                  e -> e.domain().startsWith("test"),
                  failureReason)),
          email,
          failureReason);
    }
  }

  @Nested
  class AllowNonstandardDots {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "..test@domain.test", "test..@domain.example", "test..again@domain.example"})
    void rejectsInvalidDots(String email) {
      runInvalidTest(JMail.validator().allowNonstandardDots(),
          email, FailureReason.MULTIPLE_DOT_SEPARATORS);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        ".test@domain.test.org", "test.@domain.exmple.com", ".my.email.@gmail.com"})
    void allowsNonstandardDots(String email) {
      runValidTest(JMail.validator().allowNonstandardDots(), email);
    }
  }

  @Nested
  class DisallowIpDomainAllowNonstandardDotsCombination {
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        "test@[1.2.3.4]", "test@[5.6.7.8]"})
    void rejects(String email) {
      runInvalidTest(JMail.validator().allowNonstandardDots().disallowIpDomain(),
          email, FailureReason.CONTAINS_IP_DOMAIN);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
        ".test@domain.test.org", "test.@domain.exmple.com", ".my.email.@gmail.com"})
    void allows(String email) {
      runValidTest(JMail.validator().allowNonstandardDots().disallowIpDomain(), email);
    }
  }

  private static void runValidTest(EmailValidator validator, String email) {
    Condition<String> valid = new Condition<>(validator::isValid, "valid");

    assertThat(validator.tryParse(email)).isPresent();
    assertThat(email).is(valid);
    assertThatNoException().isThrownBy(() -> validator.enforceValid(email));
    assertThat(validator.validate(email).isSuccess()).isTrue();
  }

  private static void runInvalidTest(EmailValidator validator,
                                     String email,
                                     FailureReason expectedFailureReason) {
    Condition<String> invalid = new Condition<>(validator::isInvalid, "invalid");

    assertThat(validator.tryParse(email)).isNotPresent();
    assertThat(email).is(invalid);
    assertThatExceptionOfType(InvalidEmailException.class)
        .isThrownBy(() -> validator.enforceValid(email));
    assertThat(validator.validate(email).isFailure()).isTrue();
    assertThat(validator.validate(email).getFailureReason())
        .isEqualTo(expectedFailureReason);
  }
}
