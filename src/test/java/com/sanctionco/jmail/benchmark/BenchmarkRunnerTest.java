package com.sanctionco.jmail.benchmark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Entry points for running JMH benchmarks.
 *
 * <p>These tests are disabled by default so that a normal {@code mvn package} stays fast. They are
 * enabled by setting a system property:
 *
 * <ul>
 *   <li>{@code -Djmail.benchmark=true} runs {@link #jmailStaysWithinBudget()}, the jmail-only
 *       check that CI also runs on master.</li>
 *   <li>{@code -Djmail.benchmark.full=true} runs {@link #benchmarkAllImplementations()}, the full
 *       cross-library comparison, for manual use.</li>
 * </ul>
 */
class BenchmarkRunnerTest {
  private static final Map<String, Double> MAX_NS_PER_OP = new HashMap<>();

  // These thresholds are about 4x that of what normal runs on a local Macbook result in
  // The CI in GitHub runs about twice as slow as local, and then we have additional buffer
  // top of that in case of more slowdowns in the shared runner environment.
  // This test running automatically is meant to catch significant slowdowns.
  static {
    MAX_NS_PER_OP.put("email@example.com", 400.0);
    MAX_NS_PER_OP.put("first.middle.last@sub.division.example.co.uk", 1000.0);
    MAX_NS_PER_OP.put("user@münchen.de", 3000.0);
    MAX_NS_PER_OP.put("user@12345.example.com", 700.0);
    MAX_NS_PER_OP.put("first@last@example.org", 150.0);
    MAX_NS_PER_OP.put("valid.local@exam_ple.com", 300.0);
    MAX_NS_PER_OP.put("\"john doe\"(a comment)@example.com", 1500.0);
  }

  @Test
  @EnabledIfSystemProperty(named = "jmail.benchmark", matches = "true")
  void jmailStaysWithinBudget() throws RunnerException {
    Options options = new OptionsBuilder()
        .include(ValidationBenchmark.class.getSimpleName() + "\\.jmail")
        .forks(1)
        .jvmArgs("-Xms1g", "-Xmx1g")
        .resultFormat(ResultFormatType.JSON)
        .result("target/jmh-jmail.json")
        .shouldFailOnError(true)
        .build();

    Collection<RunResult> results = new Runner(options).run();

    List<String> violations = new ArrayList<>();

    for (RunResult result : results) {
      String email = result.getParams().getParam("email");
      double score = result.getPrimaryResult().getScore(); // ns/op (AverageTime)
      double max = MAX_NS_PER_OP.getOrDefault(email, Double.MAX_VALUE);

      if (score > max) {
        violations.add(String.format(
            "%s: %.1f ns/op exceeded budget of %.1f ns/op", email, score, max));
      }
    }

    assertThat(violations)
        .as("jmail benchmark budget violations")
        .isEmpty();
  }

  @Test
  @EnabledIfSystemProperty(named = "jmail.benchmark.full", matches = "true")
  void benchmarkAllImplementations() throws RunnerException {
    Options options = new OptionsBuilder()
        .include(ValidationBenchmark.class.getSimpleName())
        .jvmArgs("-Xms1g", "-Xmx1g")
        .build();

    new Runner(options).run();
  }
}
