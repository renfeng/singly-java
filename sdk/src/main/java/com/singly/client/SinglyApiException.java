package com.singly.client;

/**
 * A RuntimeException implementation for errors making calls to the Singly API.
 */
public class SinglyApiException
  extends RuntimeException {

  public SinglyApiException(String message, Throwable cause) {
    super(message, cause);
  }

  public SinglyApiException(String message) {
    super(message);
  }

  public SinglyApiException(Throwable cause) {
    super(cause);
  }

}
