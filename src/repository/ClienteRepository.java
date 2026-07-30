package repository;

import model.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteRepository {

    Cliente salvar(Cliente cliente) throws SQLException;
    Cliente buscarPorId(Long id) throws SQLException;
    Cliente buscarPorCpfCnpj(String cpfCnpj) throws SQLException;
    List<Cliente> listarTodos() throws SQLException;
    void atualizar(Cliente cliente) throws SQLException;
    void deletar(Long id) throws SQLException;
}
