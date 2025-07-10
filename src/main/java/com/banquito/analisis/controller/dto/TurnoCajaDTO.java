package com.banquito.analisis.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.banquito.analisis.enums.EstadoTurno;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TurnoCajaDTO {

    @NotBlank(message = "El código de turno es requerido")
    @Size(max = 50, message = "El código de turno no puede exceder 50 caracteres")
    private String codigoTurno;

    @NotBlank(message = "El código de caja es requerido")
    @Size(max = 10, message = "El código de caja no puede exceder 10 caracteres")
    private String codigoCaja;

    @NotBlank(message = "El código de cajero es requerido")
    @Size(max = 10, message = "El código de cajero no puede exceder 10 caracteres")
    private String codigoCajero;

    @NotNull(message = "El inicio de turno es requerido")
    private LocalDateTime inicioTurno;

    @NotNull(message = "El monto inicial es requerido")
    @DecimalMin(value = "0.0", message = "El monto inicial no puede ser negativo")
    private BigDecimal montoInicial;

    private LocalDateTime finTurno;

    @DecimalMin(value = "0.0", message = "El monto final no puede ser negativo")
    private BigDecimal montoFinal;

    @NotNull(message = "El estado es requerido")
    private EstadoTurno estado;
} 