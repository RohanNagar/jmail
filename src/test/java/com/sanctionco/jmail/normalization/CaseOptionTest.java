package com.sanctionco.jmail.normalization;

import com.sanctionco.jmail.Email;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CaseOptionTest {
  private static final String ADDRESS = "tEsT12@gMaIl.com";

  @Test
  void ensureNoChange() {
    assertThat(Email.of(ADDRESS))
        .isPresent().get()
        .returns("tEsT12@gMaIl.com", email -> email.normalized(
            NormalizationOptions.builder().adjustCase(CaseOption.NO_CHANGE).build()));
  }

  @Test
  void ensureLowercaseEntireAddress() {
    assertThat(Email.of(ADDRESS))
        .isPresent().get()
        .returns("test12@gmail.com", email -> email.normalized(
            NormalizationOptions.builder().adjustCase(CaseOption.LOWERCASE).build()));
  }

  @Test
  void ensureLowercaseLocalPartOnly() {
    assertThat(Email.of(ADDRESS))
        .isPresent().get()
        .returns("test12@gMaIl.com", email -> email.normalized(
            NormalizationOptions.builder()
                .adjustCase(CaseOption.LOWERCASE_LOCAL_PART_ONLY)
                .build()));
  }

  @Test
  void ensureLowercaseDomainOnly() {
    assertThat(Email.of(ADDRESS))
        .isPresent().get()
        .returns("tEsT12@gmail.com", email -> email.normalized(
            NormalizationOptions.builder().adjustCase(CaseOption.LOWERCASE_DOMAIN_ONLY).build()));
  }

  @Test
  void ensureUppercaseEntireAddress() {
    assertThat(Email.of(ADDRESS))
        .isPresent().get()
        .returns("TEST12@GMAIL.COM", email -> email.normalized(
            NormalizationOptions.builder().adjustCase(CaseOption.UPPERCASE).build()));
  }

  @Test
  void ensureUppercaseLocalPartOnly() {
    assertThat(Email.of(ADDRESS))
        .isPresent().get()
        .returns("TEST12@gMaIl.com", email -> email.normalized(
            NormalizationOptions.builder()
                .adjustCase(CaseOption.UPPERCASE_LOCAL_PART_ONLY)
                .build()));
  }

  @Test
  void ensureUppercaseDomainOnly() {
    assertThat(Email.of(ADDRESS))
        .isPresent().get()
        .returns("tEsT12@GMAIL.COM", email -> email.normalized(
            NormalizationOptions.builder().adjustCase(CaseOption.UPPERCASE_DOMAIN_ONLY).build()));
  }
}
