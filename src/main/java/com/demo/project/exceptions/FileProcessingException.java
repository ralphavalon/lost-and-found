package com.demo.project.exceptions;

public class FileProcessingException extends RuntimeException {

  public FileProcessingException() {
    super();
  }

  public FileProcessingException(String message) {
    super(message);
  }

  public FileProcessingException(Exception e) {
    super(e);
  }
  
}
