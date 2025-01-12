package com.demo.project.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lost_items")
public class LostItemEntity {

  @Id
  @UuidGenerator
  private UUID id;

  private String itemName;

  @Positive
  private int quantity;

  private String place;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
    name = "lost_items_users",
    joinColumns = @JoinColumn(name = "lost_item_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private final List<LostItemUserEntity> claimedBy = new ArrayList<>();

  public void addClaimer(LostItemUserEntity claimer) {
    claimedBy.add(claimer);
  }

}
