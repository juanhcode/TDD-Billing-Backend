package com.tdd.billing.controllers;
import com.tdd.billing.dto.LoginReq;
import com.tdd.billing.dto.LoginRes;
import com.tdd.billing.entities.User;
import com.tdd.billing.helpers.JwtUtil;
import com.tdd.billing.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/auth")
public class AuthController {

    private final UserService authService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        try {
            // Autenticación del usuario
            User usuario = authService.authenticate(loginReq.getEmail(), loginReq.getPassword());
            System.out.println("Datos completos del usuario: " + usuario);

            // Convertir el objeto Usuario a User (si es necesario)
            User user = new User();
            user.setId(usuario.getId());
            user.setEmail(usuario.getEmail());
            user.setRole(usuario.getRole());
            user.setName(usuario.getName());
            // No incluyas la contraseña a menos que sea estrictamente necesario

            // Generar el token JWT
            String jwt = jwtUtil.createToken(user);

            // Crear la respuesta de login
            LoginRes loginRes = new LoginRes(user.getEmail(), jwt);
            return ResponseEntity.ok(loginRes);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
