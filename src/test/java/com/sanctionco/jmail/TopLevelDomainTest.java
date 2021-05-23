package com.sanctionco.jmail;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("JUnit5MalformedParameterized")
class TopLevelDomainTest {

  @ParameterizedTest
  @MethodSource("provideArgs")
  void ensureFromStringWorks(TopLevelDomain tld, String from) {
    assertEquals(tld, TopLevelDomain.fromString(from));
  }

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

  @Test
  void ensureNoneWorks() {
    assertEquals(TopLevelDomain.NONE, TopLevelDomain.fromString(""));
    assertEquals(TopLevelDomain.NONE, TopLevelDomain.fromString(null));
  }

  @Test
  void ensureOtherTldWork() {
    assertEquals(TopLevelDomain.fromString("unknown"), TopLevelDomain.fromString("unknown"));
    assertEquals(TopLevelDomain.fromString("?"), TopLevelDomain.fromString("?"));

    assertEquals("unknown", TopLevelDomain.fromString("unknown").stringValue());
    assertEquals("?", TopLevelDomain.fromString("?").stringValue());

    assertNotEquals(TopLevelDomain.fromString("unknown"), TopLevelDomain.fromString("?"));
  }

  @Test
  void ensureEmailComparesToNull() {
    assertNotEquals(TopLevelDomain.fromString("abc"), null);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      ".", "-com", "com-", "test.invalid.tld", "1111",
      "x234567890123456789012345678901234567890123456789012345678901234"})
  void ensureFromStringRejectsInvalidTlds(String tld) {
    assertThrows(InvalidTopLevelDomainException.class, () -> TopLevelDomain.fromString(tld));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "c-om", "co-m", "11y1", "a", ".a",
      "x23456789012345678901234567890123456789012345678901234567890123"})
  void ensureFromStringAllowsNearlyInvalidTlds(String tld) {
    assertDoesNotThrow(() -> TopLevelDomain.fromString(tld));
  }

  @Test
  void ensureFromStringAllowsLeadingDot() {
    assertDoesNotThrow(() -> TopLevelDomain.fromString(".com"));
    assertEquals(TopLevelDomain.DOT_COM, TopLevelDomain.fromString(".com"));

    assertDoesNotThrow(() -> TopLevelDomain.fromString(".net"));
    assertEquals(TopLevelDomain.DOT_NET, TopLevelDomain.fromString(".net"));
  }
}
