package com.sanctionco.jmail.benchmark;

import com.google.common.labs.email.EmailAddress;
import com.google.common.labs.parse.Parser;
import com.sanctionco.jmail.JMail;

import java.util.concurrent.TimeUnit;

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
 * JMH microbenchmark for address list parsing.
 *
 * <p>This class holds only benchmark methods. Use {@code BenchmarkRunnerTest} to run it.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
public class AddressListBenchmark {

  @Param({
      "email@example.com,test@gmail.com,my-addr@test.org",
      "Joe A Smith <email@example.com>,testmail@t.co",
      "e＿mail@hello.net,gatsby@f.sc.ot.t.f.i.tzg.era.l.d.,testmail@t.co"
  })
  public String list;

  @Benchmark
  public void jmail() {
    JMail.tryParseAddressList(list);
  }

  @Benchmark
  public void googleDotParse() {
    try {
      EmailAddress.parseAddressList(list);
    } catch (Parser.ParseException e) {
      return;
    }
  }
}
