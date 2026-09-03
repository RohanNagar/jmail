package com.sanctionco.jmail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.andreinc.mockneat.MockNeat;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class JMailTest {
  private final Condition<String> valid = new Condition<>(JMail::isValid, "valid");
  private final Condition<String> invalid = new Condition<>(JMail::isInvalid, "invalid");

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
                .replaceAll("\\s*\\([^)]*\\)\\s*", "") // no comments in parts
                .split("\\.")).map(String::trim).collect(Collectors.toList());

    final String expectedDomain = domain.startsWith("[")
        ? domain.substring(1, domain.length() - 1)
        : domain;

    final TopLevelDomain expectedTld = expectedParts.size() > 1
        ? TopLevelDomain.fromString(expectedParts.get(expectedParts.size() - 1))
        : TopLevelDomain.NONE;

    assertThat(JMail.tryParse(email))
        .isPresent().get()
        .hasToString(email)
        .returns(localPart, Email::localPart)
        .returns(expectedDomain, Email::domain)
        .returns(expectedParts, Email::domainParts)
        .returns(expectedTld, Email::topLevelDomain);

    assertThat(JMail.validate(email).isSuccess()).isTrue();

    assertThat(email).is(valid);
    assertThatNoException().isThrownBy(() -> JMail.enforceValid(email));
  }

  @Test
  void ensureRandomEmailsPass() {
    for (int i = 0; i < 1000; i++) {
      assertThat(JMail.isValid(MockNeat.threadLocal().emails().get())).isTrue();
    }
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "\"test\\\rblah\"@test.org",
      "first.(\r\n middle\r\n )last@test.org",
  })
  void ensureQuotedWhitespaceEmailsDoNotContainWhitespace(String email) {
    // Whitespace within quotes or comments should not return true
    assertThat(JMail.tryParse(email))
        .isPresent().get()
        .returns(false, Email::containsWhitespace);
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "1234   @   local(blah)  .machine .example",
      "Test.\r\n Folding.\r\n Whitespace@test.org",
      "test. \r\n \r\n obs@syntax.com",
      "\r\n (\r\n x \r\n ) \r\n first\r\n ( \r\n x\r\n ) \r\n .\r\n ( \r\n x) \r\n "
          + "last \r\n (  x \r\n ) \r\n @test.org",
  })
  void ensureWhitespaceEmailsContainWhitespace(String email) {
    // Whitespace within quotes or comments should not return true
    assertThat(JMail.tryParse(email))
        .isPresent().get()
        .returns(true, Email::containsWhitespace);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource({
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidEmails",
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidWhitespaceEmails",
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidControlEmails"})
  @CsvFileSource(resources = "/invalid-addresses.csv", delimiterString = " ;", numLinesToSkip = 1)
  void ensureInvalidFails(String email) {
    assertThat(JMail.tryParse(email)).isNotPresent();

    assertThat(JMail.validate(email).isFailure()).isTrue();

    assertThat(email).is(invalid);
    assertThatExceptionOfType(InvalidEmailException.class)
        .isThrownBy(() -> JMail.enforceValid(email));
  }

  @Test
  void tryParseSetsCommentFields() {
    String email = "test(hello)@(world)example.com";

    assertThat(JMail.tryParse(email))
        .isPresent().get()
        .hasToString(email)
        .returns("test(hello)", Email::localPart)
        .returns("test", Email::localPartWithoutComments)
        .returns("(world)example.com", Email::domain)
        .returns("example.com", Email::domainWithoutComments)
        .returns(Arrays.asList("hello", "world"), Email::comments)
        .returns(Arrays.asList("example", "com"), Email::domainParts)
        .returns(TopLevelDomain.DOT_COM, Email::topLevelDomain)
        .returns("test@example.com", Email::normalized)
        .returns(false, Email::containsWhitespace);
  }

  @Test
  void strictValidatorRejects() {
    String dotlessEmail = "test@example";
    String ipEmail = "test@[1.2.3.4]";
    String acceptedEmail = "test@example.com";

    assertThat(JMail.strictValidator().isValid(acceptedEmail)).isTrue();
    assertThat(JMail.strictValidator().isValid(dotlessEmail)).isFalse();
    assertThat(JMail.strictValidator().isValid(ipEmail)).isFalse();
  }

  @Test
  void addressWithSourceRoutingValidates() {
    String email = "@1st.relay,@2nd.relay:user@final.domain";

    assertThat(JMail.tryParse(email))
        .isPresent().get()
        .hasToString(email)
        .returns("user", Email::localPart)
        .returns("final.domain", Email::domain)
        .returns(Arrays.asList("final", "domain"), Email::domainParts)
        .returns(TopLevelDomain.fromString("domain"), Email::topLevelDomain)
        .returns(Arrays.asList("1st.relay", "2nd.relay"), Email::explicitSourceRoutes)
        .returns("user@final.domain", Email::normalized)
        .returns(false, Email::containsWhitespace);
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "@-1st.relay,@2nd.relay:user@final.domain",
      "@1st-.relay,@2nd.relay:user@final.domain",
      "@1st.relay,2nd.relay:user@final.domain",
      "@.relay,2nd.relay:user@final.domain",
      "@1st.1111,2nd.relay:user@final.domain",
      "@hello.world,user@final.domain",
      "@hello.world,",
      "@1st.relay,@2nd.relay:user@-final.domain",
      "@1st.relay,@2nd.relay:invalid",
      "@@1st.relay,@2nd.relay:user@final.domain",
      "@1st.r_elay,@2nd.relay:user@final.domain",
      "@abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijkl.relay,"
          + "@2nd.relay:user@final.domain"
  })
  void ensureInvalidSourceRoutingAddressesFail(String email) {
    assertThat(JMail.tryParse(email)).isNotPresent();

    assertThat(email).is(invalid);
    assertThatExceptionOfType(InvalidEmailException.class)
        .isThrownBy(() -> JMail.enforceValid(email));
  }

  @Test
  void ensureIdentifiersAreParsed() {
    String one = "John Smith <test@te.ex>";

    assertThat(JMail.tryParse(one)).isPresent().get()
        .hasToString(one)
        .returns(true, Email::hasIdentifier)
        .returns("John Smith ", Email::identifier)
        .returns("test@te.ex", Email::normalized)
        .returns(false, Email::containsWhitespace);

    String two = "Admin<admin@te.ex>";

    assertThat(JMail.tryParse(two)).isPresent().get()
        .hasToString(two)
        .returns(true, Email::hasIdentifier)
        .returns("Admin", Email::identifier)
        .returns("admin@te.ex", Email::normalized);

    String none = "user@te.ex";

    assertThat(JMail.tryParse(none)).isPresent().get()
        .hasToString(none)
        .returns(none, Email::normalized)
        .returns(false, Email::hasIdentifier)
        .extracting("identifier")
        .isNull();
  }

  @Test
  void isInvalidCanValidate() {
    assertThat(JMail.isInvalid("test@test.com")).isFalse();
  }

  @Nested
  class AllowNonstandardDots {
    @ParameterizedTest(name = "{0}")
    @MethodSource({
        "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidEmails",
        "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidWhitespaceEmails",
        "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidControlEmails"})
    @CsvFileSource(resources = "/invalid-addresses.csv", delimiterString = " ;", numLinesToSkip = 1)
    void ensureFailures(String email) {
      // This test only works for addresses that will fail
      // even when we allow a starting or trailing dot in the local-part
      assumeTrue(email.charAt(0) != '.' && !email.contains(".@"));

      assertThat(JMail.validate(email, true))
          .returns(true, EmailValidationResult::isFailure);
    }

    @Test
    void ensureOnlyDotFails() {
      assertThat(JMail.validate(".@test.com", true))
          .returns(true, EmailValidationResult::isFailure)
          .returns(FailureReason.LOCAL_PART_MISSING, EmailValidationResult::getFailureReason);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {".test@test.org", "yes.@example.com"})
    void ensureStartingAndTrailingDotsPass(String address) {
      assertThat(JMail.validate(address, true).getEmail())
          .isPresent().get().hasToString(address);
    }
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "test@example.com",
      "a=b@example.com",          // Single '=' is valid
      "c?d@example.com",          // Single '?' is valid
      "=notvalid@example.com",    // Fragment, not pattern
      "test?=@example.com",       // Fragment, not pattern
      "=?@example.com",           // Too short
      "=?a?b?@example.com",       // Incomplete
      "normal.user@example.com"
  })
  void ensureValidEmailsWithSpecialCharsPass(String email) {
    assertThat(JMail.tryParse(email)).isPresent();
    assertThat(email).is(valid);
  }

  @Test
  void ensureCommentBeforeIpLiteralDomainPasses() {
    String email = "aaa@(comment)[123.123.123.123]";

    assertThat(JMail.tryParse(email))
        .isPresent().get()
        .returns(true, Email::isIpAddress)
        .returns("123.123.123.123", Email::domainWithoutComments);
  }

  @Nested
  class AddressList {
    @Test
    void parsesSimpleCommaSeparatedAddresses() {
      List<Email> emails = JMail.tryParseAddressList(
          "first@example.com, second@example.org");

      assertThat(emails)
          .extracting(Email::normalized)
          .containsExactly("first@example.com", "second@example.org");
    }

    @Test
    void trimsWhitespaceAroundAddresses() {
      List<Email> emails = JMail.tryParseAddressList(
          " first@example.com ,  second@example.org ");

      assertThat(emails)
          .hasSize(2)
          .extracting(Email::normalized)
          .containsExactly("first@example.com", "second@example.org");
    }

    @Test
    void skipsEmptyTokens() {
      List<Email> emails = JMail.tryParseAddressList(
          "first@example.com,, ,second@example.org,");

      assertThat(emails)
          .hasSize(2)
          .extracting(Email::normalized)
          .containsExactly("first@example.com", "second@example.org");
    }

    @Test
    void doesNotSplitOnQuotedComma() {
      List<Email> emails = JMail.tryParseAddressList(
          "\"Smith, John\"@example.com, jane@example.org");

      assertThat(emails).hasSize(2);
      assertThat(emails.get(0).localPart()).isEqualTo("\"Smith, John\"");
      assertThat(emails.get(1).normalized()).isEqualTo("jane@example.org");
    }

    @Test
    void doesNotSplitOnQuotedIdentifierComma() {
      List<Email> emails = JMail.tryParseAddressList(
          "\"Smith, John\" <my@example.com>, jane@example.org");

      assertThat(emails).hasSize(2);
      assertThat(emails.get(0))
          .returns(true, Email::hasIdentifier)
          .returns("my@example.com", Email::normalized);
      assertThat(emails.get(1).normalized()).isEqualTo("jane@example.org");
    }

    @Test
    void doesNotSplitOnCommentComma() {
      List<Email> emails = JMail.tryParseAddressList(
          "test(hello, world)@example.com, other@example.org");

      assertThat(emails)
          .extracting(Email::normalized)
          .containsExactly("test@example.com", "other@example.org");
    }

    @Test
    void doesNotSplitOnNestedCommentComma() {
      List<Email> emails = JMail.tryParseAddressList(
          "test((inner, comma)outer)@example.com, other@example.org");

      assertThat(emails)
          .extracting(Email::normalized)
          .containsExactly("test@example.com", "other@example.org");
    }

    @Test
    void doesNotSplitOnSourceRouteComma() {
      List<Email> emails = JMail.tryParseAddressList(
          "@1st.relay,@2nd.relay:user@final.domain, next@example.com");

      assertThat(emails).hasSize(2);
      assertThat(emails.get(0))
          .returns("user@final.domain", Email::normalized)
          .returns(Arrays.asList("1st.relay", "2nd.relay"), Email::explicitSourceRoutes);
      assertThat(emails.get(1).normalized()).isEqualTo("next@example.com");
    }

    @Test
    void doesNotSplitOnAngleBracketSourceRouteComma() {
      List<Email> emails = JMail.tryParseAddressList(
          "Admin <@1st.relay,@2nd.relay:user@final.domain>, next@example.com");

      assertThat(emails).hasSize(2);
      assertThat(emails.get(0))
          .returns(true, Email::hasIdentifier)
          .returns("user@final.domain", Email::normalized);
      assertThat(emails.get(1).normalized()).isEqualTo("next@example.com");
    }

    @Test
    void doesNotSplitOnEscapedComma() {
      List<Email> emails = JMail.tryParseAddressList(
          "user\\,name@example.com, other@example.org");

      assertThat(emails)
          .extracting(Email::normalized)
          .containsExactly("user\\,name@example.com", "other@example.org");
    }

    @Test
    void splitsUnquotedDisplayNameComma() {
      List<EmailValidationResult> results = JMail.validateAddressList(
          "Smith, John <john@example.com>");

      assertThat(results).hasSize(2);
      assertThat(results.get(0).isFailure()).isTrue();
      assertThat(results.get(1).isSuccess()).isTrue();
      assertThat(results.get(1).getEmail().get().normalized())
          .isEqualTo("john@example.com");
    }

    @Test
    void validateIncludesFailuresAndSuccesses() {
      List<EmailValidationResult> results = JMail.validateAddressList(
          "good@example.com, not-an-email, also@example.org");

      assertThat(results).hasSize(3);
      assertThat(results.get(0).isSuccess()).isTrue();
      assertThat(results.get(1).isFailure()).isTrue();
      assertThat(results.get(1).getFailureReason()).isEqualTo(FailureReason.MISSING_AT_SYMBOL);
      assertThat(results.get(2).isSuccess()).isTrue();
    }


    @Test
    void tryParseSkipsInvalidAddresses() {
      List<Email> emails = JMail.tryParseAddressList(
          "good@example.com, not-an-email, also@example.org");

      assertThat(emails)
          .extracting(Email::normalized)
          .containsExactly("good@example.com", "also@example.org");
    }

    @Test
    void handlesSingleAddressWithNoDelimiter() {
      assertThat(JMail.tryParseAddressList("only@example.com"))
          .extracting(Email::normalized)
          .containsExactly("only@example.com");
    }

    @Test
    void emptyAndWhitespaceListsAreEmpty() {
      assertThat(JMail.tryParseAddressList("")).isEmpty();
      assertThat(JMail.tryParseAddressList("   ")).isEmpty();
      assertThat(JMail.tryParseAddressList(",,")).isEmpty();
      assertThat(JMail.validateAddressList("")).isEmpty();
    }

    @Test
    void nullListHandling() {
      assertThat(JMail.tryParseAddressList(null)).isEmpty();

      List<EmailValidationResult> results = JMail.validateAddressList(null);

      assertThat(results).hasSize(1);
      assertThat(results.get(0).isFailure()).isTrue();
      assertThat(results.get(0).getFailureReason())
          .isEqualTo(FailureReason.NULL_ADDRESS);
    }

    @Test
    void identifierListWithoutQuotedCommas() {
      List<Email> emails = JMail.tryParseAddressList(
          "John Smith <john@example.com>, Jane Doe <jane@example.org>");

      assertThat(emails).hasSize(2);
      assertThat(emails.get(0))
          .returns(true, Email::hasIdentifier)
          .returns("john@example.com", Email::normalized);
      assertThat(emails.get(1))
          .returns(true, Email::hasIdentifier)
          .returns("jane@example.org", Email::normalized);
    }
  }

}
