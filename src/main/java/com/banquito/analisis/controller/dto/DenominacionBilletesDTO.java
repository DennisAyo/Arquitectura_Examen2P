package com.banquito.analisis.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.banquito.analisis.enums.Denominacion;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DenominacionBilletesDTO {

    @NotNull(message = "El billete es requerido")
    private Denominacion billete;

    @NotNull(message = "La cantidad de billetes es requerida")
    @Min(value = 0, message = "La cantidad de billetes no puede ser negativa")
    private Integer cantidadBilletes;

    private BigDecimal monto;
} 