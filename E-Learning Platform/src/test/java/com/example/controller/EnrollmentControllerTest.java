package com.example.controller;

import com.example.model.dto.EnrollmentResponse;
import com.example.service.EnrollmentService;
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

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EnrollmentService enrollmentService;

    @Test
    @WithMockUser
    void getAllEnrollments_returnsList() throws Exception {
        EnrollmentResponse enrollment = new EnrollmentResponse();
        enrollment.setId(1L);
        when(enrollmentService.getAllEnrollments()).thenReturn(List.of(enrollment));
        mockMvc.perform(get("/api/enrollments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
