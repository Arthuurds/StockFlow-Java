package Model;

public class Fornecedor {

    private Long id;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;

    // Construtor padrão
    public Fornecedor() {
    }

    // Construtor sem ID (para novos cadastros)
    public Fornecedor(String nome, String cnpj, String email, String telefone) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
    }

    // Construtor completo (para mapeamento a partir do banco de dados)
    public Fornecedor(Long id, String nome, String cnpj, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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
        return String.format("Fornecedor [ID: %d | Nome: %s | CNPJ: %s | E-mail: %s | Tel: %s]",
                id, nome, cnpj, email, telefone);
    }
}
