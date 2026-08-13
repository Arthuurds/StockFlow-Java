package com.stockflow.service;

import com.stockflow.model.*;
import com.stockflow.repository.ClienteRepository;
import com.stockflow.repository.ProdutoRepository;
import com.stockflow.repository.UsuarioRepository;
import com.stockflow.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Venda> listarTodas(){
        return vendaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Venda buscarPorId(Long id){
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com o ID: " + id));
    }

    @Transactional
    public Venda realizarVenda(Venda venda) {

        // 1. Validação de itens
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new RuntimeException("Uma venda não pode ser realizada sem itens!");
        }

        // 2. Busca e reanexa o Cliente cadastrado no banco (Evita Erro 1996)
        if (venda.getCliente() != null && venda.getCliente().getId() != null) {
            Cliente cliente = clienteRepository.findById(venda.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + venda.getCliente().getId()));
            venda.setCliente(cliente);
        }

        // 3. Busca e reanexa o Usuário cadastrado no banco (Evita Erro 1996)
        if (venda.getUsuario() != null && venda.getUsuario().getId() != null) {
            Usuario usuario = usuarioRepository.findById(venda.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + venda.getUsuario().getId()));
            venda.setUsuario(usuario);
        }

        BigDecimal valorTotalVenda = BigDecimal.ZERO;

        // 4. Processa cada item, estoque e vincula as chaves
        for (ItemVenda item : venda.getItens()) {

            item.setVenda(venda);

            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto ID " + item.getProduto().getId() + " não encontrado!"));

            if (produto.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Quantidade de estoque insuficiente para o produto: " + produto.getNome() +
                        " | Solicitado: " + item.getQuantidade() + " | Em estoque: " + produto.getQuantidadeEstoque());
            }

            // Atualiza estoque do produto
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
            produtoRepository.save(produto);

            // Congela preço e associa o produto gerenciado
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());

            // Soma o subtotal e calcula o item
            item.calcularSubtotal();
            valorTotalVenda = valorTotalVenda.add(item.getSubtotal());
        }

        // 5. Define valor total e data
        venda.setValorTotal(valorTotalVenda);
        if (venda.getDataVenda() == null) {
            venda.setDataVenda(LocalDateTime.now());
        }

        // 6. Salva a venda e em cascata os seus itens
        return vendaRepository.save(venda);
    }
}
