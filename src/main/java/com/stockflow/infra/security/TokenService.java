package com.stockflow.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.stockflow.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Lê a senha secreta do servidor configurada no application.properties
    @Value("${api.security.token.secret:minha-chave-secreta-super-segura-stockflow}")
    private String secret;

    // 1. Método para GERAR o token na hora do Login
    public String gerarToken(Usuario usuario) {
        try {
            // Algoritmo de assinatura que combina o token com a nossa chave secreta
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("stockflow-api")           // Quem é o emissor do token
                    .withSubject(usuario.getLogin())       // Quem é o dono do token (o login dele)
                    .withExpiresAt(gerarDataExpiracao())   // Quando o token vai expirar
                    .sign(algoritmo);                      // Assina digitalmente o token
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // 2. Método para VALIDAR o token a cada requisição enviada
    public String validarToken(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer("stockflow-api")
                    .build()
                    .verify(tokenJWT)  // Descriptografa e checa validade/assinatura
                    .getSubject();     // Retorna o 'login' do usuário contido no token
        } catch (JWTVerificationException exception) {
            return ""; // Retorna string vazia se o token for inválido, expirado ou adulterado
        }
    }

    // Define que o token vai expirar em 2 horas (fuso de Brasília -03:00)
    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
