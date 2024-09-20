package com.example.biblioteca.controllers;

import com.example.biblioteca.entities.Usuario;
import com.example.biblioteca.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class UsuarioViewController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Página de Administração de usuários.
     */
    @GetMapping("/users")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("pageTitle", "Administração de Usuários");
        return "users";
    }
}