package com.sanctionco.jmail;

import com.sanctionco.jmail.normalization.CaseOption;
import com.sanctionco.jmail.normalization.NormalizationOptions;

import java.util.stream.Stream;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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

    System.clearProperty("jmail.normalize.case");
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

    System.clearProperty("jmail.normalize.remove.subaddress");
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("provideValidForStripQuotes")
  void ensureNormalizedStripsQuotes(String address, String expected) {
    assertThat(Email.of(address))
          .isPresent().get()
          .returns(expected, email -> email.normalized(
              NormalizationOptions.builder().stripQuotes().build()));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("provideValidForLowerCase")
  void ensureNormalizedConvertsToLowerCase(String address, String expected) {
    assertThat(Email.of(address))
            .isPresent().get()
            .returns(expected, email -> email.normalized(
                NormalizationOptions.builder().adjustCase(CaseOption.LOWERCASE).build()));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("provideValidForDots")
  void ensureNormalizedRemovesDots(String address, String expected) {
    assertThat(Email.of(address))
            .isPresent().get()
            .returns(expected, email -> email.normalized(
                NormalizationOptions.builder().removeDots().build()));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("provideValidForSubAddress")
  void ensureNormalizedRemovesSubAddresses(String address, String expected) {
    assertThat(Email.of(address))
        .isPresent().get()
        .returns(expected, email -> email.normalized(
            NormalizationOptions.builder().removeSubAddress().build()));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("provideValidForSubAddressSeparator")
  void ensureNormalizedRemovesSubAddressesWithCustomSeparator(String address, String expected) {
    assertThat(Email.of(address))
        .isPresent().get()
        .returns(expected, email -> email.normalized(
            NormalizationOptions.builder().removeSubAddress("%%").build()));
  }

  @ParameterizedTest(name = "{0}")
  @CsvFileSource(resources = "/valid-addresses.csv", numLinesToSkip = 1)
  void ensureNormalizedStripsQuotesForAllValidAddresses(String address) {
    Email validated = Email.of(address).get();

    // This test only works if the local-part has room to add quotes and if the local-part does
    // not have comments
    assumeTrue(validated.localPart().length() < 63);
    assumeTrue(validated.localPartWithoutComments().length() == validated.localPart().length());

    String domain = validated.isIpAddress() ? "[" + validated.domain() + "]" : validated.domain();

    String quoted = "\"" + validated.localPart() + "\"@" + domain;

    assertThat(Email.of(quoted))
        .isPresent().get()
        .returns(validated.normalized(), email -> email.normalized(
            NormalizationOptions.builder().stripQuotes().build()));
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "\"test..1\"@example.org", "\"\"@test.com", "\"first@last\"@test.org",
      "\"Fred Bloggs\"@test.org", "\"[[ test ]]\"@test.org", "\"Abc\\␀def\"@test.org",
      "first.\"\".last@test.org", "\"first(abc)\".last@test.org"})
  void ensureNormalizedDoesNotStripQuotesIfInvalid(String address) {
    assertThat(Email.of(address))
        .isPresent().get()
        .returns(address, email -> email.normalized(
            NormalizationOptions.builder().stripQuotes().build()));
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
        Arguments.of("\"first .last  \"@test .org", "first .last  @test .org"),
        Arguments.of("\"hello\\(world\"@test.com", "hello\\(world@test.com")
    );
  }

  static Stream<Arguments> provideValidForLowerCase() {
    return Stream.of(
            Arguments.of("TEST.1@example.org", "test.1@example.org"),
            Arguments.of("emAil@example.com", "email@example.com"),
            Arguments.of("first\\\\Last@test.org", "first\\\\last@test.org"),
            Arguments.of("AbC\\@dEf@test.org", "abc\\@def@test.org"),
            Arguments.of("Fred\\ Bloggs@test.org", "fred\\ bloggs@test.org"),
            Arguments.of("first.Middle.last@test.org", "first.middle.last@test.org"),
            Arguments.of("tESt.1@example.org", "test.1@example.org"),
            Arguments.of("tesT. 1@example.org", "test. 1@example.org"),
            Arguments.of("fiRst .Last  @test .org", "first .last  @test .org")
    );
  }

  static Stream<Arguments> provideValidForDots() {
    return Stream.of(
            Arguments.of("t.es.t@example.org", "test@example.org"),
            Arguments.of("f.i.r.s.t@test.com", "first@test.com"),
            Arguments.of("O.r.w.e.l.l@test.com", "Orwell@test.com"),
            Arguments.of("e.ma.il@example.com", "email@example.com")
    );
  }

  static Stream<Arguments> provideValidForSubAddress() {
    return Stream.of(
        Arguments.of("test+sample@example.org", "test@example.org"),
        Arguments.of("First.last+hello@test.com", "First.last@test.com"),
        Arguments.of("Oswald@test.com", "Oswald@test.com"),
        Arguments.of("woah_dude-test@example.com", "woah_dude-test@example.com")
    );
  }

  static Stream<Arguments> provideValidForSubAddressSeparator() {
    return Stream.of(
        Arguments.of("test%%sample@example.org", "test@example.org"),
        Arguments.of("First.last%%hello@test.com", "First.last@test.com"),
        Arguments.of("Oswald@test.com", "Oswald@test.com"),
        Arguments.of("woah_dude-test@example.com", "woah_dude-test@example.com"),
        Arguments.of("hello+test@example.com", "hello+test@example.com")
    );
  }
}
