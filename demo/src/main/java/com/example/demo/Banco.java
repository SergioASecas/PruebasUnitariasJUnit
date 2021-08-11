package com.example.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {
    
    private String nombre;
    private Cuenta cuenta;
    List<Cuenta> cuentas = new ArrayList<>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void transferir(Cuenta cuenta2, Cuenta cuenta1, BigDecimal monto){
       BigDecimal saldoCuenta1 = cuenta1.getSaldo().add(monto);
       cuenta1.setSaldo(saldoCuenta1);
       BigDecimal saldoCuenta2 = cuenta2.getSaldo().subtract(monto);
       cuenta2.setSaldo(saldoCuenta2);

    }

    public void addCuenta(Cuenta cuenta){
        this.cuenta = cuenta;      
        cuentas.add(cuenta);
    }

    public List<Cuenta> getCuentas(){
        return cuentas;
    }

    @Override
    public String toString() {
        return "Banco [cuenta=" + cuenta + ", nombre=" + nombre + "]";
    }

    

    
}
