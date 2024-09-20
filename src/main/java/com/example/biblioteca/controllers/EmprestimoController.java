package com.example.biblioteca.controllers;

import com.example.biblioteca.entities.Emprestimo;
import com.example.biblioteca.entities.Livro;
import com.example.biblioteca.entities.Usuario;
import com.example.biblioteca.repositories.EmprestimoRepository;
import com.example.biblioteca.repositories.LivroRepository;
import com.example.biblioteca.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos os empréstimos (somente Admin)
    @GetMapping
    public List<Emprestimo> listarEmprestimos() {
        return emprestimoRepository.findAll();
    }
    // Buscar Empréstimo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarEmprestimoPorId(@PathVariable int id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
        return ResponseEntity.ok(emprestimo);
    }

    // Realizar um novo empréstimo
    @PostMapping
    public ResponseEntity<String> realizarEmprestimo(@RequestBody Emprestimo emprestimo) {
        Usuario usuario = usuarioRepository.findById(emprestimo.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Livro livro = livroRepository.findById(emprestimo.getLivro().getId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        // Verifica se o usuário já tem 3 livros emprestados
        List<Emprestimo> emprestimosUsuario = emprestimoRepository.findByUsuarioId(usuario.getId());
        if (emprestimosUsuario.size() >= 3) {
            return ResponseEntity.badRequest().body("Usuário já possui 3 livros emprestados.");
        }

        // Verifica se o livro está disponível
        if (livro.getQuantidadeDisponivel() <= 0) {
            return ResponseEntity.badRequest().body("Livro indisponível.");
        }

        // Atualiza a quantidade disponível do livro e registra o empréstimo
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);

        emprestimo.setDataEmprestimo(new Date());
        emprestimoRepository.save(emprestimo);

        return ResponseEntity.ok("Empréstimo realizado com sucesso.");
    }

    // Devolução de um livro
    @PutMapping("/devolucao/{id}")
    public ResponseEntity<String> devolverLivro(@PathVariable int id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        Livro livro = emprestimo.getLivro();

        // Atualiza a quantidade disponível do livro
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livroRepository.save(livro);

        // Registra a data de devolução
        emprestimo.setDataDevolucao(new Date());
        emprestimoRepository.save(emprestimo);

        return ResponseEntity.ok("Livro devolvido com sucesso.");
    }

    // Editar um empréstimo
    @PutMapping("/{id}")
    public ResponseEntity<String> editarEmprestimo(@PathVariable int id, @RequestBody Emprestimo emprestimoAtualizado) {
        Emprestimo emprestimoExistente = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        // Atualiza os detalhes do empréstimo, incluindo o usuário e o livro, se necessário
        Usuario usuario = usuarioRepository.findById(emprestimoAtualizado.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Livro livro = livroRepository.findById(emprestimoAtualizado.getLivro().getId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        // Atualizar quantidade de livro se necessário
        if (!emprestimoExistente.getLivro().equals(livro)) {
            // Reverte a quantidade do livro anterior
            Livro livroAnterior = emprestimoExistente.getLivro();
            livroAnterior.setQuantidadeDisponivel(livroAnterior.getQuantidadeDisponivel() + 1);
            livroRepository.save(livroAnterior);

            // Atualiza a quantidade do novo livro
            if (livro.getQuantidadeDisponivel() <= 0) {
                return ResponseEntity.badRequest().body("Novo livro indisponível.");
            }
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
            livroRepository.save(livro);
        }

        // Atualiza os campos
        emprestimoExistente.setUsuario(usuario);
        emprestimoExistente.setLivro(livro);
        emprestimoExistente.setDataEmprestimo(emprestimoAtualizado.getDataEmprestimo());

        // Verifica se a data de devolução foi enviada, para evitar sobrescrever com null
        if (emprestimoAtualizado.getDataDevolucao() != null) {
            emprestimoExistente.setDataDevolucao(emprestimoAtualizado.getDataDevolucao());
        }

        emprestimoRepository.save(emprestimoExistente);

        return ResponseEntity.ok("Empréstimo atualizado com sucesso.");
    }

    // Excluir um empréstimo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirEmprestimo(@PathVariable int id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        // Atualiza a quantidade disponível do livro ao excluir o empréstimo
        Livro livro = emprestimo.getLivro();
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livroRepository.save(livro);

        // Exclui o empréstimo
        emprestimoRepository.delete(emprestimo);

        return ResponseEntity.ok("Empréstimo excluído com sucesso.");
    }
}
