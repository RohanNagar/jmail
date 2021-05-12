package com.sanctionco.jmail;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

@SuppressWarnings("unused")
public class AdditionalEmailProvider {

  // Some characters like carriage return ('\r')
  // can't be properly typed in the CSV file
  public static Stream<String> provideInvalidEmails() {
    return Stream.of(
        "\"test\rblah\"@test.org",
        "\"test\\\r\n blah\"@test.org",
        "Invalid \\\n Folding \\\n Whitespace@test.org",
        "\"qu@test.org",
        "ote\"@test.org",
        "\"(),:;<>[\\]@example.com",
        "\"\"\"@iana.org",
        "test.\r\n\r\n obs@syntax.com");
  }

  // The CsvFileSource has trouble parsing correctly when emails have quotes
  // or special characters, so supply those valid emails here
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
        Arguments.of("\"test\\\rblah\"@test.org", "\"test\\\rblah\"", "test.org"),
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
        Arguments.of("jdoe@machine(comment).  example", "jdoe", "machine(comment).  example"),
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
