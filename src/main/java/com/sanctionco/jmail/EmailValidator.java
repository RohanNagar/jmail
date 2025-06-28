package com.sanctionco.jmail;

import com.sanctionco.jmail.disposable.DisposableDomainSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
  private static final Predicate<Email> DISALLOW_SINGLE_CHAR_TOP_LEVEL_DOMAINS_PREDICATE
      = ValidationRules::disallowSingleCharacterTopLevelDomains;
  private static final Predicate<Email> DISALLOW_EXPLICIT_SOURCE_ROUTING_PREDICATE
      = ValidationRules::disallowExplicitSourceRouting;
  private static final Predicate<Email> DISALLOW_QUOTED_IDENTIFIERS_PREDICATE
      = ValidationRules::disallowQuotedIdentifiers;
  private static final Predicate<Email> DISALLOW_RESERVED_DOMAINS_PREDICATE
      = ValidationRules::disallowReservedDomains;
  private static final Predicate<Email> DISALLOW_OBSOLETE_WHITESPACE_PREDICATE
      = ValidationRules::disallowObsoleteWhitespace;
  private static final Predicate<Email> REQUIRE_VALID_MX_RECORD_PREDICATE
      = ValidationRules::requireValidMXRecord;
  private static final Predicate<Email> REQUIRE_ASCII_PREDICATE
      = ValidationRules::requireAscii;

  private final Map<Predicate<Email>, FailureReason> validationPredicates;
  private final boolean allowNonstandardDots;

  EmailValidator(Map<Predicate<Email>, FailureReason> validationPredicates,
                 boolean allowNonstandardDots) {
    this.validationPredicates = Collections.unmodifiableMap(validationPredicates);
    this.allowNonstandardDots = allowNonstandardDots;
  }

  EmailValidator() {
    this(new HashMap<>(), false);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * additional provided custom validation rules.
   *
   * <p>Example usage:
   *
   * <pre>
   * validator.withRules(Map.of(
   *     email -> email.domain().startsWith("test"), new FailureReason("MISSING_TEST_PREFIX"),
   *     email -> email.localPart.contains("hello"), new FailureReason("MUST_CONTAIN_HELLO"));
   * </pre>
   *
   * @param rules a map of requirements that make a valid email address and each requirement's
   *              {@link FailureReason}
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator withRules(Map<Predicate<Email>, FailureReason> rules) {
    Map<Predicate<Email>, FailureReason> ruleMap = new HashMap<>(validationPredicates);
    ruleMap.putAll(rules);

    return new EmailValidator(ruleMap, allowNonstandardDots);
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
    return withRules(rules.stream()
        .collect(Collectors.toMap(p -> p, p -> FailureReason.FAILED_CUSTOM_VALIDATION)));
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
    return withRules(Collections.singletonMap(rule, FailureReason.FAILED_CUSTOM_VALIDATION));
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and an
   * additional provided custom validation rule.
   *
   * <p>Example usage:
   *
   * <pre>
   * validator.withRule(
   *   email -> email.domain().startsWith("test"),
   *   new FailureReason("MISSING_TEST_PREFIX"));
   * </pre>
   *
   * @param rule the requirement for a valid email address. This must be a {@link Predicate} that
   *             accepts an {@link Email} object.
   * @param failureReason the {@link FailureReason} to return in the {@link EmailValidationResult}
   *                      if an email address fails to pass this rule.
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator withRule(Predicate<Email> rule, FailureReason failureReason) {
    return withRules(Collections.singletonMap(rule, failureReason));
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and an
   * additional provided custom validation rule.
   *
   * <p>Example usage:
   *
   * <pre>
   * validator.withRule(
   *   email -> email.domain().startsWith("test"), "MISSING_TEST_PREFIX");
   * </pre>
   *
   * @param rule the requirement for a valid email address. This must be a {@link Predicate} that
   *             accepts an {@link Email} object.
   * @param failureReason the reason to return in the {@link EmailValidationResult} as the
   *                      {@link FailureReason }if an email address fails to pass this rule.
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator withRule(Predicate<Email> rule, String failureReason) {
    return withRules(Collections.singletonMap(rule, new FailureReason(failureReason)));
  }

  /**
   * <p>Create a new {@code EmailValidator} (with all rules from the current instance) that
   * allows email addresses to have a local-part that either starts with or ends with a dot
   * {@code .} character.</p>
   *
   * <p>While not allowed according to RFC, a leading or trailing dot character in the local-part
   * <strong>is allowed</strong> by some mail providers (such as GMail).</p>
   *
   * <p>For example, {@code ".test@gmail.com"} would be considered valid if you use the
   * {@code EmailValidator} returned by this method.</p>
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator allowNonstandardDots() {
    return new EmailValidator(this.validationPredicates, true);
  }

  /**
   * <p>Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowIpDomain(Email)} rule.
   * Email addresses that have an IP address for a domain will fail validation with
   * {@link FailureReason#CONTAINS_IP_DOMAIN}.
   *
   * <p>For example, {@code "sample@[1.2.3.4]"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowIpDomain() {
    return withRule(
        DISALLOW_IP_DOMAIN_PREDICATE,
        FailureReason.CONTAINS_IP_DOMAIN);
  }

  /**
   * <p>Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#requireTopLevelDomain(Email)} rule.
   * Email addresses that do not have a top level domain will fail validation
   * with {@link FailureReason#MISSING_TOP_LEVEL_DOMAIN}.
   *
   * <p>For example, {@code "sample@mailserver"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator requireTopLevelDomain() {
    return withRule(
        REQUIRE_TOP_LEVEL_DOMAIN_PREDICATE,
        FailureReason.MISSING_TOP_LEVEL_DOMAIN);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowSingleCharacterTopLevelDomains(Email)} rule.
   * Email addresses that have top level domains that are just a single character will
   * fail validation with {@link FailureReason#SINGLE_CHARACTER_TOP_LEVEL_DOMAIN}.
   *
   * <p>For example, the email address {@code "name@host.c"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowSingleCharacterTopLevelDomains() {
    return withRule(
        DISALLOW_SINGLE_CHAR_TOP_LEVEL_DOMAINS_PREDICATE,
        FailureReason.SINGLE_CHARACTER_TOP_LEVEL_DOMAIN);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowExplicitSourceRouting(Email)} rule.
   * Email addresses that have explicit source routing will fail validation with
   * {@link FailureReason#CONTAINS_EXPLICIT_SOURCE_ROUTING}.
   *
   * <p>For example, {@code "@1st.relay,@2nd.relay:user@final.domain"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowExplicitSourceRouting() {
    return withRule(
        DISALLOW_EXPLICIT_SOURCE_ROUTING_PREDICATE,
        FailureReason.CONTAINS_EXPLICIT_SOURCE_ROUTING);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowQuotedIdentifiers(Email)} rule.
   * Email addresses that have quoted identifiers will fail validation with
   * {@link FailureReason#CONTAINS_QUOTED_IDENTIFIER}.
   *
   * <p>For example, {@code "John Smith <test@server.com>"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowQuotedIdentifiers() {
    return withRule(
        DISALLOW_QUOTED_IDENTIFIERS_PREDICATE,
        FailureReason.CONTAINS_QUOTED_IDENTIFIER);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowReservedDomains(Email)} rule.
   * Email addresses that have a reserved domain according to RFC 2606 will fail validation
   * with {@link FailureReason#CONTAINS_RESERVED_DOMAIN}.
   *
   * <p>For example, {@code "name@example.com"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowReservedDomains() {
    return withRule(
        DISALLOW_RESERVED_DOMAINS_PREDICATE,
        FailureReason.CONTAINS_RESERVED_DOMAIN);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#requireOnlyTopLevelDomains(Email, java.util.Set)} rule.
   * Email addresses that have top level domains other than those provided will
   * fail validation with {@link FailureReason#INVALID_TOP_LEVEL_DOMAIN}.
   *
   * <p>For example, if you require only {@link TopLevelDomain#DOT_COM}, the email address
   * {@code "name@host.net"} would be invalid.
   *
   * @param allowed the set of allowed {@link TopLevelDomain}
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator requireOnlyTopLevelDomains(TopLevelDomain... allowed) {
    return withRule(
        email -> ValidationRules.requireOnlyTopLevelDomains(
            email, Arrays.stream(allowed).collect(Collectors.toSet())),
        FailureReason.INVALID_TOP_LEVEL_DOMAIN);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowObsoleteWhitespace(Email)} rule.
   * Email addresses that have obsolete folding whitespace according to RFC 2822 will fail
   * validation with {@link FailureReason#CONTAINS_OBSOLETE_WHITESPACE}.
   *
   * <p>For example, {@code "1234   @   local(blah)  .com"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowObsoleteWhitespace() {
    return withRule(
        DISALLOW_OBSOLETE_WHITESPACE_PREDICATE,
        FailureReason.CONTAINS_OBSOLETE_WHITESPACE);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#requireValidMXRecord(Email)} rule.
   * Email addresses that have a domain without a valid MX record will fail validation with
   * {@link FailureReason#INVALID_MX_RECORD}.
   *
   * <p><strong>NOTE: Adding this rule to your EmailValidator may increase
   * the amount of time it takes to validate email addresses, as the default initial timeout is
   * 100ms and the number of retries using exponential backoff is 2.
   * Use {@link #requireValidMXRecord(int, int)} to customize the timeout and retries.</strong>
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator requireValidMXRecord() {
    return withRule(
        REQUIRE_VALID_MX_RECORD_PREDICATE,
        FailureReason.INVALID_MX_RECORD);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#requireValidMXRecord(Email, int, int)} rule.
   * Email addresses that have a domain without a valid MX record will fail validation with
   * {@link FailureReason#INVALID_MX_RECORD}.
   *
   * <p>This method allows you to customize the timeout and retries for performing DNS lookups.
   * The initial timeout is supplied in milliseconds, and the number of retries indicate how many
   * times to retry the lookup using exponential backoff. Each successive retry will use a
   * timeout that is twice as long as the previous try.
   *
   * @param initialTimeout the timeout in milliseconds for the initial DNS lookup
   * @param numRetries the number of retries to perform using exponential backoff
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator requireValidMXRecord(int initialTimeout, int numRetries) {
    return withRule(
        email -> ValidationRules.requireValidMXRecord(email, initialTimeout, numRetries),
        FailureReason.INVALID_MX_RECORD);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#disallowDisposableDomains(Email, DisposableDomainSource)}  rule.
   * Email addresses that have a disposable domain according to the provided
   * {@link DisposableDomainSource} will fail validation with
   * {@link FailureReason#CONTAINS_DISPOSABLE_DOMAIN}.
   *
   * <p>For example, if the domain {@code disposableinbox.com} is a disposable domain, then the
   * email address {@code "myemail@disposableinbox.com"} would be invalid.
   *
   * @param disposableDomainSource the source of disposable domains
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator disallowDisposableDomains(DisposableDomainSource disposableDomainSource) {
    return withRule(
        email -> ValidationRules.disallowDisposableDomains(email, disposableDomainSource),
        FailureReason.CONTAINS_DISPOSABLE_DOMAIN);
  }

  /**
   * Create a new {@code EmailValidator} with all rules from the current instance and the
   * {@link ValidationRules#requireAscii(Email)} rule.
   * Email addresses that contain characters other than those in the ASCII charset will fail
   * validation with {@link FailureReason#NON_ASCII_ADDRESS}
   *
   * <p>For example, {@code "jørn@test.com"} would be invalid.
   *
   * @return the new {@code EmailValidator} instance
   */
  public EmailValidator requireAscii() {
    return withRule(
        REQUIRE_ASCII_PREDICATE,
        FailureReason.NON_ASCII_ADDRESS);
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
    return JMail.validate(email, allowNonstandardDots)
        .getEmail()
        .filter(e -> !testPredicates(e).isPresent())
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
    EmailValidationResult result = JMail.validate(email, allowNonstandardDots);

    // If failed basic validation, just return it
    if (!result.getEmail().isPresent()) return result;

    // If the address fails custom validation, return failure, otherwise return the original result
    return testPredicates(result.getEmail().get())
        .map(EmailValidationResult::failure)
        .orElse(result);
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
    return JMail.validate(email, allowNonstandardDots).getEmail()
        .filter(e -> !testPredicates(e).isPresent());
  }

  /**
   * Test the given email address against all configured validation predicates.
   *
   * @param email the email address to test
   * @return an Optional that is either empty if all predicates passed or filled with the proper
   *         FailureReason if one failed
   */
  private Optional<FailureReason> testPredicates(Email email) {
    return validationPredicates.entrySet().stream()
        .filter(entry -> !entry.getKey().test(email))
        .findFirst()
        .map(Map.Entry::getValue);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", EmailValidator.class.getSimpleName() + "[", "]")
        .add("validationRuleCount=" + validationPredicates.size())
        .toString();
  }
}
