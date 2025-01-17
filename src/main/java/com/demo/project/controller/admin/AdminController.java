package com.demo.project.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.project.controller.admin.response.AdminLostItemResponse;
import com.demo.project.controller.admin.response.ProcessedFileResponse;
import com.demo.project.exceptions.ArgumentNotValidException;
import com.demo.project.exceptions.FileProcessingException;
import com.demo.project.model.LostItem;
import com.demo.project.parser.LostItemParser;
import com.demo.project.service.LostItemService;
import com.demo.project.service.UserService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

  private final LostItemParser lostItemParser;
  private final LostItemService lostItemService;
  private final UserService userService;
  private final Validator validator;

  @PostMapping(value = "/upload")
  public ResponseEntity<ProcessedFileResponse> processFile(@RequestParam("file") MultipartFile file) {
    List<LostItem> lostItems = new ArrayList<>();
    if (!isValid(file)) {
      throw new FileProcessingException("Not an acceptable file");
    }

    try {
      lostItems = lostItemParser.parse(file.getInputStream());
      validate(lostItems);
      lostItems.forEach(lostItemService::registerLostItem);
    } catch (ArgumentNotValidException e) {
      log.error(e.getMessage(), e);
      throw e;
    } catch (RuntimeException | IOException e) {
      log.error(e.getMessage(), e);
      throw new FileProcessingException(e.getMessage());
    }

    return ResponseEntity.ok(ProcessedFileResponse.builder()
        .processedRecords(lostItems.size())
        .build());
  }

  @GetMapping(value = "/items")
  public ResponseEntity<List<AdminLostItemResponse>> getItems() {
    List<LostItem> allLostItems = lostItemService.getAllLostItems();
    allLostItems.forEach(
        lostItem -> lostItem.setClaimedBy(userService.getAllUsersNames(lostItem)));
    return ResponseEntity.ok(allLostItems.stream().map(AdminLostItemResponse::fromModel).toList());
  }

  private boolean isValid(MultipartFile file) {
    return !file.isEmpty() && file.getOriginalFilename().endsWith(".csv") && "text/csv".equals(file.getContentType());
  }

  private void validate(List<LostItem> lostItems) {
    Set<ConstraintViolation<Object>> violations = new HashSet<>();
    lostItems.forEach(lostItem -> violations.addAll(validator.validate(lostItem)));

    if(!violations.isEmpty()) {
      throw new ArgumentNotValidException(violations);
    }
  }

}
