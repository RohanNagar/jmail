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
   * Return true if the given email passes basic RFC validation. See {@link #tryParse(String)} for
   * details on what is required of an email within basic validation.
   *
   * @param email the email to validate
   * @return the result of the validation
   */
  public static boolean isValid(String email) {
    return tryParse(email).isPresent();
  }

  /**
   * Require that the given email passes basic RFC validation, throwing
   * {@link InvalidEmailException} if the email is invalid. See {@link #tryParse(String)} for
   * details on what is required of an email within basic validation.
   *
   * @param email the email to validate
   * @throws InvalidEmailException if the validation fails
   */
  public static void enforceValid(String email) throws InvalidEmailException {
    if (!JMail.tryParse(email).isPresent()) {
      throw new InvalidEmailException();
    }
  }

  /**
   * Parse the given email into a new {@link Email} object. This method does basic
   * validation on the input email address. This method does not claim to be 100%
   * accurate in determining if an email address is valid or invalid due to the
   * complexity of the email RFC standards. That being said, if you come across
   * an email address that you expect to be valid that fails validation, or
   * vice-versa, please open an issue at the
   * <a href="https://github.com/RohanNagar/jmail">GitHub repo</a> so it can be fixed.
   *
   * <p>In general, this method checks for:
   *
   * <ol>
   * <li>Ensures the email is not null
   * <li>Ensures the email does not end with the '.' character
   * <li>Ensures the email contains both a local-part and a domain separated by
   *     a single '@' character
   * <li>Ensures the local-part is not more than 64 characters
   * <li>Ensures the domain is not more than 255 characters and each domain part is not more than
   *     63 characters
   * <li>Ensures the domain is one of:
   *   <ul>
   *     <li>Only contains allowed characters OR
   *     <li>An IPv4 address OR
   *     <li>An IPv6 address (either normal or dual)
   *   </ul>
   * <li>Ensures the local-part does not start or end with the '.' character
   * <li>Ensures the domain does not start or end with the '-' character
   * <li>Ensures that two '.' characters do not occur next to each other outside of quotes
   * <li>Ensures that the TLD of the domain is not all numeric characters
   * <li>Quoted strings in the local-part either start at the beginning or are preceded by a '.'
   * <li>Ensures that any comments and whitespace only exist in allowed locations
   * </ol>
   *
   * @param email the email to parse
   * @return an {@link Optional} containing the parsed {@link Email}, or empty if the email
   *         is invalid
   */
  public static Optional<Email> tryParse(String email) {
    Optional<Email> parsed = internalTryParse(email);

    if (!parsed.isPresent()) return parsed;

    // if its an ip, we can skip character validation
    if (parsed.get().isIpAddress()) return parsed;

    return parsed.filter(e -> isValidIdn(e.domainWithoutComments()));
  }

  /**
   * Internal parsing method.
   *
   * @param email the email to parse
   * @return a new {@link Email} instance if valid, empty if invalid
   */
  private static Optional<Email> internalTryParse(String email) {
    // email cannot be null
    if (email == null) return Optional.empty();

    // email cannot be less than 3 chars (local-part, @, domain)
    if (email.length() < 3) return Optional.empty();

    // check for source-routing
    List<String> sourceRoutes = Collections.emptyList();
    String fullSourceRoute = "";

    if (email.charAt(0) == '@') {
      Optional<SourceRouteDetail> sourceRoute = validateSourceRouting(email);

      // If there was no source routing, then starting with @ is invalid
      if (!sourceRoute.isPresent()) return Optional.empty();

      // Otherwise update the email to validate to be just the actual email
      SourceRouteDetail detail = sourceRoute.get();
      sourceRoutes = detail.routes;
      fullSourceRoute = detail.fullRoute.toString();

      email = email.substring(fullSourceRoute.length());
    }

    int size = email.length();

    // email cannot be more than 320 chars
    if (size > 320) return Optional.empty();

    // email cannot start with '.'
    if (email.charAt(0) == '.') return Optional.empty();

    // email cannot end with '.' or '-'
    if (email.charAt(size - 1) == '.' || email.charAt(size - 1) == '-') return Optional.empty();

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

    StringBuilder localPart = new StringBuilder(size);
    StringBuilder localPartWithoutComments = new StringBuilder(size);
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

      if (c == '@' && !inQuotes && !previousBackslash) {
        // If we already found an @ outside of quotes, fail
        if (atFound) return Optional.empty();

        // Otherwise
        atFound = true;
        requireAtOrDot = requireAtDotOrComment = false;
        whitespace = false;
        previousDot = true; // '@' acts like a '.' separator
        continue;
      }

      if (c == '\n') {
        if (charactersOnLine <= 0) return Optional.empty();

        charactersOnLine = 0;
      } else if (c != '\r') {
        // Increment for everything other than \r\n
        charactersOnLine++;
      }

      if (requireAtOrDot) {
        // If we needed to find the @ and we didn't, we have to fail
        if (!isWhitespace(c) && c != '.') return Optional.empty();
        else requireAtOrDot = false;
      }

      if (requireAtDotOrComment) {
        if (!isWhitespace(c) && c != '.' && c != '(') return Optional.empty();
        else requireAtDotOrComment = false;
      }

      if (whitespace) {
        // Whitespace is allowed if it is between parts
        if (!previousDot && !previousComment) {
          if (c != '.' && c != '@' && c != '(' && !isWhitespace(c)) {
            return Optional.empty();
          }
        }
      }

      if (c == '(' && !inQuotes) {
        // validate comment
        Optional<String> comment = validateComment(email.substring(i));

        if (!comment.isPresent()) return Optional.empty();

        String commentStr = comment.get();
        int commentStrLen = commentStr.length();

        // Now, what do we need surrounding the comment to make it valid?
        if (!atFound && (i != 0 && !previousDot)) {
          // if at beginning of local part or we had a dot, ok.
          // if not, we need to be at the end of the local part '@' or get a dot
          requireAtOrDot = true;
        } else if (atFound && !firstDomainChar && !previousDot) {
          // if at beginning of domain or we had a dot, ok.
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
      if (c == '.' && previousDot && !inQuotes) return Optional.empty();

      if (!atFound) {
        // No @ found, we're in the local-part
        // If we are at a new quote: it must be preceded by a dot or at the beginning
        if (c == '"' && i > 0 && !previousDot && !inQuotes) {
          return Optional.empty();
        }

        // If we are not in quotes, and this character is not the quote, make sure the
        // character is allowed
        boolean mustBeQuoted = JMail.DISALLOWED_UNQUOTED_CHARACTERS.contains(c);

        if (c != '"' && !inQuotes && !previousBackslash && mustBeQuoted) return Optional.empty();

        if (!inQuotes && previousBackslash && !mustBeQuoted && c != ' ' && c != '\\') {
          return Optional.empty();
        }

        if (inQuotes) {
          // if we are in quotes, we need to make sure that if the character requires
          // a backlash escape, that it is there
          if (JMail.ALLOWED_QUOTED_WITH_ESCAPE.contains(c)) {
            if (!previousBackslash) return Optional.empty();
          }
        }

        localPart.append(c);
        localPartWithoutComments.append(c);
      } else {
        // We're in the domain
        if (firstDomainChar && c == '[') {
          // validate IP address and be done
          String ipDomain = email.substring(i);

          if (!ipDomain.startsWith("[") || !ipDomain.endsWith("]") || ipDomain.length() < 3) {
            return Optional.empty();
          }

          String ip = ipDomain.substring(1, ipDomain.length() - 1);
          Optional<String> validatedIp = InternetProtocolAddress.validate(ip);

          if (!validatedIp.isPresent()) return Optional.empty();

          currentDomainPart.append(validatedIp.get());
          domain.append(validatedIp.get());
          domainWithoutComments.append(validatedIp.get());

          isIpAddress = true;
          break;
        }

        if (c == '.') {
          if (currentDomainPart.length() == 0 || currentDomainPart.length() > 63) {
            return Optional.empty();
          }

          if (currentDomainPart.charAt(0) == '-'
              || currentDomainPart.charAt(currentDomainPart.length() - 1) == '-') {
            return Optional.empty();
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

      if (c == '"' && !previousBackslash) {
        if (inQuotes) requireAtDotOrComment = true; // closing quote, make sure next char is . or @

        inQuotes = !inQuotes;
      }

      whitespace = isWhitespace(c) && !inQuotes && !previousBackslash;

      if (!whitespace) {
        previousDot = c == '.';
      }

      // if we already had a prev backslash, this backslash is escaped
      previousBackslash = (c == '\\' && !previousBackslash);
    }

    if (!atFound) return Optional.empty();

    if (requireAtDotOrComment) return Optional.empty();

    // Check length
    int localPartLen = localPart.length() - localPartCommentLength;
    if (localPartLen == 0 || localPartLen > 64) return Optional.empty();

    int domainLen = domain.length() - domainCommentLength;
    if (domainLen == 0 || domainLen > 255) return Optional.empty();

    // Check that local-part does not end with '.'
    if (localPart.charAt(localPart.length() - 1) == '.') return Optional.empty();

    // Check that the final domain part does not start with '-'
    // We already checked to make sure it doesn't end with '-'
    if (currentDomainPart.charAt(0) == '-') return Optional.empty();

    // Ensure the last domain part (TLD) is not all numeric
    if (currentDomainPart.toString().chars().allMatch(Character::isDigit)) return Optional.empty();

    // Ensure the TLD is not greater than 63 chars
    if (currentDomainPart.length() > 63) return Optional.empty();

    domainParts.add(currentDomainPart.toString());

    return Optional.of(new Email(
        localPart.toString(), localPartWithoutComments.toString(),
        domain.toString(), domainWithoutComments.toString(), fullSourceRoute,
        domainParts, comments, sourceRoutes, isIpAddress));
  }

  private static Optional<String> validateComment(String s) {
    if (s.length() < 2) return Optional.empty();

    StringBuilder builder = new StringBuilder(s.length());

    boolean previousBackslash = false;

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
        break;
      }

      previousBackslash = c == '\\';
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

    // If we needed a new domain (last saw a comma), fail
    if (requireNewDomain) return Optional.empty();

    return Optional.of(detail);
  }

  private static boolean isValidIdn(String test) {
    String domain = IDN.toASCII(test);

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

  // Set of characters that are not allowed in the local-part outside of quotes
  private static final Set<Character> DISALLOWED_UNQUOTED_CHARACTERS = new HashSet<>(
      Arrays.asList('\t', '(', ')', ',', ':', ';', '<', '>', '@', '[', ']', '"'));

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
