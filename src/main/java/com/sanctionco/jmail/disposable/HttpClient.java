package com.sanctionco.jmail.disposable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An HTTP client that is capable of making HTTP requests.
 */
class HttpClient {

  /**
   * Get data from the given URL using the HTTP GET method.
   *
   * @param urlString the URL to make the HTTP GET request against
   * @return the string response, or an empty string if there was an error making the request
   */
  public String get(String urlString) {
    try {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      return response.toString();
    } catch (IOException e) {
      return "";
    }
  }
}
