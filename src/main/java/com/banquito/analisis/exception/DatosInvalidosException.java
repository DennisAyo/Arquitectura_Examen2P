package com.banquito.analisis.exception;

public class DatosInvalidosException extends RuntimeException {

    private final String campo;
    private final String valor;

    public DatosInvalidosException(String campo, String valor) {
        super();
        this.campo = campo;
        this.valor = valor;
    }

    @Override
    public String getMessage() {
        return "Datos inválidos en el campo: " + this.campo + " con valor: " + this.valor;
    }
} 