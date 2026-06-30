package com.example.demo.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String departmentName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
