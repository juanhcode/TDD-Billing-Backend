package com.tdd.billing.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.billing.dto.LoginReq;
import com.tdd.billing.entities.User;
import com.tdd.billing.entities.UserRole;
import com.tdd.billing.helpers.JwtUtil;
import com.tdd.billing.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;
    private LoginReq loginReq;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();

        loginReq = new LoginReq("hoyos@gmail.com", "12345");

        user = new User();
        user.setId(1L);
        user.setEmail("hoyos@gmail.com");
        user.setFirstName("Juan");
        user.setLastName("Hoyos");
        user.setRole(UserRole.ADMIN);
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        when(userService.authenticate(anyString(), anyString()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(post("/rest/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testLoginInternalServerError() throws Exception {
        when(userService.authenticate(anyString(), anyString()))
                .thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(post("/rest/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Something went wrong"));
    }

}
