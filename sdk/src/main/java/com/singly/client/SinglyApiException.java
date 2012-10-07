package com.singly.client;

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
