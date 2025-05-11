package com.tdd.billing.helpers;

import com.tdd.billing.entities.User;
import com.tdd.billing.entities.UserRole;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {

    private final String secret_key = "mysecretkey";
    private long accessTokenValidity = 60 * 60 * 1000; // 1 hora

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    // Crear un JWT con toda la información del usuario
    public String createToken(User user) {
        System.out.println("Creando token para el usuario: " + user.getRole());

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("id", user.getId());
        claims.put("role", user.getRole().name());

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }


    // Parsear las Claims desde un token
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    // Resolver Claims desde un HttpServletRequest
    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    // Resolver el token desde un header HTTP
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    // Validar las Claims de un token
    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    // Obtener el email desde las Claims
    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    // Extraer el objeto User desde un token
    public User extractUserFromToken(String token) {
        Claims claims = parseJwtClaims(token);
        User user = new User();
        user.setId(((Number) claims.get("id")).longValue());
        user.setEmail((String) claims.get("email"));
        user.setRole(UserRole.valueOf((String) claims.get("role")));
        return user;
    }
}