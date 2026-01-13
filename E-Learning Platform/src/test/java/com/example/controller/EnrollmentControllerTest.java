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
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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

    @Test
    @WithMockUser
    void createEnrollment_success() throws Exception {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setId(1L);
        when(enrollmentService.createEnrollment(any())).thenReturn(response);
        String json = "{\"studentId\":2,\"courseId\":3}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteEnrollment_success() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/enrollments/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getEnrollmentById_found() throws Exception {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setId(1L);
        when(enrollmentService.getEnrollmentById(1L)).thenReturn(java.util.Optional.of(response));
        mockMvc.perform(get("/api/enrollments/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getEnrollmentById_notFound() throws Exception {
        when(enrollmentService.getEnrollmentById(99L)).thenReturn(java.util.Optional.empty());
        mockMvc.perform(get("/api/enrollments/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createEnrollment_invalidRequest() throws Exception {
        when(enrollmentService.createEnrollment(any())).thenThrow(new RuntimeException("Invalid"));
        String json = "{\"studentId\":2,\"courseId\":3}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }
}
