package com.demo.project.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.project.controller.request.ClaimItemRequest;
import com.demo.project.controller.response.LostItemResponse;
import com.demo.project.model.User;
import com.demo.project.service.LostItemService;

import jakarta.validation.Valid;
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

  @PostMapping("/{id}/claim")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void claimItem(@PathVariable UUID id, Principal principal, @RequestBody @Valid ClaimItemRequest request) {
    lostItemService.claimLostItem(id, request.getQuantity(), getLoggedInUser(principal.getName()));
  }

  // TODO: Get logged in user from database
  private User getLoggedInUser(String name) {
    if("user".equals(name)) {
      return User.builder().id(1L).build();
    }
    return User.builder().id(2L).build();
  }
  
}
