package com.sanctionco.jmail.helpers;

import com.sanctionco.jmail.Email;
import com.sanctionco.jmail.EmailValidator;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;

public class EmailValidatorAssert extends AbstractAssert<EmailValidatorAssert, EmailValidator> {
  private String email = null;

  public EmailValidatorAssert(EmailValidator actual) {
    super(actual, EmailValidatorAssert.class);
  }

  public static EmailValidatorAssert assertThat(EmailValidator actual) {
    return new EmailValidatorAssert(actual);
  }

  public EmailValidatorAssert finds(String email) {
    this.email = email;
    return this;
  }

  public AbstractObjectAssert<?, Email> parsable() {
    isNotNull();

    return Assertions.assertThat(actual.tryParse(email)).isPresent().get();
  }

  public EmailValidatorAssert notParsable() {
    isNotNull();

    Assertions.assertThat(actual.tryParse(email)).isNotPresent();

    return this;
  }

  public EmailValidatorAssert valid() {
    isNotNull();

    if (!actual.isValid(email)) {
      failWithMessage("Expected email <%s> to be valid but was invalid", actual);
    }

    return this;
  }

  public EmailValidatorAssert invalid() {
    isNotNull();

    if (actual.isValid(email)) {
      failWithMessage("Expected email <%s> to be invalid but was valid", actual);
    }

    return this;
  }
}
