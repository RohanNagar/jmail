package com.sanctionco.jmail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

class JMailTest {
  private final Condition<String> valid = new Condition<>(JMail::isValid, "valid");
  private final Condition<String> invalid = new Condition<>(e -> !JMail.isValid(e), "invalid");

  @ParameterizedTest(name = "{0}")
  @MethodSource({
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideValidEmails",
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideValidWhitespaceEmails"})
  @CsvFileSource(resources = "/valid-addresses.csv", numLinesToSkip = 1)
  void ensureValidPasses(String email, String localPart, String domain) {
    // Set expected values based on if the domain is an IP address or not
    final List<String> expectedParts = domain.startsWith("[")
        ? Collections.singletonList(domain.substring(1, domain.length() - 1))
        : Arrays.stream(
            domain
                .replaceAll("\\s*\\([^\\)]*\\)\\s*", "") // no comments in parts
                .split("\\.")).map(String::trim).collect(Collectors.toList());

    final String expectedDomain = domain.startsWith("[")
        ? domain.substring(1, domain.length() - 1)
        : domain;

    final TopLevelDomain expectedTld = expectedParts.size() > 1
        ? TopLevelDomain.fromString(expectedParts.get(expectedParts.size() - 1))
        : TopLevelDomain.NONE;

    assertThat(JMail.tryParse(email))
        .isPresent().get()
        .hasToString(email)
        .returns(localPart, Email::localPart)
        .returns(expectedDomain, Email::domain)
        .returns(expectedParts, Email::domainParts)
        .returns(expectedTld, Email::topLevelDomain);

    assertThat(email).is(valid);
    assertThatNoException().isThrownBy(() -> JMail.enforceValid(email));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource({
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidEmails",
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidWhitespaceEmails"})
  @CsvFileSource(resources = "/invalid-addresses.csv", delimiterString = "<br>")
  void ensureInvalidFails(String email) {
    assertThat(JMail.tryParse(email)).isNotPresent();

    assertThat(email).is(invalid);
    assertThatExceptionOfType(InvalidEmailException.class)
        .isThrownBy(() -> JMail.enforceValid(email));
  }

  @Test
  void tryParseSetsCommentFields() {
    String email = "test(hello)@(world)example.com";

    assertThat(JMail.tryParse(email))
        .isPresent().get()
        .hasToString(email)
        .returns("test(hello)", Email::localPart)
        .returns("test", Email::localPartWithoutComments)
        .returns("(world)example.com", Email::domain)
        .returns("example.com", Email::domainWithoutComments)
        .returns(Arrays.asList("hello", "world"), Email::comments)
        .returns(Arrays.asList("example", "com"), Email::domainParts)
        .returns(TopLevelDomain.DOT_COM, Email::topLevelDomain);
  }

  @Test
  void strictValidatorRejects() {
    String dotlessEmail = "test@example";
    String ipEmail = "test@[1.2.3.4]";
    String acceptedEmail = "test@example.com";

    assertThat(JMail.strictValidator().isValid(acceptedEmail)).isTrue();
    assertThat(JMail.strictValidator().isValid(dotlessEmail)).isFalse();
    assertThat(JMail.strictValidator().isValid(ipEmail)).isFalse();
  }

  @Test
  void addressWithSourceRoutingValidates() {
    String email = "@1st.relay,@2nd.relay:user@final.domain";

    assertThat(JMail.tryParse(email))
        .isPresent().get()
        .hasToString(email)
        .returns("user", Email::localPart)
        .returns("final.domain", Email::domain)
        .returns(Arrays.asList("final", "domain"), Email::domainParts)
        .returns(TopLevelDomain.fromString("domain"), Email::topLevelDomain)
        .returns(Arrays.asList("1st.relay", "2nd.relay"), Email::explicitSourceRoutes);
  }

  @ParameterizedTest(name = "{0}")
  @ValueSource(strings = {
      "@-1st.relay,@2nd.relay:user@final.domain",
      "@1st-.relay,@2nd.relay:user@final.domain",
      "@1st.relay,2nd.relay:user@final.domain",
      "@.relay,2nd.relay:user@final.domain",
      "@1st.1111,2nd.relay:user@final.domain",
      "@hello.world,user@final.domain",
      "@1st.relay,@2nd.relay:user@-final.domain",
      "@1st.relay,@2nd.relay:invalid",
      "@@1st.relay,@2nd.relay:user@final.domain",
      "@1st.r_elay,@2nd.relay:user@final.domain",
  })
  void ensureInvalidSourceRoutingAddressesFail(String email) {
    assertThat(JMail.tryParse(email)).isNotPresent();

    assertThat(email).is(invalid);
    assertThatExceptionOfType(InvalidEmailException.class)
        .isThrownBy(() -> JMail.enforceValid(email));
  }

  @Test
  void ensureIdentifiersAreParsed() {
    String one = "John Smith <test@te.ex>";

    assertThat(JMail.tryParse(one)).isPresent().get()
        .hasToString(one)
        .returns(true, Email::hasIdentifier)
        .returns("John Smith ", Email::identifier);

    String two = "Admin<admin@te.ex>";

    assertThat(JMail.tryParse(two)).isPresent().get()
        .hasToString(two)
        .returns(true, Email::hasIdentifier)
        .returns("Admin", Email::identifier);

    String none = "user@te.ex";

    assertThat(JMail.tryParse(none)).isPresent().get()
        .hasToString(none)
        .returns(false, Email::hasIdentifier)
        .extracting("identifier")
        .isNull();
  }
}
