package com.sanctionco.jmail.net;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Provides validation methods for internet protocol (IP) addresses,
 * both IPv4 and IPv6.
 *
 * <p>Note: IP addresses with prefixes are not currently supported.
 */
public final class InternetProtocolAddress {

  /**
   * Private constructor to prevent instantiation.
   */
  private InternetProtocolAddress() {
  }

  // Set of allowed characters in a HEX number
  private static final Set<Character> ALLOWED_HEX_CHARACTERS = new HashSet<>(
      Arrays.asList(
          'A', 'B', 'C', 'D', 'E', 'F', 'a', 'b', 'c', 'd', 'e', 'f',
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

  /**
   * Determines if the given string is a valid IP address.
   *
   * @param ip the IP address to validate
   * @return true if the string is a valid IP address, false otherwise
   */
  public static boolean isValid(String ip) {
    return validate(ip).isPresent();
  }

  /**
   * Require that the given IP address is valid, throwing {@link InvalidAddressException}
   * if the IP is invalid. See {@link #validate(String)} for details on IP validation.
   *
   * @param ip the IP address to validate
   * @throws InvalidAddressException if the validation fails
   */
  public static void enforceValid(String ip) {
    if (!validate(ip).isPresent()) {
      throw new InvalidAddressException();
    }
  }

  /**
   * Determines if the given string is a valid IP address (either IPv4 or IPv6), returning an
   * {@link Optional} that contains the string if valid.
   *
   * @param ip the IP address to validate
   * @return an {@link Optional} containing the string if valid, or an empty {@link Optional}
   *         otherwise
   */
  public static Optional<String> validate(String ip) {
    // First try to validate with IPv4
    Optional<String> ipv4Validated = validateIpv4(ip);

    // If that worked, return it. Otherwise, try to validate with IPv6
    if (ipv4Validated.isPresent()) return ipv4Validated;
    else return validateIpv6(ip);
  }

  /**
   * Determines if the given string is a valid IPv4 address, returning an {@link Optional}
   * that contains the string if valid.
   *
   * @param ip the IP address to validate
   * @return an {@link Optional} containing the string if valid, or an empty {@link Optional}
   *         otherwise
   */
  public static Optional<String> validateIpv4(String ip) {
    StringBuilder currentPart = new StringBuilder();
    int partCount = 0;

    for (int i = 0, size = ip.length(); i < size; i++) {
      char c = ip.charAt(i);

      if (c == '.') {
        // End of IPv4 part. Validate the current part and continue if valid
        if (isInvalidIpv4Part(currentPart.toString())) return Optional.empty();

        partCount++;
        currentPart = new StringBuilder();
        continue;
      }

      if (!Character.isDigit(c)) return Optional.empty();

      currentPart.append(c);
    }

    if (isInvalidIpv4Part(currentPart.toString())) return Optional.empty();

    partCount++;

    // IPv4 must have 4 parts
    if (partCount != 4) return Optional.empty();

    return Optional.of(ip);
  }

  /**
   * Determines if the given string is a valid IPv6 address, returning an {@link Optional}
   * that contains the string if valid.
   *
   * @param ip the IP address to validate
   * @return an {@link Optional} containing the string if valid, or an empty {@link Optional}
   *         otherwise
   */
  public static Optional<String> validateIpv6(String ip) {
    int len = ip.length();

    // Shortest IPv6 is "::"
    if (len < 2) return Optional.empty();

    // IPv6 cannot start with single colon, only double colon
    if (ip.charAt(0) == ':' && ip.charAt(1) != ':') return Optional.empty();

    // IPv6 cannot end with single colon, only double colon
    if (ip.charAt(len - 1) == ':' && ip.charAt(len - 2) != ':') return Optional.empty();

    StringBuilder currentPart = new StringBuilder();
    int partCount = 0;

    boolean previousColon = false;
    boolean doubleColon = false;
    boolean isDual = false;

    for (int i = 0, size = ip.length(); i < size; i++) {
      char c = ip.charAt(i);

      if (c == '.') {
        // A dot - we need to check if this is a dual IPv6/IPv4
        isDual = true;

        // In a dual address, the IPv6 part can only have up to 6 segments
        if (partCount > 6) return Optional.empty();

        // Validate the IPv4 address
        String remainingSubstring = ip.substring(i);
        Optional<String> ipv4 = validateIpv4(currentPart + remainingSubstring);

        if (!ipv4.isPresent()) return Optional.empty();

        break;
      }

      if (c == ':') {
        // We already saw a double colon, we can't see another one
        if (previousColon && doubleColon) return Optional.empty();

        if (previousColon) {
          // two colons in a row, we skip
          doubleColon = true;
          continue;
        }

        if (currentPart.length() > 0) {
          if (isInvalidIpv6Part(currentPart.toString())) return Optional.empty();
          else partCount++;
        }

        currentPart = new StringBuilder();
        previousColon = true;
        continue;
      } else {
        previousColon = false;
      }

      if (!ALLOWED_HEX_CHARACTERS.contains(c)) return Optional.empty();

      currentPart.append(c);
    }

    if (currentPart.length() > 0) {
      if (isInvalidIpv6Part(currentPart.toString())) return Optional.empty();
      else partCount++;
    }

    if (isDual) {
      // Dual - without a double colon there must be 7 parts exactly.
      // With a double colon there can be no more than 6.
      if ((!doubleColon && partCount != 7) || (doubleColon && partCount > 6)) {
        return Optional.empty();
      }
    } else {
      // Regular - without a double colon there must be 8 parts exactly.
      // With a double colon there can be no more than 7.
      if ((!doubleColon && partCount != 8) || (doubleColon && partCount > 7)) {
        return Optional.empty();
      }
    }

    return Optional.of(ip);
  }

  private static boolean isInvalidIpv4Part(String part) {
    try {
      // The int cannot be less than zero since we check isDigit earlier on
      if (Integer.parseInt(part) > 255) return true;
    } catch (NumberFormatException e) {
      return true;
    }

    return false;
  }

  private static boolean isInvalidIpv6Part(String part) {
    return part.length() > 4;
  }
}
