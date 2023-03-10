package com.sanctionco.jmail.dns;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public final class DNSLookupUtil {

  /**
   * Private constructor to prevent instantiation.
   */
  private DNSLookupUtil() {
  }

  public static boolean hasMXRecord(String domain) {
    Hashtable<String, String> env = new Hashtable<>();
    env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

    try {
      DirContext ctx = new InitialDirContext(env);
      Attribute attr = ctx.getAttributes(domain, new String[]{"MX"}).get("MX");

      return attr != null && attr.size() > 0;
    } catch (NamingException e) {
      return false;
    }
  }
}
