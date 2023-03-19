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
    Hashtable<String, String> env = new Hashtable<>();
    env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

    try {
      DirContext ctx = new InitialDirContext(env);
      Attribute attr = ctx.getAttributes(domain, new String[]{"MX"}).get("MX");

      return attr.size() > 0;
    } catch (NamingException e) {
      return false;
    }
  }
}
