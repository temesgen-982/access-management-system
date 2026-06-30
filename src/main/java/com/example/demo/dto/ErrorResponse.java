package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
  private String message;
  private int status;
  private LocalDateTime timestamp;
  private Map<String, String>
      errors; // Holds field-specific errors like {"email": "Invalid format"}
}
