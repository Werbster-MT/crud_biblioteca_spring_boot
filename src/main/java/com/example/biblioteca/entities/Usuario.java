package com.example.biblioteca.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String senha; // A senha deve ser armazenada em hash

    public enum TipoUsuario {
        ADMIN, USER;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
}
