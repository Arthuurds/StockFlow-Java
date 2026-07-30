package repository;

import database.ConnectionFactory;
import model.Fornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorRepositoryImpl implements FornecedorRepository {

    @Override
    public Fornecedor salvar(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO fornecedores (nome, cnpj, email, telefone) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, fornecedor.getNomeJuridico());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getEmail());
            stmt.setString(4, fornecedor.getTelefone());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    fornecedor.setId(rs.getLong(1));
                }
            }
        }
        return fornecedor;
    }

    @Override
    public Fornecedor buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM fornecedores WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairFornecedor(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Fornecedor buscarPorCnpj(String cnpj) throws SQLException {
        String sql = "SELECT * FROM fornecedores WHERE cnpj = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairFornecedor(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Fornecedor> listarTodos() throws SQLException {
        String sql = "SELECT * FROM fornecedores";
        List<Fornecedor> fornecedores = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fornecedores.add(extrairFornecedor(rs));
            }
        }
        return fornecedores;
    }
    @Override
    public void atualizar(Fornecedor fornecedor) throws SQLException {
        String sql = "UPDATE fornecedores SET nome = ?, cnpj = ?, email = ?, telefone = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNomeJuridico());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getEmail());
            stmt.setString(4, fornecedor.getTelefone());
            stmt.setLong(5, fornecedor.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM fornecedores WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private Fornecedor extrairFornecedor(ResultSet rs) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getLong("id"));
        fornecedor.setNomeJuridico(rs.getString("nome_fantasia"));
        fornecedor.setCnpj(rs.getString("cnpj"));
        fornecedor.setEmail(rs.getString("email"));
        fornecedor.setTelefone(rs.getString("telefone"));
        return fornecedor;
    }
}
