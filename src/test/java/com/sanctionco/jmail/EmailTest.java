package com.sanctionco.jmail;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class EmailTest {

  @Test
  void equalsAndHashCodeTest() {
    Email email = JMail.tryParse("test@domain.com")
        .orElseGet(() -> fail("Unable to parse test email"));
    Email sameEmail = JMail.tryParse("test@domain.com")
        .orElseGet(() -> fail("Unable to parse test email"));
    Email differentEmail = JMail.tryParse("hello@world.com")
        .orElseGet(() -> fail("Unable to parse test email"));
    Email differentEmailOnlyDomain = JMail.tryParse("test@world.com")
        .orElseGet(() -> fail("Unable to parse test email"));

    assertThat(email)
        .isEqualTo(email)
        .isEqualTo(sameEmail)
        .isNotEqualTo(new Object())
        .isNotEqualTo(differentEmail)
        .isNotEqualTo(differentEmailOnlyDomain);

    assertThat(email)
        .hasSameHashCodeAs(email)
        .hasSameHashCodeAs(sameEmail)
        .doesNotHaveSameHashCodeAs(differentEmail)
        .doesNotHaveSameHashCodeAs(differentEmailOnlyDomain);
  }
}
