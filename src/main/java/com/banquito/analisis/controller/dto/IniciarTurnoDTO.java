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
public class IniciarTurnoDTO {

    @NotBlank(message = "El código de caja es requerido")
    @Size(max = 10, message = "El código de caja no puede exceder 10 caracteres")
    private String codigoCaja;

    @NotBlank(message = "El código de cajero es requerido")
    @Size(max = 10, message = "El código de cajero no puede exceder 10 caracteres")
    private String codigoCajero;

    @NotNull(message = "Las denominaciones son requeridas")
    @Size(min = 1, message = "Debe incluir al menos una denominación")
    @Valid
    private List<DenominacionBilletesDTO> denominaciones;
} 