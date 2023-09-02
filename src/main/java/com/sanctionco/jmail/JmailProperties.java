package com.sanctionco.jmail;

class JmailProperties {
  private static final String STRIP_QUOTES = "jmail.normalize.strip.quotes";

  private JmailProperties() {
  }

  static boolean stripQuotes() {
    return Boolean.parseBoolean(System.getProperty(STRIP_QUOTES, "false"));
  }
}
