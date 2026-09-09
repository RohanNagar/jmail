package com.sanctionco.jmail.mime;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class EncodedWordTest {

  @ParameterizedTest
  @MethodSource("encodedWordProvider")
  @MethodSource("malformedWordProvider")
  void canDecodeWords(String word, String expected) {
    assertThat(EncodedWord.decodeText(word)).isEqualTo(expected);
  }

  @Test
  void discardsWhitespaceBetweenTwoEncodedWords() {
    String word = "=?ISO-8859-1?B?SWYgeW91IGNhbiByZWFkIHRoaXMgeW8=?=\n"
        + "  =?ISO-8859-2?B?dSB1bmRlcnN0YW5kIHRoZSBleGFtcGxlLg==?=";
    String expected = "If you can read this you understand the example.";

    assertThat(EncodedWord.decodeText(word)).isEqualTo(expected);

    word = "=?ISO-8859-1?B?SWYgeW91IGNhbiByZWFkIHRoaXMgeW8=?=\t"
        + "=?ISO-8859-2?B?dSB1bmRlcnN0YW5kIHRoZSBleGFtcGxlLg==?=";
    expected = "If you can read this you understand the example.";

    assertThat(EncodedWord.decodeText(word)).isEqualTo(expected);

    word = "=?ISO-8859-1?B?SWYgeW91IGNhbiByZWFkIHRoaXMgeW8=?=\r"
        + "=?ISO-8859-2?B?dSB1bmRlcnN0YW5kIHRoZSBleGFtcGxlLg==?=";
    expected = "If you can read this you understand the example.";

    assertThat(EncodedWord.decodeText(word)).isEqualTo(expected);
  }

  List<Arguments> encodedWordProvider() {
    return List.of(
        Arguments.of(
            "Hello =?UTF-8?B?V29ybGQ=?= out =?ISO-8859-1?Q?there=3F?=", "Hello World out there?"),
        Arguments.of("=?iso-8859-1?Q?=A1Hola,_se=F1or!?=", "¡Hola, señor!"),
        Arguments.of("=?utf-8?Q?Andreas_Birkeb=C3=A6k?=", "Andreas Birkebæk"),
        Arguments.of("=?utf-8?B?U3VqZXRpcyDEl3RpcyDFs3bEl25pcw==?=", "Sujetis ėtis ųvėnis"),
        Arguments.of("Unencoded word here", "Unencoded word here"),
        Arguments.of("=?utf-8?q?te?xt?=", "te?xt"),
        Arguments.of("=?utf-8?q?=40evil.com=00?=", "@evil.com\0")
    );
  }

  List<Arguments> malformedWordProvider() {
    return List.of(
        Arguments.of(null, null),
        Arguments.of("", ""),
        Arguments.of("=?utf-8hello", "=?utf-8hello"),
        Arguments.of("=?utf-8?bhello", "=?utf-8?bhello"),
        Arguments.of("=?utf-8?b?hello", "=?utf-8?b?hello"),
        Arguments.of("=?utf-8?z?te?xt?=", "=?utf-8?z?te?xt?="),
        Arguments.of("=?utf-8?Q?Andreas_Birkeb=G1=1G=A?=", "Andreas Birkeb=G1=1G=A")
    );
  }
}
