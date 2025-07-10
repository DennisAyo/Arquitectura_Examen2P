package com.banquito.analisis.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AlertaCierreDTO {

    private String codigoTurno;
    private BigDecimal montoCalculado;
    private BigDecimal montoDeclarado;
    private BigDecimal diferencia;
    private String mensaje;
    private boolean tieneAlerta;

    public AlertaCierreDTO(String codigoTurno, BigDecimal montoCalculado, BigDecimal montoDeclarado) {
        this.codigoTurno = codigoTurno;
        this.montoCalculado = montoCalculado;
        this.montoDeclarado = montoDeclarado;
        this.diferencia = montoDeclarado.subtract(montoCalculado);
        this.tieneAlerta = diferencia.compareTo(BigDecimal.ZERO) != 0;
        this.mensaje = tieneAlerta ? 
            "ALERTA: Diferencia detectada en el cierre de turno" : 
            "Turno cerrado correctamente";
    }
} 