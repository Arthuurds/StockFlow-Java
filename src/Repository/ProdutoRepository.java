package Repository;

import DataBase.ConnectionFactory;
import Model.Fornecedor;
import Model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    public Produto salvar(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, descricao, categoria, preco, quantidade_estoque, fornecedor_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, produto.getCategoria());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setInt(5, produto.getQuantidadeEstoque());

            if (produto.getFornecedor() != null && produto.getFornecedor().getId() != null) {
                stmt.setLong(6, produto.getFornecedor().getId());
            } else {
                stmt.setNull(6, java.sql.Types.BIGINT);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getLong(1));
                }
            }
        }
        return produto;
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.*, f.nome as fornecedor_nome, f.cnpj as fornecedor_cnpj, f.email as fornecedor_email, f.telefone as fornecedor_telefone " +
                "FROM produtos p LEFT JOIN fornecedores f ON p.fornecedor_id = f.id";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }
        return produtos;
    }

    public Produto buscarPorId(Long id) throws SQLException {
        String sql = "SELECT p.*, f.nome as fornecedor_nome, f.cnpj as fornecedor_cnpj, f.email as fornecedor_email, f.telefone as fornecedor_telefone " +
                "FROM produtos p LEFT JOIN fornecedores f ON p.fornecedor_id = f.id WHERE p.id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }
        return null;
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, categoria = ?, preco = ?, quantidade_estoque = ?, fornecedor_id = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, produto.getCategoria());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setInt(5, produto.getQuantidadeEstoque());

            if (produto.getFornecedor() != null && produto.getFornecedor().getId() != null) {
                stmt.setLong(6, produto.getFornecedor().getId());
            } else {
                stmt.setNull(6, java.sql.Types.BIGINT);
            }

            stmt.setLong(7, produto.getId());

            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    // Método auxiliar para converter ResultSet em Objeto Produto
    private Produto mapearProduto(ResultSet rs) throws SQLException {
        Fornecedor fornecedor = null;
        long fornecedorId = rs.getLong("fornecedor_id");

        if (!rs.wasNull()) {
            fornecedor = new Fornecedor(
                    fornecedorId,
                    rs.getString("fornecedor_nome"),
                    rs.getString("fornecedor_cnpj"),
                    rs.getString("fornecedor_email"),
                    rs.getString("fornecedor_telefone")
            );
        }

        return new Produto(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getString("categoria"),
                rs.getBigDecimal("preco"),
                rs.getInt("quantidade_estoque"),
                fornecedor
        );
    }
}