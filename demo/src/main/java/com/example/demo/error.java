package com.example.demo;

public enum error {
    
    LACUENTAENCERO("Fondos insuficientes");

    private String codigo;

    private error(String codigo) {
        this.codigo = codigo;
    }  

    public String getCode(){
        return codigo;
    }

    
}
