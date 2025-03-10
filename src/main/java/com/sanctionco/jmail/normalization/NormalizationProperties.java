package com.sanctionco.jmail.normalization;

class NormalizationProperties {
  private static final String CASE_OPTION = "jmail.normalize.case";
  private static final String REMOVE_DOTS = "jmail.normalize.remove.dots";
  private static final String STRIP_QUOTES = "jmail.normalize.strip.quotes";

  private NormalizationProperties() {
  }

  static boolean stripQuotes() {
    return Boolean.parseBoolean(System.getProperty(STRIP_QUOTES, "false"));
  }

  static CaseOption caseOption() {
    return CaseOption.valueOf(System.getProperty(CASE_OPTION, "NO_CHANGE"));
  }

  static boolean removeDots() {
    return Boolean.parseBoolean(System.getProperty(REMOVE_DOTS, "false"));
  }
}
