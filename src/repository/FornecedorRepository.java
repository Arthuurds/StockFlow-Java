package repository;

import model.Fornecedor;
import java.sql.SQLException;
import java.util.List;

public interface FornecedorRepository {
    Fornecedor salvar(Fornecedor fornecedor) throws SQLException;
    Fornecedor buscarPorId(Long id) throws SQLException;
    Fornecedor buscarPorCnpj(String cnpj) throws SQLException;
    List<Fornecedor> listarTodos() throws SQLException;
    void atualizar(Fornecedor fornecedor) throws SQLException;
    void deletar(Long id) throws SQLException;
}