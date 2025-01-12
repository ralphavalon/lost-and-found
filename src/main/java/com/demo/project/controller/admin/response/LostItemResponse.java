package com.demo.project.controller.admin.response;

import java.util.UUID;

import com.demo.project.model.LostItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LostItemResponse {

  private UUID id;
  private String itemName;
  private int quantity;
  private String place;

  public static LostItemResponse fromModel(LostItem lostItem) {
    return LostItemResponse.builder()
        .id(lostItem.getId())
        .itemName(lostItem.getItemName())
        .place(lostItem.getPlace())
        .quantity(lostItem.getQuantity())
        .build();
  }

}
