package com.demo.project.exceptions;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

@Getter
public class ArgumentNotValidException extends RuntimeException {

  private Set<ConstraintViolation<Object>> violations;

  public ArgumentNotValidException(Set<ConstraintViolation<Object>> violations) {
    this.violations = violations;
  }
  
}
