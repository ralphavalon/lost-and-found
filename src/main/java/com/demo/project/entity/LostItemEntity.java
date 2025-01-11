package com.demo.project.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
@Table(name = "lost_item")
public class LostItemEntity {

  @Id
  @UuidGenerator
  private UUID id;

  private String itemName;

  @Positive
  private int quantity;

  private String place;

  @CreatedDate
  private LocalDateTime createdDate;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "lost_item_users",
    joinColumns = @JoinColumn(name = "lost_item_id"),
    inverseJoinColumns = @JoinColumn(name = "claimer_id"))
  private List<LostItemUserEntity> claimedBy;

}
