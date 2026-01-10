package com.example.service;

import com.example.model.dto.AuditLogResponse;
import com.example.model.entities.AuditLog;
import com.example.model.entities.User;
import com.example.repository.AuditLogRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditServiceTest {
    @Mock
    private AuditLogRepository auditLogRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuditService auditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAuditLogs_returnsList() {
        AuditLog log = new AuditLog();
        log.setId(1L);
        when(auditLogRepository.findAll()).thenReturn(List.of(log));
        List<AuditLogResponse> result = auditService.getAllAuditLogs();
        assertEquals(1, result.size());
    }

    @Test
    void logAction_savesLog() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(new AuditLog());
        auditService.logAction(1L, "ACTION", "details");
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }
}
