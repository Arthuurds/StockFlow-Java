package com.stockflow.exception;

public class EstoqueInsuficienteException extends Exception{
    public EstoqueInsuficienteException (String mensage){
        super(mensage);
    }
}
