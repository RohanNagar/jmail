package com.sanctionco.jmail;

import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enumerates known common top level domains.
 */
public enum TopLevelDomain {
  // Original TLDs
  // https://en.wikipedia.org/wiki/List_of_Internet_top-level_domains#Original_top-level_domains
  DOT_COM("com"),
  DOT_ORG("org"),
  DOT_NET("net"),
  DOT_INT("int"),
  DOT_EDU("edu"),
  DOT_GOV("gov"),
  DOT_MIL("mil"),

  // To use when an email address does not have a top level domain
  NONE(""),

  // To use when the TLD is unknown
  OTHER(" ");

  private final String tld;

  TopLevelDomain(String tld) {
    this.tld = tld;
  }

  String stringValue() {
    return tld;
  }

  private static final Map<String, TopLevelDomain> MAP = Stream
      .of(TopLevelDomain.values())
      .collect(Collectors.toMap(TopLevelDomain::stringValue, UnaryOperator.identity()));

  /**
   * Get the {@code TopLevelDomain} that represents the given string.
   *
   * @param tld the string to turn into a {@code TopLevelDomain}
   * @return the {@code TopLevelDomain}, or {@code TopLevelDomain.OTHER} if it is unknown
   */
  public static TopLevelDomain fromString(String tld) {
    if (tld == null || tld.isEmpty()) return NONE;

    return MAP.getOrDefault(tld, OTHER);
  }
}
