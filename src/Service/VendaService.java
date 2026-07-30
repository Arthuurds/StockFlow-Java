package Service;

import Model.ItemVenda;
import Model.Produto;
import Model.Venda;
import Repository.ProdutoRepository;
import Repository.VendaRepository;

import java.sql.SQLException;
import java.util.List;

public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService() {
        this.vendaRepository = new VendaRepository();
        this.produtoRepository = new ProdutoRepository();
    }

    /**
     * Regra de Negócio principal de Venda:
     * 1. Valida existência de itens
     * 2. Verifica estoque de cada produto individualmente
     * 3. Registra a venda e decrementa o estoque de forma atômica
     */
    public Venda realizarVenda(Venda venda) throws IllegalArgumentException, SQLException {
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("A venda deve possuir pelo menos um item.");
        }

        for (ItemVenda item : venda.getItens()) {
            Produto produto = produtoRepository.buscarPorId(item.getProduto().getId());

            if (produto == null) {
                throw new IllegalArgumentException("Produto ID " + item.getProduto().getId() + " não existe.");
            }

            if (produto.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome() +
                        ". Disponível: " + produto.getQuantidadeEstoque() + ", Solicitado: " + item.getQuantidade());
            }

            // Atualiza preço unitário vigente no momento da venda
            item.setPrecoUnitario(produto.getPreco());
        }

        venda.recalcularValorTotal();
        return vendaRepository.salvarTransacional(venda);
    }

    public List<Venda> listarTodas() throws SQLException {
        return vendaRepository.listarTodas();
    }

    public Venda buscarPorId(Long id) throws IllegalArgumentException, SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de venda inválido.");
        }
        Venda venda = vendaRepository.buscarPorId(id);
        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada.");
        }
        return venda;
    }
}
