package com.sanctionco.jmail.net;

import org.assertj.core.api.Condition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

class InternetProtocolAddressTest {
  private final Condition<String> valid = new Condition<>(
      InternetProtocolAddress::isValid, "valid");

  private final Condition<String> invalid = new Condition<>(
      ip -> !InternetProtocolAddress.isValid(ip), "invalid");

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {"1.2.3.4", "0.0.0.0", "12.34.56.78", "255.255.255.255", "0.255.1.255"})
  void testValidIpv4Address(String ip) {
    assertThat(InternetProtocolAddress.validate(ip))
        .isPresent()
        .get().isEqualTo(ip);

    assertThat(ip).is(valid);
    assertThatNoException().isThrownBy(() -> InternetProtocolAddress.enforceValid(ip));
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "1.2.3", "1.2.3.", "1.2.3.4.5", "256.255.255.255", "255.4.6.256", "notanip", "1234.1.2.3",
      "-1.2.3.4", "1২7.0.0.1", "0127.0.0.1", "0000000127.0.0.1", "127..0.1", "127..0.0.1"})
  void testInvalidIpv4Address(String ip) {
    assertThat(InternetProtocolAddress.validate(ip))
        .isNotPresent();

    assertThat(ip).is(invalid);
    assertThatExceptionOfType(InvalidAddressException.class)
        .isThrownBy(() -> InternetProtocolAddress.enforceValid(ip));
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "2001:4860:4860::8888",
      "2001:db8:3333:4444:5555:6666:7777:8888",
      "2001:db8:3333:4444:CCCC:DDDD:EEEE:FFFF",
      "::",
      "2001:db8::",
      "::1234:5678",
      "2001:db8::1234:5678",
      "2001:db8:1::ab9:C0A8:102",
      "2001:db8:3333:4444:5555:6666:1.2.3.4",
      "::11.22.33.44",
      "2001:db8::123.123.123.123",
      "::1234:5678:91.123.4.56",
      "::1234:5678:1.2.3.4",
      "2001:db8::1234:5678:5.6.7.8"})
  void testValidIpv6Address(String ip) {
    assertThat(InternetProtocolAddress.validate(ip))
        .isPresent()
        .get().isEqualTo(ip);

    assertThat(ip).is(valid);
    assertThatNoException().isThrownBy(() -> InternetProtocolAddress.enforceValid(ip));
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "56FE::2159:5BBC::6594",
      "AAAG::2001",
      "IPv5:::1234:5678:1.2.3.4",
      "::1234:5678:1.2.3.256",
      "2001:db8:3333:4444:5555:6666:7777:888J",
      "2001:db8:3333:4444:5555:6666:7777:88881",
      "2001:db8:::",
      "notanip",
      "2001:db8:3333:4444:5555:6666:1.২.3.4",
      "2001:db8:3333:4444:5555:6666:7777:1.2.3.4",
      "2001:db8:3333:4444:5555::6666:1.2.3.4"
  })
  void testInvalidIpv6Address(String ip) {
    assertThat(InternetProtocolAddress.validate(ip))
        .isNotPresent();

    assertThat(ip).is(invalid);
    assertThatExceptionOfType(InvalidAddressException.class)
        .isThrownBy(() -> InternetProtocolAddress.enforceValid(ip));
  }
}
