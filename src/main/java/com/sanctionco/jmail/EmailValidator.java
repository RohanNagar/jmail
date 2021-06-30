package com.sanctionco.jmail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The {@code EmailValidator} class provides a way to validate email addresses
 * beyond what the basic validation of {@link JMail#tryParse(String)} provides.
 *
 * <p>Custom rules can be added to an {@code EmailValidator} that will perform any
 * additional required validations.
 *
 * <p>Example usage:
 *
 * <pre>
 * Optional&#60;Email&#62; parsedEmail = JMail.validator()
 *     .disallowIpDomain()
 *     .requireTopLevelDomain()
 *     .withRule(email -> email.domain().startsWith("test"))
 *     .tryParse("test@test.com");
 * </pre>
 */
public final class EmailValidator {
  // Define some predicates here so that when adding them to the set of validation
  // predicates we protect against adding them multiple times.
  private static final Predicate<Email> DISALLOW_IP_DOMAIN_PREDICATE
      = ValidationRules::disallowIpDomain;
  private static final Predicate<Email> REQUIRE_TOP_LEVEL_DOMAIN_PREDICATE
      = ValidationRules::requireTopLevelDomain;
  private static final Predicate<Email> DISALLOW_EXPLICIT_SOURCE_ROUTING
      = ValidationRules::disallowExplicitSourceRouting;
  private static final Predicate<Email> DISALLOW_QUOTED_IDENTIFIERS
      = ValidationRules::disallowQuotedIdentifiers;
  private static final Predicate<Email> DISALLOW_RESERVED_DOMAINS_PREDICATE
      = ValidationRules::disallowReservedDomains;

  private final Set<Predicate<Email>> validationPredicates;

  EmailValidator(Set<Predicate<Email>> validationPredicates) {
    this.validationPredicates = Collections.unmodifiableSet(validationPredicates);
  }

  EmailValidator() {
    this(new HashSet<>());
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and an
   * additional provided custom validation rule.
   *
   * <p>Example usage:
   *
   * <pre>
   * validator.withRule(email -> email.domain().startsWith("test"));
   * </pre>
   *
   * @param rule the requirement for a valid email address. This must be a {@link Predicate} that
   *             accepts an {@link Email} object.
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator withRule(Predicate<Email> rule) {
    Set<Predicate<Email>> rules = new HashSet<>(validationPredicates);
    rules.add(rule);

    return new EmailValidator(rules);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowIpDomain(Email)} rule.
   * Email addresses that have an IP address for a domain will fail validation.
   *
   * <p>For example, {@code "sample@[1.2.3.4]"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowIpDomain() {
    return withRule(DISALLOW_IP_DOMAIN_PREDICATE);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#requireTopLevelDomain(Email)} rule.
   * Email addresses that do not have a top level domain will fail validation.
   *
   * <p>For example, {@code "sample@mailserver"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator requireTopLevelDomain() {
    return withRule(REQUIRE_TOP_LEVEL_DOMAIN_PREDICATE);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowExplicitSourceRouting(Email)} rule.
   * Email addresses that have explicit source routing will fail validation.
   *
   * <p>For example, {@code "@1st.relay,@2nd.relay:user@final.domain"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowExplicitSourceRouting() {
    return withRule(DISALLOW_EXPLICIT_SOURCE_ROUTING);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowQuotedIdentifiers(Email)} rule.
   * Email addresses that have quoted identifiers will fail validation.
   *
   * <p>For example, {@code "John Smith <test@server.com>"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowQuotedIdentifiers() {
    return withRule(DISALLOW_QUOTED_IDENTIFIERS);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowReservedDomains(Email)} rule.
   * Email addresses that have a reserved domain according to RFC 2606 will fail validation.
   *
   * <p>For example, {@code "name@example.com"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowReservedDomains() {
    return withRule(DISALLOW_RESERVED_DOMAINS_PREDICATE);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#requireOnlyTopLevelDomains(Email, Set)} rule.
   * Email addresses that have top level domains other than those provided will
   * fail validation.
   *
   * <p>For example, if you require only {@link TopLevelDomain#DOT_COM}, the email address
   * {@code "name@host.net"} would be invalid.
   *
   * @param allowed the set of allowed {@link TopLevelDomain}
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator requireOnlyTopLevelDomains(TopLevelDomain... allowed) {
    return withRule(email -> ValidationRules.requireOnlyTopLevelDomains(
        email, Arrays.stream(allowed).collect(Collectors.toSet())));
  }

  /**
   * Return true if the given email address is valid according to all registered validation rules,
   * or false otherwise. See {@link JMail#tryParse(String)} for details on the basic
   * validation that is always performed.
   *
   * @param email the email address to validate
   * @return the result of the validation
   */
  public boolean isValid(String email) {
    return JMail.tryParse(email)
        .filter(this::passesPredicates)
        .isPresent();
  }

  /**
   * Require that the given email address is valid according to all registered validation rules,
   * throwing {@link InvalidEmailException} if the email is invalid. See
   * {@link JMail#tryParse(String)} for details on the basic validation that is always performed.
   *
   * @param email the email address to validate
   * @throws InvalidEmailException if the validation fails
   */
  public void enforceValid(String email) throws InvalidEmailException {
    if (!isValid(email)) {
      throw new InvalidEmailException();
    }
  }

  /**
   * Attempts to parse the given email address string, only succeeding if the given address is
   * valid according to all registered validation rules. See {@link JMail#tryParse(String)}
   * for details on the basic validation that is always performed.
   *
   * @param email the email address to parse
   * @return an {@link Optional} containing the parsed {@link Email}, or empty if the email
   *         is invalid according to all registered validation rules
   */
  public Optional<Email> tryParse(String email) {
    return JMail.tryParse(email).filter(this::passesPredicates);
  }

  /**
   * Test the given email address against all configured validation predicates.
   *
   * @param email the email address to test
   * @return true if it passes the predicates, false otherwise
   */
  private boolean passesPredicates(Email email) {
    return validationPredicates.stream()
        .allMatch(rule -> rule.test(email));
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", EmailValidator.class.getSimpleName() + "[", "]")
        .add("validationRuleCount=" + validationPredicates.size())
        .toString();
  }
}
