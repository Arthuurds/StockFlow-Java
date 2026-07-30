package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private Long id;
    private Cliente cliente;
    private Usuario usuario;
    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;
    private List<ItemVenda> itens;

    // Construtor padrão
    public Venda() {
        this.itens = new ArrayList<>();
        this.valorTotal = BigDecimal.ZERO;
        this.dataVenda = LocalDateTime.now();
    }

    // Construtor sem ID (para criação de nova venda)
    public Venda(Cliente cliente, Usuario usuario) {
        this.cliente = cliente;
        this.usuario = usuario;
        this.dataVenda = LocalDateTime.now();
        this.valorTotal = BigDecimal.ZERO;
        this.itens = new ArrayList<>();
    }

    // Construtor completo (para registros buscados do banco)
    public Venda(Long id, Cliente cliente, Usuario usuario, LocalDateTime dataVenda, BigDecimal valorTotal, List<ItemVenda> itens) {
        this.id = id;
        this.cliente = cliente;
        this.usuario = usuario;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.itens = (itens != null) ? itens : new ArrayList<>();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    // Método utilitário para adicionar item e recalcular o total da venda
    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
        recalcularValorTotal();
    }

    public void recalcularValorTotal() {
        this.valorTotal = BigDecimal.ZERO;
        for (ItemVenda item : itens) {
            if (item.getSubtotal() != null) {
                this.valorTotal = this.valorTotal.add(item.getSubtotal());
            }
        }
    }

    @Override
    public String toString() {
        String nomeCliente = (cliente != null) ? cliente.getNome() : "Consumidor Final";
        String nomeUsuario = (usuario != null) ? usuario.getNome() : "N/A";
        return String.format("Venda [ID: %d | Data: %s | Cliente: %s | Usuário: %s | Total: R$ %.2f | Qtd Itens: %d]",
                id, dataVenda, nomeCliente, nomeUsuario, valorTotal, itens.size());
    }
}