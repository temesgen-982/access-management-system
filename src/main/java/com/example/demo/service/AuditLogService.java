package com.example.demo.service;

import com.example.demo.dto.AuditLogResponse;
import com.example.demo.repository.AuditLogRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogService {

  private final AuditLogRepository auditLogRepository;

  @Transactional(readOnly = true)
  public List<AuditLogResponse> getAllAuditLogs() {
    return auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp")).stream()
        .map(
            log ->
                AuditLogResponse.builder()
                    .id(log.getId())
                    .action(log.getAction())
                    .performedBy(log.getPerformedBy())
                    .details(log.getDetails())
                    .timestamp(log.getTimestamp())
                    .build())
        .collect(Collectors.toList());
  }
}
