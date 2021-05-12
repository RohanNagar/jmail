package com.sanctionco.jmail;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EmailTest {

  @Test
  void equalsAndHashCodeTest() {
    Email email = new Email(
        "test", "test",
        "domain.com", "domain.com",
        Arrays.asList("domain", "com"), Collections.emptyList(), false);
    Email sameEmail = new Email(
        "test", "test",
        "domain.com", "domain.com",
        Arrays.asList("domain", "com"), Collections.emptyList(), false);
    Email differentEmail = new Email(
        "hello", "hello",
        "world.com", "world.com",
        Arrays.asList("world", "com"), Collections.emptyList(), false);

    assertAll("equals() works",
        () -> assertEquals(email, email),
        () -> assertEquals(email, sameEmail),
        () -> assertNotEquals(email, new Object()),
        () -> assertNotEquals(email, differentEmail));

    assertAll("hashCode() works",
        () -> assertEquals(email.hashCode(), email.hashCode()),
        () -> assertEquals(email.hashCode(), sameEmail.hashCode()),
        () -> assertNotEquals(email.hashCode(), differentEmail.hashCode()));
  }
}
