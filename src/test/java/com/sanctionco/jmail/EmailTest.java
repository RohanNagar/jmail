package com.sanctionco.jmail;

import java.util.stream.Stream;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

  @Test
  void ensureEqualsContract() {
    EqualsVerifier.forClass(Email.class).verify();
  }

  @Test
  void staticConstructorParses() {
    assertThat(Email.of("user@my.domain.com"))
        .isPresent().get()
        .hasToString("user@my.domain.com");

    assertThat(Email.of("invalid"))
        .isNotPresent();
  }

  @Test
  void ensureNormalizedIsCorrectForIpAddressEmail() {
    String address = "aaa@[123.123.123.123]";

    assertThat(Email.of(address))
        .isPresent().get()
        .returns(address, Email::normalized);
  }

  @Test
  void ensureNormalizedIsCorrectForLongComment() {
    String address = "first(Welcome to&#13;&#10; the (\"wonderful\" (!)) world&#13;&#10; of email)"
        + "@test.org";

    assertThat(Email.of(address))
        .isPresent().get()
        .returns("first@test.org", Email::normalized);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("provideValidForStripQuotes")
  void ensureNormalizedStripsQuotes(String address, String expected) {
    assertThat(Email.of(address))
          .isPresent().get()
          .returns(expected, email -> email.normalized(true));
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "\"test..1\"@example.org", "\"\"@test.com", "\"first@last\"@test.org",
      "\"Fred Bloggs\"@test.org", "\"[[ test ]]\"@test.org", "\"Abc\\␀def\"@test.org",
      "first.\"\".last@test.org"})
  void ensureNormalizedDoesNotStripQuotesIfInvalid(String address) {
    assertThat(Email.of(address))
        .isPresent().get()
        .returns(address, email -> email.normalized(true));
  }

  static Stream<Arguments> provideValidForStripQuotes() {
    return Stream.of(
        Arguments.of("\"test.1\"@example.org", "test.1@example.org"),
        Arguments.of("\"email\"@example.com", "email@example.com"),
        Arguments.of("\"first\\\\last\"@test.org", "first\\\\last@test.org"),
        Arguments.of("\"Abc\\@def\"@test.org", "Abc\\@def@test.org"),
        Arguments.of("\"Fred\\ Bloggs\"@test.org", "Fred\\ Bloggs@test.org"),
        Arguments.of("\"first\".middle.\"last\"@test.org", "first.middle.last@test.org"),
        Arguments.of("\"first..middle\".\"last\"@test.org", "\"first..middle\".last@test.org"),
        Arguments.of("\"first.middle\".\"\"@test.org", "first.middle.\"\"@test.org"),
        Arguments.of("test.\"1\"@example.org", "test.1@example.org"),
        Arguments.of("\"test. 1\"@example.org", "test. 1@example.org"),
        Arguments.of("\"first .last  \"@test .org", "first .last  @test .org")
    );
  }
}
