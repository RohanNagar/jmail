package com.sanctionco.jmail;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SuppressWarnings("JUnit5MalformedParameterized")
class TopLevelDomainTest {

  @SuppressWarnings("unused")
  Stream<Arguments> provideArgs() {
    return Stream.of(
        TopLevelDomain.DOT_COM,
        TopLevelDomain.DOT_EDU,
        TopLevelDomain.DOT_GOV,
        TopLevelDomain.DOT_INT,
        TopLevelDomain.DOT_MIL,
        TopLevelDomain.DOT_NET
    ).map(tld -> Arguments.of(tld, tld.stringValue()));
  }

  @ParameterizedTest
  @MethodSource("provideArgs")
  void ensureFromStringWorks(TopLevelDomain tld, String from) {
    assertThat(tld).as("check fromString construction of %s", tld)
        .isEqualTo(TopLevelDomain.fromString(from));
  }

  @Test
  void ensureOtherTldWork() {
    assertThat(TopLevelDomain.fromString("unknown"))
        .isInstanceOf(TopLevelDomain.class)
        .isEqualTo(TopLevelDomain.fromString("unknown"))
        .returns("unknown", TopLevelDomain::stringValue);

    assertThat(TopLevelDomain.fromString("?"))
        .isInstanceOf(TopLevelDomain.class)
        .isEqualTo(TopLevelDomain.fromString("?"))
        .returns("?", TopLevelDomain::stringValue);

    assertThat(TopLevelDomain.fromString("unknown"))
        .isNotEqualTo(TopLevelDomain.fromString("?"));
  }

  @Test
  void ensureEmailComparesToNull() {
    assertThat(TopLevelDomain.fromString("abc"))
        .isNotNull();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      ".", "-com", "com-", "test.invalid.tld", "1111", "",
      "x234567890123456789012345678901234567890123456789012345678901234"})
  void ensureFromStringRejectsInvalidTlds(String tld) {
    assertThatExceptionOfType(InvalidTopLevelDomainException.class)
        .isThrownBy(() -> TopLevelDomain.fromString(tld));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void ensureNullStringThrows() {
    assertThatExceptionOfType(InvalidTopLevelDomainException.class)
        .isThrownBy(() -> TopLevelDomain.fromString(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "c-om", "co-m", "11y1", "a", ".a",
      "x23456789012345678901234567890123456789012345678901234567890123"})
  void ensureFromStringAllowsNearlyInvalidTlds(String tld) {
    assertThatNoException().isThrownBy(() -> TopLevelDomain.fromString(tld));
  }

  @Test
  void ensureFromStringAllowsLeadingDot() {
    assertThat(TopLevelDomain.fromString(".com"))
        .isEqualTo(TopLevelDomain.DOT_COM);

    assertThat(TopLevelDomain.fromString(".net"))
        .isEqualTo(TopLevelDomain.DOT_NET);
  }
}
