package com.sanctionco.jmail;

import java.util.stream.Stream;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class FailureReasonTest {

  public static Stream<Arguments> provideTestEmails() {
    return Stream.of(
        Arguments.of("A@", FailureReason.ADDRESS_TOO_SHORT),
        Arguments.of("@my.test.com", FailureReason.BEGINS_WITH_AT_SYMBOL),
        Arguments.of("te[st@test.com", FailureReason.DISALLOWED_UNQUOTED_CHARACTER),
        Arguments.of("test@", FailureReason.DOMAIN_MISSING),
        Arguments.of("test@test-.com", FailureReason.DOMAIN_PART_ENDS_WITH_DASH),
        Arguments.of("email@example.com-", FailureReason.DOMAIN_PART_ENDS_WITH_DASH),
        Arguments.of("test@my.-test.com", FailureReason.DOMAIN_PART_STARTS_WITH_DASH),
        Arguments.of(
            "first.last@x234567890123456789012345678901234567890123456789012345678901234.test.org",
            FailureReason.DOMAIN_PART_TOO_LONG),
        Arguments.of("x@x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789."
            + "x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789."
            + "x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789."
            + "x23456789.x23456789.x23456", FailureReason.DOMAIN_TOO_LONG),
        Arguments.of("test@test.", FailureReason.ENDS_WITH_DOT),
        Arguments.of("test@test.com(i", FailureReason.INVALID_COMMENT),
        Arguments.of("test(comment)hey@test.com", FailureReason.INVALID_COMMENT_LOCATION),
        Arguments.of("user@my_example.com", FailureReason.INVALID_DOMAIN_CHARACTER),
        Arguments.of("test@[1.2]", FailureReason.INVALID_IP_DOMAIN),
        Arguments.of("just\"not\"right@example.com", FailureReason.INVALID_QUOTE_LOCATION),
        Arguments.of("this isnotallowed@example.com", FailureReason.INVALID_WHITESPACE),
        Arguments.of("test.@test.com", FailureReason.LOCAL_PART_ENDS_WITH_DOT),
        Arguments.of("()@test.com", FailureReason.LOCAL_PART_MISSING),
        Arguments.of(
            "1234567890123456789012345678901234567890123456789012345678901234+x@e.com",
            FailureReason.LOCAL_PART_TOO_LONG),
        Arguments.of("Abc.example.com", FailureReason.MISSING_AT_SYMBOL),
        Arguments.of("\"test\rblah\"@test.org", FailureReason.MISSING_BACKSLASH_ESCAPE),
        Arguments.of("test@test.(comment)", FailureReason.MISSING_FINAL_DOMAIN_PART),
        Arguments.of("A@b@c@example.com", FailureReason.MULTIPLE_AT_SYMBOLS),
        Arguments.of("test..mytest@test.com", FailureReason.MULTIPLE_DOT_SEPARATORS),
        Arguments.of(null, FailureReason.NULL_ADDRESS),
        Arguments.of("test@test.123", FailureReason.NUMERIC_TLD),
        Arguments.of(".test@test.com", FailureReason.STARTS_WITH_DOT),
        Arguments.of("test@really.long.topleveldomainisnotallowedunfortunatelyforpeople"
            + "wholikereallylongtopleveldomainnames", FailureReason.TOP_LEVEL_DOMAIN_TOO_LONG),
        Arguments.of("te<st@test.com", FailureReason.UNQUOTED_ANGLED_BRACKET),
        Arguments.of("te\\st@test.com", FailureReason.UNUSED_BACKSLASH_ESCAPE));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("provideTestEmails")
  void ensureFailureReasonsMatch(String email, FailureReason failureReason) {
    EmailValidationResult result = JMail.validate(email);

    assertThat(result.isFailure()).isTrue();
    assertThat(result.getFailureReason()).isEqualTo(failureReason);
  }

  @Test
  void ensureEqualsContract() {
    EqualsVerifier.forClass(FailureReason.class).verify();
  }
}
