package Model;

public class Cliente {

    private Long id;
    private String nome;
    private String cpfCnpj;
    private String email;
    private String telefone;

    // Construtor padrão
    public Cliente() {
    }

    // Construtor sem ID (para novos cadastros)
    public Cliente(String nome, String cpfCnpj, String email, String telefone) {
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.telefone = telefone;
    }

    // Construtor completo (para carregamento do banco de dados)
    public Cliente(Long id, String nome, String cpfCnpj, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.telefone = telefone;
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

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return String.format("Cliente [ID: %d | Nome: %s | CPF/CNPJ: %s | E-mail: %s | Tel: %s]",
                id, nome, cpfCnpj, email, telefone);
    }
}