package com.stockflow.service;

import com.stockflow.exception.RecursoNaoEncontradoException;
import com.stockflow.exception.RegistroDuplicadoException;
import com.stockflow.model.Usuario;
import com.stockflow.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public  List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado com o Id: " + id));
    }

    @Transactional
    public Usuario salvar(Usuario usuario){
        usuarioRepository.findByLogin(usuario.getLogin())
                .ifPresent(existente -> {
                    if (!existente.getId().equals(usuario.getId())) {
                        throw new RegistroDuplicadoException("Já existe um usuário cadastrado com este login!");
                    }
                });

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }


    @Transactional
    public void deletar(Long id){
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuario não encontrado para exclusão!");
        }
        usuarioRepository.deleteById(id);
    }
}
