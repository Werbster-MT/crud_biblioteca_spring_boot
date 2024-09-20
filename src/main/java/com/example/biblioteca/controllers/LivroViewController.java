package com.example.biblioteca.controllers;

import com.example.biblioteca.entities.Livro;
import com.example.biblioteca.repositories.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LivroViewController {

    @Autowired
    private LivroRepository livroRepository;

    // Método para renderizar a página de exibição de livros
    @GetMapping("/livros/view")
    public String exibirLivros(Model model) {
        List<Livro> livros = livroRepository.findAll();
        model.addAttribute("livros", livros);  // Passa a lista de livros para o template
        return "livros";  // Nome do template HTML a ser renderizado
    }
}
