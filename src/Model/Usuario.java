package Model;

public class Usuario {

    private Long id;
    private String nome;
    private String login;
    private String senha;
    private String perfil; // Ex: "ADMIN", "OPERADOR"

    // Construtor padrão
    public Usuario() {
    }

    // Construtor sem ID (para novos cadastros)
    public Usuario(String nome, String login, String senha, String perfil) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    // Construtor completo (para carregamento do banco de dados)
    public Usuario(Long id, String nome, String login, String senha, String perfil) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public String toString() {
        return String.format("Usuario [ID: %d | Nome: %s | Login: %s | Perfil: %s]",
                id, nome, login, perfil);
    }
}
