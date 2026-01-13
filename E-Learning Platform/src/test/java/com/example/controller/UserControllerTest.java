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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
        @Test
        @WithMockUser(roles = "ADMIN")
        void createUser_success() throws Exception {
            UserResponse user = new UserResponse();
            user.setId(1L);
            user.setEmail("test@example.com");
            user.setName("Test User");
            user.setRoles(Set.of("USER"));
            when(userService.createUser(any())).thenReturn(user);
            String json = "{\"email\":\"test@example.com\",\"password\":\"pass\",\"name\":\"Test User\",\"roles\":[\"USER\"]}";
            mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .with(csrf()))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void updateUser_success() throws Exception {
            UserResponse user = new UserResponse();
            user.setId(1L);
            user.setEmail("test@example.com");
            user.setName("Updated User");
            user.setRoles(Set.of("USER"));
            when(userService.updateUser(eq(1L), any())).thenReturn(user);
            String json = "{\"email\":\"test@example.com\",\"password\":\"pass\",\"name\":\"Updated User\",\"roles\":[\"USER\"]}";
            mockMvc.perform(put("/api/users/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .with(csrf()))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void updateUserRoles_success() throws Exception {
            UserResponse user = new UserResponse();
            user.setId(1L);
            user.setEmail("test@example.com");
            user.setName("Test User");
            user.setRoles(Set.of("ADMIN"));
            when(userService.updateUserRoles(eq(1L), any())).thenReturn(user);
            String json = "[\"ADMIN\"]";
            mockMvc.perform(put("/api/users/1/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .with(csrf()))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void deleteUser_success() throws Exception {
            mockMvc.perform(delete("/api/users/1")
                    .with(csrf()))
                    .andExpect(status().isOk());
        }
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
