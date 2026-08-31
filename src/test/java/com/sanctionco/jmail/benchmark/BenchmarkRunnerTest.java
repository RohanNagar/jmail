package com.sanctionco.jmail.benchmark;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Entry points for running JMH benchmarks.
 */
@Disabled("Disabled to keep CI fast. Run manually to measure performance.")
class BenchmarkRunnerTest {

  @Test
  void benchmarkJMail() throws RunnerException {
    Options options = new OptionsBuilder()
        .include(ValidationBenchmark.class.getSimpleName() + "\\.jmail")
        .jvmArgs("-Xms1g", "-Xmx1g")
        .build();

    new Runner(options).run();
  }

  @Test
  void benchmarkAllImplementations() throws RunnerException {
    Options options = new OptionsBuilder()
        .include(ValidationBenchmark.class.getSimpleName())
        .jvmArgs("-Xms1g", "-Xmx1g")
        .build();

    new Runner(options).run();
  }
}


