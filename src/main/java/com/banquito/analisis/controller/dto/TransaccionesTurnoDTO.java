package com.banquito.analisis.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.banquito.analisis.enums.TipoTransaccion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TransaccionesTurnoDTO {

    private Long id;

    @NotBlank(message = "El código de caja es requerido")
    @Size(max = 10, message = "El código de caja no puede exceder 10 caracteres")
    private String codigoCaja;

    @NotBlank(message = "El código de cajero es requerido")
    @Size(max = 10, message = "El código de cajero no puede exceder 10 caracteres")
    private String codigoCajero;

    @NotBlank(message = "El código de turno es requerido")
    @Size(max = 50, message = "El código de turno no puede exceder 50 caracteres")
    private String codigoTurno;

    @NotNull(message = "El tipo de transacción es requerido")
    private TipoTransaccion tipoTransaccion;

    @NotNull(message = "El monto total es requerido")
    @DecimalMin(value = "0.0", message = "El monto total no puede ser negativo")
    private BigDecimal montoTotal;

    @NotNull(message = "La fecha de transacción es requerida")
    private LocalDateTime fechaTransaccion;

    @NotNull(message = "Las denominaciones son requeridas")
    @Size(min = 1, message = "Debe incluir al menos una denominación")
    @Valid
    private List<DenominacionBilletesDTO> denominaciones;
} 