package com.demo.project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.project.controller.admin.response.LostItemResponse;
import com.demo.project.service.LostItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class LostItemController {

  private final LostItemService lostItemService;

  @GetMapping
  public ResponseEntity<List<LostItemResponse>> getItems() {
    return ResponseEntity.ok(lostItemService.getAllLostItems().stream().map(LostItemResponse::fromModel).toList());
  }
  
}
