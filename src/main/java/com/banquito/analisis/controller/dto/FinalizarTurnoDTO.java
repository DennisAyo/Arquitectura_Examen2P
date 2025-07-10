package com.banquito.analisis.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.util.List;

@Data
@NoArgsConstructor
public class FinalizarTurnoDTO {

    @NotBlank(message = "El código de turno es requerido")
    @Size(max = 50, message = "El código de turno no puede exceder 50 caracteres")
    private String codigoTurno;

    @NotNull(message = "Las denominaciones finales son requeridas")
    @Size(min = 1, message = "Debe incluir al menos una denominación")
    @Valid
    private List<DenominacionBilletesDTO> denominacionesFinales;
} 