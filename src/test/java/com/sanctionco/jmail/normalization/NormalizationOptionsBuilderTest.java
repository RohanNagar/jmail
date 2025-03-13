package com.sanctionco.jmail.normalization;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NormalizationOptionsBuilderTest {

  @Test
  void ensureDefaults() {
    assertThat(NormalizationOptions.builder().build())
        .returns(false, NormalizationOptions::shouldStripQuotes)
        .returns(CaseOption.NO_CHANGE, NormalizationOptions::getCaseOption)
        .returns(false, NormalizationOptions::shouldRemoveDots)
        .returns(false, NormalizationOptions::shouldRemoveSubAddress)
        .returns("+", NormalizationOptions::getSubAddressSeparator);
  }

  @Test
  void ensureValuesAreProperlySet() {
    NormalizationOptions options = NormalizationOptions.builder()
        .removeDots()
        .removeSubAddress("-")
        .stripQuotes()
        .adjustCase(CaseOption.UPPERCASE)
        .build();

    assertThat(options)
        .returns(true, NormalizationOptions::shouldStripQuotes)
        .returns(CaseOption.UPPERCASE, NormalizationOptions::getCaseOption)
        .returns(true, NormalizationOptions::shouldRemoveDots)
        .returns(true, NormalizationOptions::shouldRemoveSubAddress)
        .returns("-", NormalizationOptions::getSubAddressSeparator);
  }
}
