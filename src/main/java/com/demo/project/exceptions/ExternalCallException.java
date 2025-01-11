package com.demo.project.exceptions;

public class ExternalCallException extends RuntimeException {

  public ExternalCallException() {
    super();
  }

  public ExternalCallException(String message) {
    super(message);
  }

  public ExternalCallException(Exception e) {
    super(e);
  }

}
