package com.example.biblioteca.security.jwt;

import com.example.biblioteca.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthFilterToken extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getTokenFromCookie(request);  // Extrai o token JWT do cookie
            if (jwt != null && jwtUtil.ValidateJwtToken(jwt)) {  // Valida o token JWT
                String username = jwtUtil.getUsernameToken(jwt);  // Extrai o nome de usuário do token

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // Carrega os detalhes do usuário
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define a autenticação no SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao processar o token: " + e.getMessage());
        }

        filterChain.doFilter(request, response);  // Continua o fluxo do filtro
    }

    // Método para extrair o token JWT do cookie
    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .peek(token -> System.out.println("JWT Token from Cookie: " + token)) // Adiciona log para verificar o token
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

}
