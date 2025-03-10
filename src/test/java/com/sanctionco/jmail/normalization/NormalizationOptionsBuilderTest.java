package com.sanctionco.jmail.normalization;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NormalizationOptionsBuilderTest {

  @BeforeAll
  void setup() {
    System.setProperty("jmail.normalize.strip.quotes", "true");
    System.setProperty("jmail.normalize.case", "UPPERCASE");
    System.setProperty("jmail.normalize.remove.dots", "true");
    System.setProperty("jmail.normalize.remove.subaddress", "true");
    System.setProperty("jmail.normalize.subaddress.separator", "-");
  }

  @AfterAll
  void cleanup() {
    System.clearProperty("jmail.normalize.strip.quotes");
    System.clearProperty("jmail.normalize.case");
    System.clearProperty("jmail.normalize.remove.dots");
    System.clearProperty("jmail.normalize.remove.subaddress");
    System.clearProperty("jmail.normalize.subaddress.separator");
  }

  @Test
  void ensureDefaultUsesSystemProperties() {
    assertThat(NormalizationOptions.builder().build())
        .returns(true, NormalizationOptions::shouldStripQuotes)
        .returns(CaseOption.UPPERCASE, NormalizationOptions::getCaseOption)
        .returns(true, NormalizationOptions::shouldRemoveDots)
        .returns(true, NormalizationOptions::shouldRemoveSubAddress)
        .returns("-", NormalizationOptions::getSubAddressSeparator);
  }
}
