package com.demo.project.service;

import org.springframework.stereotype.Service;

import com.demo.project.model.LostItem;
import com.demo.project.repository.LostItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostItemService {

  private final LostItemRepository repository;

  public void registerLostItem(LostItem lostItem) {
    repository.save(lostItem.toEntity());
  }

}
