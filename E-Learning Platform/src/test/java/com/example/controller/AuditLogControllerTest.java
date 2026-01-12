package com.example.controller;

import com.example.model.dto.AuditLogResponse;
import com.example.service.AuditService;
import org.junit.jupiter.api.Test;
// import org.mockito.Mockito; // removed unused import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditLogController.class)
class AuditLogControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuditService auditService;

    @Test
    @WithMockUser
    void getAllAuditLogs_returnsList() throws Exception {
        AuditLogResponse log = new AuditLogResponse();
        log.setId(1L);
        when(auditService.getAllAuditLogs()).thenReturn(List.of(log));
        mockMvc.perform(get("/api/audit-logs").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
