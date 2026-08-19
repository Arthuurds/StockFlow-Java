package com.stockflow.service;

import com.stockflow.exception.RecursoNaoEncontradoException;
import com.stockflow.exception.RegistroDuplicadoException;
import com.stockflow.model.Cliente;
import com.stockflow.model.Fornecedor;
import com.stockflow.repository.ClienteRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.util.ClassUtils.ifPresent;


@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o Id: " + id));
    }


    @Transactional
    public Cliente salvar(Cliente cliente){
        clienteRepository.findByCpfCnpj(cliente.getCpfCnpj())
                .ifPresent(existente -> {
                    if (!existente.getId().equals(cliente.getId())) {
                        throw new RegistroDuplicadoException("Já existe outro fornecedor cadastrado com este CNPJ!");
                    }
                });
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado para a exclusão");
        }
        clienteRepository.deleteById(id);
    }
}