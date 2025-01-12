package com.demo.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.project.entity.LostItemUserEntity;

@Repository
public interface LostItemUserRepository extends JpaRepository<LostItemUserEntity, Long> {
  
}