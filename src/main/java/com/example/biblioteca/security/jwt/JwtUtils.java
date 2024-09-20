package com.example.biblioteca.security.jwt;

import com.example.biblioteca.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${biblioteca.jwtSecret}")
    private String jwtSecret;

    @Value("${biblioteca.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateTokenFromUserDetailsImpl(UserDetailsImpl userDetail) {
        return Jwts.builder().setSubject(userDetail.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(getSigninKey(), SignatureAlgorithm.HS512).compact();
    }

    public Key getSigninKey() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return key;
    }

    public String getUsernameToken(String token) {
        return Jwts.parser().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean ValidateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSigninKey()).build().parseClaimsJws(authToken);
            return true;

        } catch(MalformedJwtException e) {
            System.out.println("Token Inválido " + e.getMessage());

        } catch(ExpiredJwtException e) {
            System.out.println("Token Expirado " + e.getMessage());
        }
        catch(UnsupportedJwtException e) {
            System.out.println("Token Não Suportado " + e.getMessage());
        }
        catch(IllegalArgumentException e) {
            System.out.println("Token Argumento Inválido " + e.getMessage());
        }

        return false;
    }
}
