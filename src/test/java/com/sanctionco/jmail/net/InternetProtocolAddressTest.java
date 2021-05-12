package com.sanctionco.jmail.net;

import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InternetProtocolAddressTest {

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {"1.2.3.4", "0.0.0.0", "12.34.56.78", "255.255.255.255", "0.255.1.255"})
  void testValidIpv4Address(String ip) {
    Optional<String> result = InternetProtocolAddress.validate(ip);

    assertTrue(result.isPresent());
    assertEquals(ip, result.get());

    // Validate helper methods
    assertTrue(InternetProtocolAddress.isValid(ip));
    assertDoesNotThrow(() -> InternetProtocolAddress.enforceValid(ip));
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "1.2.3", "1.2.3.", "1.2.3.4.5", "256.255.255.255", "255.4.6.256", "notanip", "1234.1.2.3"})
  void testInvalidIpv4Address(String ip) {
    Optional<String> result = InternetProtocolAddress.validate(ip);

    assertFalse(result.isPresent());

    // Validate helper methods
    assertFalse(InternetProtocolAddress.isValid(ip));
    assertThrows(InvalidAddressException.class, () -> InternetProtocolAddress.enforceValid(ip));
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "IPv6:2001:4860:4860::8888",
      "IPv6:2001:db8:3333:4444:5555:6666:7777:8888",
      "IPv6:2001:db8:3333:4444:CCCC:DDDD:EEEE:FFFF",
      "IPv6:::",
      "IPv6:2001:db8::",
      "IPv6:::1234:5678",
      "IPv6:2001:db8::1234:5678",
      "IPv6:2001:db8:1::ab9:C0A8:102",
      "IPv6:2001:db8:3333:4444:5555:6666:1.2.3.4",
      "IPv6:::11.22.33.44",
      "IPv6:2001:db8::123.123.123.123",
      "IPv6:::1234:5678:91.123.4.56",
      "IPv6:::1234:5678:1.2.3.4",
      "IPv6:2001:db8::1234:5678:5.6.7.8"})
  void testValidIpv6Address(String ip) {
    Optional<String> result = InternetProtocolAddress.validate(ip);

    assertTrue(result.isPresent());
    assertEquals(ip, result.get());

    // Validate helper methods
    assertTrue(InternetProtocolAddress.isValid(ip));
    assertDoesNotThrow(() -> InternetProtocolAddress.enforceValid(ip));
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "IPv6:56FE::2159:5BBC::6594",
      "IPv6:AAAG::2001",
      "IPv5:::1234:5678:1.2.3.4",
      "IPv6:::1234:5678:1.2.3.256",
      "IPv6:2001:db8:::",
      "notanip"
  })
  void testInvalidIpv6Address(String ip) {
    Optional<String> result = InternetProtocolAddress.validate(ip);

    assertFalse(result.isPresent());

    // Validate helper methods
    assertFalse(InternetProtocolAddress.isValid(ip));
    assertThrows(InvalidAddressException.class, () -> InternetProtocolAddress.enforceValid(ip));
  }
}
