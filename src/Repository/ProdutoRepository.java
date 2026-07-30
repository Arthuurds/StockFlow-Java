package Repository;

import Model.Produto;

import java.sql.SQLException;
import java.util.List;

public interface ProdutoRepository {
    Produto salvar(Produto produto) throws SQLException;
    Produto buscarPorId(Long id) throws SQLException;
    List<Produto> listarTodos() throws SQLException;

    void atualizar(Produto produto) throws SQLException;
    void atualizarEstoque(Long produtoId, int quantidade) throws SQLException;
    void deletar(Long id) throws SQLException;
}
