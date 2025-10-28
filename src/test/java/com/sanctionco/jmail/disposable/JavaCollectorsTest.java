package com.sanctionco.jmail.disposable;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JavaCollectorsTest {

  @Nested
  class ToUnmodifiableSet {

    @Test
    void producesUniqueItems() {
      Set<String> set = Stream.of("a", "b", "c", "b")
          .collect(JavaCollectors.toUnmodifiableSet());

      assertEquals(Set.of("a", "b", "c"), set);
    }

    @Test
    void producesUnmodifiableSet() {
      Set<String> set = Stream.of("a", "b", "c")
          .collect(JavaCollectors.toUnmodifiableSet());

      assertThrows(
          UnsupportedOperationException.class,
          () -> set.add("d"),
          "set.add(x) should throw"
      );
    }

    @Test
    void combinesParallelStreams() {
      Set<String> set = Stream.of("a", "b", "c", "b")
          .parallel()
          .collect(JavaCollectors.toUnmodifiableSet());

      assertEquals(Set.of("a", "b", "c"), set);
    }
  }
}