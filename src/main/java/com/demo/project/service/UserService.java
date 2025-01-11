package com.demo.project.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.demo.project.client.UserClient;
import com.demo.project.exceptions.ExternalCallException;
import com.demo.project.model.User;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserClient userClient;

  @Cacheable("users")
  public User getUser(Long id) {
    try {
      return userClient.getUser(id);
    } catch (FeignException e) {
      throw new ExternalCallException(e.getMessage());
    }
  }

}
