package com.sanctionco.jmail.mime;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Utility class that provides static methods for MIME encoded word operations.
 */
public final class EncodedWord {

  /**
   * Private constructor to prevent instantiation.
   */
  private EncodedWord() {
  }

  /**
   * Decode the given string according to MIME encoded word decoding rules
   * specified in <a href ="https://datatracker.ietf.org/doc/html/rfc2047">RFC 2047</a>.
   *
   * <p>Any plaintext or malformed encoded words in the input string are passed through unchanged.
   *
   * @param input the string to decode
   * @return the decoded version of the input string
   */
  public static String decodeText(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }

    StringBuilder result = new StringBuilder();
    int currentIndex = 0;
    boolean wasPreviousEncoded = false;

    while (currentIndex < input.length()) {
      // Find the start delimiter "=?"
      int start = input.indexOf("=?", currentIndex);
      if (start == -1) {
        // No more encoded words, append remaining text
        result.append(input.substring(currentIndex));
        break;
      }

      // Get all characters before the encoded word
      String gap = "";
      if (start > currentIndex) {
        gap = input.substring(currentIndex, start);
      }

      // Find the next 3 question marks to separate components
      int firstQuestion = start + 2;
      int secondQuestion = input.indexOf('?', firstQuestion);

      if (secondQuestion == -1) {
        // Failed to parse: treat this gap and starting "=?" as regular plain text
        result.append(gap).append("=?");
        currentIndex = firstQuestion;
        wasPreviousEncoded = false;
        continue;
      }

      int thirdQuestion = input.indexOf('?', secondQuestion + 1);
      if (thirdQuestion == -1) {
        result.append(gap).append(input, start, secondQuestion + 1);
        currentIndex = secondQuestion + 1;
        wasPreviousEncoded = false;
        continue;
      }

      // Find the end delimiter "?="
      int end = input.indexOf("?=", thirdQuestion + 1);
      if (end == -1) {
        result.append(gap).append(input, start, thirdQuestion + 1);
        currentIndex = thirdQuestion + 1;
        wasPreviousEncoded = false;
        continue;
      }

      if (!gap.isEmpty()) {
        if (!wasPreviousEncoded || !isLinearWhitespace(gap)) {
          // Keep it, it's either leading text or contains real characters
          result.append(gap);
        }
      }

      // Extract the tokens safely
      String charsetStr = input.substring(firstQuestion, secondQuestion);
      String encoding = input.substring(secondQuestion + 1, thirdQuestion);
      String encodedText = input.substring(thirdQuestion + 1, end);

      boolean success = false;
      try {
        Charset charset = Charset.forName(charsetStr);
        byte[] decodedBytes;

        if ("B".equalsIgnoreCase(encoding)) {
          decodedBytes = Base64.getDecoder().decode(encodedText);
        } else if ("Q".equalsIgnoreCase(encoding)) {
          decodedBytes = decodeQuotedPrintable(encodedText);
        } else {
          // Unknown encoding mechanism, fall back to literal
          throw new IllegalArgumentException();
        }

        result.append(new String(decodedBytes, charset));
        success = true;
      } catch (Exception e) {
        // Fallback: If parsing tokens fails, retain raw string block
        result.append(input, start, end + 2);
      }

      // Advance pointer past "?="
      currentIndex = end + 2;
      wasPreviousEncoded = success;
    }

    return result.toString();
  }

  private static byte[] decodeQuotedPrintable(String mimeText) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    int len = mimeText.length();

    for (int i = 0; i < len; i++) {
      char c = mimeText.charAt(i);
      if (c == '_') {
        bos.write(' ');
      } else if (c == '=' && i + 2 < len) {
        int high = Character.digit(mimeText.charAt(i + 1), 16);
        int low = Character.digit(mimeText.charAt(i + 2), 16);
        if (high != -1 && low != -1) {
          bos.write((high << 4) + low);
          i += 2;
        } else {
          bos.write(c);
        }
      } else {
        bos.write(c);
      }
    }
    return bos.toByteArray();
  }

  // Helper method to check RFC-compliant line folding whitespace (\r, \n, \t, space)
  private static boolean isLinearWhitespace(String s) {
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
        return false;
      }
    }
    return true;
  }
}
