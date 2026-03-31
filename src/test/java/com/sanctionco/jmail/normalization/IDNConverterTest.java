package com.sanctionco.jmail.normalization;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IDNConverterTest {

  @ParameterizedTest(name = "{0} -> {1}")
  @MethodSource("provideLabelsForAsciiConversion")
  public void testLabelsToASCII(List<String> input, String expected) {
    String result = IDNConverter.labelsToAsciiDomain(input);
    assertEquals(expected, result, "Failed to convert labels: " + input);
  }

  private static Stream<Arguments> provideLabelsForAsciiConversion() {
    return Stream.of(
        // German domains
        Arguments.of(
            Arrays.asList("münchen", "de"),
            "xn--mnchen-3ya.de"
        ),
        Arguments.of(
            Arrays.asList("düsseldorf", "de"),
            "xn--dsseldorf-q9a.de"
        ),
        Arguments.of(
            Arrays.asList("größe", "de"),
            "xn--gre-6ka8i.de"
        ),

        // French domains
        Arguments.of(
            Arrays.asList("café", "fr"),
            "xn--caf-dma.fr"
        ),
        Arguments.of(
            Arrays.asList("français", "fr"),
            "xn--franais-xxa.fr"
        ),
        Arguments.of(
            Arrays.asList("naïve", "fr"),
            "xn--nave-6pa.fr"
        ),

        // Spanish domains
        Arguments.of(
            Arrays.asList("españa", "es"),
            "xn--espaa-rta.es"
        ),
        Arguments.of(
            Arrays.asList("corazon", "es"),
            "corazon.es"
        ),
        Arguments.of(
            Arrays.asList("josé", "es"),
            "xn--jos-dma.es"
        ),

        // Russian domains
        Arguments.of(
            Arrays.asList("москва", "ru"),
            "xn--80adxhks.ru"
        ),
        Arguments.of(
            Arrays.asList("пример", "рф"),
            "xn--e1afmkfd.xn--p1ai"
        ),
        Arguments.of(
            Arrays.asList("тест", "ru"),
            "xn--e1aybc.ru"
        ),

        // Chinese domains
        Arguments.of(
            Arrays.asList("中国", "cn"),
            "xn--fiqs8s.cn"
        ),
        Arguments.of(
            Arrays.asList("北京", "中国"),
            "xn--1lq90i.xn--fiqs8s"
        ),
        Arguments.of(
            Arrays.asList("例え", "jp"),
            "xn--r8jz45g.jp"
        ),

        // Japanese domains
        Arguments.of(
            Arrays.asList("日本", "jp"),
            "xn--wgv71a.jp"
        ),
        Arguments.of(
            Arrays.asList("テスト", "jp"),
            "xn--zckzah.jp"
        ),

        // Greek domains
        Arguments.of(
            Arrays.asList("ελλάδα", "gr"),
            "xn--hxakic4aa.gr"
        ),
        Arguments.of(
            Arrays.asList("παράδειγμα", "gr"),
            "xn--hxajbheg2az3al.gr"
        ),

        // Korean domains
        Arguments.of(
            Arrays.asList("한국", "kr"),
            "xn--3e0b707e.kr"
        ),
        Arguments.of(
            Arrays.asList("테스트", "kr"),
            "xn--9t4b11yi5a.kr"
        ),

        // Mixed case and multiple labels
        Arguments.of(
            Arrays.asList("MÜNCHEN", "DE"),
            "xn--mnchen-3ya.de"
        ),
        Arguments.of(
            Arrays.asList("subdomain", "münchen", "de"),
            "subdomain.xn--mnchen-3ya.de"
        ),

        // Already ASCII
        Arguments.of(
            Arrays.asList("example", "com"),
            "example.com"
        ),
        Arguments.of(
            Arrays.asList("google", "com"),
            "google.com"
        ),
        Arguments.of(
            Arrays.asList("mail", "co", "uk"),
            "mail.co.uk"
        )
    );
  }

  @Test
  void handlesNullAndEmpty() {
    assertEquals("", IDNConverter.labelsToAsciiDomain(null));
    assertEquals("", IDNConverter.labelsToAsciiDomain(Collections.emptyList()));

    assertEquals("", IDNConverter.labelsToAsciiDomain(Collections.singletonList(null)));
    assertEquals("", IDNConverter.labelsToAsciiDomain(Collections.singletonList("")));
    assertEquals(" ", IDNConverter.labelsToAsciiDomain(Collections.singletonList(" ")));
  }
}
