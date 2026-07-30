package repository;

import model.Produto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProdutoRepository {
    Produto salvar(Produto produto) throws SQLException;
    List<Produto> listarTodos() throws SQLException;
    Produto buscarPorId(Long id) throws SQLException;
    void atualizar(Produto produto) throws SQLException;
    void deletar(Long id) throws SQLException;
    void atualizarEstoque(Long produtoId, int quantidade) throws SQLException;

    // --- MÉTODOS PARA TRANSAÇÃO (UTILIZADOS NO VENDA SERVICE) ---
    int buscarEstoqueAtual(Long produtoId, Connection conn) throws SQLException;
    void darBaixaEstoque(Long produtoId, Integer quantidade, Connection conn) throws SQLException;
}
