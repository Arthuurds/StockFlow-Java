package Repository;

import DTO.RelatorioVendaDTO;
import DataBase.ConnectionFactory;
import Model.Cliente;
import Model.Usuario;
import Model.Venda;

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
}