package Repository;

import DataBase.ConnectionFactory;
import Model.Cliente;

import Model.ItemVenda;
import Model.Produto;
import Model.Usuario;
import Model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VendaRepository {

    /**
     * Salva uma venda e seus itens com controle de Transação JDBC (Commit / Rollback)
     */
    public Venda salvarTransacional(Venda venda) throws SQLException {
        String sqlVenda = "INSERT INTO vendas (cliente_id, usuario_id, data_venda, valor_total) VALUES (?, ?, ?, ?)";
        String sqlItem = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlEstoque = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";

        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Inicia a transação ACID

            // 1. Salvar Cabeçalho da Venda
            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                if (venda.getCliente() != null) {
                    stmtVenda.setLong(1, venda.getCliente().getId());
                } else {
                    stmtVenda.setNull(1, java.sql.Types.BIGINT);
                }

                if (venda.getUsuario() != null) {
                    stmtVenda.setLong(2, venda.getUsuario().getId());
                } else {
                    stmtVenda.setNull(2, java.sql.Types.BIGINT);
                }

                stmtVenda.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda()));
                stmtVenda.setBigDecimal(4, venda.getValorTotal());

                stmtVenda.executeUpdate();

                try (ResultSet rs = stmtVenda.getGeneratedKeys()) {
                    if (rs.next()) {
                        venda.setId(rs.getLong(1));
                    }
                }
            }

            // 2. Salvar Itens da Venda e Atualizar Estoque do Produto
            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                 PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {

                for (ItemVenda item : venda.getItens()) {
                    // Inserir Item
                    stmtItem.setLong(1, venda.getId());
                    stmtItem.setLong(2, item.getProduto().getId());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setBigDecimal(4, item.getPrecoUnitario());
                    stmtItem.setBigDecimal(5, item.getSubtotal());
                    stmtItem.addBatch();

                    // Decrementar Estoque
                    stmtEstoque.setInt(1, item.getQuantidade());
                    stmtEstoque.setLong(2, item.getProduto().getId());
                    stmtEstoque.addBatch();
                }

                stmtItem.executeBatch();
                stmtEstoque.executeBatch();
            }

            conn.commit(); // Confirmar transação
            return venda;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Desfazer alterações em caso de erro
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                ConnectionFactory.closeConnection(conn);
            }
        }
    }

    public List<Venda> listarTodas() throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT v.*, c.nome as cliente_nome, u.nome as usuario_nome " +
                "FROM vendas v " +
                "LEFT JOIN clientes c ON v.cliente_id = c.id " +
                "LEFT JOIN usuarios u ON v.usuario_id = u.id " +
                "ORDER BY v.data_venda DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = null;
                if (rs.getLong("cliente_id") != 0) {
                    cliente = new Cliente();
                    cliente.setId(rs.getLong("cliente_id"));
                    cliente.setNome(rs.getString("cliente_nome"));
                }

                Usuario usuario = null;
                if (rs.getLong("usuario_id") != 0) {
                    usuario = new Usuario();
                    usuario.setId(rs.getLong("usuario_id"));
                    usuario.setNome(rs.getString("usuario_nome"));
                }

                Venda venda = new Venda(
                        rs.getLong("id"),
                        cliente,
                        usuario,
                        rs.getTimestamp("data_venda").toLocalDateTime(),
                        rs.getBigDecimal("valor_total"),
                        new ArrayList<>()
                );
                vendas.add(venda);
            }
        }
        return vendas;
    }

    public Venda buscarPorId(Long id) throws SQLException {
        String sqlVenda = "SELECT v.*, c.nome as cliente_nome, u.nome as usuario_nome " +
                "FROM vendas v " +
                "LEFT JOIN clientes c ON v.cliente_id = c.id " +
                "LEFT JOIN usuarios u ON v.usuario_id = u.id " +
                "WHERE v.id = ?";

        String sqlItens = "SELECT iv.*, p.nome as produto_nome " +
                "FROM itens_venda iv " +
                "JOIN produtos p ON iv.produto_id = p.id " +
                "WHERE iv.venda_id = ?";

        Venda venda = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda)) {

            stmtVenda.setLong(1, id);

            try (ResultSet rs = stmtVenda.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = null;
                    if (rs.getLong("cliente_id") != 0) {
                        cliente = new Cliente();
                        cliente.setId(rs.getLong("cliente_id"));
                        cliente.setNome(rs.getString("cliente_nome"));
                    }

                    Usuario usuario = null;
                    if (rs.getLong("usuario_id") != 0) {
                        usuario = new Usuario();
                        usuario.setId(rs.getLong("usuario_id"));
                        usuario.setNome(rs.getString("usuario_nome"));
                    }

                    venda = new Venda(
                            rs.getLong("id"),
                            cliente,
                            usuario,
                            rs.getTimestamp("data_venda").toLocalDateTime(),
                            rs.getBigDecimal("valor_total"),
                            new ArrayList<>()
                    );
                }
            }

            if (venda != null) {
                try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                    stmtItens.setLong(1, venda.getId());

                    try (ResultSet rsItens = stmtItens.executeQuery()) {
                        while (rsItens.next()) {
                            Produto p = new Produto();
                            p.setId(rsItens.getLong("produto_id"));
                            p.setNome(rsItens.getString("produto_nome"));

                            ItemVenda item = new ItemVenda(
                                    rsItens.getLong("id"),
                                    venda,
                                    p,
                                    rsItens.getInt("quantidade"),
                                    rsItens.getBigDecimal("preco_unitario"),
                                    rsItens.getBigDecimal("subtotal")
                            );
                            venda.getItens().add(item);
                        }
                    }
                }
            }
        }
        return venda;
    }
}
