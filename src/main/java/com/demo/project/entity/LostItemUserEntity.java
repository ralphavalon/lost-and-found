package com.demo.project.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "lost_items_users")
public class LostItemUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "lost_item_id")
  private LostItemEntity lostItem;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "claim_date")
  private LocalDateTime claimDate;

  @Column(name = "quantity_claimed")
  @Positive
  private Integer quantityClaimed;

}
