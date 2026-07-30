package Service;

import Model.Usuario;
import Repository.UsuarioRepository;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }

    public Usuario salvar(Usuario usuario) throws IllegalArgumentException, SQLException {
        validarUsuario(usuario);

        Usuario existente = usuarioRepository.buscarPorLogin(usuario.getLogin());
        if (existente != null) {
            throw new IllegalArgumentException("Login já está em uso por outro usuário.");
        }
        return usuarioRepository.salvar(usuario);
    }

    public List<Usuario> listarTodos() throws SQLException {
        return usuarioRepository.listarTodos();
    }

    public Usuario autenticar(String login, String senha) throws IllegalArgumentException, SQLException {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        if (usuario == null || !usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Login ou senha incorretos.");
        }
        return usuario;
    }

    public Usuario buscarPorId(Long id) throws IllegalArgumentException, SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do usuário é inválido.");
        }
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        return usuario;
    }

    public void deletar(Long id) throws IllegalArgumentException, SQLException {
        buscarPorId(id);
        usuarioRepository.deletar(id);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do usuário é obrigatório.");
        }
        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("Login é obrigatório.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().trim().length() < 4) {
            throw new IllegalArgumentException("A senha deve possuir no mínimo 4 caracteres.");
        }
    }
}
