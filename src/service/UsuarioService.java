package service;

import model.Usuario;
import repository.UsuarioRepository;
import repository.UsuarioRepositoryImpl;
import util.Password;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrarUsuario(Usuario usuario) throws SQLException {
        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("O login é obrigatório.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        if (usuarioRepository.buscarPorLogin(usuario.getLogin()) != null) {
            throw new IllegalArgumentException("Já existe um usuário com o login: " + usuario.getLogin());
        }

        String senhaCriptografada = Password.hashPassword(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return usuarioRepository.salvar(usuario);
    }

    public Usuario autenticar(String login, String senhaDigitada) throws SQLException {
        if (login == null || senhaDigitada == null) {
            throw new IllegalArgumentException("Login e senha são obrigatórios.");
        }

        Usuario usuario = usuarioRepository.buscarPorLogin(login);

        if (usuario == null || !Password.verificarSenha(senhaDigitada, usuario.getSenha())) {
            throw new IllegalArgumentException("Login ou senha inválidos.");
        }

        System.out.println("🔓 Usuário " + usuario.getLogin() + " autenticado com sucesso!");
        return usuario;
    }

    public List<Usuario> listarTodos() throws SQLException {
        return usuarioRepository.listarTodos();
    }

    public Usuario buscarPorId(Long id) throws IllegalArgumentException, SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para busca de usuário.");
        }

        Usuario usuario = usuarioRepository.buscarPorId(id);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado com o ID: " + id);
        }

        return usuario;
    }
}
