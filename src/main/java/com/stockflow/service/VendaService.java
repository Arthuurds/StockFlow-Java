package com.stockflow.service;

import com.stockflow.DTO.RelatorioVendaDTO;
import com.stockflow.exception.EstoqueInsuficienteException;
import com.stockflow.model.ItemVenda;
import com.stockflow.model.Venda;
import com.stockflow.repository.ProdutoRepository;
import com.stockflow.repository.ProdutoRepositoryImpl;
import com.stockflow.repository.VendaRepository;
import com.stockflow.repository.VendaRepositoryImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService() {
        this.vendaRepository = new VendaRepositoryImpl();
        this.produtoRepository = new ProdutoRepositoryImpl();
    }


    public boolean realizarVenda(Venda venda) throws EstoqueInsuficienteException, SQLException {

        if (venda == null || venda.getCliente() == null || venda.getUsuario() == null) {
            throw new IllegalArgumentException("Venda, cliente e usuário responsável são obrigatórios.");
        }

        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            for (ItemVenda item : venda.getItens()) {
                int estoqueAtual = produtoRepository.buscarEstoqueAtual(item.getProduto().getId(), conn);

                if (item.getQuantidade() > estoqueAtual) {
                    throw new EstoqueInsuficienteException(
                            "Estoque insuficiente para o produto: " + item.getProduto().getNome() +
                                    ". Disponível: " + estoqueAtual + ", Solicitado: " + item.getQuantidade()
                    );
                }

                produtoRepository.darBaixaEstoque(item.getProduto().getId(), item.getQuantidade(), conn);
            }

            long vendaId = vendaRepository.salvar(venda, conn);
            venda.setId(vendaId);

            for (ItemVenda item : venda.getItens()) {
                vendaRepository.salvarItem(vendaId, item, conn);
            }

            conn.commit();
            System.out.println("✅ Venda #" + vendaId + " realizada e estoque atualizado com sucesso!");
            return true;

        } catch (EstoqueInsuficienteException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            System.err.println("❌ Erro ao processar venda no banco de dados. Transação cancelada.");
            throw e;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<RelatorioVendaDTO> gerarRelatorioPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws SQLException {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas inicial e final são obrigatórias.");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final.");
        }

        return vendaRepository.buscarVendasPorPeriodo(inicio, fim);
    }

    public List<Venda> listarTodas() throws SQLException {
        return vendaRepository.listarTodas();
    }

    public Venda buscarPorId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de venda inválido.");
        }
        Venda venda = vendaRepository.buscarPorId(id);
        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada com o ID: " + id);
        }
        return venda;
    }
}
