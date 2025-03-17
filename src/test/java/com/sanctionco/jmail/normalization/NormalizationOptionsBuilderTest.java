package com.sanctionco.jmail.normalization;

import java.text.Normalizer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NormalizationOptionsBuilderTest {

  @Test
  void ensureDefaults() {
    assertThat(NormalizationOptions.builder().build())
        .returns(true, NormalizationOptions::shouldStripQuotes)
        .returns(CaseOption.LOWERCASE, NormalizationOptions::getCaseOption)
        .returns(false, NormalizationOptions::shouldRemoveDots)
        .returns(false, NormalizationOptions::shouldRemoveSubAddress)
        .returns("+", NormalizationOptions::getSubAddressSeparator)
        .returns(false, NormalizationOptions::shouldPerformUnicodeNormalization)
        .returns(Normalizer.Form.NFKC, NormalizationOptions::getUnicodeNormalizationForm);
  }

  @Test
  void ensureValuesAreProperlySet() {
    NormalizationOptions options = NormalizationOptions.builder()
        .removeDots()
        .removeSubAddress("-")
        .keepQuotes()
        .adjustCase(CaseOption.UPPERCASE)
        .performUnicodeNormalization(Normalizer.Form.NFC)
        .build();

    assertThat(options)
        .returns(false, NormalizationOptions::shouldStripQuotes)
        .returns(CaseOption.UPPERCASE, NormalizationOptions::getCaseOption)
        .returns(true, NormalizationOptions::shouldRemoveDots)
        .returns(true, NormalizationOptions::shouldRemoveSubAddress)
        .returns("-", NormalizationOptions::getSubAddressSeparator)
        .returns(true, NormalizationOptions::shouldPerformUnicodeNormalization)
        .returns(Normalizer.Form.NFC, NormalizationOptions::getUnicodeNormalizationForm);
  }
}
