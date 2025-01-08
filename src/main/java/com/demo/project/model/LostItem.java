package com.demo.project.model;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LostItem {

  private String itemName;
  @Positive
  private int quantity;
  private String place;

}
