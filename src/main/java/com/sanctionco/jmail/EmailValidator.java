package com.sanctionco.jmail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
public class EmailValidator {
  // Define some predicates here so that when adding them to the set of validation
  // predicates we protect against adding them multiple times.
  private static final Predicate<Email> DISALLOW_IP_DOMAIN_PREDICATE
      = ValidationRules::disallowIpDomain;
  private static final Predicate<Email> REQUIRE_TOP_LEVEL_DOMAIN_PREDICATE
      = ValidationRules::requireTopLevelDomain;

  private final Set<Predicate<Email>> validationPredicates;

  EmailValidator(Set<Predicate<Email>> validationPredicates) {
    this.validationPredicates = validationPredicates;
  }

  EmailValidator() {
    this(new HashSet<>());
  }

  /**
   * Add a custom validation rule to this {@code EmailValidator}.
   *
   * <p>Example usage:
   *
   * <pre>
   * validator.withRule(email -> email.domain().startsWith("test"));
   * </pre>
   *
   * @param rule the requirement for a valid email. This must be a {@link Predicate} that
   *             accepts an {@link Email} object.
   * @return this, for chaining
   */
  public EmailValidator withRule(Predicate<Email> rule) {
    validationPredicates.add(rule);
    return this;
  }

  /**
   * Add the {@link ValidationRules#disallowIpDomain(Email)} rule to this validator.
   * Email addresses that have an IP address for a domain will fail validation.
   *
   * @return this, for chaining
   */
  public EmailValidator disallowIpDomain() {
    validationPredicates.add(DISALLOW_IP_DOMAIN_PREDICATE);
    return this;
  }

  /**
   * Add the {@link ValidationRules#requireTopLevelDomain(Email)} rule to this validator.
   * Email addresses that do not have a top level domain will fail validation.
   *
   * @return this, for chaining
   */
  public EmailValidator requireTopLevelDomain() {
    validationPredicates.add(REQUIRE_TOP_LEVEL_DOMAIN_PREDICATE);
    return this;
  }

  /**
   * Add the {@link ValidationRules#requireOnlyTopLevelDomains(Email, Set)} rule to this validator.
   * Email addresses that have top level domains other than those provided will
   * fail validation.
   *
   * @param allowed the set of allowed {@link TopLevelDomain}
   * @return this, for chaining
   */
  public EmailValidator requireOnlyTopLevelDomains(TopLevelDomain... allowed) {
    validationPredicates.add(email -> ValidationRules.requireOnlyTopLevelDomains(
        email, Arrays.stream(allowed).collect(Collectors.toSet())));

    return this;
  }

  /**
   * Return true if the given email is valid according to all registered validation rules,
   * or false otherwise. See {@link JMail#tryParse(String)} for details on the basic
   * validation that is always performed.
   *
   * @param email the email to validate
   * @return the result of the validation
   */
  public boolean isValid(String email) {
    return JMail.tryParse(email)
        .filter(this::passesPredicates)
        .isPresent();
  }

  /**
   * Require that the given email is valid according to all registered validation rules,
   * throwing {@link InvalidEmailException} if the email is invalid. See
   * {@link JMail#tryParse(String)} for details on the basic validation that is always performed.
   *
   * @param email the email to validate
   * @throws InvalidEmailException if the validation fails
   */
  public void enforceValid(String email) throws InvalidEmailException {
    if (!JMail.tryParse(email).filter(this::passesPredicates).isPresent()) {
      throw new InvalidEmailException();
    }
  }

  /**
   * Attempts to parse the given email string, only succeeding if the given email is
   * valid according to all registered validation rules. See {@link JMail#tryParse(String)}
   * for details on the basic validation that is always performed.
   *
   * @param email the email to parse
   * @return an {@link Optional} containing the parsed {@link Email}, or empty if the email
   *         is invalid according to all registered validation rules
   */
  public Optional<Email> tryParse(String email) {
    Optional<Email> result = JMail.tryParse(email);

    if (result.isPresent() && passesPredicates(result.get())) {
      return result;
    }

    return Optional.empty();
  }

  /**
   * Test the given email against all configured validation predicates.
   *
   * @param email the email to test
   * @return true if it passes the predicates, false otherwise
   */
  private boolean passesPredicates(Email email) {
    return validationPredicates.stream()
        .allMatch(rule -> rule.test(email));
  }
}
