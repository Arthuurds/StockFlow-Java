package com.stockflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 NOT FOUND - Registro não encontrado no banco
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409 CONFLICT - Tentativa de cadastrar CPF/CNPJ/Login duplicado
    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> tratarRegistroDuplicado(RegistroDuplicadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 400 BAD REQUEST - Validações de Negócio / Estoque / Documento
    @ExceptionHandler({
            EstoqueInsuficienteException.class,
            DocumentoInvalidoException.class,
            RegraDeNegocioException.class
    })
    public ResponseEntity<Map<String, Object>> tratarRegrasDeNegocio(RuntimeException ex) {
        return construirResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 500 INTERNAL SERVER ERROR - Erros inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErroGeral(Exception ex) {
        ex.printStackTrace();
        return construirResposta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno no servidor.");
    }

    private ResponseEntity<Map<String, Object>> construirResposta(HttpStatus status, String mensagem) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("timestamp", LocalDateTime.now());
        erro.put("status", status.value());
        erro.put("error", status.getReasonPhrase());
        erro.put("message", mensagem);
        return ResponseEntity.status(status).body(erro);
    }
}
