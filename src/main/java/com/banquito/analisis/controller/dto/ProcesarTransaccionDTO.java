package com.banquito.analisis.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.banquito.analisis.enums.TipoTransaccion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.util.List;

@Data
@NoArgsConstructor
public class ProcesarTransaccionDTO {

    @NotBlank(message = "El código de turno es requerido")
    @Size(max = 50, message = "El código de turno no puede exceder 50 caracteres")
    private String codigoTurno;

    @NotNull(message = "El tipo de transacción es requerido")
    private TipoTransaccion tipoTransaccion;

    @NotNull(message = "Las denominaciones son requeridas")
    @Size(min = 1, message = "Debe incluir al menos una denominación")
    @Valid
    private List<DenominacionBilletesDTO> denominaciones;
} 