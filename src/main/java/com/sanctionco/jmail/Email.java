package com.sanctionco.jmail;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents an email address.
 */
public final class Email {
  private final String localPart;
  private final String localPartWithoutComments;
  private final String domain;
  private final String domainWithoutComments;
  private final String fullSourceRoute;
  private final String identifier;
  private final List<String> domainParts;
  private final List<String> comments;
  private final List<String> sourceRoutes;
  private final boolean isIpAddress;
  private final boolean hasIdentifier;
  private final TopLevelDomain tld;

  Email(String localPart, String localPartWithoutComments,
        String domain, String domainWithoutComments,
        String fullSourceRoute, String identifier,
        List<String> domainParts, List<String> comments, List<String> sourceRoutes,
        boolean isIpAddress) {
    this.localPart = localPart;
    this.localPartWithoutComments = localPartWithoutComments;
    this.domain = domain;
    this.domainWithoutComments = domainWithoutComments;
    this.fullSourceRoute = fullSourceRoute;
    this.identifier = identifier;
    this.domainParts = Collections.unmodifiableList(domainParts);
    this.comments = Collections.unmodifiableList(comments);
    this.sourceRoutes = Collections.unmodifiableList(sourceRoutes);
    this.isIpAddress = isIpAddress;
    this.hasIdentifier = identifier != null && identifier.length() > 0;

    this.tld = domainParts.size() > 1
        ? TopLevelDomain.fromString(domainParts.get(domainParts.size() - 1))
        : TopLevelDomain.NONE;
  }

  Email(Email other, String identifier) {
    this.localPart = other.localPart;
    this.localPartWithoutComments = other.localPartWithoutComments;
    this.domain = other.domain;
    this.domainWithoutComments = other.domainWithoutComments;
    this.fullSourceRoute = other.fullSourceRoute;
    this.identifier = identifier;
    this.domainParts = other.domainParts;
    this.comments = other.comments;
    this.sourceRoutes = other.sourceRoutes;
    this.isIpAddress = other.isIpAddress;
    this.hasIdentifier = identifier != null && identifier.length() > 0;
    this.tld = other.tld;
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
   * as {@link #domain()} ()}.
   *
   * @return the domain string without comments
   */
  public String domainWithoutComments() {
    return domainWithoutComments;
  }

  /**
   * Returns the identifier of the email address, if it has one. For example, the identifier
   * of the email <pre>"John Smith &#60;test@server.com&#62;"</pre> is {@code "John Smith "}.
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
   * as of RFC 5321 and you SHOULD NOT use explicit source routing except under unusual
   * circumstances.
   *
   * @return the list of explict source routes
   */
  public List<String> explicitSourceRoutes() {
    return sourceRoutes;
  }

  /**
   * Get whether or not this email address has an IP address domain. For example,
   * the address {@code "test@[12.34.56.78]"} will return {@code true}, but the
   * address {@code "test@example.com"} will return {@code false}.
   *
   * @return true if this email has an IP address domain, false otherwise
   */
  public boolean isIpAddress() {
    return isIpAddress;
  }

  /**
   * Get whether or not this email address has an identifier. For example, the address
   * <pre>"John Smith &#60;test@server.com&#62;"</pre> will return {@code true}, but the address
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
    return localPart.equals(email.localPart)
        && domain.equals(email.domain);
  }

  @Override
  public int hashCode() {
    return Objects.hash(localPart, domain);
  }
}
