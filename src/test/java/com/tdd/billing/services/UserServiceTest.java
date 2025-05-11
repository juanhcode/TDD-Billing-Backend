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
        user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setEmail("juan@mail.com");
        user.setPassword("123456");
        user.setRole(UserRole.ADMIN);
        user.setPhotoUrl("http://photo.com/pic.jpg");
        user.setStatus(true);
        user.setPhoneNumber("123456789");
    }

    @Test
    void testRegisterUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(user);

        assertEquals(user.getEmail(), result.getEmail());
        verify(passwordEncoder).encode("123456"); // Verificar que se haya llamado al encoder
        verify(userRepository).save(any(User.class)); // Verificar que el repositorio ha guardado el usuario
    }

    @Test
    void testFindById_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> found = userService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
    }

    @Test
    void testUpdateUser_Success() {
        User updatedInfo = new User();
        updatedInfo.setName("Pedro");
        updatedInfo.setEmail("pedro@mail.com");
        updatedInfo.setPassword("newPass");
        updatedInfo.setRole(UserRole.CUSTOMER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updated = userService.updateUser(1L, updatedInfo);

        assertEquals("Pedro", updated.getName());
        assertEquals("pedro@mail.com", updated.getEmail());
        verify(userRepository).save(any(User.class)); // Verificar que el repositorio ha guardado la actualización
    }

    @Test
    void testAuthenticate_Success() {
        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", user.getPassword())).thenReturn(true);

        User authenticated = userService.authenticate("juan@mail.com", "123456");

        assertNotNull(authenticated);
        assertEquals("juan@mail.com", authenticated.getEmail());
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", user.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.authenticate("juan@mail.com", "wrong"));
    }
}
