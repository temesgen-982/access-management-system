package com.example.demo.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuditLogResponse {
  private Long id;
  private String action;
  private String performedBy;
  private String details;
  private LocalDateTime timestamp;
}
