package com.sanctionco.jmail;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

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
        .returns(FailureReason.NULL_ADDRESS, EmailValidationResult::getFailureReason)
        .hasToString("EmailValidationResult"
            + "[success=false, emailAddress=null, failureReason=NULL_ADDRESS]");
  }

  @Test
  void testIfValidTrue() {
    Optional<Email> testEmail = Email.of("test@test.com");
    EmailValidationResult result = EmailValidationResult.success(testEmail.get());

    AtomicBoolean completedAction = new AtomicBoolean(false);

    Consumer<Email> action = email -> {
      completedAction.set(true);
      assertThat(email).isEqualTo(Email.of("test@test.com").get());
    };

    result.ifValid(action);
    assertThat(completedAction).isTrue();
  }

  @Test
  void testIfValidFalse() {
    EmailValidationResult result = EmailValidationResult.failure(FailureReason.NULL_ADDRESS);

    AtomicBoolean completedAction = new AtomicBoolean(false);

    Consumer<Email> action = email -> completedAction.set(true);

    result.ifValid(action);
    assertThat(completedAction).isFalse();
  }

  @Test
  void testIfValidOrElseTrue() {
    Optional<Email> testEmail = Email.of("test@test.com");
    EmailValidationResult result = EmailValidationResult.success(testEmail.get());

    AtomicBoolean completedAction = new AtomicBoolean(false);
    AtomicBoolean completedFailureAction = new AtomicBoolean(false);

    Consumer<Email> action = email -> {
      completedAction.set(true);
      assertThat(email).isEqualTo(Email.of("test@test.com").get());
    };

    Consumer<FailureReason> failureAction = failureReason -> completedFailureAction.set(true);

    result.ifValidOrElse(action, failureAction);
    assertThat(completedAction).isTrue();
    assertThat(completedFailureAction).isFalse();
  }

  @Test
  void testIfValidOrElseFalse() {
    EmailValidationResult result = EmailValidationResult.failure(FailureReason.NULL_ADDRESS);

    AtomicBoolean completedAction = new AtomicBoolean(false);
    AtomicBoolean completedFailureAction = new AtomicBoolean(false);

    Consumer<Email> action = email -> completedAction.set(true);
    Consumer<FailureReason> failureAction = failureReason -> {
      completedFailureAction.set(true);
      assertThat(failureReason).isEqualTo(FailureReason.NULL_ADDRESS);
    };

    result.ifValidOrElse(action, failureAction);
    assertThat(completedAction).isFalse();
    assertThat(completedFailureAction).isTrue();
  }
}
