package com.stockflow.service;

import com.stockflow.exception.RecursoNaoEncontradoException;
import com.stockflow.exception.RegraDeNegocioException;
import com.stockflow.model.Produto;
import com.stockflow.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProdutoService {

    // 1. O tipo da variável passa a ser a INTERFACE
    private final ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public List<Produto> listarTodos(){
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id){
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o Id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorCategoria(String categoria){
        return produtoRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome){
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public Produto salvar(Produto produto){
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraDeNegocioException("O preço do produto não pode ser nulo ou negativo!");
        }

        if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
            throw new RegraDeNegocioException("A quantidade em estoque não pode ser negativa!");
        }

        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto adicionarEstoque(Long produtoId, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new RegraDeNegocioException("A quantidade a ser adicionada deve ser maior que zero!");
        }

        Produto produto = buscarPorId(produtoId);
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);

        return produtoRepository.save(produto);
    }

    @Transactional
    public void deletar(Long id){
        if (!produtoRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Produto não encontrado para e exclusão!");
        }
        produtoRepository.deleteById(id);
    }
}
