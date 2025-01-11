package com.demo.project.utils;

import com.demo.project.model.User;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class UserUtils {

  static PodamFactory podamFactory = new PodamFactoryImpl();

  public static User generateUser() {
    return podamFactory.manufacturePojo(User.class);
  }

  public static User user(Long id, String name) {
    return User.builder()
        .id(id)
        .name(name)
        .build();
  }

}
