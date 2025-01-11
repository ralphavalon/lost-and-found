package com.demo.project.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.demo.project.model.LostItem;
import com.demo.project.model.User;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class LostItemUtils {

  static PodamFactory podamFactory = new PodamFactoryImpl();

  public static LostItem generateLostItem() {
    return podamFactory.manufacturePojo(LostItem.class);
  }

  public static LostItem lostItemWithoutClaimedBy() {
    return lostItem(new ArrayList<>());
  }

  public static LostItem lostItemWithClaimedBy(List<User> claimedBy) {
    return lostItem(claimedBy);
  }

  private static LostItem lostItem(List<User> claimedBy) {
    return LostItem.builder()
        .id(UUID.fromString("a1caa384-c4dd-42d7-8e2c-a7a1a774749e"))
        .itemName("itemName")
        .quantity(2)
        .place("place")
        .claimedBy(claimedBy)
        .build();
  }

}
