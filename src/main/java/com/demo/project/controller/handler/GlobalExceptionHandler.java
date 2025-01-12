package com.demo.project.controller.handler;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.demo.project.exceptions.ArgumentNotValidException;
import com.demo.project.exceptions.ExternalCallException;
import com.demo.project.exceptions.FileProcessingException;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ErrorMessage exception(Exception e) {
    log.error(e.getMessage(), e);
    var error = "Internal Server Error";
    var message = "Unexpected server error";
    return errorMessage(error, message);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ NoResourceFoundException.class, EntityNotFoundException.class })
  public ErrorMessage notFound(Exception e) {
    log.error(e.getMessage(), e);
    var error = "Cannot find resource";
    var message = e.getMessage();
    return errorMessage(error, message);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ExternalCallException.class)
  public ErrorMessage exception(ExternalCallException e) {
    log.error(e.getMessage(), e);
    var error = "Internal Server Error";
    var message = e.getMessage();
    return errorMessage(error, message);
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(FileProcessingException.class)
  public ErrorMessage fileProcessingException(FileProcessingException e) {
    log.error(e.getMessage(), e);
    var error = "Error processing file";
    return errorMessage(error, e.getMessage());
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(IllegalArgumentException.class)
  public ErrorMessage illegalArgumentException(IllegalArgumentException e) {
    log.error(e.getMessage(), e);
    var error = "Argument error";
    return errorMessage(error, e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ServletRequestBindingException.class)
  public ErrorMessage missingRequestAttribute(ServletRequestBindingException ex) {
    log.error(ex.getMessage(), ex);
    var error = "Wrong or missing parameters";
    var message = ex.getMessage();
    return errorMessage(error, message);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorMessage notValid(MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    var invalidFieldMessage = new HashMap<String, String>();
    e.getBindingResult().getFieldErrors()
        .forEach(error -> invalidFieldMessage.put(error.getField(), error.getDefaultMessage()));
    var error = "Invalid Request";
    var message = "Invalid Field(s)";
    return errorMessage(error, message, invalidFieldMessage);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ArgumentNotValidException.class)
  public ErrorMessage notValid(ArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    var invalidFieldMessage = new HashMap<String, String>();
    e.getViolations()
        .forEach(error -> invalidFieldMessage.put(error.getPropertyPath().toString() ,error.getMessage()));
    var error = "Invalid Request";
    var message = "Invalid Field(s)";
    return errorMessage(error, message, invalidFieldMessage);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorMessage notReadable(HttpMessageNotReadableException e) {
    log.error(e.getMessage(), e);
    var error = "Invalid Request";
    var message = "Unacceptable request body";
    return errorMessage(error, message);
  }

  @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ErrorMessage handleMaxSizeException(MaxUploadSizeExceededException exc) {
    log.error(exc.getMessage(), exc);
    var error = "Invalid Request";
    var message = "File too large!";
    return errorMessage(error, message);
  }

  private ErrorMessage errorMessage(String error, String message) {
    return errorMessage(error, message, null);
  }

  private ErrorMessage errorMessage(String error, String message, Object details) {
    return ErrorMessage.builder().error(error).message(message).details(details).build();
  }

  @Getter
  @Builder
  static class ErrorMessage {

    @NonNull
    private String error;
    @NonNull
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object details;

  }

}
