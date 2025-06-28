package com.sanctionco.jmail.disposable;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IsTempMailAPISourceTest {

  @Test
  void shouldNotBlockIfApiRequestFails() {
    DisposableDomainSource source = DisposableDomainSource.isTempMailAPI("testkey");
    assertFalse(source.isDisposableDomain("gmail.com"));
  }

  @Test
  void identifiesDisposableDomain() {
    HttpClient client = mock(HttpClient.class);
    DisposableDomainSource source = new IsTempMailAPISource(client, "apikey");

    when(client.get(anyString()))
        .thenReturn("{\"name\":\"disposableinbox.com\",\"blocked\": true}");

    assertTrue(source.isDisposableDomain("disposableinbox.com"));

    when(client.get(anyString()))
        .thenReturn("{\"name\":\"disposableinbox2.com\",\"blocked\":true,\"unresolvable\":true}");

    assertTrue(source.isDisposableDomain("disposableinbox2.com"));
  }

  @Test
  void identifiesValidDomains() {
    HttpClient client = mock(HttpClient.class);
    DisposableDomainSource source = new IsTempMailAPISource(client, "apikey");

    when(client.get(anyString()))
        .thenReturn("{\"name\":\"gmail.com\",\"blocked\":false}");

    assertFalse(source.isDisposableDomain("gmail.com"));

    when(client.get(anyString()))
        .thenReturn("{\"name\":\"disposableinbox.com\",\"blocked\":false,\"unresolvable\":true}");

    assertFalse(source.isDisposableDomain("gmail.com"));
  }

  @Test
  void allowsDomainsWhenApiReturnsMalformedData() {
    HttpClient client = mock(HttpClient.class);

    // Missing closing '}'
    when(client.get(contains("malformed1")))
        .thenReturn("{\"name\":\"malformed1.com\",\"blocked\":false");

    // Missing 'blocked' key
    when(client.get(contains("malformed2")))
        .thenReturn("{\"name\":\"malformed2.com\",\"deny\":true}");

    // Missing colon after 'blocked' key
    when(client.get(contains("malformed3")))
        .thenReturn("{\"name\":\"malformed3.com\",\"blocked\" true}");

    // Missing everything after the 'blocked' key
    when(client.get(contains("malformed4")))
        .thenReturn("{\"name\":\"malformed4.com\",\"blocked\": ");

    DisposableDomainSource source = new IsTempMailAPISource(client, "apikey");

    assertAll("Malformed responses return false",
        () -> assertFalse(source.isDisposableDomain("malformed1.com")),
        () -> assertFalse(source.isDisposableDomain("malformed2.com")),
        () -> assertFalse(source.isDisposableDomain("malformed3.com")),
        () -> assertFalse(source.isDisposableDomain("malformed4.com")));
  }

  @Nested
  @Disabled // disabled to avoid making API requests regularly
  class APIIntegrationTests {
    // Replace this with an actual API key if enabling these tests
    private static final String API_KEY = "API_KEY";

    @Test
    void shouldIdentifyDisposableDomains() {
      DisposableDomainSource source = DisposableDomainSource.isTempMailAPI(API_KEY);
      assertTrue(source.isDisposableDomain("disposableinbox.com"));
    }

    @Test
    void shouldNotBlockValidDomains() {
      DisposableDomainSource source = DisposableDomainSource.isTempMailAPI(API_KEY);
      assertFalse(source.isDisposableDomain("gmail.com"));
    }
  }
}
