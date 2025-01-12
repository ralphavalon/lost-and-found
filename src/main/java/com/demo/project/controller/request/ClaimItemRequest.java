package com.demo.project.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ClaimItemRequest {

  @Positive
  @NotNull
  private Integer quantity;
  
}
