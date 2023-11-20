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

  @Test
  void failsToNoServiceMXRecord() {
    assertThat(DNSLookupUtil.hasMXRecord("gmail.de")).isFalse();
  }

  @Test
  void customTimeoutWorksAsExpected() {
    long startTime = System.currentTimeMillis();
    assertThat(DNSLookupUtil.hasMXRecord("coolio.com", 10, 1)).isFalse();
    long endTime = System.currentTimeMillis();

    assertThat(endTime - startTime).isLessThan(100);
  }
}
