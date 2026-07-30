package Repository;

import Model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario) throws SQLException;
    Usuario buscarPorId(Long id) throws SQLException;
    Usuario buscarPorLogin(String login) throws SQLException;
    List<Usuario> listarTodos() throws SQLException;
}
