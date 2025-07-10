package com.banquito.analisis.model;


public class Enums {
    
   
    public enum EstadoConsulta {
        PENDIENTE,
        COMPLETADA,
        FALLIDA,
        REINTENTANDO
    }
    
   
    public enum DecisionManual {
        APROBADO,
        RECHAZADO,
        PENDIENTE
    }
    
    public enum DecisionFinalAnalista {
        APROBADO,
        RECHAZADO,
        PENDIENTE
    }
    
    public enum ResultadoAutomatico {
        APROBADO,
        RECHAZADO,
        REVISION_MANUAL
    }
} 