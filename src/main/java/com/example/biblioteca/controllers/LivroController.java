package com.example.biblioteca.controllers;

import com.example.biblioteca.entities.Livro;
import com.example.biblioteca.repositories.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    // Listar todos os livros
    @GetMapping
    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    // Buscar um livro por id
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarLivroPorId(@PathVariable int id) {
        return livroRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Adicionar um novo livro
    @PostMapping
    public ResponseEntity<Livro> adicionarLivro(@RequestBody Livro livro) {
        // Verifica se o ISBN é único
        if (livroRepository.existsByIsbn(livro.getIsbn())) {
            return ResponseEntity.badRequest().body(null);  // ISBN duplicado
        }
        return ResponseEntity.ok(livroRepository.save(livro));
    }

    // Editar um livro
    @PutMapping("/{id}")
    public ResponseEntity<?> editarLivro(@PathVariable int id, @RequestBody Livro livroAtualizado) {
        return livroRepository.findById(id)
                .map(livro -> {
                    // Atualiza os campos do livro
                    livro.setTitulo(livroAtualizado.getTitulo());
                    livro.setAutor(livroAtualizado.getAutor());
                    // Verifica se o ISBN foi alterado e se é único
                    if (!livro.getIsbn().equals(livroAtualizado.getIsbn()) && livroRepository.existsByIsbn(livroAtualizado.getIsbn())) {
                        return ResponseEntity.badRequest().body(null); // ISBN duplicado
                    }
                    livro.setIsbn(livroAtualizado.getIsbn());
                    livro.setQuantidadeDisponivel(livroAtualizado.getQuantidadeDisponivel());

                    Livro livroEditado = livroRepository.save(livro);
                    return ResponseEntity.ok(livroEditado);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Remover um livro (somente Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerLivro(@PathVariable int id) {
        livroRepository.deleteById(id);
        return ResponseEntity.ok("Livro removido com sucesso.");
    }
}
