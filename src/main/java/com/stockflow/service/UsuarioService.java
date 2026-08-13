package com.stockflow.service;

import com.stockflow.model.Usuario;
import com.stockflow.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public  List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com o Id: " + id));
    }

    @Transactional
    public Usuario salvar(Usuario usuario){
        usuarioRepository.findByLogin(usuario.getLogin())
                .ifPresent(existente -> {
                    if (!existente.getId().equals(usuario.getId())) {
                        throw new RuntimeException("Já existe um usuário cadastrado com este login!");
                    }
                });

        return usuarioRepository.save(usuario);
    }


    @Transactional
    public void deletar(Long id){
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario nãp encontrado para exclusão!");
        }
        usuarioRepository.deleteById(id);
    }
}
