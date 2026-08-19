package com.stockflow.exception;

public class EstoqueInsuficienteException extends RuntimeException{
    public EstoqueInsuficienteException (String mensage){
        super(mensage);
    }
}
