package com.demo.project.model;

import java.util.List;
import java.util.UUID;

import com.demo.project.entity.LostItemEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LostItem {

  private UUID id;
  @NotBlank
  @Pattern(regexp = "^[A-Za-z0-9çÇÑøßåâäàáÄÅéÉêëèìïíîñòóôöÖúùüÜû &'()+,\\-.]*$", message = "must have valid characters")
  @Size(max = 100)
  private String itemName;
  @Positive
  private int quantity;
  @NotBlank
  @Pattern(regexp = "^[A-Za-z0-9çÇÑøßåâäàáÄÅéÉêëèìïíîñòóôöÖúùüÜû &'()+,\\-.]*$", message = "must have valid characters")
  @Size(max = 100)
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
