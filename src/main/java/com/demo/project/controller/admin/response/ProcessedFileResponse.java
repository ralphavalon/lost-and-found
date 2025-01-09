package com.demo.project.controller.admin.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessedFileResponse {
  
  private Integer processedRecords;

}
