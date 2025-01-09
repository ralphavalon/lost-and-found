package com.demo.project.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.project.controller.admin.response.ProcessedFileResponse;
import com.demo.project.exceptions.FileProcessingException;
import com.demo.project.model.LostItem;
import com.demo.project.parser.LostItemParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

  private final LostItemParser lostItemParser;

  @PostMapping(value = "/upload")
  public ResponseEntity<ProcessedFileResponse> processFile(@RequestParam("file") MultipartFile file) {
    List<LostItem> lostItems = new ArrayList<>();
    if (!isValid(file)) {
      throw new FileProcessingException("Not an acceptable file");
    }

    try {
      lostItems = lostItemParser.parse(file.getInputStream());
    } catch (RuntimeException | IOException e) {
      log.error(e.getMessage(), e);
      throw new FileProcessingException(e.getMessage());
    }

    return ResponseEntity.ok(ProcessedFileResponse.builder()
        .processedRecords(lostItems.size())
        .build());
  }

  private boolean isValid(MultipartFile file) {
    return !file.isEmpty() && file.getOriginalFilename().endsWith(".csv") && "text/csv".equals(file.getContentType());
  }

}
