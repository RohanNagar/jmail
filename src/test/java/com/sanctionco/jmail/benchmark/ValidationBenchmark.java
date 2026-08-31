package com.sanctionco.jmail.benchmark;

import com.google.common.labs.email.EmailAddress;
import com.google.common.labs.parse.Parser;
import com.sanctionco.jmail.JMail;

import jakarta.mail.internet.InternetAddress;

import java.util.concurrent.TimeUnit;

import org.apache.commons.validator.routines.EmailValidator;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressCriteria;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * JMH microbenchmark for email validation. Measures the average time (ns/op) to validate a
 * range of representative email addresses, and provides the same measurement for several other
 * validation libraries for comparison.
 *
 * <p>This class holds only benchmark methods. Use {@code BenchmarkRunnerTest} to run it.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
public class ValidationBenchmark {

  /**
   * The email address under test. Each value is measured independently so that we can see which
   * kinds of address benefit (or regress) from a change.
   */
  @Param({
      "email@example.com",
      "first.middle.last@sub.division.example.co.uk",
      "user@münchen.de",
      "user@12345.example.com",
      "first@last@example.org",
      "valid.local@exam_ple.com",
      "\"john doe\"(a comment)@example.com"
  })
  public String email;

  /**
   * Benchmark JMail validation. This is the implementation under test for the consolidation work.
   *
   * @return the validation result, returned so JMH does not eliminate the call
   */
  @Benchmark
  public boolean jmail() {
    return JMail.isValid(email);
  }

  /**
   * Benchmark Apache Commons Validator.
   *
   * @return the validation result, returned so JMH does not eliminate the call
   */
  @Benchmark
  public boolean apacheCommons() {
    return EmailValidator.getInstance(true, true).isValid(email);
  }

  /**
   * Benchmark Jakarta (Javax) Mail address validation.
   *
   * @return the validation result, returned so JMH does not eliminate the call
   */
  @Benchmark
  public boolean jakartaMail() {
    try {
      new InternetAddress(email).validate();
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  /**
   * Benchmark the email-rfc2822 validator in RFC-compliant mode.
   *
   * @return the validation result, returned so JMH does not eliminate the call
   */
  @Benchmark
  public boolean emailRfc2822() {
    return EmailAddressValidator.isValid(email, EmailAddressCriteria.RFC_COMPLIANT);
  }

  /**
   * Benchmark the Google dot-parse (mug) email parser.
   *
   * @return the validation result, returned so JMH does not eliminate the call
   */
  @Benchmark
  public boolean googleDotParse() {
    try {
      EmailAddress.of(email);
    } catch (Parser.ParseException e) {
      return false;
    }

    return true;
  }
}


