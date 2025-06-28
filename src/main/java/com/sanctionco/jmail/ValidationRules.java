package com.sanctionco.jmail;

import com.sanctionco.jmail.disposable.DisposableDomainSource;
import com.sanctionco.jmail.dns.DNSLookupUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides common email address validation rules that can be added to an
 * {@link EmailValidator}.
 */
public final class ValidationRules {

  /**
   * Private constructor to prevent instantiation.
   */
  private ValidationRules() {
  }

  // Set of reserved TLDs according to RFC 2606, section 2
  // https://datatracker.ietf.org/doc/html/rfc2606
  private static final Set<String> reservedTopLevelDomains = new HashSet<>(
      Arrays.asList("test", "invalid", "example", "localhost"));

  // Set of reserved second level domains according to RFC 2606, section 3
  // Reserved second level domains are all "example.*" The values for * are defined here.
  private static final Set<TopLevelDomain> reservedExampleTlds = new HashSet<>(
      Arrays.asList(TopLevelDomain.DOT_COM, TopLevelDomain.DOT_NET, TopLevelDomain.DOT_ORG));

  /**
   * Rejects an email address that has an IP address as the domain. For example, the address
   * {@code "test@[12.34.56.78]"} would be rejected.
   *
   * @param email the email address to validate
   * @return true if this email address does not have an IP address domain, or false if it does
   */
  public static boolean disallowIpDomain(Email email) {
    return !email.isIpAddress();
  }

  /**
   * Rejects an email address that does not have a top-level domain. For example, the address
   * {@code "admin@mailserver"} would be rejected.
   *
   * @param email the email address to validate
   * @return true if this email address has a top-level domain, or false if it does not
   */
  public static boolean requireTopLevelDomain(Email email) {
    // IP address domains have an inherent top level domain
    return email.isIpAddress() || !email.topLevelDomain().equals(TopLevelDomain.NONE);
  }

  /**
   * Rejects an email address that has a TLD with only one (or zero) character(s). For example,
   * the address {@code "test@server.c"} would be rejected.
   *
   * @param email the email address to validate
   * @return true if the email address has a TLD with more than one character, or false if not
   */
  public static boolean disallowSingleCharacterTopLevelDomains(Email email) {
    return email.topLevelDomain().equals(TopLevelDomain.NONE)
        || email.topLevelDomain().stringValue().length() > 1;
  }

  /**
   * Rejects an email address that uses explicit source routing. Explicit source routing has been
   * <a href="https://datatracker.ietf.org/doc/html/rfc5321#section-3.6.1">deprecated</a>
   * as of RFC 5321 and you SHOULD NOT use explicit source routing except under unusual
   * circumstances.
   *
   * <p>For example, the address {@code "@1st.relay,@2nd.relay:user@final.domain"} would be
   * rejected.
   *
   * @param email the email address to validate
   * @return true if the email address does not contain explicit source routing, or false if it does
   */
  public static boolean disallowExplicitSourceRouting(Email email) {
    return email.explicitSourceRoutes().size() <= 0;
  }

  /**
   * Rejects an email address that has quoted identifiers. For example, the address
   * {@code "John Smith <test@server.com>"} would be rejected.
   *
   * @param email the email address to validate
   * @return true if this email address does not have a quoted identifier, or false if it does
   */
  public static boolean disallowQuotedIdentifiers(Email email) {
    return !email.hasIdentifier();
  }

  /**
   * Rejects an email address that has a top-level domain other than the ones in the allowed set.
   * For example, if {@code allowed} is {@code [DOT_COM, DOT_ORG]}, then the address
   * {@code "test@example.net"} would be rejected.
   *
   * @param email the email address to validate
   * @param allowed the set of allowed {@link TopLevelDomain}
   * @return true if this email address has an allowed top-level domain, or false if it does not
   */
  public static boolean requireOnlyTopLevelDomains(Email email, Set<TopLevelDomain> allowed) {
    return allowed.contains(email.topLevelDomain());
  }

  /**
   * Rejects an email address that has obsolete whitespace within the local-part or domain.
   * For example, the address {@code "1234   @   local(blah)  .com"} would be rejected.
   *
   * @param email the email address to validate
   * @return true if this email does not contain obsolete whitespace, or false if it does
   */
  public static boolean disallowObsoleteWhitespace(Email email) {
    return !email.containsWhitespace();
  }

  /**
   * Rejects an email address that has a reserved domain according to
   * <a href="https://datatracker.ietf.org/doc/html/rfc2606">RFC 2606</a>. The reserved domains
   * are:
   *
   * <ul>
   *   <li>{@code .test}
   *   <li>{@code .example}
   *   <li>{@code .invalid}
   *   <li>{@code .localhost}
   *   <li>{@code example.com}
   *   <li>{@code example.org}
   *   <li>{@code example.net}
   * </ul>
   *
   * @param email the email address to validate
   * @return true if this email address does not have a reserved domain, or false if it does
   */
  public static boolean disallowReservedDomains(Email email) {
    List<String> domainParts = email.domainParts();

    // Check the top level domain to see if it is reserved, if so return false
    if (reservedTopLevelDomains.contains(domainParts.get(domainParts.size() - 1))) {
      return false;
    }

    // Check the second level domain to see if it is example.*, where * is contained in
    // the reservedExampleTlds set
    if (domainParts.size() > 1
        && "example".equals(domainParts.get(domainParts.size() - 2))
        && reservedExampleTlds.contains(email.topLevelDomain())) {
      return false;
    }

    return true;
  }

  /**
   * Rejects an email address that does not have a valid MX record for the domain.
   *
   * @param email the email address to validate
   * @return true if this email address has a valid MX record, or false if it does not
   */
  public static boolean requireValidMXRecord(Email email) {
    return DNSLookupUtil.hasMXRecord(email.domainWithoutComments());
  }

  /**
   * Rejects an email address that does not have a valid MX record for the domain.
   *
   * @param email the email address to validate
   * @param initialTimeout the timeout in milliseconds for the initial DNS lookup
   * @param numRetries the number of retries to perform using exponential backoff
   * @return true if this email address has a valid MX record, or false if it does not
   */
  public static boolean requireValidMXRecord(Email email, int initialTimeout, int numRetries) {
    return DNSLookupUtil.hasMXRecord(email.domainWithoutComments(), initialTimeout, numRetries);
  }

  /**
   * Rejects an email address that has a disposable domain. The set of disposable domains
   * is determined by the provided {@link DisposableDomainSource}.
   *
   * @param email the email address to validate
   * @param disposableDomainSource the source of disposable domains
   * @return true if this email address does not have a disposable domain, or false if it does
   */
  public static boolean disallowDisposableDomains(Email email,
                                                  DisposableDomainSource disposableDomainSource) {
    return email.isIpAddress()
        || !disposableDomainSource.isDisposableDomain(email.domainWithoutComments());
  }

  /**
   * Rejects an email address that contains characters other than those in the ASCII set.
   *
   * @param email the email address to validate
   * @return true if this email address only contains ASCII characters, or false if it does not
   */
  public static boolean requireAscii(Email email) {
    return email.isAscii();
  }
}
