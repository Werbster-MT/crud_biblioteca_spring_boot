package com.example.biblioteca.controllers;

import com.example.biblioteca.entities.Livro;
import com.example.biblioteca.repositories.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeViewController {

    @Autowired
    private LivroRepository livroRepository;

    /**
     * Redireciona o usuário para a página correta de acordo com o papel.
     */
    @GetMapping("/home")
    public String homeRedirect(Model model) {
        // Obtém a autenticação atual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica as permissões (roles) do usuário logado
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Se o usuário tiver a role "ROLE_ADMIN", redireciona para a home do admin
        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/home/admin";
        }
        // Se o usuário tiver a role "ROLE_USER", redireciona para a home do user
        if (roles.contains("ROLE_USER")) {
            return "redirect:/home/user";
        }
        // Se o usuário não tiver um role apropriado, redireciona para a página de login com erro
        return "redirect:/login?error=true";
    }

    /**
     * Página inicial para administradores.
     */
    @GetMapping("/home/admin")
    public String homeAdmin(Model model) {
        model.addAttribute("pageTitle", "Administração de Livros");

        // Buscar todos os livros do repositório
        List<Livro> livros = livroRepository.findAll();
        model.addAttribute("livros", livros);

        // Retorna o template específico para o admin
        return "home-admin";  // Certifique-se de que o template "home-admin.html" existe
    }

    /**
     * Página inicial para usuários comuns.
     */
    @GetMapping("/home/user")
    public String homeUser(Model model) {
        model.addAttribute("pageTitle", "Catálogo de Livros");

        // Você pode personalizar a lógica de quais livros o usuário pode ver.
        List<Livro> livros = livroRepository.findAll();
        model.addAttribute("livros", livros);

        // Retorna o template específico para o usuário
        return "home-user";  // Certifique-se de que o template "home-user.html" existe
    }

    /**
     * Página de login.
     */
    @GetMapping("/login")
    public String login() {
        return "login";  // Certifique-se de que o template "login.html" existe
    }
}