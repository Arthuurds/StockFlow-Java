package com.stockflow.service;

import com.stockflow.exception.EstoqueInsuficienteException;
import com.stockflow.exception.RecursoNaoEncontradoException;
import com.stockflow.exception.RegraDeNegocioException;
import com.stockflow.model.ItemVenda;
import com.stockflow.model.Produto;
import com.stockflow.model.Venda;
import com.stockflow.repository.ClienteRepository;
import com.stockflow.repository.ProdutoRepository;
import com.stockflow.repository.UsuarioRepository;
import com.stockflow.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private VendaService vendaService;

    private Produto criarProduto(Long id, String preco, int estoque) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome("Produto " + id);
        produto.setPreco(new BigDecimal(preco));
        produto.setQuantidadeEstoque(estoque);
        return produto;
    }

    private ItemVenda criarItem(Long produtoId, Integer quantidade) {
        Produto referencia = new Produto();
        referencia.setId(produtoId);
        ItemVenda item = new ItemVenda();
        item.setProduto(referencia);
        item.setQuantidade(quantidade);
        return item;
    }

    private Venda criarVenda(ItemVenda... itens) {
        Venda venda = new Venda();
        venda.getItens().addAll(List.of(itens));
        return venda;
    }

    @Test
    void deveRealizarVendaBaixarEstoqueECalcularTotal() {
        Produto caneta = criarProduto(1L, "2.50", 10);
        Produto caderno = criarProduto(2L, "15.00", 5);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(caneta));
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(caderno));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        Venda venda = vendaService.realizarVenda(criarVenda(criarItem(1L, 4), criarItem(2L, 2)));

        assertEquals(6, caneta.getQuantidadeEstoque());
        assertEquals(3, caderno.getQuantidadeEstoque());
        assertEquals(0, new BigDecimal("40.00").compareTo(venda.getValorTotal()));
        assertEquals(0, new BigDecimal("2.50").compareTo(venda.getItens().get(0).getPrecoUnitario()));
        assertNotNull(venda.getDataVenda());
        verify(vendaRepository).save(venda);
    }

    @Test
    void deveRecusarVendaQuandoEstoqueForInsuficiente() {
        Produto caneta = criarProduto(1L, "2.50", 2);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(caneta));

        assertThrows(EstoqueInsuficienteException.class,
                () -> vendaService.realizarVenda(criarVenda(criarItem(1L, 5))));

        assertEquals(2, caneta.getQuantidadeEstoque());
        verify(vendaRepository, never()).save(any());
    }

    @Test
    void deveRecusarVendaSemItens() {
        assertThrows(RegraDeNegocioException.class, () -> vendaService.realizarVenda(new Venda()));
        verify(vendaRepository, never()).save(any());
    }

    @Test
    void deveRecusarItemComQuantidadeZeroOuNegativa() {
        assertThrows(RegraDeNegocioException.class,
                () -> vendaService.realizarVenda(criarVenda(criarItem(1L, 0))));
        assertThrows(RegraDeNegocioException.class,
                () -> vendaService.realizarVenda(criarVenda(criarItem(1L, -3))));
        verify(vendaRepository, never()).save(any());
    }

    @Test
    void deveRecusarVendaDeProdutoInexistente() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> vendaService.realizarVenda(criarVenda(criarItem(99L, 1))));
        verify(vendaRepository, never()).save(any());
    }
}
