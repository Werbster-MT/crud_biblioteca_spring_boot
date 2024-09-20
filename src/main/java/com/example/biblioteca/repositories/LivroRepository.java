package com.example.biblioteca.repositories;

import com.example.biblioteca.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {

    // Verifica se um livro com o ISBN já existe
    boolean existsByIsbn(String isbn);

    // Busca um livro pelo ISBN
    Optional<Livro> findByIsbn(String isbn);
}
