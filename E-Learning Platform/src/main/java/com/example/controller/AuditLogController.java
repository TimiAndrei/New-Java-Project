package com.example.controller;

import com.example.model.dto.AuditLogResponse;
import com.example.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    private final AuditService auditService;

    @Autowired
    public AuditLogController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public List<AuditLogResponse> getAllAuditLogs() {
        return auditService.getAllAuditLogs();
    }

    @GetMapping("/user/{userId}")
    public List<AuditLogResponse> getAuditLogsByUser(@PathVariable Long userId) {
        return auditService.getAuditLogsByUser(userId);
    }
}
