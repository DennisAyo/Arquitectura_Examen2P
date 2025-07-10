package com.banquito.analisis.exception;

public class TurnoYaCerradoException extends RuntimeException {

    private final String codigoTurno;

    public TurnoYaCerradoException(String codigoTurno) {
        super();
        this.codigoTurno = codigoTurno;
    }

    @Override
    public String getMessage() {
        return "El turno con código: " + this.codigoTurno + " ya está cerrado";
    }
} 