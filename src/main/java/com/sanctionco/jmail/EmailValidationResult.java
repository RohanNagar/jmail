package com.sanctionco.jmail;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Contains information about an email address validation, including success or failure,
 * the parsed email address (if success), and the failure reason (if failure).
 */
public final class EmailValidationResult {
  private final boolean success;
  private final Email emailAddress;
  private final FailureReason failureReason;

  EmailValidationResult(boolean success, Email emailAddress, FailureReason failureReason) {
    this.success = success;
    this.emailAddress = emailAddress;
    this.failureReason = failureReason;
  }

  static EmailValidationResult failure(FailureReason failureReason) {
    return new EmailValidationResult(false, null, failureReason);
  }

  static EmailValidationResult success(Email email) {
    return new EmailValidationResult(true, email, FailureReason.NONE);
  }

  /**
   * Return if the email address validation was a success or not.
   *
   * @return true if the validation was successful, or false if it was not
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * Return if the email address validation was a failure or not.
   *
   * @return true if the validation was not successful, or false if it was
   */
  public boolean isFailure() {
    return !success;
  }

  /**
   * Get the parsed {@link Email} object.
   *
   * @return the parsed {@link Email} or {@code Optional.empty()} if the validation failed
   */
  public Optional<Email> getEmail() {
    return Optional.ofNullable(emailAddress);
  }

  /**
   * Get the reason for failure. If the validation was successful, this method will return
   * {@code FailureReason.NONE}.
   *
   * @return the {@link FailureReason} that describes why the email address failed validation
   */
  public FailureReason getFailureReason() {
    return failureReason;
  }

  /**
   * If the email address is valid, performs the given action with the valid email address,
   * otherwise does nothing.
   *
   * @param action the action to be performed, if the email address is valid
   */
  public void ifValid(Consumer<Email> action) {
    if (emailAddress != null) {
      action.accept(emailAddress);
    }
  }

  /**
   * If the email address is valid, performs the given action with the valid email address,
   * otherwise performs the given failure action with the failure reason.
   *
   * @param action the action to be performed, if the email address is valid
   * @param failureAction the action to be performed, if the email address is invalid
   */
  public void ifValidOrElse(Consumer<Email> action, Consumer<FailureReason> failureAction) {
    if (emailAddress != null) {
      action.accept(emailAddress);
    } else {
      failureAction.accept(failureReason);
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", EmailValidationResult.class.getSimpleName() + "[", "]")
        .add("success=" + success)
        .add("emailAddress=" + emailAddress)
        .add("failureReason=" + failureReason)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EmailValidationResult)) return false;
    EmailValidationResult that = (EmailValidationResult) o;
    return Objects.equals(success, that.success)
        && Objects.equals(emailAddress, that.emailAddress)
        && Objects.equals(failureReason, that.failureReason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, emailAddress, failureReason);
  }
}
