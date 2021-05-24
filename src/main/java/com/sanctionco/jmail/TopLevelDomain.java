package com.sanctionco.jmail;

/**
 * Enumerates known common top level domains.
 */
public final class TopLevelDomain {
  // Original TLDs
  // https://en.wikipedia.org/wiki/List_of_Internet_top-level_domains#Original_top-level_domains
  public static final TopLevelDomain DOT_COM = new TopLevelDomain("com");
  public static final TopLevelDomain DOT_ORG = new TopLevelDomain("org");
  public static final TopLevelDomain DOT_NET = new TopLevelDomain("net");
  public static final TopLevelDomain DOT_INT = new TopLevelDomain("int");
  public static final TopLevelDomain DOT_EDU = new TopLevelDomain("edu");
  public static final TopLevelDomain DOT_GOV = new TopLevelDomain("gov");
  public static final TopLevelDomain DOT_MIL = new TopLevelDomain("mil");

  // To use when an email address does not have a top level domain
  public static final TopLevelDomain NONE = new TopLevelDomain("");

  private final String tld;

  private TopLevelDomain(String tld) {
    this.tld = tld;
  }

  String stringValue() {
    return tld;
  }

  /**
   * Get the {@code TopLevelDomain} that represents the given string.
   *
   * @param tld the string to turn into a {@code TopLevelDomain}
   * @return a {@code TopLevelDomain} that represents the valid input
   * @throws InvalidTopLevelDomainException if the provided string is an invalid top level domain
   */
  public static TopLevelDomain fromString(String tld) {
    if (tld == null || tld.isEmpty()) throw new InvalidTopLevelDomainException();

    String dotless = tld.startsWith(".") ? tld.substring(1) : tld;

    if (!isValidTopLevelDomain(dotless)) throw new InvalidTopLevelDomainException();

    return new TopLevelDomain(dotless);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TopLevelDomain)) return false;
    TopLevelDomain that = (TopLevelDomain) o;
    return tld.equals(that.tld);
  }

  @Override
  public int hashCode() {
    return this.tld.hashCode();
  }

  @Override
  public String toString() {
    return "TopLevelDomain[tld='" + tld + "']";
  }

  private static boolean isValidTopLevelDomain(String domain) {
    // TLD cannot be more than 63 characters or empty
    if (domain.length() > 63 || domain.length() == 0) return false;

    // TLD cannot start or end with '-'
    if (domain.startsWith("-") || domain.endsWith("-")) return false;

    boolean isAllNumeric = true;

    for (int i = 0, size = domain.length(); i < size; i++) {
      char c = domain.charAt(i);

      // TLD cannot contain a dot
      if (c == '.') return false;

      if (!Character.isDigit(c)) isAllNumeric = false;
    }

    // TLD cannot be all numeric
    return !isAllNumeric;
  }
}
