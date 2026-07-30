package Service;

import Model.Produto;
import Repository.ProdutoRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService() {
        this.produtoRepository = new ProdutoRepository();
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
        buscarPorId(produto.getId()); // Garante que o produto existe
        validarProduto(produto);
        produtoRepository.atualizar(produto);
    }

    public void deletar(Long id) throws IllegalArgumentException, SQLException {
        buscarPorId(id); // Valida existência
        produtoRepository.deletar(id);
    }

    public void darEntradaEstoque(Long produtoId, Integer quantidade) throws IllegalArgumentException, SQLException {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de entrada deve ser maior que zero.");
        }
        Produto produto = buscarPorId(produtoId);
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        produtoRepository.atualizar(produto);
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
