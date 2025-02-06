package com.sanctionco.jmail;

class JmailProperties {
  private static final String STRIP_QUOTES = "jmail.normalize.strip.quotes";
  private static final String LOWER_CASE = "jmail.normalize.lower.case";
  private static final String DOTS = "jmail.normalize.dots";

  private JmailProperties() {
  }

  static boolean stripQuotes() {
    return Boolean.parseBoolean(System.getProperty(STRIP_QUOTES, "false"));
  }

  static boolean lowerCase() {
    return Boolean.parseBoolean(System.getProperty(LOWER_CASE, "false"));
  }

  static boolean dots() {
    return Boolean.parseBoolean(System.getProperty(DOTS, "false"));
  }
}
