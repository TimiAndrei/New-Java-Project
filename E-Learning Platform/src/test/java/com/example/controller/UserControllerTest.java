package com.example.controller;

import com.example.model.dto.UserResponse;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
// import org.mockito.Mockito; // removed unused import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.List;
import java.util.Set;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void getAllUsers_returnsList() throws Exception {
        UserResponse user = new UserResponse();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setRoles(Set.of("USER"));
        when(userService.getAllUsers()).thenReturn(List.of(user));
        mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
