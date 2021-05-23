package com.sanctionco.jmail;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
}
