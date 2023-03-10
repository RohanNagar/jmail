package com.sanctionco.jmail;

/**
 * Enumerates all possible reasons that an email address can fail validation.
 */
public enum FailureReason {

  /**
   * An email address cannot be longer than 320 characters.
   */
  ADDRESS_TOO_LONG,

  /**
   * An email address cannot be shorter than 3 characters. There must be at least one
   * character in the local-part, an {@code '@'} symbol, and at least one character
   * in the domain.
   */
  ADDRESS_TOO_SHORT,

  /**
   * An email address cannot begin with the {@code '@'} symbol, unless it has valid source
   * routing at the start of the address.
   */
  BEGINS_WITH_AT_SYMBOL,

  /**
   * Certain characters are not allowed to appear within the local-part of an email address
   * unless they are quoted. The set of characters that are not allowed outside of quotes is:
   * {@code '\t', '(', ')', ',', ':', ';', '<', '>', '@', '[', ']', '"'}.
   */
  DISALLOWED_UNQUOTED_CHARACTER,

  /**
   * An email address must contain a domain.
   */
  DOMAIN_MISSING,

  /**
   * Any single domain part (separated by dots) of am email address cannot end with the
   * {@code '-'} character.
   */
  DOMAIN_PART_ENDS_WITH_DASH,

  /**
   * Any single domain part (separated by dots) of am email address cannot start with the
   * {@code '-'} character.
   */
  DOMAIN_PART_STARTS_WITH_DASH,

  /**
   * Any single domain part (separated by dots) of am email address cannot be more than
   * 63 characters.
   */
  DOMAIN_PART_TOO_LONG,

  /**
   * The domain of an email address cannot be more than 255 characters.
   */
  DOMAIN_TOO_LONG,

  /**
   * An email address cannot end with the {@code '.'} character.
   */
  ENDS_WITH_DOT,

  /**
   * If an email address fails custom validation that was added to an {@link EmailValidator},
   * then this failure reason indicates that the email address failed custom validation.
   */
  FAILED_CUSTOM_VALIDATION,

  /**
   * A comment within an email address should have surrounding parenthesis. If it does not,
   * for example there is no closing parenthesis, then the email address is invalid.
   */
  INVALID_COMMENT,

  /**
   * A comment within an email address should be dot-separated from other parts of the address.
   */
  INVALID_COMMENT_LOCATION,

  /**
   * The domain of an email address can only contain alphanumeric characters, as well as the
   * characters {@code '.', '-'} and whitespace. This failure reason indicates that a character
   * was found in the domain that is not allowed.
   */
  INVALID_DOMAIN_CHARACTER,

  /**
   * An email address can use an IP address for the domain. The IP address must be in the format
   * {@code [IPV4_addr]} or {@code [IPV6_addr]}. This failure reason indicates that the address
   * attempted to use an IP address domain, but it has either an invalid format or an
   * invalid IP address.
   */
  INVALID_IP_DOMAIN,

  /**
   * Quoted parts within an email address must be dot-separated from other parts of the address.
   */
  INVALID_QUOTE_LOCATION,

  /**
   * Whitespace is only allowed in an email address if it is between parts or the address has
   * an identifier.
   */
  INVALID_WHITESPACE,

  /**
   * The local-part of an email address cannot end with the {@code '.'} character.
   */
  LOCAL_PART_ENDS_WITH_DOT,

  /**
   * An email address must contain a local-part of at least one character.
   */
  LOCAL_PART_MISSING,

  /**
   * The local-part of an email address cannot be more than 64 characters.
   */
  LOCAL_PART_TOO_LONG,

  /**
   * An email address must contain the at {@code '@'} symbol exactly once.
   */
  MISSING_AT_SYMBOL,

  /**
   * The characters {@code '\r', '␀', '\n'} are allowed within quotes only if they are escaped
   * with a backslash. This failure reason indicates that the backslash was missing in the address.
   */
  MISSING_BACKSLASH_ESCAPE,

  /**
   * An email address must contain a top level domain, the final part of the domain.
   *
   * @see TopLevelDomain
   */
  MISSING_TOP_LEVEL_DOMAIN,

  /**
   * An email address can only have a single unquoted {@code '@'} symbol. Multiple
   * {@code '@'} symbols can only appear within quoted parts, or escaped with a preceding
   * {@code '\'} character.
   */
  MULTIPLE_AT_SYMBOLS,

  /**
   * An email address cannot have two consecutive dot {@code '.'} characters outside of quotes.
   */
  MULTIPLE_DOT_SEPARATORS,

  /**
   * Indicates no validation failure.
   */
  NONE,

  /**
   * An email address cannot be {@code null}.
   */
  NULL_ADDRESS,

  /**
   * The TLD of an email address cannot be all numeric (ex: {@code test@hello.123}).
   */
  NUMERIC_TLD,

  /**
   * An email address cannot start with the {@code '.'} character.
   */
  STARTS_WITH_DOT,

  /**
   * The top level domain of an email address cannot be more than 63 characters.
   */
  TOP_LEVEL_DOMAIN_TOO_LONG,

  /**
   * An email address cannot contain the {@code '<'} character outside of quotes,
   * unless the address has an identifier.
   */
  UNQUOTED_ANGLED_BRACKET,

  /**
   * A backslash {@code '\'} within the local-part of an email address must be used to escape
   * a character, it cannot exist on its own.
   */
  UNUSED_BACKSLASH_ESCAPE,
}
