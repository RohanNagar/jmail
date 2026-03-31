package com.sanctionco.jmail.normalization;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Contains static utility methods for converting internationalized
 * domain names (IDN) to ASCII using Punycode.
 */
public class IDNConverter {
  private static final String ACE_PREFIX = "xn--";
  private static final int BASE = 36;
  private static final int TMIN = 1;
  private static final int TMAX = 26;
  private static final int SKEW = 38;
  private static final int DAMP = 700;
  private static final int INITIAL_BIAS = 72;
  private static final int INITIAL_N = 0x80;

  /**
   * Convert a list of labels to ASCII-compatible format, and join them into a domain name.
   * Handles internationalized domain labels.
   *
   * <p>Any labels that are {@code null} will be dropped and excluded from the result.
   *
   * @param labels the list of labels to convert like {@code [münchen, co, uk]}
   * @return ASCII domain like {@code xn--mnchen-3ya.co.uk"}
   */
  public static String labelsToAsciiDomain(List<String> labels) {
    if (labels == null || labels.isEmpty()) {
      return "";
    }

    return labels.stream()
        .filter(Objects::nonNull)
        .map(IDNConverter::labelToASCII)
        .collect(Collectors.joining("."));
  }

  /**
   * Convert a single domain label to ASCII using Punycode.
   *
   * @param label Single label like "münchen"
   * @return ASCII label like "xn--mnchen-3ya"
   */
  private static String labelToASCII(String label) {
    if (label.isEmpty()) {
      return label;
    }

    label = label.toLowerCase();

    // Check if already ASCII. If so, just return the lowercased version
    if (isASCII(label)) {
      return label;
    }

    // Encode to Punycode
    try {
      return ACE_PREFIX + punycodeEncode(label);
    } catch (Exception e) {
      // If encoding fails, return original lowercase
      return label;
    }
  }

  /**
   * Encode a Unicode string to Punycode.
   *
   * @param input Unicode string
   * @return Punycode string (without "xn--" prefix)
   */
  private static String punycodeEncode(String input) {
    StringBuilder output = new StringBuilder();
    int[] codePoints = input.codePoints().toArray();
    int n = INITIAL_N;
    int bias = INITIAL_BIAS;
    int delta = 0;
    int basicCount = 0;

    // First, add all ASCII characters
    for (int cp : codePoints) {
      if (cp < 128) {
        output.append((char) cp);
        basicCount++;
      }
    }

    if (basicCount > 0) {
      output.append('-');
    }

    int handledCount = basicCount;

    while (handledCount < codePoints.length) {
      int nextCodePoint = Integer.MAX_VALUE;

      // Find the next smallest code point >= n
      for (int cp : codePoints) {
        if (cp >= n && cp < nextCodePoint) {
          nextCodePoint = cp;
        }
      }

      delta += (nextCodePoint - n) * (handledCount + 1);
      n = nextCodePoint;

      for (int cp : codePoints) {
        if (cp < n) {
          delta++;
        } else if (cp == n) {
          // Encode delta as variable-length integer
          int q = delta;
          for (int k = BASE; ; k += BASE) {
            int t;
            if (k <= bias) {
              t = TMIN;
            } else if (k >= bias + TMAX) {
              t = TMAX;
            } else {
              t = k - bias;
            }

            if (q < t) {
              break;
            }

            output.append(digitToBasic(t + (q - t) % (BASE - t)));
            q = (q - t) / (BASE - t);
          }

          output.append(digitToBasic(q));
          bias = adaptBias(delta, handledCount + 1, handledCount == basicCount);
          delta = 0;
          handledCount++;
        }
      }

      delta++;
      n++;
    }

    return output.toString();
  }

  /**
   * Encode a digit as a basic code point.
   */
  private static char digitToBasic(int digit) {
    if (digit < 26) {
      return (char) ('a' + digit);
    } else {
      return (char) ('0' + digit - 26);
    }
  }

  /**
   * Adapt bias according to Punycode algorithm.
   */
  private static int adaptBias(int delta, int length, boolean firstTime) {
    if (firstTime) {
      delta /= DAMP;
    } else {
      delta /= 2;
    }

    delta += delta / length;

    int k = 0;
    while (delta > ((BASE - TMIN) * TMAX) / 2) {
      delta /= (BASE - TMIN);
      k += BASE;
    }

    return k + (((BASE - TMIN + 1) * delta) / (delta + SKEW));
  }

  /**
   * Check if a string contains only ASCII characters.
   */
  private static boolean isASCII(String str) {
    for (char c : str.toCharArray()) {
      if (c > 127) {
        return false;
      }
    }
    return true;
  }
}