package com.banquito.analisis.exception;

public class EvaluacionCrediticiaExcepcion extends RuntimeException {
    
    private final String evaluationStep;
    private final String reason;
    
    public EvaluacionCrediticiaExcepcion(String evaluationStep, String reason) {
        super();
        this.evaluationStep = evaluationStep;
        this.reason = reason;
    }
    
    @Override
    public String getMessage() {
        return "Error en la evaluación crediticia en el paso: " + this.evaluationStep + ". Razón: " + reason;
    }
} 