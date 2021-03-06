package com.example.demo;

import java.math.BigDecimal;

public class Cuenta {

    public String persona;
    public BigDecimal saldo;
    public Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto) throws DineroInsuficienteException {
        Double resta = getSaldo().doubleValue() - monto.doubleValue();
        if (resta < 0) {
            throw new DineroInsuficienteException("No tiene dinero en la cuenta",error.LACUENTAENCERO);
        } else {
            this.saldo = this.saldo.subtract(monto);
        }        
    }

    public void credito(BigDecimal monto) {
        this.saldo = this.saldo.add(monto);
    }    

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((persona == null) ? 0 : persona.hashCode());
        result = prime * result + ((saldo == null) ? 0 : saldo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cuenta other = (Cuenta) obj;
        if (persona == null) {
            if (other.persona != null)
                return false;
        } else if (!persona.equals(other.persona))
            return false;
        if (saldo == null) {
            if (other.saldo != null)
                return false;
        } else if (!saldo.equals(other.saldo))
            return false;
        return true;
    }

}
