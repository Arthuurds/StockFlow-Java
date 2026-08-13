package com.stockflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();


    public Venda(Cliente cliente, Usuario usuario) {
        this.cliente = cliente;
        this.usuario = usuario;
        this.dataVenda = LocalDateTime.now();

    }



    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
        item.setVenda(this);
        item.calcularSubtotal();
        recalcularValorTotal();
    }

    public void removerItem(ItemVenda item) {
        this.itens.remove(item);
        item.setVenda(null);
        recalcularValorTotal();
    }

    @PrePersist
    @PreUpdate
    public void recalcularValorTotal() {
        if (this.dataVenda == null) {
            this.dataVenda = LocalDateTime.now();
        }

        this.valorTotal = BigDecimal.ZERO;
        if (this.itens != null) {
            for (ItemVenda item : itens) {
                item.calcularSubtotal();
                if (item.getSubtotal() != null) {
                    this.valorTotal = this.valorTotal.add(item.getSubtotal());
                }
            }
        }
    }
}