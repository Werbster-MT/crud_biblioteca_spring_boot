package com.example.biblioteca.services;

import com.example.biblioteca.dto.AccessDTO;
import com.example.biblioteca.dto.AuthenticationDTO;
import com.example.biblioteca.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public AccessDTO login(AuthenticationDTO authDTO) {
        try {
            // Usa o campo username (que é o email) para autenticar
            UsernamePasswordAuthenticationToken userAuth =
                    new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword());

            // Autentica o usuário
            Authentication authentication = authenticationManager.authenticate(userAuth);

            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetailsImpl) {
                UserDetailsImpl userAuthenticate = (UserDetailsImpl) principal;

                // Gera o token JWT
                String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthenticate);

                // Obtém o papel do usuário
                String role = userAuthenticate.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .findFirst()
                        .orElse("ROLE_USER");

                // Retorna o token, papel, email
                return new AccessDTO(token, role, userAuthenticate.getUsername());
            } else {
                throw new IllegalArgumentException("O principal não é uma instância de UserDetailsImpl");
            }

        } catch (BadCredentialsException e) {
            return new AccessDTO("Acesso Negado", "", "");
        }
    }
}