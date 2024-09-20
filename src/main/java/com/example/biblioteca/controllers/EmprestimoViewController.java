package com.example.biblioteca.controllers;

import com.example.biblioteca.entities.Emprestimo;
import com.example.biblioteca.entities.Livro;
import com.example.biblioteca.entities.Usuario;
import com.example.biblioteca.repositories.EmprestimoRepository;
import com.example.biblioteca.repositories.LivroRepository;
import com.example.biblioteca.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EmprestimoViewController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para exibir a página de administração de empréstimos
    @GetMapping("/loans")
    public String listarEmprestimos(Model model) {
        // Busca todos os empréstimos, usuários e livros para a view
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        List<Livro> livros = livroRepository.findAll();
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Adiciona os objetos ao modelo para serem acessados na página
        model.addAttribute("emprestimos", emprestimos);
        model.addAttribute("livros", livros);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("pageTitle", "Administração de Empréstimos");

        // Retorna a página HTML correspondente
        return "loans";
    }
}
