package com.tdd.billing.middlewares;
import com.tdd.billing.dto.LoginReq;
import com.tdd.billing.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService authService;

    public CustomAuthenticationFilter(UserService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Extraemos el email y la contraseña del body de la solicitud
            LoginReq loginReq = new ObjectMapper().readValue(request.getInputStream(), LoginReq.class);

            // Autenticamos al usuario
            UserDetails userDetails = (UserDetails) authService.authenticate(loginReq.getEmail(), loginReq.getPassword());

            // Creamos el token de autenticación para Spring Security
            return new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
