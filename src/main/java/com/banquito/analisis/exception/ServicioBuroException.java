package com.banquito.analisis.exception;

public class ServicioBuroException extends RuntimeException {
    
    private final String operation;
    private final String details;
    
    public ServicioBuroException(String operation, String details) {
        super();
        this.operation = operation;
        this.details = details;
    }
    
    @Override
    public String getMessage() {
        return "Error en el servicio del buró durante la operación: " + this.operation + ". Detalles: " + details;
    }
} 