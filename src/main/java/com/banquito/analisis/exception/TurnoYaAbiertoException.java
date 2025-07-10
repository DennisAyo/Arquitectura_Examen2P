package com.banquito.analisis.exception;

public class TurnoYaAbiertoException extends RuntimeException {

    private final String codigoCaja;
    private final String codigoCajero;

    public TurnoYaAbiertoException(String codigoCaja, String codigoCajero) {
        super();
        this.codigoCaja = codigoCaja;
        this.codigoCajero = codigoCajero;
    }

    @Override
    public String getMessage() {
        return "Ya existe un turno abierto para la caja: " + this.codigoCaja + " y cajero: " + this.codigoCajero;
    }
} 