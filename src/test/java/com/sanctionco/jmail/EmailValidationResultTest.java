package com.sanctionco.jmail;

import java.util.Optional;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailValidationResultTest {

  @Test
  void ensureEqualsContract() {
    EqualsVerifier.forClass(EmailValidationResult.class).verify();
  }

  @Test
  void testStaticSuccessConstructor() {
    Optional<Email> testEmail = Email.of("test@test.com");

    assertThat(EmailValidationResult.success(testEmail.get()))
        .returns(true, EmailValidationResult::isSuccess)
        .returns(false, EmailValidationResult::isFailure)
        .returns(testEmail, EmailValidationResult::getEmail)
        .returns(FailureReason.NONE, EmailValidationResult::getFailureReason);
  }

  @Test
  void testStaticFailureConstructor() {
    assertThat(EmailValidationResult.failure(FailureReason.NULL_ADDRESS))
        .returns(false, EmailValidationResult::isSuccess)
        .returns(true, EmailValidationResult::isFailure)
        .returns(Optional.empty(), EmailValidationResult::getEmail)
        .returns(FailureReason.NULL_ADDRESS, EmailValidationResult::getFailureReason);
  }
}
