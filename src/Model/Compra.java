package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Compra {

    private Long id;
    private Produto produto;
    private Fornecedor fornecedor;
    private Integer quantidade;
    private BigDecimal precoCusto;
    private BigDecimal valorTotal;
    private LocalDateTime dataCompra;

    // Construtor padrão
    public Compra() {
        this.dataCompra = LocalDateTime.now();
    }

    // Construtor para registrar nova compra
    public Compra(Produto produto, Fornecedor fornecedor, Integer quantidade, BigDecimal precoCusto) {
        this.produto = produto;
        this.fornecedor = fornecedor;
        this.quantidade = quantidade;
        this.precoCusto = precoCusto;
        this.dataCompra = LocalDateTime.now();
        this.calcularValorTotal();
    }

    // Construtor completo (para mapeamento a partir do banco de dados)
    public Compra(Long id, Produto produto, Fornecedor fornecedor, Integer quantidade, BigDecimal precoCusto, BigDecimal valorTotal, LocalDateTime dataCompra) {
        this.id = id;
        this.produto = produto;
        this.fornecedor = fornecedor;
        this.quantidade = quantidade;
        this.precoCusto = precoCusto;
        this.valorTotal = (valorTotal != null) ? valorTotal : precoCusto.multiply(BigDecimal.valueOf(quantidade));
        this.dataCompra = dataCompra;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        this.calcularValorTotal();
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
        this.calcularValorTotal();
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    private void calcularValorTotal() {
        if (this.precoCusto != null && this.quantidade != null) {
            this.valorTotal = this.precoCusto.multiply(BigDecimal.valueOf(this.quantidade));
        }
    }

    @Override
    public String toString() {
        String nomeProduto = (produto != null) ? produto.getNome() : "N/A";
        String nomeFornecedor = (fornecedor != null) ? fornecedor.getNome() : "N/A";
        return String.format("Compra [ID: %d | Data: %s | Produto: %s | Fornecedor: %s | Qtd: %d | Total: R$ %.2f]",
                id, dataCompra, nomeProduto, nomeFornecedor, quantidade, valorTotal);
    }
}
