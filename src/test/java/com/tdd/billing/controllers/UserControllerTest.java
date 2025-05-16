package com.tdd.billing.controllers;

import com.tdd.billing.entities.User;
import com.tdd.billing.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        mockUser.setEmail("test@example.com");
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
    void listActiveUsers_shouldReturnListOfUsers() {
        List<User> users = List.of(mockUser);
        when(userService.listarUsuariosActivos()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.listActiveUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
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
    void getUserById_shouldReturnNotFound_whenUserNotExists() {
        when(userService.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(2L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService).findById(2L);
    }

//    @Test
//    void update_shouldReturnUpdatedUser() {
//        User updatedUser = new User();
//        updatedUser.setId(1L);
//        updatedUser.setFirstName("Updated");
//        updatedUser.setLastName("User");
//
//        when(userService.updateUser(1L, updatedUser)).thenReturn(updatedUser);
//
//        ResponseEntity<User> response = userController.update(1L, updatedUser);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(updatedUser, response.getBody());
//        verify(userService).updateUser(1L, updatedUser);
//    }

    @Test
    void delete_shouldReturnNoContent() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.delete(1L);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService).deleteUser(1L);
    }
}
