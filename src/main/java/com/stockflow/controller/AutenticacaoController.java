package com.stockflow.controller;

import com.stockflow.dto.DadosAutenticacao;
import com.stockflow.dto.DadosTokenJWT;
import com.stockflow.model.Usuario;
import com.stockflow.infra.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody DadosAutenticacao dados) {
        // 1. Converte o DTO para o objeto de autenticação do Spring
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.getLogin(), dados.getSenha());

        // 2. O Spring Security valida a senha criptografada no banco de dados
        var authentication = manager.authenticate(authenticationToken);

        // 3. Gera o Token JWT para o usuário autenticado
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // 4. Retorna o Token no corpo da resposta
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}