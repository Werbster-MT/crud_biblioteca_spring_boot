CREATE TABLE usuarios (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          senha VARCHAR(100) NOT NULL,
                          tipo ENUM('ADMIN', 'USER') NOT NULL
);

CREATE TABLE livros (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        titulo VARCHAR(200) NOT NULL,
                        autor VARCHAR(150) NOT NULL,
                        isbn VARCHAR(13) NOT NULL UNIQUE,
                        quantidade_disponivel INT NOT NULL
);

CREATE TABLE emprestimos (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             usuario_id INT,
                             livro_id INT,
                             data_emprestimo DATE NOT NULL,
                             data_devolucao DATE,
                             FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                             FOREIGN KEY (livro_id) REFERENCES livros(id)
);
