package com.stockflow.service;

import com.stockflow.model.Fornecedor;
import com.stockflow.repository.FornecedorRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    @Transactional(readOnly = true)
    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Fornecedor buscarPorId(Long id){
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com esse Id: " + id));
    }

    @Transactional
    public Fornecedor salvar(Fornecedor fornecedor){
        fornecedorRepository.findByCnpj(fornecedor.getCnpj())
                .ifPresent(existente -> {
                    if (!existente.getId().equals(fornecedor.getId())) {
                        throw new RuntimeException("Já existe outro fornecedor cadastrado com este CNPJ!");
                    }
                });
        return fornecedorRepository.save(fornecedor);
    }

    @Transactional
    public void deletar(Long id){
        if (!fornecedorRepository.existsById(id)) {
            throw new RuntimeException("Forncedor não encontrado para exclusão!");
        }
        fornecedorRepository.deleteById(id);
    }
}