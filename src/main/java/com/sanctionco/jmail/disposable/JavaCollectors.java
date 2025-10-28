package com.sanctionco.jmail.disposable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;

/**
 * Contains convenient Stream collectors.
 * Internal usage only.
 */
class JavaCollectors {

  // As JMail supports java 8, then having this copy of Collectors.toUnmodifiableSet() from java 10+
  static <T> Collector<T, HashSet<T>, Set<T>> toUnmodifiableSet() {
    return Collector.of(
        HashSet::new,
        Set::add,
        (left, right) -> {
          left.addAll(right);
          return left;
        },
        Collections::unmodifiableSet
    );
  }
}
