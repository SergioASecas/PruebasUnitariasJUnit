package com.example.demo;

public class DineroInsuficienteException extends Exception {

    private String error;

    public DineroInsuficienteException(String mensaje, error error){
        super(mensaje);
        this.error = error.getCode();
    }

}
