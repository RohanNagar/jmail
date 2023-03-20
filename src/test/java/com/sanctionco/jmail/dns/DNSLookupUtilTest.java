package com.sanctionco.jmail.dns;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DNSLookupUtilTest {

  @Test
  void canLookupValidMXRecord() {
    assertThat(DNSLookupUtil.hasMXRecord("gmail.com")).isTrue();
    assertThat(DNSLookupUtil.hasMXRecord("yahoo.com")).isTrue();
    assertThat(DNSLookupUtil.hasMXRecord("hotmail.com")).isTrue();
    assertThat(DNSLookupUtil.hasMXRecord("utexas.edu")).isTrue();
  }

  @Test
  void failsToFindInvalidMXRecord() {
    assertThat(DNSLookupUtil.hasMXRecord("a.com")).isFalse();
    assertThat(DNSLookupUtil.hasMXRecord("whatis.hello")).isFalse();
  }
}
