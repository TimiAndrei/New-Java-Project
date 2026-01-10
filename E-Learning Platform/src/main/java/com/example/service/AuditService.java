package com.example.service;

import com.example.model.entities.AuditLog;
import com.example.model.entities.User;
import com.example.repository.AuditLogRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import com.example.model.dto.AuditLogResponse;
import java.time.format.DateTimeFormatter;

@Service
public class AuditService {
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private UserRepository userRepository;

    public void logAction(Long userId, String action, String details) {
        User user = userRepository.findById(userId).orElse(null);
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    public List<AuditLog> findByUser(Long userId) {
        return auditLogRepository.findByUser_Id(userId);
    }

    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }

            public java.util.List<AuditLogResponse> getAllAuditLogs() {
                return auditLogRepository.findAll().stream().map(this::toAuditLogResponse).toList();
            }

            public java.util.List<AuditLogResponse> getAuditLogsByUser(Long userId) {
                return auditLogRepository.findByUser_Id(userId).stream().map(this::toAuditLogResponse).toList();
            }

            private AuditLogResponse toAuditLogResponse(AuditLog log) {
                AuditLogResponse dto = new AuditLogResponse();
                dto.setId(log.getId());
                dto.setUserId(log.getUser() != null ? log.getUser().getId() : null);
                dto.setUserName(log.getUser() != null ? log.getUser().getName() : null);
                dto.setAction(log.getAction());
                dto.setDetails(log.getDetails());
                dto.setTimestamp(log.getTimestamp() != null ? log.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
                return dto;
            }
}
