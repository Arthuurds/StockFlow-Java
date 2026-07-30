-- 1. Criação do Banco de Dados
CREATE DATABASE IF NOT EXISTS stockflow_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE stockflow_db;

-- 2. Remoção de tabelas (na ordem correta de dependências) para reset limpo, se necessário
DROP TABLE IF EXISTS itens_venda;
DROP TABLE IF EXISTS compras;
DROP TABLE IF EXISTS vendas;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS fornecedores;

-- 3. Tabela de Fornecedores
CREATE TABLE fornecedores (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              nome VARCHAR(100) NOT NULL,
                              cnpj VARCHAR(20) UNIQUE NOT NULL,
                              email VARCHAR(100),
                              telefone VARCHAR(20)
) ENGINE=InnoDB;

-- 4. Tabela de Clientes
CREATE TABLE clientes (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          cpf_cnpj VARCHAR(20) UNIQUE NOT NULL,
                          email VARCHAR(100),
                          telefone VARCHAR(20)
) ENGINE=InnoDB;

-- 5. Tabela de Usuários
CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          login VARCHAR(50) UNIQUE NOT NULL,
                          senha VARCHAR(255) NOT NULL,
                          perfil VARCHAR(20) NOT NULL DEFAULT 'OPERADOR'
) ENGINE=InnoDB;

-- 6. Tabela de Produtos
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

-- 7. Tabela de Vendas
CREATE TABLE vendas (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        cliente_id BIGINT,
                        usuario_id BIGINT,
                        data_venda DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        valor_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                        CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                        CONSTRAINT fk_venda_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB;

-- 8. Tabela de Itens da Venda
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

-- 9. Tabela de Compras (Entradas de Estoque)
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
-- INSERTS INICIAIS DE TESTE
-- ========================================================

-- Usuário Padrão Admin (senha: admin123)
INSERT INTO usuarios (nome, login, senha, perfil)
VALUES ('Administrador System', 'admin', 'admin123', 'ADMIN');

-- Fornecedor Inicial
INSERT INTO fornecedores (nome, cnpj, email, telefone)
VALUES ('Tech Distribuidora Ltda', '12.345.678/0001-99', 'contato@techdist.com', '(11) 98888-7777');

-- Cliente Inicial
INSERT INTO clientes (nome, cpf_cnpj, email, telefone)
VALUES ('João Silva', '123.456.789-00', 'joao@email.com', '(11) 97777-6666');

-- Produto Inicial
INSERT INTO produtos (nome, descricao, categoria, preco, quantidade_estoque, fornecedor_id)
VALUES ('Teclado Mecânico RGB', 'Teclado Switch Blue ABNT2', 'Periféricos', 250.00, 15, 1);