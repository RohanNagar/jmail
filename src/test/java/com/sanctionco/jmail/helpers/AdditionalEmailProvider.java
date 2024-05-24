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
  public static Stream<Arguments> provideInvalidEmails() {
    return Stream.of(
        Arguments.of("\"qu@test.org", "Opening quote must have a closing quote"),
        Arguments.of("ote\"@test.org", "Closing quote must have an opening quote"),
        Arguments.of("\"(),:;<>[\\]@example.com", "Opening quote must have a closing quote"),
        Arguments.of("\"\"\"@iana.org", "Each quote must be in a pair"),
        Arguments.of("a@b.com\u0081",
            "The high octet preset character is not allowed at the end of the domain")
    );
  }

  // Invalid emails with whitespace
  public static Stream<String> provideInvalidWhitespaceEmails() {
    return Stream.of(
        "\"test\rblah\"@test.org",
        "\"test\\\r\n blah\"@test.org",
        "Invalid \\\n Folding \\\n Whitespace@test.org",
        "test.\r\n\r\n obs@syntax.com",
        "ABC.DEF@GHI.JKL . ");
  }

  // Invalid emails with control characters
  public static Stream<Arguments> provideInvalidControlEmails() {
    return Stream.of(
        Arguments.of("first.last␁@test.org", "Start of Heading is not allowed (ASCII 1)"),
        Arguments.of("first.last␂@test.org", "Start of Text is not allowed (ASCII 2)"),
        Arguments.of("first.last␃@test.org", "End of Text is not allowed (ASCII 3)"),
        Arguments.of("first.last␄@test.org", "End of Transmission is not allowed (ASCII 4)"),
        Arguments.of("first.last␅@test.org", "Enquiry is not allowed (ASCII 5)"),
        Arguments.of("first.last␆@test.org", "Acknowledge is not allowed (ASCII 6)"),
        Arguments.of("first.last␇@test.org", "Bell is not allowed (ASCII 7)"),
        Arguments.of("first.last␈@test.org", "Backspace is not allowed (ASCII 8)"),
        Arguments.of("first.last␋@test.org", "Vertical Tabulation is not allowed (ASCII 11)"),
        Arguments.of("first.last␌@test.org", "Form Feed is not allowed (ASCII 12)"),
        Arguments.of("first.last␎@test.org", "Shift Out is not allowed (ASCII 14)"),
        Arguments.of("first.last␏@test.org", "Shift In is not allowed (ASCII 15)"),
        Arguments.of("first.last␐@test.org", "Data Link Escape is not allowed (ASCII 16)"),
        Arguments.of("first.last␑@test.org", "Device Control One is not allowed (ASCII 17)"),
        Arguments.of("first.last␒@test.org", "Device Control Two is not allowed (ASCII 18)"),
        Arguments.of("first.last␓@test.org", "Device Control Three is not allowed (ASCII 19)"),
        Arguments.of("first.last␔@test.org", "Device Control Four is not allowed (ASCII 20)"),
        Arguments.of("first.last␕@test.org", "Negative Acknowledge is not allowed (ASCII 21)"),
        Arguments.of("first.last␖@test.org", "Synchronous Idle is not allowed (ASCII 22)"),
        Arguments.of("first.last␗@test.org", "End of Transmission Block is not allowed (ASCII 23)"),
        Arguments.of("first.last␘@test.org", "Cancel is not allowed (ASCII 24)"),
        Arguments.of("first.last␙@test.org", "End of Medium is not allowed (ASCII 25)"),
        Arguments.of("first.last␚@test.org", "Substitute is not allowed (ASCII 26)"),
        Arguments.of("first.last␛@test.org", "Escape is not allowed (ASCII 27)"),
        Arguments.of("first.last␜@test.org", "File Separator is not allowed (ASCII 28)"),
        Arguments.of("first.last␝@test.org", "Group Separator is not allowed (ASCII 29)"),
        Arguments.of("first.last␟@test.org", "Record Separator is not allowed (ASCII 30)"),
        Arguments.of("first.last␁@test.org", "Unit Separator is not allowed (ASCII 31)"));
  }

  // Valid emails with quotes
  public static Stream<Arguments> provideValidEmails() {
    return Stream.of(
        Arguments.of("\" \"@example.org", "\" \"", "example.org",
            "Quoted whitespace is allowed"),
        Arguments.of("\"john..doe\"@example.org", "\"john..doe\"", "example.org",
            "Multiple dots in a row are allowed within quotes"),
        Arguments.of("\"email\"@example.com", "\"email\"", "example.com",
            "Simple quotes are allowed"),
        Arguments.of("\"first@last\"@test.org", "\"first@last\"", "test.org",
            "The @ symbol can be quoted"),
        Arguments.of("very.unusual.\"@\".unusual.com@example.com",
            "very.unusual.\"@\".unusual.com", "example.com",
            "Quotes are allowed within the local-part when dot separated"),
        Arguments.of("\"first\\\"last\"@test.org", "\"first\\\"last\"", "test.org",
            "Quote characters can be backslash-escaped"),
        Arguments.of("much.\"more\\ unusual\"@example.com",
            "much.\"more\\ unusual\"", "example.com",
            "Whitespace within quotes is allowed when backslash-escaped"),
        // Arguments.of("very.\"(),:;<>[]\".VERY.\"very@\\\\ \"very\".unusual@strange.example.com",
        //     "very.\"(),:;<>[]\".VERY.\"very@\\\\ \"very\".unusual", "strange.example.com"),
        Arguments.of("\"first\\\\last\"@test.org", "\"first\\\\last\"", "test.org",
            "Escaped backslashes are allowed"),
        Arguments.of("\"Abc\\@def\"@test.org", "\"Abc\\@def\"", "test.org", ""),
        Arguments.of("\"Fred\\ Bloggs\"@test.org", "\"Fred\\ Bloggs\"", "test.org", ""),
        Arguments.of("\"Joe.\\\\Blow\"@test.org", "\"Joe.\\\\Blow\"", "test.org", ""),
        Arguments.of("\"Abc@def\"@test.org", "\"Abc@def\"", "test.org", ""),
        Arguments.of("\"Fred Bloggs\"@test.org", "\"Fred Bloggs\"", "test.org", ""),
        Arguments.of("\"first\\last\"@test.org", "\"first\\last\"", "test.org",
            "Backslash is allowed without an escape within quotes"),
        Arguments.of("\"Doug \\\"Ace\\\" L.\"@test.org", "\"Doug \\\"Ace\\\" L.\"", "test.org", ""),
        Arguments.of("\"[[ test ]]\"@test.org", "\"[[ test ]]\"", "test.org", ""),
        Arguments.of("\"test.test\"@test.org", "\"test.test\"", "test.org", ""),
        Arguments.of("test.\"test\"@test.org", "test.\"test\"", "test.org", ""),
        Arguments.of("\"test@test\"@test.org", "\"test@test\"", "test.org", ""),
        Arguments.of("\"test\\test\"@test.org", "\"test\\test\"", "test.org", ""),
        Arguments.of("\"first\".\"last\"@test.org", "\"first\".\"last\"", "test.org", ""),
        Arguments.of("\"first\".middle.\"last\"@test.org", "\"first\".middle.\"last\"",
            "test.org", ""),
        Arguments.of("\"first\".last@test.org", "\"first\".last", "test.org", ""),
        Arguments.of("first.\"last\"@test.org", "first.\"last\"", "test.org", ""),
        Arguments.of("\"first\".\"middle\".\"last\"@test.org",
            "\"first\".\"middle\".\"last\"", "test.org", ""),
        Arguments.of("\"first.middle\".\"last\"@test.org", "\"first.middle\".\"last\"",
            "test.org", ""),
        Arguments.of("\"first.middle.last\"@test.org", "\"first.middle.last\"", "test.org", ""),
        Arguments.of("\"first..last\"@test.org", "\"first..last\"", "test.org", ""),
        Arguments.of("\"Unicode NULL \\␀\"@char.com", "\"Unicode NULL \\␀\"", "char.com", ""),
        Arguments.of("\"test\\\\blah\"@test.org", "\"test\\\\blah\"", "test.org", ""),
        Arguments.of("\"test\\blah\"@test.org", "\"test\\blah\"", "test.org", ""),
        Arguments.of("\"test\\\"blah\"@test.org", "\"test\\\"blah\"", "test.org", ""),
        Arguments.of("\"first\\\\\\\"last\"@test.org", "\"first\\\\\\\"last\"", "test.org", ""),
        Arguments.of("first.\"mid\\dle\".\"last\"@test.org",
            "first.\"mid\\dle\".\"last\"", "test.org", ""),
        Arguments.of("\"Test \\\"Fail\\\" Ing\"@test.org", "\"Test \\\"Fail\\\" Ing\"",
            "test.org", ""),
        Arguments.of("\"test&#13;&#10; blah\"@test.org", "\"test&#13;&#10; blah\"", "test.org", ""),
        Arguments.of("first.last @test.org", "first.last ", "test.org", ""),
        Arguments.of("first.last  @test.org", "first.last  ", "test.org", ""),
        Arguments.of("first .last  @test .org", "first .last  ", "test .org", ""),
        Arguments.of("jdoe@machine(comment).  example", "jdoe", "machine(comment).  example", ""),
        Arguments.of("very.\"(),:;<>[]\".VERY.\"very@\\\\ ”very\".unusual@strange.example.com",
            "very.\"(),:;<>[]\".VERY.\"very@\\\\ ”very\".unusual", "strange.example.com", ""),
        Arguments.of("first.\"\".last@test.org", "first.\"\".last", "test.org", ""),
        Arguments.of("\"\"@test.org", "\"\"", "test.org", "")
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
