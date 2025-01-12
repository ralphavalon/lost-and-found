package com.demo.project.model;

import com.demo.project.entity.UserEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

  private Long id;
  private String name;

  public static User fromEntity(UserEntity userEntity) {
    return User.builder()
        .id(userEntity.getId())
        .build();
  }

}
