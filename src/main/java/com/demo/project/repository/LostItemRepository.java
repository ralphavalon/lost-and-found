package com.demo.project.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.project.entity.LostItemEntity;

@Repository
public interface LostItemRepository extends JpaRepository<LostItemEntity, UUID> {
  
}