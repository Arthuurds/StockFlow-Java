package repository;

import DTO.RelatorioVendaDTO;
import database.ConnectionFactory;
import model.Cliente;
import model.ItemVenda;
import model.Usuario;
import model.Venda;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaRepositoryImpl implements VendaRepository {

    @Override
    public List<Venda> listarTodas() throws SQLException {
        String sql = "SELECT id, cliente_id, usuario_id, data_venda, valor_total FROM vendas ORDER BY data_venda DESC";
        List<Venda> vendas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getLong("id"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("cliente_id"));
                venda.setCliente(cliente);

                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("usuario_id"));
                venda.setUsuario(usuario);

                Timestamp timestamp = rs.getTimestamp("data_venda");
                if (timestamp != null) {
                    venda.setDataVenda(timestamp.toLocalDateTime());
                }

                venda.setValorTotal(rs.getBigDecimal("valor_total"));
                vendas.add(venda);
            }
        }
        return vendas;
    }

    @Override
    public Venda buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, cliente_id, usuario_id, data_venda, valor_total FROM vendas WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Venda venda = new Venda();
                    venda.setId(rs.getLong("id"));

                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getLong("cliente_id"));
                    venda.setCliente(cliente);

                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("usuario_id"));
                    venda.setUsuario(usuario);

                    Timestamp timestamp = rs.getTimestamp("data_venda");
                    if (timestamp != null) {
                        venda.setDataVenda(timestamp.toLocalDateTime());
                    }

                    venda.setValorTotal(rs.getBigDecimal("valor_total"));
                    return venda;
                }
            }
        }
        return null;
    }

    @Override
    public List<RelatorioVendaDTO> buscarVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws SQLException {
        String sql = "SELECT v.id, c.nome AS cliente_nome, u.login AS usuario_login, v.data_venda, v.valor_total " +
                "FROM vendas v " +
                "JOIN clientes c ON v.cliente_id = c.id " +
                "JOIN usuarios u ON v.usuario_id = u.id " +
                "WHERE v.data_venda BETWEEN ? AND ? " +
                "ORDER BY v.data_venda DESC";

        List<RelatorioVendaDTO> relatorio = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(inicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    relatorio.add(new RelatorioVendaDTO(
                            rs.getLong("id"),
                            rs.getString("cliente_nome"),
                            rs.getString("usuario_login"),
                            rs.getTimestamp("data_venda").toLocalDateTime(),
                            rs.getBigDecimal("valor_total")
                    ));
                }
            }
        }
        return relatorio;
    }

    @Override
    public long salvar(Venda venda, Connection conn) throws SQLException {
        String sql = "INSERT INTO vendas (cliente_id, usuario_id, valor_total) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, venda.getCliente().getId());
            stmt.setLong(2, venda.getUsuario().getId());
            stmt.setBigDecimal(3, venda.getValorTotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("Falha ao obter o ID da venda gerada.");
                }
            }
        }
    }

    @Override
    public void salvarItem(long vendaId, ItemVenda item, Connection conn) throws SQLException {
        String sql = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, vendaId);
            stmt.setLong(2, item.getProduto().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setBigDecimal(4, item.getPrecoUnitario());
            stmt.setBigDecimal(5, item.getSubtotal());
            stmt.executeUpdate();
        }
    }
}