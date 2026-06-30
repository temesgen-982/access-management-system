package com.example.demo.event;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

  private final AuditLogRepository auditLogRepository;

  @Async
  @EventListener
  public void handleAuditRequired(AuditActionEvt event) {
    AuditLog log =
        AuditLog.builder()
            .action(event.getAction())
            .performedBy(event.getPerformedBy())
            .details(event.getDetails())
            .timestamp(LocalDateTime.now())
            .build();

    auditLogRepository.save(log);
  }
}
