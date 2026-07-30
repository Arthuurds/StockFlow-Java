package DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RelatorioVendaDTO {

    private Long vendaId;
    private String nomeCliente;
    private String nomeUsuario;
    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;

    public RelatorioVendaDTO(Long vendaId, String nomeCliente, String nomeUsuario, LocalDateTime dataVenda, BigDecimal valorTotal) {
        this.vendaId = vendaId;
        this.nomeCliente = nomeCliente;
        this.nomeUsuario = nomeUsuario;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
    }

    public Long getVendaId() { return vendaId; }
    public String getNomeCliente() { return nomeCliente; }
    public String getNomeUsuario() { return nomeUsuario; }
    public LocalDateTime getDataVenda() { return dataVenda; }
    public BigDecimal getValorTotal() { return valorTotal; }

    @Override
    public String toString() {
        return String.format("Venda #%d | Data: %s | Cliente: %s | Vendedor: %s | Total: R$ %.2f",
                vendaId, dataVenda, nomeCliente, nomeUsuario, valorTotal);
    }
}