package model;

import java.math.BigDecimal;

public class Produto {

    private Long id;
    private String nome;
    private String descricao;
    private String categoria;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
    private Fornecedor fornecedor;

    // Construtor padrão (necessário para instanciação reflexiva ou construção progressiva)
    public Produto() {
    }

    // Construtor completo (sem ID - para novos cadastros)
    public Produto(String nome, String descricao, String categoria, BigDecimal preco, Integer quantidadeEstoque, Fornecedor fornecedor) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.fornecedor = fornecedor;
    }

    // Construtor completo (com ID - para consultas/atualizações)
    public Produto(Long id, String nome, String descricao, String categoria, BigDecimal preco, Integer quantidadeEstoque, Fornecedor fornecedor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.fornecedor = fornecedor;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        String nomeFornecedor = (fornecedor != null) ? fornecedor.getNomeJuridico() : "N/A";
        return String.format("Produto [ID: %d | Nome: %s | Categoria: %s | Preço: R$ %.2f | Estoque: %d | Fornecedor: %s]",
                id, nome, categoria, preco, quantidadeEstoque, nomeFornecedor);
    }
}
