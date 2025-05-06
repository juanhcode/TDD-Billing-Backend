package com.tdd.billing.services;

import com.tdd.billing.entities.User;
import com.tdd.billing.entities.UserRole;
import com.tdd.billing.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Juan", "juan@mail.com", "123456", UserRole.CUSTOMER,"", true,"12121", null);
    }

    @Test
    void registrarUsuario() {
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User newUser = new User(null, "Juan", "juan@mail.com", "123456", UserRole.CUSTOMER,"", true,"12121", null);
        User result = userService.registerUser(newUser);

        assertNotNull(result);
        assertEquals("Juan", result.getName());
        verify(userRepository, times(1)).save(any(User.class));
        System.out.println("Test registrarUsuario pasó exitosamente ✅");
    }


//    @Test
//    void loginUsuario_Exitoso() {
//        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("123456", user.getPassword())).thenReturn(true);
//
//        User result = userService.login("juan@mail.com", "123456");
//
//        assertNotNull(result);
//        assertEquals("Juan", result.getName());
//        System.out.println("Test registrarUsuario pasó exitosamente ✅");
//    }
//
//    @Test
//    void loginUsuario_Fallido() {
//        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () -> userService.login("juan@mail.com", "123456"));
//        System.out.println("Test registrarUsuario pasó exitosamente ✅");
//    }

    @Test
    void listarUsuariosActivos() {
        when(userRepository.findByStatusTrue()).thenReturn(List.of(user));

        List<User> result = userService.listarUsuariosActivos();

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getName());
        System.out.println("Test registrarUsuario pasó exitosamente ✅");
    }

    @Test
    void actualizarUsuario() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = new User(1L, "Juan Updated", "juan_updated@mail.com", "newpass", UserRole.SELLER,"", true,"12121", null);
        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("Juan Updated", result.getName());
        System.out.println("Test registrarUsuario pasó exitosamente ✅");
    }

    @Test
    void eliminarUsuario() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
        System.out.println("Test registrarUsuario pasó exitosamente ✅");
    }
}