package com.demo.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.demo.project.entity.LostItemEntity;
import com.demo.project.entity.LostItemUserEntity;
import com.demo.project.model.LostItem;
import com.demo.project.model.User;
import com.demo.project.repository.LostItemRepository;
import com.demo.project.repository.LostItemUserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostItemService {

  private final LostItemRepository repository;
  private final LostItemUserRepository claimItemRepository;
  private final UserService userService;

  public void registerLostItem(LostItem lostItem) {
    repository.save(lostItem.toEntity());
  }

  public List<LostItem> getAllLostItems() {
    return repository.findAll().stream().map(lostItemEntity -> {
      return LostItem.fromEntity(lostItemEntity, parseUser(lostItemEntity));
    }).toList();
  }

  @Transactional
  public void claimLostItem(UUID id, int quantityClaimed, User user) {
    LostItemEntity lostItemEntity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Lost item not found"));

    if(quantityClaimed > lostItemEntity.getQuantity()) {
      throw new IllegalArgumentException("Requested more than available");
    }

    LostItemUserEntity claimedEntity = LostItemUserEntity.builder()
        .claimDate(LocalDateTime.now())
        .lostItem(lostItemEntity)
        .quantityClaimed(quantityClaimed)
        .user(userService.getUserEntity(user.getId()))
        .build();

    claimItemRepository.save(claimedEntity);
  }

  private List<User> parseUser(LostItemEntity lostItemEntity) {
    return lostItemEntity.getClaimedBy().stream().map(
        claimedItem -> User.fromEntity(claimedItem.getUser())).toList();
  }

}
