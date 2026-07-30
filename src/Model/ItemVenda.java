package Model;

import java.math.BigDecimal;

public class ItemVenda {

    private Long id;
    private Venda venda;
    private Produto produto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    // Construtor padrão
    public ItemVenda() {
    }

    // Construtor para inclusão de item antes de salvar a venda
    public ItemVenda(Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.calcularSubtotal();
    }

    // Construtor completo (para mapeamento a partir do banco de dados)
    public ItemVenda(Long id, Venda venda, Produto produto, Integer quantidade, BigDecimal precoUnitario, BigDecimal subtotal) {
        this.id = id;
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = (subtotal != null) ? subtotal : precoUnitario.multiply(new BigDecimal(quantidade));
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        this.calcularSubtotal();
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        this.calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    // Método privado auxiliar para manter o subtotal sempre consistente
    private void calcularSubtotal() {
        if (this.precoUnitario != null && this.quantidade != null) {
            this.subtotal = this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
        }
    }

    @Override
    public String toString() {
        String nomeProduto = (produto != null) ? produto.getNome() : "N/A";
        return String.format("ItemVenda [ID: %d | Produto: %s | Qtd: %d | Preço Un.: R$ %.2f | Subtotal: R$ %.2f]",
                id, nomeProduto, quantidade, precoUnitario, subtotal);
    }
}
