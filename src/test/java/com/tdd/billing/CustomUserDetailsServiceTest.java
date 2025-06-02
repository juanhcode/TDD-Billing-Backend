package com.tdd.billing;

import com.tdd.billing.entities.User;
import com.tdd.billing.entities.UserRole;
import com.tdd.billing.repositories.UserRepository;
import com.tdd.billing.services.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setEmail("test@example.com");
        sampleUser.setPassword("hashedpassword");
        sampleUser.setRole(UserRole.ADMIN);
    }

    @Test
    void testLoadUserByUsername_UserExists_ReturnsUserDetails() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(sampleUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("hashedpassword");
        assertThat(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))).isTrue();
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("missing@example.com");
        });
    }
}
