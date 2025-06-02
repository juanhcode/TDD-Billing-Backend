package com.tdd.billing.controllers;
import com.tdd.billing.dto.UserResponseDTO;
import com.tdd.billing.entities.User;
import com.tdd.billing.services.S3Service;
import com.tdd.billing.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private S3Service s3Service;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("123456");
    }

    @Test
    void register_shouldReturnRegisteredUser() {
        when(userService.registerUser(mockUser)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.register(mockUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
        verify(userService).registerUser(mockUser);
    }

    @Test
    void listActiveUsers_shouldReturnActiveUsers() {
        when(userService.listarUsuariosActivos()).thenReturn(List.of(mockUser));

        ResponseEntity<List<User>> response = userController.listActiveUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(userService).listarUsuariosActivos();
    }

    @Test
    void getUserById_shouldReturnUser_whenExists() {
        when(userService.findById(1L)).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
        verify(userService).findById(1L);
    }

    @Test
    void getUserById_shouldReturnNotFound_whenMissing() {
        when(userService.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(2L);

        assertEquals(404, response.getStatusCodeValue());
        verify(userService).findById(2L);
    }

    @Test
    void delete_shouldReturnNoContent() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.delete(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService).deleteUser(1L);
    }

    @Test
    void getUsersByStoreId_shouldReturnUsers() {
        List<UserResponseDTO> users = List.of(new UserResponseDTO());
        when(userService.getUsersByStoreId(10L)).thenReturn(users);

        ResponseEntity<List<UserResponseDTO>> response = userController.getUsersByStoreId(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
        verify(userService).getUsersByStoreId(10L);
    }
}
