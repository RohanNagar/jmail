package com.sanctionco.jmail;

/**
 * Enumerates all possible reasons that an email address can fail validation.
 */
public enum FailureReason {
  ADDRESS_TOO_LONG,
  ADDRESS_TOO_SHORT,
  BEGINS_WITH_AT_SYMBOL,
  ENDS_WITH_DOT,
  MULTIPLE_AT_SYMBOLS,
  NONE,
  NULL_ADDRESS,
  STARTS_WITH_DOT
}
