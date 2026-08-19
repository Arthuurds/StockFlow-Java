package com.stockflow.exception;

public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
