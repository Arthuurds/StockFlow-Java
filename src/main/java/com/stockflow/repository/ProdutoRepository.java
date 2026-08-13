package com.stockflow.repository;

import com.stockflow.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByCategoria(String categoria);

    List<Produto> findByQuantidadeEstoqueLessThan(Integer quantidadeEstoque);
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}

