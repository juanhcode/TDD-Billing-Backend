package com.tdd.billing;

import com.tdd.billing.entities.User;
import com.tdd.billing.entities.UserRole;
import com.tdd.billing.helpers.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setEmail("test@example.com");
        sampleUser.setRole(UserRole.ADMIN);
        sampleUser.setFirstName("Juan");
        sampleUser.setLastName("Pérez");
    }

    @Test
    void testCreateTokenAndParseClaims() {
        String token = jwtUtil.createToken(sampleUser);
        assertThat(token).isNotBlank();

        Claims claims = jwtUtil.resolveClaims(mockRequestWithToken(token));
        assertThat(claims.getSubject()).isEqualTo("test@example.com");
        assertThat(claims.get("id", Integer.class)).isEqualTo(1);
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
        assertThat(claims.get("firstName", String.class)).isEqualTo("Juan");
        assertThat(claims.get("lastName", String.class)).isEqualTo("Pérez");
    }

    @Test
    void testGetEmailFromClaims() {
        String token = jwtUtil.createToken(sampleUser);
        Claims claims = jwtUtil.resolveClaims(mockRequestWithToken(token));
        String email = jwtUtil.getEmail(claims);
        assertThat(email).isEqualTo("test@example.com");
    }

    @Test
    void testValidateClaimsValidToken() {
        String token = jwtUtil.createToken(sampleUser);
        Claims claims = jwtUtil.resolveClaims(mockRequestWithToken(token));
        boolean isValid = jwtUtil.validateClaims(claims);
        assertThat(isValid).isTrue();
    }

    @Test
    void testExtractUserFromToken() {
        String token = jwtUtil.createToken(sampleUser);
        User extractedUser = jwtUtil.extractUserFromToken(token);

        assertThat(extractedUser.getId()).isEqualTo(sampleUser.getId());
        assertThat(extractedUser.getRole()).isEqualTo(sampleUser.getRole());
    }

    @Test
    void testResolveTokenReturnsNullWhenHeaderMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = jwtUtil.resolveToken(request);
        assertThat(token).isNull();
    }

    @Test
    void testResolveTokenReturnsNullWhenHeaderIsMalformed() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("BadHeader");

        String token = jwtUtil.resolveToken(request);
        assertThat(token).isNull();
    }

    @Test
    void testResolveTokenReturnsTokenCorrectly() {
        String validToken = "Bearer abc.def.ghi";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(validToken);

        String token = jwtUtil.resolveToken(request);
        assertThat(token).isEqualTo("abc.def.ghi");
    }

    private HttpServletRequest mockRequestWithToken(String token) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        return request;
    }
}
