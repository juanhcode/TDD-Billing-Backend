package com.tdd.billing;

import com.tdd.billing.security.SecurityConfig;
import com.tdd.billing.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void passwordEncoderBeanNotNull() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertThat(encoder).isNotNull();
        assertThat(encoder.matches("password", encoder.encode("password"))).isTrue();
    }

    @Test
    void corsConfigurationSourceNotNull() {
        CorsConfigurationSource corsSource = securityConfig.corsConfigurationSource();
        assertThat(corsSource).isNotNull();
    }

    @Test
    void authenticationManagerSuccess() {
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("secret")
                .authorities("ROLE_USER")
                .build();

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        AuthenticationManager authManager = securityConfig.authenticationManager();

        var authRequest = new UsernamePasswordAuthenticationToken("test@example.com", "secret");
        var authResult = authManager.authenticate(authRequest);

        assertThat(authResult.getName()).isEqualTo("test@example.com");
        assertThat(authResult.getCredentials()).isEqualTo("secret");
    }

    @Test
    void authenticationManagerThrowsOnBadPassword() {
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("correct_password")
                .authorities("ROLE_USER")
                .build();

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        AuthenticationManager authManager = securityConfig.authenticationManager();

        var authRequest = new UsernamePasswordAuthenticationToken("test@example.com", "wrong_password");

        assertThatThrownBy(() -> authManager.authenticate(authRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Credenciales inválidas");
    }
}
