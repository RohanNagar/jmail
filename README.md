# JMail

<a href="https://search.maven.org/artifact/com.sanctionco.jmail/jmail">
  <img src="https://img.shields.io/maven-central/v/com.sanctionco.jmail/jmail.svg?colorB=brightgreen&label=maven%20central" alt="Maven Central">
</a>
<a href="http://javadoc.io/doc/com.sanctionco.jmail/jmail">
  <img src="http://javadoc.io/badge/com.sanctionco.jmail/jmail.svg" alt="Javadoc">
</a>

A modern, fast, zero-dependency library for working with emails
and performing email validation in Java.

Built for Java 8 and up.

[Why JMail?](#why-jmail) • [Installation](#installation) • [Usage](#usage) •
[IP Validation](#bonus-ip-address-validation) • [Contributing](#contributing)

## Why JMail?

JMail was built mainly because I wanted to tackle the complex problem
of email validation without using Regex. Along the way, JMail became a
much better choice than other Java email validation libraries
(such as Apache Commons Validator or Java Mail Validation) for the
following reasons:

1. JMail is **_more correct_** than other libraries. For example, both
   Apache Commons and Java Mail consider `first@last@iana.org` as a valid
   email address! It clearly is not, as it has two `@` characters. JMail correctly
   considers this address invalid.
   
2. JMail is **_faster_** than other libraries by, on average, at least
   27%.
   
3. JMail has **_zero dependencies_** and is very lightweight.

4. JMail is **_modern_**. It is built for Java 8+, and provides many
   [useful methods and data accessors](#usage).

[Click here for a full report](results/results.md) of the differences in correctness and speed
between JMail and other libraries.

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
  <version>1.0.0</version>
</dependency>
```

## Usage

### Standard Email Validation

To perform standard email validation, use the static methods
available in `JMail`. For example, to test validation:

```java
String email = "test@example,com";

if (JMail.isValid(email)) {
  // Work with your email string
}
```

Or to enforce validation, throwing an `InvalidEmailException` on failure:

```java
String email = "test@example,com";

try {
  JMail.enforceValid(email);
  
  // Work with your email string
} catch (InvalidEmailException) {
  // Handle invalid email
}
```

### Custom Email Validation

JMail also provides an `EmailValidator` class that allows for much more
customization of what constitutes a valid email address. You can require
additional [common validation rules](#additional-validation-rules),
or supply your own. For example:

```java
EmailValidator validator = JMail.validator()
    // Require that the domain has a top-level domain
    .requireTopLevelDomain()
    // Require that the local-part starts with "allowed"
    .withRule(email -> email.localPart().startsWith("allowed"));

boolean valid = validator.isValid("allowed-email@test.com");
boolean invalidWithoutTld = validator.isValid("allowed@test");
boolean invalidWithoutAllowed = validator.isValid("invalid@test.com");
```

### The `Email` Object

JMail also includes an `Email` object that makes working with
email addresses easier. The `Email` object has the following properties:

| Property getter | Description | Example using `test(hello)@(world)example.one.com` |
| --------------- | ----------- | ------------------------------------ |
| localPart() | The local-part of the email address | `test(hello)` |
| localPartWithoutComments()  | The local-part of the email address without comments | `test` |
| domain() | The domain of the email address | `(world)example.one.com` |
| domainWithoutComments() | The domain of the email address without comments | `example.one.com` |
| domainParts() | A list of the parts of the domain | `[example, one, com]` |
| comments() | A list of the comments in the email address | `[hello, world]` |
| isIpAddress() | Whether or not the domain is an IP address | `false` |
| topLevelDomain() | The `TopLevelDomain` of the email address, or `TopLevelDomain.OTHER` if it is unknown | `TopLevelDomain.DOT_COM` |

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

### Additional Validation Rules

#### Disallow IP Address Domain

Although an email with an IP address in the domain is valid,
these email addresses are often rejected from mail servers or only
used for spam. You can require that your `EmailValidator` reject all
emails with an IP address in the domain:

```java
JMail.validator().disallowIpDomain();
```

#### Require a Top Level Domain

Although an email address can be a local domain name with no TLD,
ICANN highly discourages dotless email addresses. You can require that
your `EmailValidator` reject all emails without a TLD:

```java
JMail.validator().requireTopLevelDomain();
```

#### Require a specific common Top Level Domain

You can require that your `EmailValidator` reject all emails that have
a top-level domain other than the ones you specify:

```java
JMail.validator().requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM);
JMail.validator().requireOnlyTopLevelDomains(
    TopLevelDomain.DOT_NET, TopLevelDomain.DOT_EDU);
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
String ipv6 = "IPv6:2001:db8::1234:5678";

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
String ipv6 = "IPv6:2001:db8::1234:5678";

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
String ipv6 = "IPv6:2001:db8::1234:5678";

Optional<String> validated = InternetProtocolAddress.validate(ipv6);

// The validate() method allows for convenience such as:
String ip = InternetProtocolAddress
    .validate("notvalid")
    .orElse("IPv6:2001:db8::1234:5678");
```

### Contributing

All contributions are welcome! Open issues for bug reports or
feature requests. Pull requests with fixes or enhancements are
encouraged.
