package com.sanctionco.jmail;

import java.util.Objects;

/**
 * Enumerates all possible reasons that an email address can fail validation.
 */
public final class FailureReason {

  /**
   * An email address cannot be longer than 320 characters.
   */
  public static final FailureReason ADDRESS_TOO_LONG
      = new FailureReason("ADDRESS_TOO_LONG");

  /**
   * An email address cannot be shorter than 3 characters. There must be at least one
   * character in the local-part, an {@code '@'} symbol, and at least one character
   * in the domain.
   */
  public static final FailureReason ADDRESS_TOO_SHORT
      = new FailureReason("ADDRESS_TOO_SHORT");

  /**
   * An email address cannot begin with the {@code '@'} symbol, unless it has valid source
   * routing at the start of the address.
   */
  public static final FailureReason BEGINS_WITH_AT_SYMBOL
      = new FailureReason("BEGINS_WITH_AT_SYMBOL");

  /**
   * Certain characters are not allowed to appear within the local-part of an email address
   * unless they are quoted. The set of characters that are not allowed outside of quotes is:
   * {@code '\t', '(', ')', ',', ':', ';', '<', '>', '@', '[', ']', '"'}.
   */
  public static final FailureReason DISALLOWED_UNQUOTED_CHARACTER
      = new FailureReason("DISALLOWED_UNQUOTED_CHARACTER");

  /**
   * An email address must contain a domain.
   */
  public static final FailureReason DOMAIN_MISSING
      = new FailureReason("DOMAIN_MISSING");

  /**
   * Any single domain part (separated by dots) of am email address cannot end with the
   * {@code '-'} character.
   */
  public static final FailureReason DOMAIN_PART_ENDS_WITH_DASH
      = new FailureReason("DOMAIN_PART_ENDS_WITH_DASH");

  /**
   * Any single domain part (separated by dots) of am email address cannot start with the
   * {@code '-'} character.
   */
  public static final FailureReason DOMAIN_PART_STARTS_WITH_DASH
      = new FailureReason("DOMAIN_PART_STARTS_WITH_DASH");

  /**
   * Any single domain part (separated by dots) of am email address cannot be more than
   * 63 characters.
   */
  public static final FailureReason DOMAIN_PART_TOO_LONG
      = new FailureReason("DOMAIN_PART_TOO_LONG");

  /**
   * The domain of an email address cannot be more than 255 characters.
   */
  public static final FailureReason DOMAIN_TOO_LONG
      = new FailureReason("DOMAIN_TOO_LONG");

  /**
   * An email address cannot end with the {@code '.'} character.
   */
  public static final FailureReason ENDS_WITH_DOT
      = new FailureReason("ENDS_WITH_DOT");

  /**
   * If an email address fails custom validation that was added to an {@link EmailValidator},
   * then this failure reason indicates that the email address failed custom validation.
   */
  public static final FailureReason FAILED_CUSTOM_VALIDATION
      = new FailureReason("FAILED_CUSTOM_VALIDATION");

  /**
   * A comment within an email address should have surrounding parenthesis. If it does not,
   * for example there is no closing parenthesis, then the email address is invalid.
   */
  public static final FailureReason INVALID_COMMENT
      = new FailureReason("INVALID_COMMENT");

  /**
   * A comment within an email address should be dot-separated from other parts of the address.
   */
  public static final FailureReason INVALID_COMMENT_LOCATION
      = new FailureReason("INVALID_COMMENT_LOCATION");

  /**
   * The domain of an email address can only contain alphanumeric characters, as well as the
   * characters {@code '.', '-'} and whitespace. This failure reason indicates that a character
   * was found in the domain that is not allowed.
   */
  public static final FailureReason INVALID_DOMAIN_CHARACTER
      = new FailureReason("INVALID_DOMAIN_CHARACTER");

  /**
   * An email address can use an IP address for the domain. The IP address must be in the format
   * {@code [IPV4_addr]} or {@code [IPV6_addr]}. This failure reason indicates that the address
   * attempted to use an IP address domain, but it has either an invalid format or an
   * invalid IP address.
   */
  public static final FailureReason INVALID_IP_DOMAIN
      = new FailureReason("INVALID_IP_DOMAIN");

  /**
   * Quoted parts within an email address must be dot-separated from other parts of the address.
   */
  public static final FailureReason INVALID_QUOTE_LOCATION
      = new FailureReason("INVALID_QUOTE_LOCATION");

  /**
   * Whitespace is only allowed in an email address if it is between parts or the address has
   * an identifier.
   */
  public static final FailureReason INVALID_WHITESPACE
      = new FailureReason("INVALID_WHITESPACE");

  /**
   * The local-part of an email address cannot end with the {@code '.'} character.
   */
  public static final FailureReason LOCAL_PART_ENDS_WITH_DOT
      = new FailureReason("LOCAL_PART_ENDS_WITH_DOT");

  /**
   * An email address must contain a local-part of at least one character.
   */
  public static final FailureReason LOCAL_PART_MISSING
      = new FailureReason("LOCAL_PART_MISSING");

  /**
   * The local-part of an email address cannot be more than 64 characters.
   */
  public static final FailureReason LOCAL_PART_TOO_LONG
      = new FailureReason("LOCAL_PART_TOO_LONG");

  /**
   * An email address must contain the at {@code '@'} symbol exactly once.
   */
  public static final FailureReason MISSING_AT_SYMBOL
      = new FailureReason("MISSING_AT_SYMBOL");

  /**
   * The characters {@code '\r', '␀', '\n'} are allowed within quotes only if they are escaped
   * with a backslash. This failure reason indicates that the backslash was missing in the address.
   */
  public static final FailureReason MISSING_BACKSLASH_ESCAPE
      = new FailureReason("MISSING_BACKSLASH_ESCAPE");

  /**
   * An email address must contain a top level domain, the final part of the domain.
   *
   * @see TopLevelDomain
   */
  public static final FailureReason MISSING_TOP_LEVEL_DOMAIN
      = new FailureReason("MISSING_TOP_LEVEL_DOMAIN");

  /**
   * An email address can only have a single unquoted {@code '@'} symbol. Multiple
   * {@code '@'} symbols can only appear within quoted parts, or escaped with a preceding
   * {@code '\'} character.
   */
  public static final FailureReason MULTIPLE_AT_SYMBOLS
      = new FailureReason("MULTIPLE_AT_SYMBOLS");

  /**
   * An email address cannot have two consecutive dot {@code '.'} characters outside of quotes.
   */
  public static final FailureReason MULTIPLE_DOT_SEPARATORS
      = new FailureReason("MULTIPLE_DOT_SEPARATORS");

  /**
   * Indicates no validation failure.
   */
  public static final FailureReason NONE
      = new FailureReason("NONE");

  /**
   * An email address cannot be {@code null}.
   */
  public static final FailureReason NULL_ADDRESS
      = new FailureReason("NULL_ADDRESS");

  /**
   * The TLD of an email address cannot be all numeric (ex: {@code test@hello.123}).
   */
  public static final FailureReason NUMERIC_TLD
      = new FailureReason("NUMERIC_TLD");

  /**
   * An email address cannot start with the {@code '.'} character.
   */
  public static final FailureReason STARTS_WITH_DOT
      = new FailureReason("STARTS_WITH_DOT");

  /**
   * The top level domain of an email address cannot be more than 63 characters.
   */
  public static final FailureReason TOP_LEVEL_DOMAIN_TOO_LONG
      = new FailureReason("TOP_LEVEL_DOMAIN_TOO_LONG");

  /**
   * An email address cannot contain the {@code '<'} character outside of quotes,
   * unless the address has an identifier.
   */
  public static final FailureReason UNQUOTED_ANGLED_BRACKET
      = new FailureReason("UNQUOTED_ANGLED_BRACKET");

  /**
   * A backslash {@code '\'} within the local-part of an email address must be used to escape
   * a character, it cannot exist on its own.
   */
  public static final FailureReason UNUSED_BACKSLASH_ESCAPE
      = new FailureReason("UNUSED_BACKSLASH_ESCAPE");

  private final String reason;

  public FailureReason(String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FailureReason)) return false;
    FailureReason that = (FailureReason) o;
    return Objects.equals(reason, that.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason);
  }

  @Override
  public String toString() {
    return reason;
  }
}
