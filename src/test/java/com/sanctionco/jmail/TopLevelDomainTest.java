package com.sanctionco.jmail;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("JUnit5MalformedParameterized")
class TopLevelDomainTest {

  @ParameterizedTest
  @MethodSource("provideArgs")
  void ensureFromStringWorks(TopLevelDomain tld, String from) {
    assertEquals(tld, TopLevelDomain.fromString(from));
  }

  @SuppressWarnings("unused")
  Stream<Arguments> provideArgs() {
    return Arrays.stream(TopLevelDomain.values())
        .filter(tld -> !tld.equals(TopLevelDomain.NONE) && !tld.equals(TopLevelDomain.OTHER))
        .map(tld -> Arguments.of(tld, tld.stringValue()));
  }

  @Test
  void ensureNoneWorks() {
    assertEquals(TopLevelDomain.NONE, TopLevelDomain.fromString(""));
    assertEquals(TopLevelDomain.NONE, TopLevelDomain.fromString(null));
  }

  @Test
  void ensureOtherWorks() {
    assertEquals(TopLevelDomain.OTHER, TopLevelDomain.fromString("unknown"));
    assertEquals(TopLevelDomain.OTHER, TopLevelDomain.fromString("?"));
  }
}
