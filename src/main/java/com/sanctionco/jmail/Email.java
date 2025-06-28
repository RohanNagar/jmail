package com.sanctionco.jmail;

import com.sanctionco.jmail.normalization.CaseOption;
import com.sanctionco.jmail.normalization.NormalizationOptions;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents an email address.
 */
public final class Email {
  private final String localPart;
  private final String localPartWithoutComments;
  private final String localPartWithoutQuotes;
  private final String domain;
  private final String domainWithoutComments;
  private final String fullSourceRoute;
  private final String identifier;
  private final List<String> domainParts;
  private final List<String> comments;
  private final List<String> sourceRoutes;
  private final boolean isIpAddress;
  private final boolean containsWhitespace;
  private final boolean isAscii;
  private final boolean hasIdentifier;
  private final TopLevelDomain tld;

  Email(String localPart, String localPartWithoutComments, String localPartWithoutQuotes,
        String domain, String domainWithoutComments,
        String fullSourceRoute,
        List<String> domainParts, List<String> comments, List<String> sourceRoutes,
        boolean isIpAddress, boolean containsWhitespace, boolean isAscii) {
    this.localPart = localPart;
    this.localPartWithoutComments = localPartWithoutComments;
    this.localPartWithoutQuotes = localPartWithoutQuotes;
    this.domain = domain;
    this.domainWithoutComments = domainWithoutComments;
    this.fullSourceRoute = fullSourceRoute;
    this.domainParts = Collections.unmodifiableList(domainParts);
    this.comments = Collections.unmodifiableList(comments);
    this.sourceRoutes = Collections.unmodifiableList(sourceRoutes);
    this.isIpAddress = isIpAddress;
    this.containsWhitespace = containsWhitespace;
    this.isAscii = isAscii;
    this.identifier = null;
    this.hasIdentifier = false;

    this.tld = domainParts.size() > 1
        ? TopLevelDomain.fromString(domainParts.get(domainParts.size() - 1))
        : TopLevelDomain.NONE;
  }

  Email(Email other, String identifier) {
    this.localPart = other.localPart;
    this.localPartWithoutComments = other.localPartWithoutComments;
    this.localPartWithoutQuotes = other.localPartWithoutQuotes;
    this.domain = other.domain;
    this.domainWithoutComments = other.domainWithoutComments;
    this.fullSourceRoute = other.fullSourceRoute;
    this.identifier = identifier;
    this.domainParts = other.domainParts;
    this.comments = other.comments;
    this.sourceRoutes = other.sourceRoutes;
    this.isIpAddress = other.isIpAddress;
    this.containsWhitespace = other.containsWhitespace;
    this.isAscii = other.isAscii;
    this.hasIdentifier = identifier != null && identifier.length() > 0;
    this.tld = other.tld;
  }

  /**
   * Try to construct a new {@link Email} object from the given email address
   * string. This method is a convenience wrapper around {@link JMail#tryParse(String)}
   * and performs the same validation.
   *
   * @param email the email address to parse
   * @return an {@link Optional} containing the parsed {@link Email}, or empty if the email
   *         is invalid
   */
  public static Optional<Email> of(String email) {
    return JMail.tryParse(email);
  }

  /**
   * Get the local-part of this email address. For example, the local-part of
   * {@code "test@example.com"} is {@code "test"}.
   *
   * @return the local-part string
   */
  public String localPart() {
    return localPart;
  }

  /**
   * Get the local-part of this email address without any comments. For example,
   * the local-part without comments of {@code "test(comment)@example.com"} is
   * {@code "test"}. If the email has no comments, this is effectively the same
   * as {@link #localPart()}.
   *
   * @return the local-part string without comments
   */
  public String localPartWithoutComments() {
    return localPartWithoutComments;
  }

  /**
   * Get the domain of this email address. For example, the domain of
   * {@code "test@example.com"} is {@code "example.com"}.
   *
   * @return the domain string
   */
  public String domain() {
    return domain;
  }

  /**
   * Get the domain of this email address without any comments. For example,
   * the local-part without comments of {@code "test@(comment)example.com"} is
   * {@code "example.com"}. If the email has no comments, this is effectively the same
   * as {@link #domain()}.
   *
   * @return the domain string without comments
   */
  public String domainWithoutComments() {
    return domainWithoutComments;
  }

  /**
   * Returns the identifier of the email address, if it has one. For example, the identifier
   * of the email {@code "John Smith <test@server.com>"} is {@code "John Smith "}.
   *
   * @return the identifier of the email or {@code null} if it does not have one
   */
  public String identifier() {
    return identifier;
  }

  /**
   * Get the list of domain parts of this email address. A domain part is each
   * of the dot-separated strings in the domain. For example, the domain parts of
   * {@code "test@my.example.com"} are {@code ["my", "example", "com"]}.
   *
   * @return the list of domain part strings
   */
  public List<String> domainParts() {
    return domainParts;
  }

  /**
   * Get a list of comments that are contained in this email address. For example,
   * the comments of {@code "test(hello)@(world)example.com"} are {@code ["hello", "world"]}.
   *
   * @return the list of comment strings
   */
  public List<String> comments() {
    return comments;
  }

  /**
   * Get a list of explicit source routes defined in this email address. For example,
   * the source routes of {@code "@1st.relay,@2nd.relay:user@final.domain"} are
   * {@code ["1st.relay", "2nd.relay"]}.
   *
   * <p>Note that explicit source routing has been
   * <a href="https://datatracker.ietf.org/doc/html/rfc5321#section-3.6.1">deprecated</a>
   * as of RFC 5321 and you <em>SHOULD NOT</em> allow addresses with explicit source routing
   * except under unusual circumstances.
   *
   * @return the list of explict source routes
   */
  public List<String> explicitSourceRoutes() {
    return sourceRoutes;
  }

  /**
   * Get whether this email address has an IP address domain. For example,
   * the address {@code "test@[12.34.56.78]"} will return {@code true}, but the
   * address {@code "test@example.com"} will return {@code false}.
   *
   * @return true if this email has an IP address domain, false otherwise
   */
  public boolean isIpAddress() {
    return isIpAddress;
  }

  /**
   * Get whether this email address contains whitespace in either the local-part
   * or the domain, outside of comments or quoted-strings. For example, the
   * address {@code "1234   @   local(blah)  .com"} will return {@code true}, but
   * the address {@code "first.(  middle  )last@test.org"} will return {@code false}.
   *
   * @return true if this email contains whitespace outside of comments or quoted-strings,
   *     false otherwise
   */
  public boolean containsWhitespace() {
    return containsWhitespace;
  }

  /**
   * Get whether this email address contains only ASCII characters. For example, the address
   * {@code "test12@gmail.com"} will return {@code true}, but the address
   * {@code "jørn@test.com"} will return {@code false}.
   *
   * @return true if this email contains only ASCII characters, false otherwise
   */
  public boolean isAscii() {
    return isAscii;
  }

  /**
   * Get whether this email address has an identifier. For example, the address
   * {@code "John Smith <test@server.com>"} will return {@code true}, but the address
   * {@code "test@example.com"} will return {@code false}.
   *
   * @return true if this email has en identifier, false otherwise
   */
  public boolean hasIdentifier() {
    return hasIdentifier;
  }

  /**
   * Get the {@link TopLevelDomain} of this email address. For example,
   * the address {@code "test@example.com"} will return {@link TopLevelDomain#DOT_COM}.
   *
   * @return the {@link TopLevelDomain} of this email, or {@link TopLevelDomain#NONE}
   *         if the email does not have a top level domain
   */
  public TopLevelDomain topLevelDomain() {
    return tld;
  }

  /**
   * <p>Return a "normalized" version of this email address. This method returns a version of the
   * email address that is the same as the original email address, except:</p>
   *
   * <ul>
   *   <li>All comments are removed</li>
   *   <li>All identifiers or source routing are removed</li>
   *   <li>Any unnecessary quotes within the local-part are removed</li>
   *   <li>The entire address is lowercased</li>
   * </ul>
   *
   * <p>For example, the address {@code "tEST@(comment)example.com"} will return
   * {@code "test@example.com"}.</p>
   *
   * <p>This method uses the default set of {@link NormalizationOptions} when performing
   * normalization. Use {@link #normalized(NormalizationOptions)} instead of this method
   * to further customize how normalization behaves.</p>
   *
   * @return the normalized version of this email address
   */
  public String normalized() {
    return normalized(NormalizationOptions.DEFAULT_OPTIONS);
  }

  /**
   * <p>Return a "normalized" version of this email address. The actual result of normalization
   * depends on the configured normalization options, but in general this method returns
   * a version of the email address that is the same as the original email address, except:</p>
   *
   * <ul>
   *   <li>All comments are removed</li>
   *   <li>All identifiers or source routing are removed</li>
   *   <li>Any unnecessary quotes within the local-part are removed</li>
   *   <li>The entire address is lowercased</li>
   * </ul>
   *
   * <p>For example, the address {@code "tEST@(comment)example.com"} will return
   * {@code "test@example.com"}.</p>
   *
   * <p>See {@link NormalizationOptions} for more details on all of the configurable options.</p>
   *
   * @param options the {@link NormalizationOptions} to use when normalizing
   * @return the normalized version of this email address
   * @see NormalizationOptions
   */
  public String normalized(NormalizationOptions options) {
    return normalizedLocalPart(options) + "@" + normalizedDomain(options);
  }

  /**
   * <p>Returns an MD5 reference to the email address. This format can be useful to share references
   * to the email address without sharing the actual address.</p>
   *
   * <p>The reference is calculated by first performing normalization on the address, and then
   * taking the MD5 hash of the normalized address. See {@link #normalized()} for more details
   * on how normalization works.</p>
   *
   * <p>This method uses the default {@link NormalizationOptions}. If you wish to customize how the
   * normalization happens, use {@link #reference(NormalizationOptions)} instead.</p>
   *
   * @return the MD5 reference string for the address
   * @throws NoSuchAlgorithmException if the MD5 algorithm is unable to be loaded
   */
  public String reference() throws NoSuchAlgorithmException {
    return reference(NormalizationOptions.DEFAULT_OPTIONS);
  }

  /**
   * <p>Returns an MD5 reference to the email address. This format can be useful to share references
   * to the email address without sharing the actual address.</p>
   *
   * <p>The reference is calculated by first performing normalization on the address, and then
   * taking the MD5 hash of the normalized address. See {@link #normalized(NormalizationOptions)}
   * for more details on how normalization works.</p>
   *
   * @param options the {@link NormalizationOptions} to use when normalizing
   * @return the MD5 reference string for the address
   * @throws NoSuchAlgorithmException if the MD5 algorithm is unable to be loaded
   */
  public String reference(NormalizationOptions options) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");

    byte[] normalized = normalized(options).getBytes(StandardCharsets.UTF_8);
    byte[] digest = md.digest(normalized);

    return toHexString(digest);
  }

  /**
   * <p>Returns a redacted version of the email address in the format {@code "{local-part}@domain"}.
   * This format can be useful when storing addresses in a data store (to avoid storing the original
   * address).</p>
   *
   * <p>The redacted address is calculated by first performing normalization on the address, and
   * then taking the SHA-1 hash of the local-part of the normalized address to construct the final
   * redacted version of the address. See {@link #normalized()} for more details on how
   * normalization works.</p>
   *
   * <p>This method uses the default {@link NormalizationOptions}. If you wish to customize how the
   * normalization happens, use {@link #redacted(NormalizationOptions)} instead.</p>
   *
   * @return the redacted version of the email address
   * @throws NoSuchAlgorithmException if the SHA-A algorithm is unable to be loaded
   */
  public String redacted() throws NoSuchAlgorithmException {
    return redacted(NormalizationOptions.DEFAULT_OPTIONS);
  }

  /**
   * <p>Returns a redacted version of the email address in the format {@code "{local-part}@domain"}.
   * This format can be useful when storing addresses in a data store (to avoid storing the original
   * address).</p>
   *
   * <p>The redacted address is calculated by first performing normalization on the address, and
   * then taking the SHA-1 hash of the local-part of the normalized address to construct the final
   * redacted version of the address. See {@link #normalized(NormalizationOptions)} for more
   * details on how normalization works.</p>
   *
   * @param options the {@link NormalizationOptions} to use when normalizing
   * @return the redacted version of the email address
   * @throws NoSuchAlgorithmException if the SHA-1 algorithm is unable to be loaded
   */
  public String redacted(NormalizationOptions options) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA1");

    byte[] normalizedLocalPart = normalizedLocalPart(options).getBytes(StandardCharsets.UTF_8);
    byte[] digest = md.digest(normalizedLocalPart);

    return "{" + toHexString(digest) + "}@" + normalizedDomain(options);
  }

  /**
   * <p>Returns a munged version of the email address in the format {@code "lo*****@do*****"}.
   * This format can be useful when displaying addresses on a user account page.
   *
   * <p>The munged address is calculated by first performing normalization on the address, and
   * then taking the first two characters of both the local-part and the domain, and adding
   * five {@code *} characters to each. See {@link #normalized()} for more
   * details on how normalization works.</p>
   *
   * <p>This method uses the default {@link NormalizationOptions}. If you wish to customize how the
   * normalization happens, use {@link #munged(NormalizationOptions)} instead.</p>
   *
   * @return the munged version of the email address
   */
  public String munged() {
    return munged(NormalizationOptions.DEFAULT_OPTIONS);
  }


  /**
   * <p>Returns a munged version of the email address in the format {@code "lo*****@do*****"}.
   * This format can be useful when displaying addresses on a user account page.
   *
   * <p>The munged address is calculated by first performing normalization on the address, and
   * then taking the first two characters of both the local-part and the domain, and adding
   * five {@code *} characters to each. See {@link #normalized(NormalizationOptions)} for more
   * details on how normalization works.</p>
   *
   * @param options the {@link NormalizationOptions} to use when normalizing
   * @return the munged version of the email address
   */
  public String munged(NormalizationOptions options) {
    String localPart = normalizedLocalPart(options);
    localPart = localPart.length() < 2 ? localPart : localPart.substring(0, 2);

    String domain = normalizedDomain(options);
    domain = domain.length() < 2 ? domain : domain.substring(0, 2);

    return localPart + "*****@" + domain + "*****";
  }

  private String normalizedLocalPart(NormalizationOptions options) {
    String localPart = options.shouldStripQuotes()
        ? localPartWithoutQuotes
        : localPartWithoutComments;

    if (options.shouldRemoveSubAddress()) {
      int separatorIndex = localPart.indexOf(options.getSubAddressSeparator());

      if (separatorIndex != -1) {
        localPart = localPart.substring(0, separatorIndex);
      }
    }

    CaseOption caseOption = options.getCaseOption();

    localPart = options.shouldRemoveDots()
        ? caseOption.adjustLocalPart(localPart.replace(".", ""))
        : caseOption.adjustLocalPart(localPart);

    if (options.shouldPerformUnicodeNormalization()) {
      localPart = Normalizer.normalize(localPart, options.getUnicodeNormalizationForm());
    }

    return localPart;
  }

  private String normalizedDomain(NormalizationOptions options) {
    CaseOption caseOption = options.getCaseOption();

    return isIpAddress
        ? "[" + this.domainWithoutComments + "]"
        : caseOption.adjustDomain(this.domainWithoutComments);
  }

  private String toHexString(byte[] bytes) {
    char[] hexArray = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'};
    char[] hexChars = new char[bytes.length * 2];

    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v / 16];
      hexChars[j * 2 + 1] = hexArray[v % 16];
    }

    return new String(hexChars);
  }

  /**
   * Get the string value of this email address. For example, the address
   * {@code "test@example.com"} will return {@code "test@example.com"}.
   *
   * @return the string email address
   */
  @Override
  public String toString() {
    String fixedDomain = isIpAddress ? "[" + domain + "]" : domain;
    String fixedLocalPart = fullSourceRoute + localPart;

    String addr = fixedLocalPart + "@" + fixedDomain;

    return hasIdentifier
        ? identifier + "<" + addr + ">"
        : addr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Email)) return false;
    Email email = (Email) o;
    return Objects.equals(localPart, email.localPart)
        && Objects.equals(localPartWithoutComments, email.localPartWithoutComments)
        && Objects.equals(localPartWithoutQuotes, email.localPartWithoutQuotes)
        && Objects.equals(domain, email.domain)
        && Objects.equals(domainWithoutComments, email.domainWithoutComments)
        && Objects.equals(fullSourceRoute, email.fullSourceRoute)
        && Objects.equals(identifier, email.identifier)
        && Objects.equals(domainParts, email.domainParts)
        && Objects.equals(sourceRoutes, email.sourceRoutes)
        && Objects.equals(comments, email.comments)
        && Objects.equals(isIpAddress, email.isIpAddress)
        && Objects.equals(containsWhitespace, email.containsWhitespace)
        && Objects.equals(isAscii, email.isAscii)
        && Objects.equals(hasIdentifier, email.hasIdentifier)
        && Objects.equals(tld, email.tld);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        localPart, localPartWithoutComments, localPartWithoutQuotes, domain, domainWithoutComments,
        fullSourceRoute, identifier, domainParts, sourceRoutes, comments, isIpAddress,
        containsWhitespace, isAscii, hasIdentifier, tld);
  }
}
