package service;

import model.Produto;
import repository.ProdutoRepository;
import repository.ProdutoRepositoryImpl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProdutoService {

    // 1. O tipo da variável passa a ser a INTERFACE
    private final ProdutoRepository produtoRepository;

    public ProdutoService() {
        this.produtoRepository = new ProdutoRepositoryImpl();
    }

    // Construtor flexível para Injeção de Dependência em testes unitários
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto salvar(Produto produto) throws IllegalArgumentException, SQLException {
        validarProduto(produto);
        return produtoRepository.salvar(produto);
    }

    public List<Produto> listarTodos() throws SQLException {
        return produtoRepository.listarTodos();
    }

    public Produto buscarPorId(Long id) throws IllegalArgumentException, SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para busca.");
        }
        Produto produto = produtoRepository.buscarPorId(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com o ID: " + id);
        }
        return produto;
    }

    public void atualizar(Produto produto) throws IllegalArgumentException, SQLException {
        if (produto.getId() == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório para atualização.");
        }
        buscarPorId(produto.getId()); // Garante que o produto existe antes de alterar
        validarProduto(produto);
        produtoRepository.atualizar(produto);
    }

    public void deletar(Long id) throws IllegalArgumentException, SQLException {
        buscarPorId(id); // Valida existência
        produtoRepository.deletar(id);
    }

    // === MOVIMENTAÇÕES DE ESTOQUE ===

    /**
     * Atualiza o estoque diretamente no banco.
     * Aceita valores positivos (soma) ou negativos (subtração com validação).
     */
    public void atualizarEstoque(Long produtoId, int quantidade) throws IllegalArgumentException, SQLException {
        if (produtoId == null || produtoId <= 0) {
            throw new IllegalArgumentException("ID de produto inválido para atualização de estoque.");
        }

        Produto produto = buscarPorId(produtoId);

        // Se for uma baixa de estoque (quantidade negativa), valida se há saldo suficiente
        if (quantidade < 0) {
            int quantidadeParaDescontar = Math.abs(quantidade);
            if (produto.getQuantidadeEstoque() < quantidadeParaDescontar) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente! Estoque atual: " + produto.getQuantidadeEstoque() +
                                " | Tentativa de baixa: " + quantidadeParaDescontar
                );
            }
        }

        produtoRepository.atualizarEstoque(produtoId, quantidade);
    }

    /**
     * Adiciona unidades ao estoque existente.
     */
    public void darEntradaEstoque(Long produtoId, Integer quantidade) throws IllegalArgumentException, SQLException {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de entrada deve ser maior que zero.");
        }
        atualizarEstoque(produtoId, quantidade);
    }

    /**
     * Remove unidades do estoque existente com segurança.
     */
    public void darBaixaEstoque(Long produtoId, Integer quantidade) throws IllegalArgumentException, SQLException {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de baixa deve ser maior que zero.");
        }
        atualizarEstoque(produtoId, -quantidade);
    }

    private void validarProduto(Produto produto) throws IllegalArgumentException {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
    }
}
