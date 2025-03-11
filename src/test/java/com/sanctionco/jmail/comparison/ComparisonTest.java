package com.sanctionco.jmail.comparison;

import com.sanctionco.jmail.JMail;

import jakarta.mail.internet.InternetAddress;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
  private final Path lastUpdatedFile = Paths.get(".", "docs", "lastupdated.html").toAbsolutePath();

  private int totalTests = 0;

  private final Implementation jmailImpl = new Implementation("JMail",
      "https://www.rohannagar.com/jmail/", JMail::isValid);

  private final Implementation apacheImpl = new Implementation("Apache Commons",
      "https://commons.apache.org/proper/commons-validator/",
      org.apache.commons.validator.routines.EmailValidator
          .getInstance(true, true)::isValid);

  private final Implementation javaMailImpl = new Implementation("Jakarta (Javax) Mail",
      "https://eclipse-ee4j.github.io/mail/", s -> {
    try {
      new InternetAddress(s).validate();
    } catch (Exception e) {
      return false;
    }

    return true;
  });

  private final Implementation rfc2822Impl = new Implementation("email-rfc2822",
      "https://github.com/bbottema/email-rfc2822-validator",
      s -> EmailAddressValidator.isValid(s, EmailAddressCriteria.RFC_COMPLIANT));

  private final List<Implementation> implementations = Arrays
      .asList(jmailImpl, apacheImpl, javaMailImpl, rfc2822Impl);

  @BeforeAll
  @SuppressWarnings({"unused"})
  void setupFile() throws Exception {
    Files.write(htmlFile, "".getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    StringBuilder heading = new StringBuilder()
        .append("<table class=\"table table-dark table-borderless\">\n"
            + "  <thead>\n"
            + "    <tr>\n"
            + "      <th scope=\"col\">Email Address</th>\n"
            + "      <th scope=\"col\">Expected</th>\n");

    implementations.stream()
        .map(Implementation::getHeading)
        .forEach(heading::append);

    heading.append("    </tr>\n"
        + "  </thead>\n"
        + "  <tbody>");

    Files.write(htmlFile,
        heading.toString().getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.APPEND);
  }

  @AfterAll
  @SuppressWarnings({"unused"})
  void addTotals() throws Exception {
    StringBuilder totals = new StringBuilder().append("    <tr>\n"
        + "      <th scope=\"row\">Totals</th>\n");

    totals.append("      <td>").append(totalTests).append('/').append(totalTests).append("</td>\n");

    implementations.stream()
        .map(impl -> impl.getTotal(totalTests))
        .forEach(totals::append);

    Files.write(htmlFile,
        totals.toString().getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.APPEND);

    Files.write(htmlFile,
        "  </tbody>\n</table>".getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.APPEND);

    // Update timestamp
    String prefix = "Comparison last ran at: ";
    String timeString = prefix + LocalDateTime
        .now(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " UTC";

    Files.write(lastUpdatedFile, timeString.getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideValidEmails")
  @CsvFileSource(resources = "/valid-addresses.csv", numLinesToSkip = 1)
  void compareValid(String email, String localPart, String domain, String desc) throws Exception {
    runTest(email, true, desc == null ? null : desc.trim());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource({
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidEmails",
      "com.sanctionco.jmail.helpers.AdditionalEmailProvider#provideInvalidControlEmails"})
  @CsvFileSource(resources = "/invalid-addresses.csv", delimiterString = " ;", numLinesToSkip = 1)
  void compareInvalid(String email, String description) throws Exception {
    runTest(email, false, description == null ? null : description.trim());
  }

  private void runTest(String email, boolean expected, String description) throws Exception {
    totalTests++;

    String desc = description == null
        ? ""
        : "<small class=\"text-muted\">" + description + "</small>";

    String expectedResult = expected
        ? "<td valign=\"middle\">Valid<br/>" + desc + "</td>"
        : "<td valign=\"middle\">Invalid<br/>" + desc + "</td>";

    StringBuilder result = new StringBuilder()
        .append("    <tr>\n      <th scope=\"row\" valign=\"middle\">")
        .append(splitEqually(email
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;"), 40)
            .stream().map(s -> s + "<br/>")
            .collect(Collectors.joining()))
        .append("</th>\n")
        .append("      ").append(expectedResult).append("\n");

    implementations.forEach(impl -> {
      String implRes = impl.runTest(email, expected);

      result.append("      ").append(implRes).append("\n");
    });

    Files.write(htmlFile,
        result.toString().getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.APPEND);
  }

  private static String testImplementation(Implementation impl,
                                           String email, boolean expected, boolean jmail) {
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

    str.append("<br/>");

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
    str.append(new DecimalFormat("#,###.##").format(avg));
    str.append(" ns</td>");

    impl.times.add(avg);

    return str.toString();
  }

  private static List<String> splitEqually(String text, int size) {
    List<String> ret = new ArrayList<>((text.length() + size - 1) / size);

    for (int start = 0; start < text.length(); start += size) {
      ret.add(text.substring(start, Math.min(text.length(), start + size)));
    }

    return ret;
  }

  private static final class Implementation {
    private final String name;
    private final String url;
    private final Predicate<String> predicate;
    private final List<Double> times = new ArrayList<>();
    private int successes = 0;

    Implementation(String name, String url, Predicate<String> predicate) {
      this.name = name;
      this.url = url;
      this.predicate = predicate;
    }

    String getHeading() {
      return "      <th scope=\"col\"><a href=\"" + url + "\">" + name + "</a></th>\n";
    }

    String getTotal(int totalTests) {
      return "      <td>" + successes + "/" + totalTests
          + "<br/>Average Time: "
          + new DecimalFormat("#,###.##").format(average())
          + " ns</td>\n";
    }

    String runTest(String email, boolean expected) {
      if (name.equals("JMail")) {
        return ComparisonTest.testImplementation(this, email, expected, true);
      }

      return ComparisonTest.testImplementation(this, email, expected, false);
    }

    private Double average() {
      return times.stream()
          .mapToDouble(n -> n)
          .average()
          .orElse(0.0);
    }
  }
}
