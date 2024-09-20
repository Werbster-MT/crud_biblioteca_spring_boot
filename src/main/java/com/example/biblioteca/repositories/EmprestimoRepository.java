package com.example.biblioteca.repositories;

import com.example.biblioteca.entities.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {
    List<Emprestimo> findByUsuarioId(int usuarioId);
}
