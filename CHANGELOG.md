# JMail Changelog

## 2.0.0 (Upcoming)

### Breaking Changes

- By default, `Email#normalized` now lowercases the email address **and** removes any extraneous quotes in the local-part of the address.
  To revert this behavior so that it behaves the same as v1, use the following:
  
  ```
  myEmailObject.normalized(
    NormalizationOptions.builder()
      .keepQuotes()
      .adjustCase(CaseOption.NO_CHANGE)
      .build());
  ```


- The `jmail.normalize.strip.quotes` JVM system property no longer does anything. Quotes are stripped by default now.
  If you need to disable quote stripping, use `NormalizationOptionsBuilder#keepQuotes()`.


- Removed `Email#normalized(boolean)` method which allowed for a normalized email with stripped quotes.
  Quotes are stripped by default now. If you need to disable quote stripping, use `NormalizationOptionsBuilder#keepQuotes()`.


- `FailureReason` was switched from an enum to a class in order to support custom failure reasons, so you cannot
  use it in a `switch` statement.


- `FailureReason.MISSING_TOP_LEVEL_DOMAIN` was changed to `FailureReason.MISSING_FINAL_DOMAIN_PART`.
  `MISSING_TOP_LEVEL_DOMAIN` was previously used for email addresses that failed validation because
  they ended the email address with a comment. This `FailureReason` was potentially misleading, for example if you
  enabled `requireTopLevelDomain()` on your `EmailValidator`. Note that the `MISSING_TOP_LEVEL_DOMAIN` failure reason
  is now used properly: if you use the rule `requireTopLevelDomain()`, any address that is missing the TLD will give
  that failure reason.


- Email addresses that fail validation due to additional rules added to the `EmailValidator` (such as
  `disallowIpDomain()` or `requireValidMXRecord()`) no longer return a generic `FailureReason.FAILED_CUSTOM_VALIDATION`
  in the `EmailValidationResult`. Instead, it returns a more specific `FailureReason` depending on the rule. 

### FailureReason Improvements

#### Changes to the FailureReason supplied for additional rules

The `FailureReason` returned in the `EmailValidationResult` is useful to understand why a specific
email address failed validation. In v2.0.0, the `FailureReason` returned for email addresses that failed
one of the additional validation rules added to your `EmailValidator` (such as `disallowIpDomain()` or
`requireValidMXRecord()`) now return more specific and useful reasons (such as `CONTAINS_IP_DOMAIN` or
`INVALID_MX_RECORD`).

```
EmailValidator validator = JMail.strictValidator()
  .requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM);
  
EmailValidationResult result = validator.validate("test@test.org");

assertEquals(FailureReason.INVALID_TOP_LEVEL_DOMAIN, result.getFailureReason());
```

#### Option to provide FailureReason for custom rules

Additionally, you can specify your own `FailureReason` for any custom validation rules that you add
to your `EmailValidator`. Use the new `withRule(Predicate<Email>, FailureReason)` or
`withRules(Map<Predicate<Email>, FailureReason>)` methods to specify the failure reason for each
of your custom rules. If no failure reason is supplied, then the rule will default to the
`FailureReason.FAILED_CUSTOM_VALIDATION` reason.

```
FailureReason nonGmailFailure = new FailureReason("NON_GMAIL_ADDRESS");

EmailValidator validator = JMail.strictValidator()
  .withRule(e -> e.domain.startsWith("gmail"), nonGmailFailure);
  
EmailValidationResult result = validator.validate("test@yahoo.com");

assertEquals(nonGmailFailure, result.getFailureReason());
```

### Email Address Normalization Improvements

#### New Normalization Methods

This version introduces a new `NormalizationOptions` class that is used to provide
configuration of the behavior of the `Email#normalize()` method. See the table below to see
all the new and existing options.

In v2.0.0, you can use either `Email#normalize()` (with no parameters) or `Email#normalize(NormalizationOptions options)`.

The first method without parameters will return a normalized email address based on the default
normalization options. The second method allows you to provide your own `NormalizationOptions` at runtime
depending on your needs. The custom normalization options can be created using the `NormalizationOptions#builder()`
method.

#### Normalization Options

Thanks, @Sprokof, for contributing (introducing `removeDots` and `adjustCase` options)! 🎉

| Option                      | Description                                                                                                                                                                                                   | `NormalizationOptionsBuilder` Method                                              |
|-----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| stripQuotes                 | Remove all unnecessary quotes in the local-part of the address                                                                                                                                                | `stripQuotes()`                                                                   |
| adjustCase                  | Adjust the case of the email address. Possible options are: `NO_CHANGE`, `LOWERCASE`, `LOWERCASE_LOCAL_PART_ONLY`, `LOWERCASE_DOMAIN_ONLY`, `UPPERCASE`, `UPPERCASE_LOCAL_PART_ONLY`, `UPPERCASE_DOMAIN_ONLY` | `adjustCase(CaseOption)`                                                          |
| removeDots                  | Remove all dots from the local-part of the address                                                                                                                                                            | `removeDots()`                                                                    |
| removeSubAddress            | Remove any sub-addressing (or tagged-addressing) from the local-part of the address. For example, `first.last+test@gmail.com` will become `first.last@gmail.com`                                              | `removeSubAddress()` or `removeSubAddress(String)`                                |
| performUnicodeNormalization | Perform unicode normalization on the local-part of the email address                                                                                                                                          | `performUnicodeNormalization()` or `performUnicodeNormalization(Normalizer.Form)` |

---
## 1.6.3

- Fix bug where email addresses containing control characters in the local-part were incorrectly considered valid. (Thanks @PascalSchumacher for reporting!)
- Add new methods `ifValid(Consumer<Email> action)` and `ifValidOrElse(Consumer<Email> action, Consumer<FailureReason> failureAction)` to the `EmailValidationResult` object.

---
## 1.6.2

- Fix bug where IPv4 addresses with non-arabic numerals would incorrectly be considered valid. (Thanks @harrel56 for reporting!)
- Fix bug where IPv4 addresses with extraneous leading zeros would incorrectly be considered valid. (Thanks @harrel56 for reporting!)
- The `requireValidMXRecord()` validation rule now correctly fails validation for domains that use a "Null MX" record. (Thanks @elmolm for contributing! 🎉)

---
## 1.6.1

- Fix bug so that email addresses that end in a dash `-` character now correctly fail validation with the reason `FailureReason.DOMAIN_PART_ENDS_WITH_DASH` instead of incorrectly returning `FailureReason.ENDS_WITH_DOT`. (Thanks @tbatchlear for reporting!)

---
## 1.6.0

- Add a new rule `requireAscii()` that considers an email address containing non-ASCII characters to be invalid. (Thanks @frodeto for suggesting!)
- Add new property `isAscii()` on `Email` objects that returns if the email address only contains ASCII characters or not.
- Add option to strip quotes within the local-part of an email address when normalizing the address with the `normalize()` method. (Thanks @tdelaney-leadiro for suggesting!)
    - This new option will remove quotes if the email address would still be valid and semantically the same without them.
    - To enable the option, either:
        - Call the normalize method that takes a boolean as the parameter, and use `true`. Example: `email.normalize(true)`
        - Set the `-Djmail.normalize.strip.quotes=true` JVM property at runtime, and continue to use the `normalize()` method without parameters.

---
## 1.5.1

- Add a new rule `requireValidMXRecord(int initialTimeout, int numRetries)` that allows for customization of the timeout for DNS lookups. (Thanks @dotneutron for suggesting!)
- Reduce the default timeout for DNS lookups when adding the `requireValidMXRecord()` rule to an `EmailValidator` from potentially taking a maximum of 25 seconds to a maximum of 600 milliseconds.

---
## 1.5.0

- Add new method `validate(String email)` that returns an `EmailValidationResult` object, containing the reason for validation failure upon failure. (Thanks @bobharner for suggesting!)
- Add new `ValidationRule` `requireValidMXRecord()` to consider email addresses that have a domain with no MX record in DNS as invalid. (Thanks @lpellegr for suggesting!)
- Fix bug where an email address that ends with a comment that is missing the closing parentheses were incorrectly considered as valid. For example: `test@test.com(comment`

---
## 1.4.1

- Add new `ValidationRule` `disallowObsoleteWhitespace()` to consider email addresses with obsolete whitespace as invalid. (Thanks @PascalSchumacher for suggesting!)

---
## 1.4.0

- Add new `normalized()` method on the `Email` class to provide a way to get a "normalized" version of an email address (the address without any comments or optional parts).

---
## 1.3.3

- Fix bug where invalid characters in the domain could result in an `IllegalArgumentException` instead of returning false. (Thanks @PascalSchumacher for reporting!)

---
## 1.3.2

- Fix bug where domain names that contained an emoji would be incorrectly invalid. (Thanks @Autom8edChaos for reporting!)

---
## 1.3.1

- Improve `equals()` and `hashCode()` methods for `Email` and `TopLevelDomain`
- Fix inconsistencies in some Javadocs

---
## 1.3.0

- `InternetProtocolAddress.validate(String ip)` now validates IPv6 addresses without requiring the `IPv6:` prefix.
- Add new `JMail.isInvalid(String email)` and `EmailValidator#isInvalid(String email)` methods as a convenience for testing if an email address is invalid.

---
## 1.2.3

- Add `toString()` method on `EmailValidator`
- Add `withRules(Collection<Predicate<Email>> rules)` method on `EmailValidator` to create a new `EmailValidator` from the collection of rules provided

---
## 1.2.2

- Fix bug where an exception would be thrown on invalid email addresses with whitespace or comments after a trailing `.` character. For example, `abc.def@ghi. (comment)` is invalid, and before this version JMail would throw an exception instead of return invalid.
  (Thanks @ea234 for reporting!)

---
## 1.2.1

- `EmailValidator` is now immutable

---
## 1.2.0

- Switch `TopLevelDomain` from an enum to a class, allowing for creation of any valid top level domain (Thanks @bowbahdoe!)
- Add `module-info.java` so projects on JDK 9+ can use this library as a Java module
- Bugfix: Addresses with empty quoted strings (`""@test.org`) are now correctly considered valid
- Bugfix: Addresses with explicit source routing (`@1st.relay,@2nd.relay:user@final.domain`) are now considered valid. However, explicit source routing is deprecated since RFC 5321. `JMail.strictValidator()` disallows explicit source routing by default
- Bugfix: Addresses with quoted identifiers (`John Smith <John@smith.com>`) are now correctly considered valid
- New properties on the `Email` object:
    - `identifier()`
    - `hasIdentifier()`
    - `explicitSourceRoutes()`

---
## 1.1.0

- Disallow construction of utility classes and prevent classes from being subclassed (Thanks @bowbahdoe!)
- Fix bug where email addresses that have a dotless domain or top level domain starting with the `-` character would be incorrectly classified as valid.
  For example, `test@-foo` and `test@my.-domain` should both be invalid.

---
## 1.0.4

- You can now disallow email addresses with reserved domains listed in [RFC 2606](https://datatracker.ietf.org/doc/html/rfc2606), such as `example.com` or `.invalid`.

   ```
   JMail.validator().disallowReservedDomains().isValid("test@example.com");
   ```
  
---
## 1.0.3

- Fix bug where JMail did not consider single quoted symbols (ex. `\@`) as valid.

---
## 1.0.2

- Better javadocs
- Internal performance improvements

---
## 1.0.1

- Add `JMail.strictValidator()` that has pre-configured common rules enabled (stricter than the RFCs allow)

---
## 1.0.0

- Initial release of JMail with email validation, IP address validation, and custom rules.
