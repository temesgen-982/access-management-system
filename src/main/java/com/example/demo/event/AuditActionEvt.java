package com.example.demo.event;

import lombok.Getter;

@Getter
public class AuditActionEvt {
  private final String action;
  private final String performedBy;
  private final String details;

  public AuditActionEvt(String action, String performedBy, String details) {
    this.action = action;
    this.performedBy = performedBy;
    this.details = details;
  }
}
