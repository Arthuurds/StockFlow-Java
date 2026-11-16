package com.stockflow.exception;

public class DocumentoInvalidoException extends RuntimeException {

    public DocumentoInvalidoException(String mensage){
        super(mensage);
    }
}
