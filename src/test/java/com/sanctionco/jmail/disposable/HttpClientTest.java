package com.sanctionco.jmail.disposable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpClientTest {

  @Test
  void canMakeGetRequest() {
    HttpClient client = new HttpClient();

    String response = client.get("https://dummyjson.com/test");
    assertEquals("{\"status\":\"ok\",\"method\":\"GET\"}", response);
  }

  @Test
  void returnsEmptyStringOnFailure() {
    HttpClient client = new HttpClient();

    // Use a bad URL in the request
    String response = client.get("htps://dummyjson.com/test");
    assertEquals("", response);
  }
}
