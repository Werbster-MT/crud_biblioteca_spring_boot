package com.example.biblioteca.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/")
    public String loginPage() {
        return "login";  // Retorna o nome da página de login (login.html no diretório templates)
    }
}