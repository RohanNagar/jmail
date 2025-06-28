# JMail

<a href="https://search.maven.org/artifact/com.sanctionco.jmail/jmail">
  <img src="https://img.shields.io/maven-central/v/com.sanctionco.jmail/jmail.svg?colorB=brightgreen&label=maven%20central" alt="Maven Central">
</a>
<a href="http://javadoc.io/doc/com.sanctionco.jmail/jmail">
  <img src="http://javadoc.io/badge/com.sanctionco.jmail/jmail.svg" alt="Javadoc">
</a>
<a href="https://codecov.io/gh/RohanNagar/jmail">
  <img src="https://codecov.io/gh/RohanNagar/jmail/branch/master/graph/badge.svg" alt="Coverage Status">
</a>
<a href="https://search.maven.org/artifact/com.sanctionco.jmail/jmail">
  <img src="https://img.shields.io/badge/monthly_downloads-110%2C503-green" alt="Monthly Downloads">
</a>

A modern, fast, zero-dependency library for working with email addresses
and performing email address validation in Java.

Built for Java 8 and up.

[Try out the algorithm online!](https://www.rohannagar.com/jmail/)

[Why JMail?](#why-jmail) • [Installation](#installation) • [Usage](#usage) •
[IP Validation](#bonus-ip-address-validation) • [Contributing](#contributing)

## Why JMail?

JMail was built mainly because I wanted to tackle the complex problem
of email address validation without using Regex. Along the way, JMail became a
much better choice than other Java email validation libraries
(such as Apache Commons Validator or Jakarta (Javax) Mail Validation) for the
following reasons:

1. JMail is **_more correct_** than other libraries. For example, both
   Apache Commons and Jakarta Mail consider `first@last@test.org` as a valid
   email address! It clearly is not, as it has two `@` characters. JMail correctly
   considers this address invalid. You can
   [see a full comparison of correctness and try it out for yourself online](https://www.rohannagar.com/jmail/).

2. JMail is **_faster_** than other libraries by, on average, at least
   2x, thanks in part to lack of regex.

3. JMail has **_zero dependencies_** and is very lightweight.

4. JMail is **_modern_**. It is built for Java 8+, and provides many
   [useful methods and data accessors](#usage).

[Click here for a full report](https://www.rohannagar.com/jmail/)
of the differences in correctness and speed between JMail and other libraries.

> While JMail is more correct than other libraries, I cannot guarantee
that it is 100% correct. Email RFCs are long and complex, and I have
likely missed some specific details. Please open an issue if you
find an incorrect validation result for a specific email (or even better,
a pull request with a fix).</br></br>
I also highly recommend that you send verification emails to user-provided
email addresses. This is the only way to ensure that the email
address exists and that the recipient wants that email address to be used.

## Installation

Add this library as a dependency in your `pom.xml`:

```xml
<dependency>
  <groupId>com.sanctionco.jmail</groupId>
  <artifactId>jmail</artifactId>
  <version>2.0.2</version>
</dependency>
```

Or in your `build.gradle`:

```groovy
implementation 'com.sanctionco.jmail:jmail:2.0.2'
```

## Usage

### Standard Email Validation

To perform standard, RFC-compliant email validation, you can use the static methods
available in `JMail`. For example, to test validation:

```java
String email = "test@example.com";

if (JMail.isValid(email)) {
  // Work with your email string
}
```

Or to enforce validation, throwing an `InvalidEmailException` on failure:

```java
String email = "test@example.com";

try {
  JMail.enforceValid(email);
  
  // Work with your email string
} catch (InvalidEmailException _) {
  // Handle invalid email
}
```

You can also retrieve the reason for validation failure with the `validate` method:

```java
String email = "test@example.com"

EmailValidationResult result = JMail.validate(email);

if (result.isSuccess()) {
  // Use the email address
} else {
  FailureReason reason = result.getFailureReason();

  logger.error("Validating email address failed with reason: " + reason);
}
```

### Custom Email Validation

JMail also provides an `EmailValidator` class that allows for much more
customization of what constitutes a valid email address. You can require
additional [common validation rules](#additional-validation-rules),
or supply your own. For example:

```java
// In general, you should use JMail.strictValidator()
EmailValidator validator = JMail.strictValidator()
    // Require that the top-level-domain is ".com"
    .requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM)
    // Require that the local-part starts with "allowed"
    .withRule(email -> email.localPart().startsWith("allowed"),
            new FailureReason("DOES_NOT_START_WITH_ALLOWED"));

boolean valid = validator.isValid("allowed-email@test.com");
boolean invalidWithoutTld = validator.isValid("allowed@test");
boolean invalidWithoutDotCom = validator.isValid("allowed@test.net");
boolean invalidWithoutAllowed = validator.isValid("invalid@test.com");
```

### The `Email` Object

JMail also includes an `Email` object that makes working with
email addresses easier. The `Email` object has the following properties:

| Property getter            | Description                                                                           | Example using `test(hello)@(world)example.one.com`                                             |
|----------------------------|---------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| localPart()                | The local-part of the email address                                                   | `test(hello)`                                                                                  |
| localPartWithoutComments() | The local-part of the email address without comments                                  | `test`                                                                                         |
| domain()                   | The domain of the email address                                                       | `(world)example.one.com`                                                                       |
| domainWithoutComments()    | The domain of the email address without comments                                      | `example.one.com`                                                                              |
| domainParts()              | A list of the parts of the domain                                                     | `[example, one, com]`                                                                          |
| identifier()               | The identifier of the email address, if it has one.                                   | `null`<br/>(For `Admin <test@server.com>`, it would be `Admin`)                                |
| comments()                 | A list of the comments in the email address                                           | `[hello, world]`                                                                               |
| explicitSourceRoutes()     | A list of explicit source routes in the address, if present                           | `[]`<br/>(For `@1st.relay,@2nd.relay:user@final.domain`, it would be `[1st.relay, 2nd.relay]`) |
| isIpAddress()              | Whether the domain is an IP address                                                   | `false`                                                                                        |
| containsWhitespace()       | Whether the address contains obsolete whitespace                                      | `false`                                                                                        |
| isAscii()                  | Whether the address contains **only** ASCII characters                                    | `true`                                                                                         |
| hasIdentifier()            | Whether the address has an identifier                                                 | `false`                                                                                        |
| topLevelDomain()           | The `TopLevelDomain` of the email address, or `TopLevelDomain.OTHER` if it is unknown | `TopLevelDomain.DOT_COM`                                                                       |

To create a new instance of `Email` from a string, use the `tryParse(String email)`
method, either the default version or on your own `EmailValidator` instance:

```java
Optional<Email> parsed = JMail.tryParse("test@example.com");

Optional<Email> parsed = JMail.validator()
    .disallowIpDomain()
    .tryParse("test@example.com");
```

Since `tryParse(String email)` returns an `Optional<Email>`, you can do
some cool things, such as:

#### Use a default email address

```java
String email = JMail.tryParse("invalidEmailString")
    .map(Email::toString)
    .orElse("default@example.com");
```

#### Send an email if the address is valid

```java
JMail.tryParse("test@example.com")
    .ifPresentOrElse(
        email -> myEmailService.sendTo(email.toString()),
        () -> log.error("Could not send email to invalid email"));
```

#### Get different versions of the email address

```java
// Get a normalized email address
Optional<String> normalized = JMail.tryParse("admin(comment)@mysite.org")
    .map(Email::normalized);

// normalized == Optional.of("admin@mysite.org")
```

```java
// Get a normalized email address and remove any sub-addressing when normalizing
Optional<String> normalized = JMail.tryParse("test.1+mytag@mysite.org")
        .map(e -> e.normalized(
            NormalizationOptions.builder()
                    .removeSubAddress()
                    .build()));

// normalized == Optional.of("test.1@mysite.org")
```

```java
// Get a reference (MD5 hash) of the email address
Optional<String> reference = JMail.tryParse("test@gmail.com")
        .map(Email::reference);

// redacted == Optional.of("1aedb8d9dc4751e229a335e371db8058");
```

```java
// Get a redacted version of the email address
Optional<String> redacted = JMail.tryParse("test@gmail.com")
        .map(Email::redacted);

// redacted == Optional.of("{a94a8fe5ccb19ba61c4c0873d391e987982fbbd3}@gmail.com");
```

```java
// Get a munged version of the email address
Optional<String> redacted = JMail.tryParse("test@gmail.com")
        .map(Email::munged);

// redacted == Optional.of("te*****@gm*****");
```

### Additional Validation Rules

#### Disallow IP Address Domain

Although an email with an IP address in the domain is valid,
these email addresses are often rejected from mail servers or only
used for spam. You can require that your `EmailValidator` reject all
emails with an IP address in the domain:

```java
JMail.validator().disallowIpDomain();
```

> Note: `JMail.strictValidator()` includes this rule automatically.

#### Require a Top Level Domain

Although an email address can be a local domain name with no TLD,
ICANN highly discourages dotless email addresses. You can require that
your `EmailValidator` reject all emails without a TLD:

```java
JMail.validator().requireTopLevelDomain();
```

> Note: `JMail.strictValidator()` includes this rule automatically.

#### Disallow Explicit Source Routing

Explicit source routing has been [deprecated](https://datatracker.ietf.org/doc/html/rfc5321#section-3.6.1)
as of RFC 5321 and you SHOULD NOT use explicit source routing except under unusual
circumstances.

```java
JMail.validator().disallowExplicitSourceRouting();
```

> Note: `JMail.strictValidator()` includes this rule automatically.

#### Disallow Single Character Top Level Domains

A common user error is single-character top level domains (such as accidentally typing
`test@test.c` instead of `test@test.com`). These single character TLDs don't actually exist
and are not resolvable.

You can require that addresses do not have these single-character TLDs:

```java
JMail.validator().disallowSingleCharacterTopLevelDomains();
```

#### Disallow Reserved Domains

As specified in [RFC 2606](https://datatracker.ietf.org/doc/html/rfc2606),
some domains are reserved and should not be resolvable. Mail addressed to
mailboxes in those reserved domains (and their subdomains) should be non-deliverable.
You can require that your `EmailValidator` reject
all emails that have a reserved domain:

```java
JMail.validator().disallowReservedDomains();
```

#### Disallow Quoted Identifiers

If you want email addresses to only be the raw email address, use this rule.
Adding this will invalidate addresses of the form `John Smith <john@smith.com>`.

```java
JMail.validator().disallowQuotedIdentifiers();
```

#### Require a specific common Top Level Domain

You can require that your `EmailValidator` reject all emails that have
a top-level domain other than the ones you specify:

```java
JMail.validator().requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM);
JMail.validator().requireOnlyTopLevelDomains(
    TopLevelDomain.DOT_NET, TopLevelDomain.DOT_EDU);
```

#### Disallow Obsolete Whitespace

Whitespace (spaces, newlines, and carriage returns) is by default allowed between dot-separated
parts of the local-part and domain since RFC 822. However, this whitespace is
considered [obsolete since RFC 2822](https://datatracker.ietf.org/doc/html/rfc2822#section-4.4).

You can require that your `EmailValidator` reject all emails that have obsolete whitespace.

```java
JMail.validator().disallowObsoleteWhitespace();
```

#### Require a valid MX record

You can require that your `EmailValidator` reject all email addresses that do not have a valid MX
record associated with the domain.

> **Please note that including this rule on your email validator can increase the
amount of time it takes to validate email addresses by approximately 600ms in the worst case.
To further control the amount of time spent doing DNS lookups, you can use the overloaded method
to customize the timeout and retries.**

```java
JMail.validator().requireValidMXRecord();

// Or, customize the timeout and retries
JMail.validator().requireValidMXRecord(50, 2);
```

#### Disallow Disposable Domains

There are many services that provide disposable (or temporary) email addresses. Many applications
want to block these email addresses since they tend to be used by bots or users who are not serious
about signing up.

You can require that your `EmailValidator` reject all email addresses that have a disposable domain.

You must provide a `DisposableDomainSource` that is able to determine what domains are considered disposable.

Currently, there are two provided `DisposableDomainSource` implementations:

1. `DisposableDomainSource.file(String path)` uses a provided file containing disposable domains as the source. The file could
   be taken from [disposable-email-domains(https://github.com/disposable-email-domains/disposable-email-domains) for example, and
   included in your application.
2. `DisposableDomainSource.isTempMailAPI(String apiKey)` uses the [IsTempMail API](https://www.istempmail.com) to determine which
   domains are disposable. To use this source, you must sign up with IsTempMail and get an API key for usage.

> **Please note that using the IsTempMailAPI source for this rule on your email validator can increase the
amount of time it takes to validate email addresses. This is due to the time taken to make the API requests.**

```java
// Using a FileSource
DisposableDomainSource fileSource = DisposableDomainSource.file("path/to/my/file.txt");
JMail.validator().disallowDisposableDomains(fileSource);

// Using a IsTempMailAPISource
DisposableDomainSource apiSource = DisposableDomainSource.isTempMailAPI("MY_API_KEY");
JMail.validator().disallowDisposableDomains(apiSource);
```

#### Require the address to be ASCII

Some older email servers cannot yet accept non-ASCII email addresses. You can
require that your `EmailValidator` reject all email addresses that contain characters
other than ASCII characters.

```java
JMail.validator().requireAscii();
```

#### Allow Nonstandard Dots in the Local-Part

While technically disallowed under published RFCs, some email providers (ex: GMail)
consider email addresses that have local-parts that start with or end with a dot `.` character
as valid. For example, GMail considers `.my.email.@gmail.com` valid, even though it is not
actually valid according to RFC.

```java
JMail.validator().allowNonstandardDots();
```

### Bonus: IP Address Validation

Since validating email addresses requires validation of IP addresses,
these IP address validation methods are exposed for your convenience!

#### Determine if an IP Address is Valid

```java
String ipv4 = "12.34.56.78";

if (InternetProtocolAddress.isValid(ipv4)) {
  // Use address
}
```

```java
String ipv6 = "2001:db8::1234:5678";

if (InternetProtocolAddress.isValid(ipv6)) {
  // Use address
}
```

#### Enforce an IP Address to be Valid

```java
String ipv4 = "12.34.56.78";

try {
  InternetProtocolAddress.enforceValid(ipv4);
} catch (InvalidAddressException e) {
  // Failure
}
```

```java
String ipv6 = "2001:db8::1234:5678";

try {
  InternetProtocolAddress.enforceValid(ipv6);
} catch (InvalidAddressException e) {
  // Failure
}
```

#### Validate and return the IP

```java
String ipv4 = "12.34.56.78";

Optional<String> validated = InternetProtocolAddress.validate(ipv4);

// The validate() method allows for convenience such as:
String ip = InternetProtocolAddress
        .validate("notvalid")
        .orElse("0.0.0.0");
```

```java
String ipv6 = "2001:db8::1234:5678";

Optional<String> validated = InternetProtocolAddress.validate(ipv6);

// The validate() method allows for convenience such as:
String ip = InternetProtocolAddress
        .validate("notvalid")
        .orElse("2001:db8::1234:5678");
```

### Contributing

All contributions are welcome! Open issues for bug reports or
feature requests. Pull requests with fixes or enhancements are
encouraged.

Relevant RFCs:
[822](https://datatracker.ietf.org/doc/html/rfc822),
[2822](https://datatracker.ietf.org/doc/html/rfc2822),
[5321](https://datatracker.ietf.org/doc/html/rfc5321),
[5322](https://datatracker.ietf.org/doc/html/rfc5322)
