package com.stockflow.service;

import com.stockflow.model.Cliente;
import com.stockflow.repository.ClienteRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos(){
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o Id: "+ id));
    }


    @Transactional
    public Cliente salvar(Cliente cliente){
        if (cliente.getId() == null && clienteRepository.existsByCpfCnpj(cliente.getCpfCnpj())) {
            throw new RuntimeException("Já existe um cliente cadastrado com esse CPF/CNPJ");
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long id){
        if (!clienteRepository.existsById(id)){
            throw new RuntimeException("Cliente não encontrado para a exclusão");
        }
        clienteRepository.deleteById(id);
    }
}