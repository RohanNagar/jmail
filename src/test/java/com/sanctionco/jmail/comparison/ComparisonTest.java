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
import java.util.stream.Collectors;

import javax.mail.internet.InternetAddress;

import org.hazlewood.connor.bottema.emailaddress.EmailAddressCriteria;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
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
  private final Path htmlFile = Paths.get(".", "docs", "results.html").toAbsolutePath();

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

  private final Implementation rfc2822Impl = new Implementation(
      s -> EmailAddressValidator.isValid(s, EmailAddressCriteria.RFC_COMPLIANT));

  @BeforeAll
  @SuppressWarnings({"unused", "BeforeOrAfterWithIncorrectSignature"})
  void setupFile() throws Exception {
    Files.write(htmlFile, "".getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    Files.write(htmlFile,
        ("<table class=\"table table-dark table-borderless\">\n"
            + "  <thead>\n"
            + "    <tr>\n"
            + "      <th scope=\"col\">Email Address</th>\n"
            + "      <th scope=\"col\">Expected</th>\n"
            + "      <th scope=\"col\">JMail</th>\n"
            + "      <th scope=\"col\">Apache Commons</th>\n"
            + "      <th scope=\"col\">Javax Mail</th>\n"
            + "      <th scope=\"col\">email-rfc2822</th>\n"
            + "    </tr>\n"
            + "  </thead>\n"
            + "  <tbody>")
            .getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.APPEND);
  }

  @AfterAll
  @SuppressWarnings({"unused", "BeforeOrAfterWithIncorrectSignature"})
  void addTotals() throws Exception {
    String html = String.format("    <tr>\n"
        + "      <th scope=\"row\">Totals</th>\n"
        + "      <td>%s</td>\n"
        + "      <td>%s</td>\n"
        + "      <td>%s</td>\n"
        + "      <td>%s</td>\n"
        + "      <td>%s</td>\n"
        + "    </tr>\n",
        totalTests + "/" + totalTests,
        jmailImpl.successes + "/" + totalTests + "</br>"
            + "Average Time: " + jmailImpl.average() + " ns",
        apacheImpl.successes + "/" + totalTests + "</br>"
            + "Average Time: " + apacheImpl.average() + " ns",
        javaMailImpl.successes + "/" + totalTests + "</br>"
            + "Average Time: " + javaMailImpl.average() + " ns",
        rfc2822Impl.successes + "/" + totalTests + "</br>"
            + "Average Time: " + rfc2822Impl.average() + " ns");

    Files.write(htmlFile, html.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

    Files.write(htmlFile,
        ("  </tbody>\n</table>").getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.APPEND);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("com.sanctionco.jmail.AdditionalEmailProvider#provideValidEmails")
  @CsvFileSource(resources = "/valid-addresses.csv", numLinesToSkip = 1)
  void compareValid(String email) throws Exception {
    runTest(email, true);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("com.sanctionco.jmail.AdditionalEmailProvider#provideInvalidEmails")
  @CsvFileSource(resources = "/invalid-addresses.csv", delimiter = '\u007F')
  void compareInvalid(String email) throws Exception {
    runTest(email, false);
  }

  private void runTest(String email, boolean expected) throws Exception {
    totalTests++;

    String jmail = testImplementation(jmailImpl, email, expected, true);
    String apache = testImplementation(apacheImpl, email, expected);
    String javaMail = testImplementation(javaMailImpl, email, expected);
    String rfc2822 = testImplementation(rfc2822Impl, email, expected);

    email = splitEqually(email, 40).stream().map(s -> s + "</br>")
        .collect(Collectors.joining());

    String expectedResult = expected
        ? "<td valign=\"middle\">Valid</td>"
        : "<td valign=\"middle\">Invalid</td>";

    String html = String.format("    <tr>\n"
        + "      <th scope=\"row\" valign=\"middle\">%s</th>\n"
        + "      %s\n"
        + "      %s\n"
        + "      %s\n"
        + "      %s\n"
        + "      %s\n"
        + "    </tr>\n", email, expectedResult, jmail, apache, javaMail, rfc2822);

    Files.write(htmlFile, html.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
  }

  private String testImplementation(Implementation impl,
                                    String email,
                                    boolean expected) {
    return testImplementation(impl, email, expected, false);
  }

  private String testImplementation(Implementation impl,
                                    String email,
                                    boolean expected, boolean jmail) {
    Predicate<String> predicate = impl.predicate;

    StringBuilder str = new StringBuilder();

    if (predicate.test(email) == expected) {
      String successString = expected ? "Valid" : "Invalid";
      String dataString = jmail
          ? "<td style=\"background-color:#56666B\" valign=\"middle\">"
          : "<td valign=\"middle\">";
      str.append(dataString).append(successString);
      impl.successes++;
    } else {
      String failureString = expected ? "Invalid" : "Valid";
      str.append("<td style=\"background-color:#815355\" valign=\"middle\">").append(failureString);
    }

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
    str.append(" ns</td>");

    impl.times.add(avg);

    return str.toString();
  }

  private static List<String> splitEqually(String text, int size) {
    // Give the list the right capacity to start with. You could use an array
    // instead if you wanted.
    List<String> ret = new ArrayList<>((text.length() + size - 1) / size);

    for (int start = 0; start < text.length(); start += size) {
      ret.add(text.substring(start, Math.min(text.length(), start + size)));
    }
    return ret;
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
