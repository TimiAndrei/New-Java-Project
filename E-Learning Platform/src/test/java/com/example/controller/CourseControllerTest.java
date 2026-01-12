package com.example.controller;

import com.example.model.dto.CourseResponse;
// import com.example.model.dto.CreateCourseRequest; // removed unused import
import com.example.service.CourseService;
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

@WebMvcTest(CourseController.class)
class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CourseService courseService;
    @MockBean
    private com.example.repository.CategoryRepository categoryRepository;
    @MockBean
    private com.example.repository.UserRepository userRepository;

    @Test
    @WithMockUser
    void getAllCourses_returnsList() throws Exception {
        CourseResponse course = new CourseResponse();
        course.setId(1L);
        course.setTitle("Test Course");
        // Accept any userId (nullable) since controller may pass null
        when(courseService.getAllCourses(org.mockito.ArgumentMatchers.<Long>any())).thenReturn(List.of(course));
        mockMvc.perform(get("/api/courses").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
