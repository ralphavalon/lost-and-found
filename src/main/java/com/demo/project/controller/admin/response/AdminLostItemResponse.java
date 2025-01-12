package com.demo.project.controller.admin.response;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.demo.project.model.LostItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLostItemResponse {

  private UUID id;
  private String itemName;
  private int quantity;
  private String place;
  private Set<AdminUserResponse> claimedBy;

  public static AdminLostItemResponse fromModel(LostItem lostItem) {
    return AdminLostItemResponse.builder()
        .id(lostItem.getId())
        .itemName(lostItem.getItemName())
        .quantity(lostItem.getQuantity())
        .place(lostItem.getPlace())
        .claimedBy(lostItem.getClaimedBy() != null
            ? new HashSet<>(lostItem.getClaimedBy().stream().map(AdminUserResponse::fromModel).toList())
            : null)
        .build();
  }

}
