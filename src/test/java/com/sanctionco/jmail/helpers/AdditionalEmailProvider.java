package com.sanctionco.jmail.helpers;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

/**
 * The JUnit 5 CSV parser has trouble with emails that contain quotes
 * or whitespace such as '\r' or '\n'. These methods are used to provide
 * those kinds of emails to tests.
 */
@SuppressWarnings("unused")
public class AdditionalEmailProvider {

  // Invalid emails with odd pairs of quotes
  public static Stream<String> provideInvalidEmails() {
    return Stream.of("\"qu@test.org",
        "ote\"@test.org",
        "\"(),:;<>[\\]@example.com",
        "\"\"\"@iana.org");
  }

  // Invalid emails with whitespace
  public static Stream<String> provideInvalidWhitespaceEmails() {
    return Stream.of(
        "\"test\rblah\"@test.org",
        "\"test\\\r\n blah\"@test.org",
        "Invalid \\\n Folding \\\n Whitespace@test.org",
        "test.\r\n\r\n obs@syntax.com");
  }

  // Valid emails with quotes
  public static Stream<Arguments> provideValidEmails() {
    return Stream.of(
        Arguments.of("\" \"@example.org", "\" \"", "example.org"),
        Arguments.of("\"john..doe\"@example.org", "\"john..doe\"", "example.org"),
        Arguments.of("\"email\"@example.com", "\"email\"", "example.com"),
        Arguments.of("\"first@last\"@test.org", "\"first@last\"", "test.org"),
        Arguments.of("very.unusual.\"@\".unusual.com@example.com",
            "very.unusual.\"@\".unusual.com", "example.com"),
        Arguments.of("\"first\\\"last\"@test.org", "\"first\\\"last\"", "test.org"),
        Arguments.of("much.\"more\\ unusual\"@example.com",
            "much.\"more\\ unusual\"", "example.com"),
        // Arguments.of("very.\"(),:;<>[]\".VERY.\"very@\\\\ \"very\".unusual@strange.example.com",
        //     "very.\"(),:;<>[]\".VERY.\"very@\\\\ \"very\".unusual", "strange.example.com"),
        Arguments.of("\"first\\\\last\"@test.org", "\"first\\\\last\"", "test.org"),
        Arguments.of("\"Abc\\@def\"@test.org", "\"Abc\\@def\"", "test.org"),
        Arguments.of("\"Fred\\ Bloggs\"@test.org", "\"Fred\\ Bloggs\"", "test.org"),
        Arguments.of("\"Joe.\\\\Blow\"@test.org", "\"Joe.\\\\Blow\"", "test.org"),
        Arguments.of("\"Abc@def\"@test.org", "\"Abc@def\"", "test.org"),
        Arguments.of("\"Fred Bloggs\"@test.org", "\"Fred Bloggs\"", "test.org"),
        Arguments.of("\"first\\last\"@test.org", "\"first\\last\"", "test.org"),
        Arguments.of("\"Doug \\\"Ace\\\" L.\"@test.org", "\"Doug \\\"Ace\\\" L.\"", "test.org"),
        Arguments.of("\"[[ test ]]\"@test.org", "\"[[ test ]]\"", "test.org"),
        Arguments.of("\"test.test\"@test.org", "\"test.test\"", "test.org"),
        Arguments.of("test.\"test\"@test.org", "test.\"test\"", "test.org"),
        Arguments.of("\"test@test\"@test.org", "\"test@test\"", "test.org"),
        Arguments.of("\"test\\test\"@test.org", "\"test\\test\"", "test.org"),
        Arguments.of("\"first\".\"last\"@test.org", "\"first\".\"last\"", "test.org"),
        Arguments.of("\"first\".middle.\"last\"@test.org", "\"first\".middle.\"last\"", "test.org"),
        Arguments.of("\"first\".last@test.org", "\"first\".last", "test.org"),
        Arguments.of("first.\"last\"@test.org", "first.\"last\"", "test.org"),
        Arguments.of("\"first\".\"middle\".\"last\"@test.org",
            "\"first\".\"middle\".\"last\"", "test.org"),
        Arguments.of("\"first.middle\".\"last\"@test.org", "\"first.middle\".\"last\"", "test.org"),
        Arguments.of("\"first.middle.last\"@test.org", "\"first.middle.last\"", "test.org"),
        Arguments.of("\"first..last\"@test.org", "\"first..last\"", "test.org"),
        Arguments.of("\"Unicode NULL \\␀\"@char.com", "\"Unicode NULL \\␀\"", "char.com"),
        Arguments.of("\"test\\\\blah\"@test.org", "\"test\\\\blah\"", "test.org"),
        Arguments.of("\"test\\blah\"@test.org", "\"test\\blah\"", "test.org"),
        Arguments.of("\"test\\\"blah\"@test.org", "\"test\\\"blah\"", "test.org"),
        Arguments.of("\"first\\\\\\\"last\"@test.org", "\"first\\\\\\\"last\"", "test.org"),
        Arguments.of("first.\"mid\\dle\".\"last\"@test.org",
            "first.\"mid\\dle\".\"last\"", "test.org"),
        Arguments.of("\"Test \\\"Fail\\\" Ing\"@test.org", "\"Test \\\"Fail\\\" Ing\"", "test.org"),
        Arguments.of("\"test&#13;&#10; blah\"@test.org", "\"test&#13;&#10; blah\"", "test.org"),
        Arguments.of("first.last @test.org", "first.last ", "test.org"),
        Arguments.of("first.last  @test.org", "first.last  ", "test.org"),
        Arguments.of("first .last  @test .org", "first .last  ", "test .org"),
        Arguments.of("jdoe@machine(comment).  example", "jdoe", "machine(comment).  example")
    );
  }

  // Valid emails with whitespace
  public static Stream<Arguments> provideValidWhitespaceEmails() {
    return Stream.of(
        Arguments.of("\"test\\\rblah\"@test.org", "\"test\\\rblah\"", "test.org"),
        Arguments.of("first.(\r\n middle\r\n )last@test.org",
            "first.(\r\n middle\r\n )last", "test.org"),
        Arguments.of("1234   @   local(blah)  .machine .example",
            "1234   ", "   local(blah)  .machine .example"),
        Arguments.of("Test.\r\n Folding.\r\n Whitespace@test.org",
            "Test.\r\n Folding.\r\n Whitespace", "test.org"),
        Arguments.of("test. \r\n \r\n obs@syntax.com", "test. \r\n \r\n obs", "syntax.com"),
        Arguments.of("\r\n (\r\n x \r\n ) \r\n first\r\n ( \r\n x\r\n ) \r\n .\r\n ( \r\n x) \r\n "
                + "last \r\n (  x \r\n ) \r\n @test.org",
            "\r\n (\r\n x \r\n ) \r\n first\r\n ( \r\n x\r\n ) \r\n .\r\n ( \r\n x) \r\n "
                + "last \r\n (  x \r\n ) \r\n ",
            "test.org")
    );
  }
}
