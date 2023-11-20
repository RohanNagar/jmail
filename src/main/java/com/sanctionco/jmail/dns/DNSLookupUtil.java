package com.sanctionco.jmail.dns;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * Utility class that provides static methods for DNS related operations.
 */
public final class DNSLookupUtil {
  private static final int DEFAULT_INITIAL_TIMEOUT = 100;
  private static final int DEFAULT_RETRIES = 2;
  private static final String NO_SERVICE_MX_PR_RDATA = "0 .";

  /**
   * Private constructor to prevent instantiation.
   */
  private DNSLookupUtil() {
  }

  /**
   * Determine if the given domain has a valid MX record.
   *
   * @param domain the domain whose MX record to check
   * @return true if the domain has a valid MX record, or false if it does not
   */
  public static boolean hasMXRecord(String domain) {
    return hasMXRecord(domain, DEFAULT_INITIAL_TIMEOUT, DEFAULT_RETRIES);
  }

  /**
   * Determine if the given domain has a valid MX record.
   *
   * @param domain the domain whose MX record to check
   * @param initialTimeout the timeout in milliseconds for the initial DNS lookup
   * @param numRetries the number of retries to perform using exponential backoff
   * @return true if the domain has a valid MX record, or false if it does not
   */
  public static boolean hasMXRecord(String domain, int initialTimeout, int numRetries) {
    Hashtable<String, String> env = new Hashtable<>();
    env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
    env.put("com.sun.jndi.dns.timeout.initial", String.valueOf(initialTimeout));
    env.put("com.sun.jndi.dns.timeout.retries", String.valueOf(numRetries));

    try {
      DirContext ctx = new InitialDirContext(env);
      Attribute attr = ctx.getAttributes(domain, new String[]{"MX"}).get("MX");

      return attr != null && attr.size() > 0 && !attr.get(0).equals(NO_SERVICE_MX_PR_RDATA);
    } catch (NamingException e) {
      return false;
    }
  }
}
