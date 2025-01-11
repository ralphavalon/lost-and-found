package com.demo.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.project.entity.LostItemEntity;
import com.demo.project.model.LostItem;
import com.demo.project.model.User;
import com.demo.project.repository.LostItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostItemService {

  private final LostItemRepository repository;
  private final UserService userService;

  public void registerLostItem(LostItem lostItem) {
    repository.save(lostItem.toEntity());
  }

  public List<LostItem> getAllLostItems() {
    return repository.findAll().stream().map(lostItemEntity -> {
      return LostItem.fromEntity(lostItemEntity, getAllUsersNames(lostItemEntity));
    }).toList();
  }

  private List<User> getAllUsersNames(LostItemEntity lostItemEntity) {
    return lostItemEntity.getClaimedBy().stream().map(
      claimedItem -> userService.getUser(claimedItem.getUser().getId())
    ).toList();
  }

}
