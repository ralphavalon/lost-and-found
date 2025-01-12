package com.demo.project.controller.admin.response;

import com.demo.project.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserResponse {

  private Long id;
  private String name;

  public static AdminUserResponse fromModel(User user) {
    return AdminUserResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .build();
  }

}
