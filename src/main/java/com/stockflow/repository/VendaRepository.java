package com.stockflow.repository;

import com.stockflow.DTO.RelatorioVendaDTO;
import com.stockflow.model.ItemVenda;
import com.stockflow.model.Venda;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository {
    List<Venda> listarTodas() throws SQLException;
    Venda buscarPorId(Long id) throws SQLException;
    List<RelatorioVendaDTO> buscarVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws SQLException;

    // Métodos para suporte a transações gerenciadas pelo VendaService
    long salvar(Venda venda, Connection conn) throws SQLException;
    void salvarItem(long vendaId, ItemVenda item, Connection conn) throws SQLException;
}