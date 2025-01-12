package com.demo.project.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.demo.project.client.UserClient;
import com.demo.project.entity.UserEntity;
import com.demo.project.exceptions.ExternalCallException;
import com.demo.project.model.LostItem;
import com.demo.project.model.User;
import com.demo.project.repository.UserRepository;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserClient userClient;

  @Cacheable("users")
  public User getUserAdditionalData(Long id) {
    try {
      return userClient.getUser(id);
    } catch (FeignException e) {
      throw new ExternalCallException(e.getMessage());
    }
  }

  public List<User> getAllUsersNames(LostItem lostItem) {
    return lostItem.getClaimedBy().stream().map(
        user -> this.getUserAdditionalData(user.getId())).toList();
  }

  protected UserEntity getUserEntity(Long id) {
    return userRepository.findById(id).orElseThrow((() -> new EntityNotFoundException("User not found")));
  }

}
