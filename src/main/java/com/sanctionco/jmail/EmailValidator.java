package com.sanctionco.jmail;

import java.util.Arrays;
import java.util.Collection;
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
  private static final Predicate<Email> DISALLOW_OBSOLETE_WHITESPACE_PREDICATE
      = ValidationRules::disallowObsoleteWhitespace;
  private static final Predicate<Email> REQUIRE_VALID_MX_RECORD_PREDICATE
      = ValidationRules::requireValidMXRecord;

  private final Set<Predicate<Email>> validationPredicates;

  EmailValidator(Set<Predicate<Email>> validationPredicates) {
    this.validationPredicates = Collections.unmodifiableSet(validationPredicates);
  }

  EmailValidator() {
    this(new HashSet<>());
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * additional provided custom validation rules.
   *
   * <p>Example usage:
   *
   * <pre>
   * validator.withRules(List.of(
   *     email -> email.domain().startsWith("test"),
   *     email -> email.localPart.contains("hello")));
   * </pre>
   *
   * @param rules a collection of requirements that make a valid email address
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator withRules(Collection<Predicate<Email>> rules) {
    Set<Predicate<Email>> ruleSet = new HashSet<>(validationPredicates);
    ruleSet.addAll(rules);

    return new EmailValidator(ruleSet);
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
    return withRules(Collections.singletonList(rule));
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
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowObsoleteWhitespace(Email)} rule.
   * Email addresses that have obsolete folding whitespace according to RFC 2822 will fail
   * validation.
   *
   * <p>For example, {@code "1234   @   local(blah)  .com"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowObsoleteWhitespace() {
    return withRule(DISALLOW_OBSOLETE_WHITESPACE_PREDICATE);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#requireValidMXRecord(Email)} rule.
   * Email addresses that have a domain without a valid MX record will fail validation.
   *
   * <p><strong>NOTE: Adding this rule to your EmailValidator may significantly increase
   * the amount of time it takes to validate email addresses.</strong>
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator requireValidMXRecord() {
    return withRule(REQUIRE_VALID_MX_RECORD_PREDICATE);
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
   * Return true if the given email address is <strong>NOT</strong> valid according to all
   * registered validation rules, or false otherwise. See {@link JMail#tryParse(String)} for
   * details on the basic validation that is always performed.
   *
   * @param email the email address to validate
   * @return the result of the validation
   */
  public boolean isInvalid(String email) {
    return !isValid(email);
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
   * Determine if the given email address is valid, returning a new {@link EmailValidationResult}
   * object that contains details on the result of the validation. Use this method if you need to
   * see the {@link FailureReason} upon validation failure. See {@link JMail#tryParse(String)}
   * for details on what is required of an email address within basic validation.
   *
   * @param email the email address to validate
   * @return a {@link EmailValidationResult} containing success or failure, along with the parsed
   *         {@link Email} object if successful, or the {@link FailureReason} if not
   */
  public EmailValidationResult validate(String email) {
    EmailValidationResult result = JMail.validate(email);

    // If failed basic validation, just return it
    if (!result.getEmail().isPresent()) return result;

    // If the address fails custom validation, return failure
    if (!passesPredicates(result.getEmail().get())) {
      return EmailValidationResult.failure(FailureReason.FAILED_CUSTOM_VALIDATION);
    }

    return result;
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
