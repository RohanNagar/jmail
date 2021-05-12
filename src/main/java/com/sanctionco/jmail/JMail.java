package com.sanctionco.jmail;

import com.sanctionco.jmail.net.InternetProtocolAddress;

import java.net.IDN;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JMail {

  /**
   * Returns a new instance of {@link EmailValidator}.
   *
   * @return the new {@link EmailValidator} instance
   */
  public static EmailValidator validator() {
    return new EmailValidator();
  }

  /**
   * Return true if the given email passes basic validation. See {@link #tryParse(String)} for
   * details on what is required of an email within basic validation.
   *
   * @param email the email to validate
   * @return the result of the validation
   */
  public static boolean isValid(String email) {
    return tryParse(email).isPresent();
  }

  /**
   * Require that the given email passes basic validation, throwing {@link InvalidEmailException}
   * if the email is invalid. See {@link #tryParse(String)} for details on what is required
   * of an email within basic validation.
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
   * <li>Ensures that any comments only exist in allowed locations
   * </ol>
   *
   * <p>Note: Internationalized domain names are supported. Domains are converted to punycode
   *    before checking their characters.
   *
   * @param email the email to parse
   * @return an {@link Optional} containing the parsed {@link Email}, or empty if the email
   *         is invalid
   */
  public static Optional<Email> tryParse(String email) {
    Optional<Email> parsed = internalTryParse(email);

    if (!parsed.isPresent()) return parsed;

    Email parsedEmail = parsed.get();

    // if its an ip, we can skip character validation
    if (parsedEmail.isIpAddress()) return parsed;

    String domain = IDN.toASCII(parsedEmail.domainWithoutComments());

    for (int i = 0, size = domain.length(); i < size; i++) {
      char c = domain.charAt(i);

      if (!JMail.ALLOWED_DOMAIN_CHARACTERS.contains(c)) return Optional.empty();
    }

    return parsed;
  }

  /**
   * Internal parsing method.
   *
   * @param email the email to parse
   * @return a new {@link Email} instance if valid, empty if invalid
   */
  private static Optional<Email> internalTryParse(String email) {
    // email cannot be null, less than 3 chars (local-part, @, domain), or more than 320 chars
    if (email == null || email.length() < 3 || email.length() > 320) return Optional.empty();

    // email cannot start with '.'
    if (email.startsWith(".")) return Optional.empty();

    // email cannot end with '.' or '-'
    if (email.endsWith(".") || email.endsWith("-")) return Optional.empty();

    boolean atFound = false;
    boolean inQuotes = false;
    boolean previousDot = false;
    boolean previousBackslash = false;
    boolean previousQuote = false;
    boolean firstDomainChar = true;
    boolean isIpAddress = false;
    boolean requireAtOrDot = false;
    boolean requireAtDotOrComment = false;
    boolean whitespace = false;
    boolean previousComment = false;

    int size = email.length();

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

      if (c == '@' && !inQuotes) {
        // If we already found an @ outside of quotes, fail
        if (atFound) return Optional.empty();

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
        // Quotes are a pain in the ass
        // If we are at a new quote: it must be preceded by a dot or at the beginning
        if (c == '"' && i > 0 && !previousDot && !inQuotes) {
          return Optional.empty();
        }

        // If we are not in quotes, and this character is not the quote, make sure the
        // character is allowed
        if (c != '"'
            && !inQuotes
            && JMail.DISALLOWED_UNQUOTED_CHARACTERS.contains(c)) return Optional.empty();

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

        // if we opened and closed quotes without anything inside, fail
        if (previousQuote) return Optional.empty();

        inQuotes = !inQuotes;
        previousQuote = true;
      } else {
        previousQuote = false;
      }

      whitespace = isWhitespace(c) && !inQuotes;

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

    // Ensure the last domain part (TLD) is not all numeric
    if (currentDomainPart.toString().chars().allMatch(Character::isDigit)) {
      return Optional.empty();
    }

    // Ensure the TLD is not greater than 63 chars
    if (currentDomainPart.length() > 63) return Optional.empty();

    domainParts.add(currentDomainPart.toString());

    return Optional.of(new Email(
        localPart.toString(), localPartWithoutComments.toString(),
        domain.toString(), domainWithoutComments.toString(),
        domainParts, comments, isIpAddress));
  }

  private static Optional<String> validateComment(String s) {
    if (!s.startsWith("(") || s.length() < 2) return Optional.empty();

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

  /**
   * Returns true if the given character is a whitespace character.
   *
   * @param c the character to check
   * @return true if whitespace, false otherwise
   */
  private static boolean isWhitespace(char c) {
    return (c == ' ' || c == '\n' || c == '\r');
  }

  // Set of characters that are not allowed in the local-part outside of quotes
  private static final Set<Character> DISALLOWED_UNQUOTED_CHARACTERS = Arrays
      .stream(new Character[]{
          '\t', '(', ')', ',', ':', ';', '<', '>', '@', '[', ']', '"', '\\'
      })
      .collect(Collectors.toCollection(HashSet::new));

  // Set of characters that are allowed in the domain
  private static final Set<Character> ALLOWED_DOMAIN_CHARACTERS = Arrays
      .stream(new Character[]{
          // A - Z
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
          'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          // a - z
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
          's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          // 0 - 9
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
          // Hyphen and dot (also allow whitespace between parts)
          '-', '.', ' '
      })
      .collect(Collectors.toCollection(HashSet::new));

  // Set of characters within local-part quotes that require an escape
  private static final Set<Character> ALLOWED_QUOTED_WITH_ESCAPE = Arrays
      .stream(new Character[]{
          '\r', '␀', '\n'
      })
      .collect(Collectors.toCollection(HashSet::new));
}
