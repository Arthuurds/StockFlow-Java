-- ========================================================
-- 1. CRIAÇÃO DA BASE DE DADOS
-- ========================================================
CREATE DATABASE IF NOT EXISTS stockflow_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE stockflow_db;

-- Reset de tabelas para execução limpa (ordem inversa de dependência)
DROP TABLE IF EXISTS itens_venda;
DROP TABLE IF EXISTS compras;
DROP TABLE IF EXISTS vendas;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS fornecedores;

-- ========================================================
-- 2. CRIAÇÃO DAS TABELAS (Estrutura ER)
-- ========================================================

-- Tabela 1: Fornecedores
CREATE TABLE fornecedores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(20)
) ENGINE=InnoDB;

-- Tabela 2: Clientes
CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf_cnpj VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(20)
) ENGINE=InnoDB;

-- Tabela 3: Usuários
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL DEFAULT 'OPERADOR'
) ENGINE=InnoDB;

-- Tabela 4: Produtos
CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    categoria VARCHAR(50),
    preco DECIMAL(10,2) NOT NULL CHECK (preco > 0),
    quantidade_estoque INT NOT NULL DEFAULT 0 CHECK (quantidade_estoque >= 0),
    fornecedor_id BIGINT,
    CONSTRAINT fk_produto_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Tabela 5: Vendas
CREATE TABLE vendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT,
    usuario_id BIGINT,
    data_venda DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_venda_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB;

-- Tabela 6: Itens da Venda
CREATE TABLE itens_venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_item_venda FOREIGN KEY (venda_id) REFERENCES vendas(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
) ENGINE=InnoDB;

-- Tabela 7: Compras (Entradas de Estoque)
CREATE TABLE compras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade > 0),
    preco_custo DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    data_compra DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_compra_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT fk_compra_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
) ENGINE=InnoDB;

-- ========================================================
-- 3. INSERÇÃO DE DADOS (Pelo menos 5 registos por tabela)
-- ========================================================

-- Inserindo em Fornecedores (6 registos)
INSERT INTO fornecedores (nome, cnpj, email, telefone) VALUES
('Tech Distribuidora Ltda', '12.345.678/0001-99', 'contato@techdist.com', '(11) 98888-7777'),
('Mega Eletrônicos S.A.', '98.765.432/0001-11', 'vendas@megaeletronicos.com', '(21) 97777-1111'),
('LogiDistribuição Import', '45.123.789/0001-22', 'suporte@logidist.com', '(31) 98888-2222'),
('Alfa Componentes', '33.444.555/0001-33', 'comercial@alfacomp.com', '(41) 99999-3333'),
('Beta Suprimentos', '66.777.888/0001-44', 'atendimento@betasup.com', '(51) 98888-4444'),
('Temp Supply Teste', '00.000.000/0001-00', 'temp@teste.com', '(00) 00000-0000');

-- Inserindo em Clientes (6 registos)
INSERT INTO clientes (nome, cpf_cnpj, email, telefone) VALUES
('João Silva', '123.456.789-00', 'joao@email.com', '(11) 97777-6666'),
('Maria Oliveira', '234.567.890-11', 'maria@email.com', '(21) 98888-5555'),
('Carlos Souza', '345.678.901-22', 'carlos@email.com', '(31) 99999-4444'),
('Ana Lima', '456.789.012-33', 'ana@email.com', '(41) 97777-3333'),
('Pedro Santos', '567.890.123-44', 'pedro@email.com', '(51) 98888-2222'),
('Cliente Teste Exclusao', '000.000.000-99', 'teste@excluir.com', '(00) 90000-0000');

-- Inserindo em Usuários (6 registos)
INSERT INTO usuarios (nome, login, senha, perfil) VALUES
('Administrador System', 'admin', 'admin123', 'ADMIN'),
('Lucas Operador', 'lucas_op', 'senha123', 'OPERADOR'),
('Fernanda Gerente', 'fernanda_g', 'senha123', 'GERENTE'),
('Bruno Vendedor', 'bruno_v', 'senha123', 'OPERADOR'),
('Camila Supervisor', 'camila_s', 'senha123', 'SUPERVISOR'),
('Usuario Temp', 'usertemp', 'temp123', 'OPERADOR');

-- Inserindo em Produtos (6 registos)
INSERT INTO produtos (nome, descricao, categoria, preco, quantidade_estoque, fornecedor_id) VALUES
('Teclado Mecânico RGB', 'Teclado Switch Blue ABNT2', 'Periféricos', 250.00, 15, 1),
('Mouse Óptico 1600 DPI', 'Mouse ergonómico USB', 'Periféricos', 80.00, 30, 2),
('Monitor 24 polegadas Full HD', 'Monitor 144Hz IPS', 'Monitores', 899.90, 8, 3),
('Headset Gamer 7.1', 'Headset com microfone antirruído', 'Áudio', 199.90, 20, 4),
('Webcam Full HD 1080p', 'Webcam com autofoco e microfone', 'Acessórios', 150.00, 12, 5),
('Produto Obsoleto Teste', 'Produto sem estoque para teste', 'Testes', 10.00, 0, 6);

-- Inserindo em Vendas (6 registos)
INSERT INTO vendas (cliente_id, usuario_id, valor_total) VALUES
(1, 1, 330.00),
(2, 2, 899.90),
(3, 4, 199.90),
(4, 2, 150.00),
(5, 4, 250.00),
(6, 6, 10.00);

-- Inserindo em Itens da Venda (6 registos)
INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal) VALUES
(1, 1, 1, 250.00, 250.00),
(1, 2, 1, 80.00, 80.00),
(2, 3, 1, 899.90, 899.90),
(3, 4, 1, 199.90, 199.90),
(4, 5, 1, 150.00, 150.00),
(6, 6, 1, 10.00, 10.00);

-- Inserindo em Compras (6 registos)
INSERT INTO compras (produto_id, fornecedor_id, quantidade, preco_custo, valor_total) VALUES
(1, 1, 10, 150.00, 1500.00),
(2, 2, 20, 40.00, 800.00),
(3, 3, 5, 600.00, 3000.00),
(4, 4, 15, 100.00, 1500.00),
(5, 5, 10, 80.00, 800.00),
(6, 6, 1, 5.00, 5.00);


-- ========================================================
-- 4. EXIBIÇÃO DOS DADOS (SELECT)
-- ========================================================

-- Busca Geral em todas as tabelas
SELECT * FROM fornecedores;
SELECT * FROM clientes;
SELECT * FROM usuarios;
SELECT * FROM produtos;
SELECT * FROM vendas;
SELECT * FROM itens_venda;
SELECT * FROM compras;

-- Buscas específicas com a cláusula WHERE
SELECT * FROM fornecedores WHERE nome LIKE '%Tech%';
SELECT * FROM clientes WHERE cpf_cnpj = '123.456.789-00';
SELECT * FROM usuarios WHERE perfil = 'OPERADOR';
SELECT * FROM produtos WHERE preco > 200.00;
SELECT * FROM vendas WHERE valor_total >= 300.00;
SELECT * FROM itens_venda WHERE quantidade > 1;
SELECT * FROM compras WHERE valor_total > 1000.00;


-- ========================================================
-- 5. EDIÇÃO DOS DADOS (UPDATE) - Atualiza 1 registo por tabela
-- ========================================================

UPDATE fornecedores SET telefone = '(11) 99999-0000' WHERE id = 1;
UPDATE clientes SET email = 'joao.silva.novo@email.com' WHERE id = 1;
UPDATE usuarios SET perfil = 'GERENTE' WHERE id = 2;
UPDATE produtos SET preco = 269.90 WHERE id = 1;
UPDATE vendas SET valor_total = 330.00 WHERE id = 1;
UPDATE itens_venda SET quantidade = 2, subtotal = 160.00 WHERE id = 2;
UPDATE compras SET valor_total = 1600.00 WHERE id = 1;


-- ========================================================
-- 6. EXCLUSÃO DOS DADOS (DELETE) - Elimina 1 registo por tabela
-- (Executado na ordem correta para não quebrar Restrições FK)
-- ========================================================

-- 1. Elimina itens e compras associados ao registo de teste (ID 6)
DELETE FROM itens_venda WHERE id = 6;
DELETE FROM compras WHERE id = 6;

-- 2. Elimina a venda de teste (ID 6)
DELETE FROM vendas WHERE id = 6;

-- 3. Elimina o produto de teste (ID 6)
DELETE FROM produtos WHERE id = 6;

-- 4. Elimina o utilizador, cliente e fornecedor de teste (ID 6)
DELETE FROM usuarios WHERE id = 6;
DELETE FROM clientes WHERE id = 6;
DELETE FROM fornecedores WHERE id = 6;


-- ========================================================
-- CONFIRMAÇÃO FINAL: Verifica que cada tabela ficou com 5 registos
-- ========================================================
SELECT 'fornecedores' AS Tabela, COUNT(*) AS Total FROM fornecedores
UNION ALL
SELECT 'clientes', COUNT(*) FROM clientes
UNION ALL
SELECT 'usuarios', COUNT(*) FROM usuarios
UNION ALL
SELECT 'produtos', COUNT(*) FROM produtos
UNION ALL
SELECT 'vendas', COUNT(*) FROM vendas
UNION ALL
SELECT 'itens_venda', COUNT(*) FROM itens_venda
UNION ALL
SELECT 'compras', COUNT(*) FROM compras;