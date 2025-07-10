package com.banquito.analisis.enums;

import java.math.BigDecimal;

public enum Denominacion {
    UNO(new BigDecimal("1.00")),
    CINCO(new BigDecimal("5.00")),
    DIEZ(new BigDecimal("10.00")),
    VEINTE(new BigDecimal("20.00")),
    CINCUENTA(new BigDecimal("50.00")),
    CIEN(new BigDecimal("100.00"));

    private final BigDecimal valor;

    Denominacion(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }
} 