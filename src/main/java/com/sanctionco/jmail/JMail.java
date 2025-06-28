package com.sanctionco.jmail;

import com.sanctionco.jmail.net.InternetProtocolAddress;

import java.net.IDN;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Provides static methods to validate an email address
 * using standard RFC validation, or to create a new {@link EmailValidator}.
 */
public final class JMail {

  /**
   * Private constructor to prevent instantiation.
   */
  private JMail() {
  }

  /**
   * Returns a new instance of {@link EmailValidator}. In general,
   * you should favor using {@link #strictValidator()} instead of this method,
   * as this method will allow IP address domain literals, dotless domains, and explicit
   * source routing unless configured separately.
   *
   * @return the new {@link EmailValidator} instance
   */
  public static EmailValidator validator() {
    return new EmailValidator();
  }

  /**
   * Returns a new instance of {@link EmailValidator} with stricter rules applied
   * to the validator. The following rules are applied and do not need to be
   * added:
   *
   * <ul>
   *   <li>The email address cannot have an IP Address domain
   *   <li>The email address cannot have a dotless domain
   *   <li>The email address cannot have explicit source routing
   * </ul>
   *
   * @return the new {@link EmailValidator} instance
   */
  public static EmailValidator strictValidator() {
    return new EmailValidator()
        .disallowIpDomain()
        .requireTopLevelDomain()
        .disallowExplicitSourceRouting();
  }

  /**
   * Return true if the given email address passes basic RFC validation. See
   * {@link #tryParse(String)} for details on what is required of an email address
   * within basic validation.
   *
   * @param email the email address to validate
   * @return true if the given string is a valid email address, false otherwise
   */
  public static boolean isValid(String email) {
    return tryParse(email).isPresent();
  }

  /**
   * Return true if the given email address fails basic RFC validation. See
   * {@link #tryParse(String)} for details on what is required of an email address
   * within basic validation.
   *
   * @param email the email address to validate
   * @return true if the given string is not a valid email address, false otherwise
   */
  public static boolean isInvalid(String email) {
    return !tryParse(email).isPresent();
  }

  /**
   * Require that the given email address passes basic RFC validation, throwing
   * {@link InvalidEmailException} if the address is invalid. See {@link #tryParse(String)} for
   * details on what is required of an email address within basic validation.
   *
   * @param email the email address to validate
   * @throws InvalidEmailException if the validation fails
   */
  public static void enforceValid(String email) throws InvalidEmailException {
    if (!tryParse(email).isPresent()) {
      throw new InvalidEmailException();
    }
  }

  /**
   * Parse the given email address into a new {@link Email} object. This method does basic
   * validation on the input email address. This method does not claim to be 100%
   * accurate in determining if an email address is valid or invalid due to the
   * complexity of the email RFC standards. That being said, if you come across
   * an email address that you expect to be valid that fails validation, or
   * vice-versa, please open an issue at the
   * <a href="https://github.com/RohanNagar/jmail">GitHub repo</a> so it can be fixed.
   *
   * <p>In general, this method should be more or less compliant with the latest RFCs.
   *
   * @param email the email address to parse
   * @return an {@link Optional} containing the parsed {@link Email}, or empty if the email
   *         is invalid
   */
  public static Optional<Email> tryParse(String email) {
    EmailValidationResult result = validate(email);

    return result.getEmail();
  }

  /**
   * Determine if the given email address is valid, returning a new {@link EmailValidationResult}
   * object that contains details on the result of the validation. Use this method if you need to
   * see the {@link FailureReason} upon validation failure. See {@link #tryParse(String)}
   * for details on what is required of an email address within basic validation.
   *
   * @param email the email address to validate
   * @return a {@link EmailValidationResult} containing success or failure, along with the parsed
   *         {@link Email} object if successful, or the {@link FailureReason} if not
   */
  public static EmailValidationResult validate(String email) {
    return validateInternal(email, false);
  }

  /**
   * Package-private validate method that exposes an additional option {@code allowNonstandardDots}.
   *
   * @param email the email address to parse and validate
   * @param allowNonstandardDots true if a leading or trailing dot in the local-part should be
   *                             allowed
   * @return a {@link EmailValidationResult} containing success or failure, along with the parsed
   *         {@link Email} object if successful, or the {@link FailureReason} if not
   */
  static EmailValidationResult validate(String email, boolean allowNonstandardDots) {
    return validateInternal(email, allowNonstandardDots);
  }

  /**
   * Internal parsing method.
   *
   * @param email the email address to parse
   * @param allowNonstandardDots true if a leading or trailing dot in the local-part should be
   *                             allowed
   * @return a new {@link Email} instance if valid, empty if invalid
   */
  private static EmailValidationResult validateInternal(String email,
                                                        boolean allowNonstandardDots) {
    // email cannot be null
    if (email == null) return EmailValidationResult.failure(FailureReason.NULL_ADDRESS);

    // email cannot be less than 3 chars (local-part, @, domain)
    if (email.length() < 3) return EmailValidationResult.failure(FailureReason.ADDRESS_TOO_SHORT);

    // check for source-routing
    List<String> sourceRoutes = Collections.emptyList();
    String fullSourceRoute = "";

    if (email.charAt(0) == '@') {
      Optional<SourceRouteDetail> sourceRoute = validateSourceRouting(email);

      // If the sourceRoute is not present, then either the route was invalid or there was no
      // source routing. In either case, starting with the @ symbol would be invalid.
      if (!sourceRoute.isPresent()) {
        return EmailValidationResult.failure(FailureReason.BEGINS_WITH_AT_SYMBOL);
      }

      // Otherwise, update the email to validate to be just the actual email
      SourceRouteDetail detail = sourceRoute.get();
      sourceRoutes = detail.routes;
      fullSourceRoute = detail.fullRoute.toString();

      email = email.substring(fullSourceRoute.length());

      // If the actual email is empty then the source route was valid but
      // the entire address just ended with a : character
      if (email.isEmpty()) {
        return EmailValidationResult.failure(FailureReason.BEGINS_WITH_AT_SYMBOL);
      }
    }

    int size = email.length();

    // email cannot be more than 320 chars
    if (size > 320) return EmailValidationResult.failure(FailureReason.ADDRESS_TOO_LONG);

    // email cannot end with '.'
    if (email.charAt(size - 1) == '.') {
      return EmailValidationResult.failure(FailureReason.ENDS_WITH_DOT);
    }

    // email cannot end with '-'
    if (email.charAt(size - 1) == '-') {
      return EmailValidationResult.failure(FailureReason.DOMAIN_PART_ENDS_WITH_DASH);
    }

    boolean startsWithDot = false;         // set to true if we start with a dot
    boolean atFound = false;               // set to true when the '@' character is found
    boolean inQuotes = false;              // set to true if we are currently within quotes
    boolean previousDot = false;           // set to true if the previous character is '.'
    boolean previousBackslash = false;     // set to true if the previous character is '\'
    boolean firstDomainChar = true;        // set to false after beginning parsing the domain
    boolean isIpAddress = false;           // set to true if encountered an IP address domain
    boolean requireAtOrDot = false;        // set to true if the next character should be @ or .
    boolean requireAtDotOrComment = false; // set to true if the next character should be @ . or (
    boolean whitespace = false;            // set to true if we are currently within whitespace
    boolean previousComment = false;       // set to true if the last character was the end comment
    boolean requireAngledBracket = false;  // set to true if we need an angled bracket before the @
    boolean containsWhiteSpace = false;    // set to true if the email contains whitespace anywhere
    boolean isAscii = true;                // set to false if the email contains non-ascii chars

    boolean removableQuotePair = true;     // set to false if the current quote could not be removed
    boolean previousQuotedDot = false;     // set to true if the previous character is '.' in quotes
    boolean requireQuotedDot = false;      // set to true if we need a . for a removable quote

    StringBuilder localPart = new StringBuilder(size);
    StringBuilder localPartWithoutComments = new StringBuilder(size);
    StringBuilder localPartWithoutQuotes = new StringBuilder(size);
    StringBuilder currentQuote = new StringBuilder();
    StringBuilder domain = new StringBuilder(size);
    StringBuilder domainWithoutComments = new StringBuilder(size);
    StringBuilder currentDomainPart = new StringBuilder();
    List<String> domainParts = new ArrayList<>();
    List<String> comments = new ArrayList<>();

    int localPartCommentLength = 0;
    int domainCommentLength = 0;
    int charactersOnLine = 1; // sine we can have 0 chars on the first line, start at 1

    for (int i = 0; i < size; i++) {
      char c = email.charAt(i);

      if (c >= 128) isAscii = false;

      if (i == 0 && c == '.' && !allowNonstandardDots) {
        // email cannot start with '.'
        // unless we are configured to allow it (GMail doesn't care about a starting dot)
        // we set a flag instead of immediately invalidating the address since it could
        // start with a dot as part of the identifier
        startsWithDot = true;
      }

      if (c == '<' && !inQuotes && !previousBackslash) {
        // could be "phrase <address>" format. If not, it's not allowed
        if (!(email.charAt(size - 1) == '>')) {
          return EmailValidationResult.failure(FailureReason.UNQUOTED_ANGLED_BRACKET);
        }

        EmailValidationResult innerResult
            = validateInternal(email.substring(i + 1, size - 1), allowNonstandardDots);

        // If the address passed validation, return success with the identifier included.
        // Otherwise, just return the failed internal result
        return innerResult.getEmail()
            .map(e -> EmailValidationResult.success(new Email(e, localPart.toString())))
            .orElse(innerResult);
      }

      if (c == '@' && !inQuotes && !previousBackslash) {
        // If we already found an @ outside of quotes, fail
        if (atFound) return EmailValidationResult.failure(FailureReason.MULTIPLE_AT_SYMBOLS);

        // If we need an angled bracket we should fail, it's too late
        if (requireAngledBracket) {
          return EmailValidationResult.failure(FailureReason.INVALID_WHITESPACE);
        }

        // Otherwise
        atFound = true;
        requireAtOrDot = requireAtDotOrComment = false;
        whitespace = false;
        previousDot = true; // '@' acts like a '.' separator
        continue;
      }

      if (c == '\n') {
        // Ensure there are no empty lines
        if (charactersOnLine <= 0) {
          return EmailValidationResult.failure(FailureReason.INVALID_WHITESPACE);
        }

        charactersOnLine = 0;
      } else if (c != '\r') {
        // Increment for everything other than \r\n
        charactersOnLine++;
      }

      if (requireAtOrDot) {
        // If we needed to find the @ or . and we didn't, we have to fail
        if (!isWhitespace(c) && c != '.') {
          return EmailValidationResult.failure(FailureReason.INVALID_COMMENT_LOCATION);
        } else requireAtOrDot = false;
      }

      if (requireAtDotOrComment) {
        // If we needed to find the @ or . ( and we didn't, we have to fail
        if (!isWhitespace(c) && c != '.' && c != '(') {
          return EmailValidationResult.failure(FailureReason.INVALID_QUOTE_LOCATION);
        } else requireAtDotOrComment = false;
      }

      if (whitespace) {
        // Whitespace is allowed if it is between parts
        if (!previousDot && !previousComment) {
          if (c != '.' && c != '(' && !isWhitespace(c)) {
            if (!atFound) requireAngledBracket = true; // or in phrase <addr> format
            else return EmailValidationResult.failure(FailureReason.INVALID_WHITESPACE);
          }
        }
      }

      // Additional logic to check if the current quote could be removable
      if (requireQuotedDot && inQuotes) {
        if (c != '.' && !isWhitespace(c) && c != '"') {
          removableQuotePair = false;
        } else if (!isWhitespace(c) && c != '"') {
          requireQuotedDot = false;
        }
      }

      // If we tried to remove a quote with a comment it would change the
      // meaning of the address
      if (c == '(' && inQuotes && !previousBackslash) {
        removableQuotePair = false;
      }

      if (c == '(' && !inQuotes) {
        // validate comment
        Optional<String> comment = validateComment(email.substring(i));

        if (!comment.isPresent()) {
          return EmailValidationResult.failure(FailureReason.INVALID_COMMENT);
        }

        String commentStr = comment.get();
        int commentStrLen = commentStr.length();

        // Now, what do we need surrounding the comment to make it valid?
        if (!atFound && (i != 0 && !previousDot)) {
          // if at beginning of local part, or we had a dot, ok.
          // if not, we need to be at the end of the local part '@' or get a dot
          requireAtOrDot = true;
        } else if (atFound && !firstDomainChar && !previousDot) {
          // if at beginning of domain, or we had a dot, ok.
          // if not, we need to be at the end of the domain or get a dot
          if (!(i + commentStrLen == size)) requireAtOrDot = true;
        }

        i += (commentStrLen - 1);

        if (!atFound) {
          localPart.append(commentStr);
          localPartCommentLength += commentStrLen;
        } else {
          // comment is part of domain string, but not a domain part
          domain.append(commentStr);
          domainCommentLength += commentStrLen;
        }

        previousComment = true;
        comments.add(commentStr.substring(1, commentStrLen - 1)); // add the comment without ()
        continue;
      }

      // If we find two dots outside of quotes, fail
      if (c == '.' && previousDot) {
        if (!inQuotes) {
          return EmailValidationResult.failure(FailureReason.MULTIPLE_DOT_SEPARATORS);
        } else {
          removableQuotePair = false;
        }
      }

      if (!atFound) {
        // No @ found, we're in the local-part
        // If we are at a new quote: it must be preceded by a dot or at the beginning
        if (c == '"' && i > 0 && !previousDot && !inQuotes) {
          return EmailValidationResult.failure(FailureReason.INVALID_QUOTE_LOCATION);
        }

        // If we are not in quotes, and this character is not the quote, make sure the
        // character is allowed
        boolean mustBeQuoted = JMail.DISALLOWED_UNQUOTED_CHARACTERS.contains(c);

        if (c != '"' && !inQuotes && !previousBackslash && mustBeQuoted) {
          return EmailValidationResult.failure(FailureReason.DISALLOWED_UNQUOTED_CHARACTER);
        }

        // If we are in quotes and the character requires quotes, mark the pair as not removable
        if (mustBeQuoted && inQuotes && !previousBackslash && c != '"') {
          removableQuotePair = false;
        }

        // If we previously saw a backslash, we must make sure it is being used to quote something
        if (!inQuotes && previousBackslash && !mustBeQuoted && c != ' ' && c != '\\') {
          return EmailValidationResult.failure(FailureReason.UNUSED_BACKSLASH_ESCAPE);
        }

        if (inQuotes) {
          // if we are in quotes, we need to make sure that if the character requires
          // a backlash escape, that it is there
          if (JMail.ALLOWED_QUOTED_WITH_ESCAPE.contains(c)) {
            if (!previousBackslash) {
              return EmailValidationResult.failure(FailureReason.MISSING_BACKSLASH_ESCAPE);
            }

            removableQuotePair = false;
          }
        }

        localPart.append(c);
        localPartWithoutComments.append(c);

        if (c != '"') {
          if (inQuotes) {
            currentQuote.append(c);
          } else {
            localPartWithoutQuotes.append(c);
          }
        }
      } else {
        // We're in the domain

        // Once we make it to the domain, there is no longer a chance of the address being in
        // display-name <addr> format anymore. So, if we saw that we started with a '.' character,
        // it's time to invalidate the address
        if (startsWithDot) {
          return EmailValidationResult.failure(FailureReason.STARTS_WITH_DOT);
        }

        if (firstDomainChar && c == '[') {
          // validate IP address and be done
          String ipDomain = email.substring(i);

          // We already know it starts with a '[', so make sure it ends with a ']'
          if (!ipDomain.endsWith("]") || ipDomain.length() < 3) {
            return EmailValidationResult.failure(FailureReason.INVALID_IP_DOMAIN);
          }

          String ip = ipDomain.substring(1, ipDomain.length() - 1);
          Optional<String> validatedIp = ip.startsWith(IPV6_PREFIX)
              // If it starts with the IPv6 prefix, validate with IPv6
              ? InternetProtocolAddress.validateIpv6(ip.substring(IPV6_PREFIX.length()))
              .map(s -> IPV6_PREFIX + s)
              // Otherwise, it must be IPv4
              : InternetProtocolAddress.validateIpv4(ip);

          if (!validatedIp.isPresent()) {
            return EmailValidationResult.failure(FailureReason.INVALID_IP_DOMAIN);
          }

          currentDomainPart.append(validatedIp.get());
          domain.append(validatedIp.get());
          domainWithoutComments.append(validatedIp.get());

          isIpAddress = true;
          break;
        }

        if (c == '.') {
          if (currentDomainPart.length() > 63) {
            return EmailValidationResult.failure(FailureReason.DOMAIN_PART_TOO_LONG);
          }

          if (currentDomainPart.charAt(0) == '-') {
            return EmailValidationResult.failure(FailureReason.DOMAIN_PART_STARTS_WITH_DASH);
          }

          if (currentDomainPart.charAt(currentDomainPart.length() - 1) == '-') {
            return EmailValidationResult.failure(FailureReason.DOMAIN_PART_ENDS_WITH_DASH);
          }

          domainParts.add(currentDomainPart.toString());
          currentDomainPart = new StringBuilder();
        } else {
          if (!isWhitespace(c)) currentDomainPart.append(c);
        }

        domain.append(c);
        domainWithoutComments.append(c);
        firstDomainChar = false;
      }

      final boolean quotedWhitespace = isWhitespace(c) && inQuotes;

      if (c == '"' && !previousBackslash) {
        if (inQuotes) {
          requireAtDotOrComment = true; // closing quote, make sure next char is . or @

          if (currentQuote.length() == 0) {
            removableQuotePair = false;
          }

          if (removableQuotePair) {
            localPartWithoutQuotes.append(currentQuote);
          } else {
            localPartWithoutQuotes.append('"');
            localPartWithoutQuotes.append(currentQuote);
            localPartWithoutQuotes.append('"');
          }
        } else { // opening quote
          removableQuotePair = true;
          currentQuote = new StringBuilder();
        }

        inQuotes = !inQuotes;
      }

      whitespace = isWhitespace(c) && !inQuotes && !previousBackslash;

      if (whitespace) {
        containsWhiteSpace = true;
      }

      if (!whitespace) {
        previousDot = c == '.';
      }

      if (!quotedWhitespace) {
        previousQuotedDot = c == '.';
      }

      // For whitespace within quotes we need some special checks to see
      // if this quote would be removable
      if (quotedWhitespace) {
        if (!previousQuotedDot && !previousBackslash) {
          requireQuotedDot = true;
        }
      }

      // if we already had a prev backslash, this backslash is escaped
      previousBackslash = (c == '\\' && !previousBackslash);
    }

    if (!atFound) return EmailValidationResult.failure(FailureReason.MISSING_AT_SYMBOL);

    // Check length
    int localPartLen = localPart.length() - localPartCommentLength;
    if (localPartLen == 0) return EmailValidationResult.failure(FailureReason.LOCAL_PART_MISSING);
    if (localPartLen > 64) return EmailValidationResult.failure(FailureReason.LOCAL_PART_TOO_LONG);

    int domainLen = domain.length() - domainCommentLength;
    if (domainLen == 0) return EmailValidationResult.failure(FailureReason.DOMAIN_MISSING);
    if (domainLen > 255) return EmailValidationResult.failure(FailureReason.DOMAIN_TOO_LONG);

    // Check that local-part does not end with '.'
    if (localPart.charAt(localPart.length() - 1) == '.') {
      // unless we are configured to allow it (GMail doesn't care about a trailing dot)
      if (!allowNonstandardDots) {
        return EmailValidationResult.failure(FailureReason.LOCAL_PART_ENDS_WITH_DOT);
      }

      // if we allow a trailing dot, just make sure it's not the only thing in the local-part
      if (localPartLen <= 1) {
        return EmailValidationResult.failure(FailureReason.LOCAL_PART_MISSING);
      }
    }

    // Ensure the TLD is not empty or greater than 63 chars
    if (currentDomainPart.length() <= 0) {
      return EmailValidationResult.failure(FailureReason.MISSING_FINAL_DOMAIN_PART);
    }

    if (currentDomainPart.length() > 63) {
      return EmailValidationResult.failure(FailureReason.TOP_LEVEL_DOMAIN_TOO_LONG);
    }

    // Check that the final domain part does not start with '-'
    // We already checked to make sure it doesn't end with '-'
    if (currentDomainPart.charAt(0) == '-') {
      return EmailValidationResult.failure(FailureReason.DOMAIN_PART_STARTS_WITH_DASH);
    }

    // Ensure the last domain part (TLD) is not all numeric
    String tld = currentDomainPart.toString();

    if (tld.chars().allMatch(Character::isDigit)) {
      return EmailValidationResult.failure(FailureReason.NUMERIC_TLD);
    }

    domainParts.add(tld);

    // Validate the characters in the domain if it is not an IP address
    if (!isIpAddress && !isValidIdn(domainWithoutComments.toString())) {
      return EmailValidationResult.failure(FailureReason.INVALID_DOMAIN_CHARACTER);
    }

    Email parsed = new Email(
        localPart.toString(), localPartWithoutComments.toString(),
        localPartWithoutQuotes.toString(), domain.toString(), domainWithoutComments.toString(),
        fullSourceRoute, domainParts, comments, sourceRoutes, isIpAddress,
        containsWhiteSpace, isAscii);

    return EmailValidationResult.success(parsed);
  }

  private static Optional<String> validateComment(String s) {
    if (s.length() < 2) return Optional.empty();

    StringBuilder builder = new StringBuilder(s.length());

    boolean previousBackslash = false;
    boolean foundClosingParenthesis = false;

    for (int i = 0, size = s.length(); i < size; i++) {
      char c = s.charAt(i);

      if (c == '(' && !previousBackslash && i != 0) {
        // comment within a comment??
        Optional<String> inner = validateComment(s.substring(i));

        if (!inner.isPresent()) return Optional.empty();

        i += inner.get().length() - 1;
        builder.append(inner.get());
        continue;
      }

      builder.append(c);

      if (c == ')' && !previousBackslash) {
        foundClosingParenthesis = true;
        break;
      }

      previousBackslash = c == '\\';
    }

    if (!foundClosingParenthesis) {
      return Optional.empty();
    }

    return Optional.of(builder.toString());
  }

  private static Optional<SourceRouteDetail> validateSourceRouting(String s) {
    boolean requireNewDomain = true;

    SourceRouteDetail detail = new SourceRouteDetail();
    StringBuilder sourceRoute = new StringBuilder();
    StringBuilder currentDomainPart = new StringBuilder();

    int i = 0;

    for (int size = s.length(); i < size; i++) {
      char c = s.charAt(i);

      // We need the @ character for a new domain
      if (requireNewDomain && c != '@') return Optional.empty();

      // We can't see the @ character unless we need it
      if (c == '@' && !requireNewDomain) return Optional.empty();

      // A . , : means we should validate the current domain part
      if (c == '.' || c == ',' || c == ':') {
        // Cannot be empty or more than 63 chars
        if (currentDomainPart.length() == 0 || currentDomainPart.length() > 63) {
          return Optional.empty();
        }

        // Cannot start or end with '-'
        if (currentDomainPart.charAt(0) == '-'
            || currentDomainPart.charAt(currentDomainPart.length() - 1) == '-') {
          return Optional.empty();
        }

        // TLD cannot be all numeric
        if ((c == ',' || c == ':')
            && currentDomainPart.toString().chars().allMatch(Character::isDigit)) {
          return Optional.empty();
        }

        currentDomainPart = new StringBuilder();
      } else {
        if (c != '@') currentDomainPart.append(c);
      }

      // A comma is the end of the current domain route
      requireNewDomain = c == ',';

      detail.fullRoute.append(c);

      if (c == ',' || c == ':') {
        String route = sourceRoute.toString();

        if (!isValidIdn(route)) return Optional.empty();

        detail.routes.add(route);

        sourceRoute = new StringBuilder();
      } else if (c != '@') {
        sourceRoute.append(c);
      }

      if (c == ':') break;
    }

    // If we haven't seen the end of the current part, its invalid
    if (currentDomainPart.length() > 0) return Optional.empty();

    // If we haven't seen the end of the current source route, its invalid
    if (sourceRoute.length() > 0) return Optional.empty();

    // If we needed a new domain (last saw a comma), fail
    if (requireNewDomain) return Optional.empty();

    return Optional.of(detail);
  }

  private static boolean isValidIdn(String test) {
    String domain;

    try {
      domain = IDN.toASCII(test, IDN.ALLOW_UNASSIGNED);
    } catch (IllegalArgumentException e) {
      // If IDN.toASCII fails, it's not valid
      return false;
    }

    for (int i = 0, size = domain.length(); i < size; i++) {
      char c = domain.charAt(i);

      if (!JMail.ALLOWED_DOMAIN_CHARACTERS.contains(c)) return false;
    }

    return true;
  }

  /**
   * Returns true if the given character is a whitespace character.
   *
   * @param c the character to check
   * @return true if whitespace, false otherwise
   */
  private static boolean isWhitespace(char c) {
    return (c == ' ' || c == '\n' || c == '\r');
  }

  private static final class SourceRouteDetail {
    private final StringBuilder fullRoute = new StringBuilder();
    private final List<String> routes = new ArrayList<>();
  }

  private static final String IPV6_PREFIX = "IPv6:";

  // Set of characters that are not allowed in the local-part outside of quotes
  private static final Set<Character> DISALLOWED_UNQUOTED_CHARACTERS = new HashSet<>(
      Arrays.asList('\t', '(', ')', ',', ':', ';', '<', '>', '@', '[', ']', '"',
          // Control characters 1-8, 11, 12, 14-31
          '␁', '␂', '␃', '␄', '␅', '␆', '␇', '␈', '␋', '␌', '␎', '␏', '␐', '␑',
          '␒', '␓', '␔', '␕', '␖', '␗', '␘', '␙', '␚', '␛', '␜', '␝', '␟', '␁'));

  // Set of characters that are allowed in the domain
  private static final Set<Character> ALLOWED_DOMAIN_CHARACTERS = new HashSet<>(
      Arrays.asList(
          // A - Z
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
          'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          // a - z
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
          's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          // 0 - 9
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
          // Hyphen and dot (also allow whitespace between parts)
          '-', '.', ' '));

  // Set of characters within local-part quotes that require an escape
  private static final Set<Character> ALLOWED_QUOTED_WITH_ESCAPE = new HashSet<>(
      Arrays.asList('\r', '␀', '\n'));
}
