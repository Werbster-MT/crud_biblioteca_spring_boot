INSERT INTO usuarios (nome, email, senha, tipo) VALUES
('Admin User', 'admin@biblioteca.com', '$2a$10$sMkZ/Tn0GNOgGZY5jyk8tesewzCsRwq1ewwewfBTp0l6Tr8FtCBlK', 'ADMIN'),
('Maria Santos', 'maria@biblioteca.com', '$2a$10$eWZp5R9bbjCR1ZQHzS6ffewBhA2sZwBi4ZbtbVqjdcd.vTx1wp.XO', 'USER');

INSERT INTO livros (titulo, autor, isbn, quantidade_disponivel) VALUES
('O Senhor dos Anéis', 'J.R.R. Tolkien', '9780261102385', 5),
('1984', 'George Orwell', '9780451524935', 3),
('O Alquimista', 'Paulo Coelho', '9780061122415', 7);

INSERT INTO emprestimos (usuario_id, livro_id, data_emprestimo, data_devolucao) VALUES
(1, 1, '2024-09-15', NULL),  -- Livro 1 emprestado pelo Admin User
(2, 2, '2024-09-16', '2024-09-18');  -- Livro 2 devolvido pelo Common User