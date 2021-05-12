package com.sanctionco.jmail.comparison;

import com.sanctionco.jmail.JMail;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.mail.internet.InternetAddress;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Run this test to generate a comparison between JMail and other implementations.
 */
@Disabled
class ComparisonTest {
  private final Path resultsFile = Paths.get(".", "results", "README.md").toAbsolutePath();

  private int totalTests = 0;

  private final Implementation jmailImpl = new Implementation(JMail::isValid);

  private final Implementation apacheImpl = new Implementation(
      org.apache.commons.validator.routines.EmailValidator
          .getInstance(true, true)::isValid);

  private final Implementation javaMailImpl = new Implementation(s -> {
    try {
      new InternetAddress(s).validate();
    } catch (Exception e) {
      return false;
    }

    return true;
  });

  @BeforeAll
  @SuppressWarnings({"unused", "BeforeOrAfterWithIncorrectSignature"})
  void setupFile() throws Exception {
    Files.write(resultsFile, "".getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    Files.write(resultsFile,
        "| Address | Expected | JMail | Apache Commons | Javax Mail |\n".getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.APPEND);
    Files.write(resultsFile,
        "| --- | :---: | :---: | :---: | :---: |\n".getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.APPEND);
  }

  @AfterAll
  @SuppressWarnings({"unused", "BeforeOrAfterWithIncorrectSignature"})
  void addTotals() throws Exception {
    String s = "| Totals | "
        + totalTests + "/" + totalTests + " | "
        + jmailImpl.successes + "/" + totalTests + "</br>"
        + "Average Time: " + jmailImpl.average() + " ns" + " | "
        + apacheImpl.successes + "/" + totalTests + "</br>"
        + "Average Time: " + apacheImpl.average() + " ns" + " | "
        + javaMailImpl.successes + "/" + totalTests + "</br>"
        + "Average Time: " + javaMailImpl.average() + " ns" + " |\n";

    Files.write(resultsFile, s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
  }

  @ParameterizedTest(name = "{0}")
  @CsvFileSource(resources = "/valid-addresses.csv", numLinesToSkip = 1)
  void compareValid(String email) throws Exception {
    runTest(email, true);
  }

  @ParameterizedTest(name = "{0}")
  @CsvFileSource(resources = "/invalid-addresses.csv", delimiter = '\u007F')
  void compareInvalid(String email) throws Exception {
    runTest(email, false);
  }

  private void runTest(String email, boolean expected) throws Exception {
    totalTests++;

    String jmail = testImplementation(jmailImpl, email, expected);
    String apache = testImplementation(apacheImpl, email, expected);
    String javaMail = testImplementation(javaMailImpl, email, expected);

    if (email.contains("|")) email = email.replaceAll("\\|", "&#124;");

    String s = "| " + email + " | Invalid | " + jmail + " | " + apache + " | " + javaMail + " |\n";
    Files.write(resultsFile, s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
  }

  private String testImplementation(Implementation impl,
                                    String email,
                                    boolean expected) {
    Predicate<String> predicate = impl.predicate;

    boolean result = predicate.test(email);

    StringBuilder str = new StringBuilder();

    if (result == expected) {
      str.append("✅");
      impl.successes++;
    }
    else str.append("❌");

    str.append("</br>");

    // test 100 times to get an avg
    int repetitions = 100;
    List<Long> times = new ArrayList<>(repetitions);

    for (int i = 0; i < repetitions; i++) {
      long start = System.nanoTime();
      predicate.test(email);
      long end = System.nanoTime();

      times.add(end - start);
    }

    double avg = times.stream().mapToDouble(n -> n).average().orElse(0.0);

    str.append("in ");
    str.append(avg);
    str.append(" ns");

    impl.times.add(avg);

    return str.toString();
  }

  private static final class Implementation {
    private final Predicate<String> predicate;
    private final List<Double> times = new ArrayList<>();
    private int successes = 0;

    public Implementation(Predicate<String> predicate) {
      this.predicate = predicate;
    }

    private Double average() {
      return times.stream()
          .mapToDouble(n -> n)
          .average()
          .orElse(0.0);
    }
  }
}
