package com.stockflow.repository;


import com.stockflow.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> finfByClienteId(Long clienteId);
    List<Venda> findByUsuarioId(Long usuarioId);
    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
}