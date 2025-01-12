package com.demo.project.model;

import java.util.List;
import java.util.UUID;

import com.demo.project.entity.LostItemEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LostItem {

  @NotNull
  private UUID id;
  @NotBlank
  @Pattern(regexp = "^[A-Za-z0-9çÇÑøßåâäàáÄÅéÉêëèìïíîñòóôöÖúùüÜû &'()+,\\-.]*$")
  private String itemName;
  @Positive
  private int quantity;
  @NotBlank
  @Pattern(regexp = "^[A-Za-z0-9çÇÑøßåâäàáÄÅéÉêëèìïíîñòóôöÖúùüÜû &'()+,\\-.]*$")
  private String place;
  private List<User> claimedBy;

  public LostItemEntity toEntity() {
    return LostItemEntity.builder()
      .id(id == null ? null : id)
      .itemName(itemName)
      .quantity(quantity)
      .place(place)
      .build();
  }

  public static LostItem fromEntity(LostItemEntity lostItem, List<User> claimedBy) {
    return LostItem.builder()
      .id(lostItem.getId())
      .itemName(lostItem.getItemName())
      .quantity(lostItem.getQuantity())
      .place(lostItem.getPlace())
      .claimedBy(claimedBy)
      .build();
  }

}
