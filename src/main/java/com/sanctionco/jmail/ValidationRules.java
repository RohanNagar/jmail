package com.sanctionco.jmail;

import java.util.Set;

/**
 * Provides common email validation rules that can be added to an
 * {@link EmailValidator}.
 */
public class ValidationRules {

  /**
   * Rejects an email that has an IP address as the domain. For example, the address
   * {@code "test@[12.34.56.78]"} would be rejected.
   *
   * @param email the email to validate
   * @return true if this email does not have an IP address domain, or false if it does
   */
  public static boolean disallowIpDomain(Email email) {
    return !email.isIpAddress();
  }

  /**
   * Rejects an email that does not have a top-level domain. For example, the address
   * {@code "admin@mailserver"} would be rejected.
   *
   * @param email the email to validate
   * @return true if this email has a top-level domain, or false if it does not
   */
  public static boolean requireTopLevelDomain(Email email) {
    return !email.topLevelDomain().equals(TopLevelDomain.NONE);
  }

  /**
   * Rejects an email that has a top-level domain other than the ones in the allowed set.
   * For example, if {@code allowed} is {@code [DOT_COM, DOT_ORG]}, then the address
   * {@code "test@example.net"} would be rejected.
   *
   * @param email the email to validate
   * @param allowed the set of allowed {@link TopLevelDomain}
   * @return true if this email has an allowed top-level domain, or false if it does not
   */
  public static boolean requireOnlyTopLevelDomains(Email email, Set<TopLevelDomain> allowed) {
    return allowed.contains(email.topLevelDomain());
  }
}
