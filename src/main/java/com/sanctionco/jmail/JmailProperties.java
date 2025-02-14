package com.sanctionco.jmail;

class JmailProperties {
  private static final String STRIP_QUOTES = "jmail.normalize.strip.quotes";
  private static final String LOWER_CASE = "jmail.normalize.lower.case";
  private static final String LOWER_CASE_LOCAL_PART = "jmail.normalize.lower.case.localpart";
  private static final String LOWER_CASE_DOMAIN = "jmail.normalize.lower.case.domain";
  private static final String REMOVE_DOTS = "jmail.normalize.remove.dots";

  private JmailProperties() {
  }

  static boolean stripQuotes() {
    return Boolean.parseBoolean(System.getProperty(STRIP_QUOTES, "false"));
  }

  static boolean lowerCase() {
    return Boolean.parseBoolean(System.getProperty(LOWER_CASE, "false"));
  }

  static boolean lowerCaseLocalPart() {
    return Boolean.parseBoolean(System.getProperty(LOWER_CASE_LOCAL_PART, "true"));
  }

  static boolean lowerCaseDomain() {
    return Boolean.parseBoolean(System.getProperty(LOWER_CASE_DOMAIN, "true"));
  }

  static boolean removeDots() {
    return Boolean.parseBoolean(System.getProperty(REMOVE_DOTS, "false"));
  }
}
