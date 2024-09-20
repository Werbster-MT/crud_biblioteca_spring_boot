package com.example.biblioteca.controllers;

import com.example.biblioteca.dto.AccessDTO;
import com.example.biblioteca.dto.AuthenticationDTO;
import com.example.biblioteca.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDTO authDTO, HttpServletResponse response) {
        AccessDTO accessDTO = authService.login(authDTO);  // Agora retorna AccessDTO
        System.out.println("TIPO DE USER: " + accessDTO.getRole());

        // Certifica-se de que o AccessDTO contém um token válido
        if (accessDTO.getToken().equals("Acesso Negado")) {
            return ResponseEntity.status(401).body("Acesso Negado");
        }

        // Gera o cookie com o JWT
        Cookie jwtCookie = new Cookie("jwt", accessDTO.getToken());
        jwtCookie.setHttpOnly(true);   // Impede que o JavaScript tenha acesso ao cookie
        jwtCookie.setSecure(false);    // Em produção, use 'true' para HTTPS; aqui usamos 'false' para ambiente local
        jwtCookie.setPath("/");        // O cookie estará disponível para toda a aplicação
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // Validade de 7 dias

        // Adiciona o cookie JWT à resposta
        response.addCookie(jwtCookie);

        // Retorna o objeto JSON com as informações do usuário (token, role, username)
        return ResponseEntity.ok(accessDTO);
    }

}
