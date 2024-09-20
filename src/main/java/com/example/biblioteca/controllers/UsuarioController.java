package com.example.biblioteca.controllers;

import com.example.biblioteca.entities.Usuario;
import com.example.biblioteca.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Listar todos os usuários
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Buscar um usuário por ID
    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable int id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // Editar um usuário
    @PutMapping("/{id}")
    public Usuario editarUsuario(@PathVariable int id, @RequestBody Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    // Atualizar campos do usuário
                    usuario.setNome(usuarioAtualizado.getNome());
                    usuario.setEmail(usuarioAtualizado.getEmail());

                    // Verificar se a senha foi alterada e, se sim, codificar a nova senha
                    if (!passwordEncoder.matches(usuarioAtualizado.getSenha(), usuario.getSenha())) {
                        usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
                    }

                    usuario.setTipo(usuarioAtualizado.getTipo());
                    return usuarioRepository.save(usuario);
                }).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // Criar um novo usuário (Cadastro)
    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario usuario) {
        // Hash da senha antes de salvar
        String senha = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senha);

        System.out.println(passwordEncoder.matches("test", senha));

        return usuarioRepository.save(usuario);
    }

    // Deletar um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerUsuario(@PathVariable int id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuário removido com sucesso.");
    }


}
