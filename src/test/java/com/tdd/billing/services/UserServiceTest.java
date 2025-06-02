package com.tdd.billing.services;
import com.tdd.billing.dto.UpdateUserDTO;
import com.tdd.billing.dto.UserResponseDTO;
import com.tdd.billing.entities.User;
import com.tdd.billing.entities.UserRole;
import com.tdd.billing.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        user.setFirstName("Juan");
        user.setLastName("Pérez");
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

        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testFindById_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> found = userService.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> found = userService.findById(2L);

        assertThat(found).isNotPresent();
    }

    @Test
    void testListarUsuariosActivos() {
        User activeUser = new User();
        activeUser.setStatus(true);
        List<User> activeUsers = List.of(user, activeUser);

        when(userRepository.findByStatusTrue()).thenReturn(activeUsers);

        List<User> result = userService.listarUsuariosActivos();

        assertThat(result).hasSize(2);
        verify(userRepository).findByStatusTrue();
    }

    @Test
    void testUpdateUser_AllFieldsUpdated() {
        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setFirstName("Pedro");
        dto.setLastName("Gómez");
        dto.setEmail("pedro@mail.com");
        dto.setPhoneNumber("987654321");
        dto.setAddress("Calle Falsa 123");
        dto.setStatus(false);
        dto.setPassword("newPass");
        dto.setRole("CUSTOMER");

        String newPhotoUrl = "http://photo.com/newpic.jpg";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUser(1L, dto, newPhotoUrl);

        assertThat(updated.getFirstName()).isEqualTo("Pedro");
        assertThat(updated.getLastName()).isEqualTo("Gómez");
        assertThat(updated.getEmail()).isEqualTo("pedro@mail.com");
        assertThat(updated.getPhoneNumber()).isEqualTo("987654321");
        assertThat(updated.getAddress()).isEqualTo("Calle Falsa 123");
        assertThat(updated.getStatus()).isFalse();
        assertThat(updated.getPassword()).isEqualTo("encodedNewPass");
        assertThat(updated.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(updated.getPhotoUrl()).isEqualTo(newPhotoUrl);

        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode("newPass");
        verify(userRepository).save(updated);
    }

    @Test
    void testUpdateUser_PasswordNotChanged_PhotoUrlNotChanged_RoleNotChanged() {
        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setFirstName("Pedro");
        dto.setLastName("Gómez");
        dto.setEmail("pedro@mail.com");
        dto.setPhoneNumber("987654321");
        dto.setAddress("Calle Falsa 123");
        dto.setStatus(false);
        dto.setPassword(null);  // No cambia password
        dto.setRole(null);      // No cambia rol

        String photoUrl = "";   // No cambia foto

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUser(1L, dto, photoUrl);

        assertThat(updated.getPassword()).isEqualTo(user.getPassword());
        assertThat(updated.getRole()).isEqualTo(user.getRole());
        assertThat(updated.getPhotoUrl()).isEqualTo(user.getPhotoUrl());

        verify(userRepository).findById(1L);
        verify(userRepository).save(updated);
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UpdateUserDTO dto = new UpdateUserDTO();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(1L, dto, null));

        assertThat(exception).hasMessage("Usuario no encontrado");
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        assertThat(user.getStatus()).isFalse();
        verify(userRepository).findById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(1L));

        assertThat(exception).hasMessage("Usuario no encontrado");
    }

    @Test
    void testAuthenticate_Success() {
        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", user.getPassword())).thenReturn(true);

        User authenticated = userService.authenticate("juan@mail.com", "123456");

        assertThat(authenticated).isNotNull();
        assertThat(authenticated.getEmail()).isEqualTo("juan@mail.com");
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", user.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.authenticate("juan@mail.com", "wrong"));
    }

    @Test
    void testGetUsersByStoreId() {
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("maria@mail.com");
        user2.setRole(UserRole.CUSTOMER);  // <-- Importante asignar un role no nulo

        when(userRepository.findByStoreId(1L)).thenReturn(List.of(user, user2));

        List<UserResponseDTO> dtos = userService.getUsersByStoreId(1L);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getEmail()).isEqualTo(user.getEmail());
        assertThat(dtos.get(1).getEmail()).isEqualTo(user2.getEmail());

        verify(userRepository).findByStoreId(1L);
    }

}
