package com.example.demo.controller;

import com.example.demo.dto.AuditLogResponse;
import com.example.demo.service.AuditLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

  private final AuditLogService auditLogService;

  @GetMapping
  @PreAuthorize("hasAuthority('VIEW_AUDIT_LOGS')")
  public ResponseEntity<List<AuditLogResponse>> getAuditLogs() {
    return ResponseEntity.ok(auditLogService.getAllAuditLogs());
  }
}
