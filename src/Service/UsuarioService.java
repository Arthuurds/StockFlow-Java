package Service;

import Model.Usuario;
import Repository.UsuarioRepositoryImpl;
import Util.Password;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private final UsuarioRepositoryImpl usuarioRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    /**
     * Regra de Cadastro: Valida os campos, aplica a criptografia e salva o usuário.
     */
    public Usuario cadastrarUsuario(Usuario usuario) throws SQLException {
        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("O login é obrigatório.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        // Verifica se o login já está em uso
        if (usuarioRepository.buscarPorLogin(usuario.getLogin()) != null) {
            throw new IllegalArgumentException("Já existe um usuário com o login: " + usuario.getLogin());
        }

        // Criptografa a senha antes de enviar para a camada de persistência (Banco)
        String senhaCriptografada = Password.hashPassword(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return usuarioRepository.salvar(usuario);
    }

    /**
     * Regra de Login: Autentica o usuário comparando o Hash da senha.
     */
    public Usuario autenticar(String login, String senhaDigitada) throws SQLException {
        if (login == null || senhaDigitada == null) {
            throw new IllegalArgumentException("Login e senha são obrigatórios.");
        }

        Usuario usuario = usuarioRepository.buscarPorLogin(login);

        // Mensagem genérica para não revelar se o erro foi o login ou a senha (Boa Prática de Segurança)
        if (usuario == null || !Password.verificarSenha(senhaDigitada, usuario.getSenha())) {
            throw new IllegalArgumentException("Login ou senha inválidos.");
        }

        System.out.println("🔓 Usuário " + usuario.getLogin() + " autenticado com sucesso!");
        return usuario;
    }

    /**
     * Lista todos os usuários cadastrados no sistema.
     */
    public List<Usuario> listarTodos() throws SQLException {
        return usuarioRepository.listarTodos();
    }

    /**
     * Busca um usuário específico pelo seu ID.
     */
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
