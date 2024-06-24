package com.sanctionco.jmail;

class JmailProperties {
  private static final String STRIP_QUOTES = "jmail.normalize.strip.quotes";
  private static final String LOWER_CASE = "jmail.normalize.lower.case";

  private JmailProperties() {
  }

  static boolean stripQuotes() {
    return Boolean.parseBoolean(System.getProperty(STRIP_QUOTES, "false"));
  }

  static boolean lowerCase() {
    return Boolean.parseBoolean(System.getProperty(LOWER_CASE, "true"));
  }
}
