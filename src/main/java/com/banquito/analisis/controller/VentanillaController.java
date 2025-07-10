package com.banquito.analisis.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.analisis.controller.dto.AlertaCierreDTO;
import com.banquito.analisis.controller.dto.FinalizarTurnoDTO;
import com.banquito.analisis.controller.dto.IniciarTurnoDTO;
import com.banquito.analisis.controller.dto.ProcesarTransaccionDTO;
import com.banquito.analisis.controller.dto.TransaccionesTurnoDTO;
import com.banquito.analisis.controller.dto.TurnoCajaDTO;
import com.banquito.analisis.exception.DatosInvalidosException;
import com.banquito.analisis.exception.TurnoNotFoundException;
import com.banquito.analisis.exception.TurnoYaAbiertoException;
import com.banquito.analisis.exception.TurnoYaCerradoException;
import com.banquito.analisis.service.VentanillaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/ventanilla")
@Tag(name = "Ventanilla", description = "API para manejo de efectivo en ventanillas bancarias")
@Slf4j
public class VentanillaController {

    private final VentanillaService ventanillaService;

    public VentanillaController(VentanillaService ventanillaService) {
        this.ventanillaService = ventanillaService;
    }

    @PostMapping("/turnos/iniciar")
    @Operation(summary = "Iniciar turno de ventanilla", 
               description = "Permite al cajero iniciar un turno registrando el dinero recibido de la bóveda")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Turno iniciado exitosamente",
                    content = @Content(schema = @Schema(implementation = TurnoCajaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe un turno abierto para la caja y cajero")
    })
    public ResponseEntity<TurnoCajaDTO> iniciarTurno(@Valid @RequestBody IniciarTurnoDTO iniciarTurnoDTO) {
        log.info("Iniciando turno para caja: {} y cajero: {}", 
                iniciarTurnoDTO.getCodigoCaja(), iniciarTurnoDTO.getCodigoCajero());
        
        TurnoCajaDTO turnoCreado = ventanillaService.iniciarTurno(iniciarTurnoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(turnoCreado);
    }

    @PostMapping("/transacciones/procesar")
    @Operation(summary = "Procesar transacción", 
               description = "Permite al cajero procesar una transacción de retiro o depósito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transacción procesada exitosamente",
                    content = @Content(schema = @Schema(implementation = TransaccionesTurnoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
        @ApiResponse(responseCode = "409", description = "El turno ya está cerrado")
    })
    public ResponseEntity<TransaccionesTurnoDTO> procesarTransaccion(@Valid @RequestBody ProcesarTransaccionDTO procesarTransaccionDTO) {
        log.info("Procesando transacción para turno: {}", procesarTransaccionDTO.getCodigoTurno());
        
        TransaccionesTurnoDTO transaccionCreada = ventanillaService.procesarTransaccion(procesarTransaccionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionCreada);
    }

    @PostMapping("/turnos/finalizar")
    @Operation(summary = "Finalizar turno de ventanilla", 
               description = "Permite al cajero finalizar un turno y verificar el balance de efectivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno finalizado exitosamente",
                    content = @Content(schema = @Schema(implementation = AlertaCierreDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
        @ApiResponse(responseCode = "409", description = "El turno ya está cerrado")
    })
    public ResponseEntity<AlertaCierreDTO> finalizarTurno(@Valid @RequestBody FinalizarTurnoDTO finalizarTurnoDTO) {
        log.info("Finalizando turno: {}", finalizarTurnoDTO.getCodigoTurno());
        
        AlertaCierreDTO alertaCierre = ventanillaService.finalizarTurno(finalizarTurnoDTO);
        return ResponseEntity.ok(alertaCierre);
    }

    @GetMapping("/turnos/{codigoTurno}")
    @Operation(summary = "Obtener turno por código", 
               description = "Permite consultar la información de un turno específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = TurnoCajaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    public ResponseEntity<TurnoCajaDTO> obtenerTurnoPorCodigo(
            @Parameter(description = "Código del turno", required = true)
            @PathVariable String codigoTurno) {
        log.info("Consultando turno: {}", codigoTurno);
        
        TurnoCajaDTO turno = ventanillaService.obtenerTurnoPorCodigo(codigoTurno);
        return ResponseEntity.ok(turno);
    }

    @GetMapping("/cajeros/{codigoCajero}/turnos")
    @Operation(summary = "Obtener turnos por cajero", 
               description = "Permite consultar todos los turnos de un cajero específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turnos encontrados exitosamente",
                    content = @Content(schema = @Schema(implementation = TurnoCajaDTO.class)))
    })
    public ResponseEntity<List<TurnoCajaDTO>> obtenerTurnosPorCajero(
            @Parameter(description = "Código del cajero", required = true)
            @PathVariable String codigoCajero) {
        log.info("Consultando turnos para cajero: {}", codigoCajero);
        
        List<TurnoCajaDTO> turnos = ventanillaService.obtenerTurnosPorCajero(codigoCajero);
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/turnos/{codigoTurno}/transacciones")
    @Operation(summary = "Obtener transacciones por turno", 
               description = "Permite consultar todas las transacciones de un turno específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transacciones encontradas exitosamente",
                    content = @Content(schema = @Schema(implementation = TransaccionesTurnoDTO.class)))
    })
    public ResponseEntity<List<TransaccionesTurnoDTO>> obtenerTransaccionesPorTurno(
            @Parameter(description = "Código del turno", required = true)
            @PathVariable String codigoTurno) {
        log.info("Consultando transacciones para turno: {}", codigoTurno);
        
        List<TransaccionesTurnoDTO> transacciones = ventanillaService.obtenerTransaccionesPorTurno(codigoTurno);
        return ResponseEntity.ok(transacciones);
    }

    @ExceptionHandler({TurnoNotFoundException.class})
    public ResponseEntity<String> handleTurnoNotFoundException(TurnoNotFoundException ex) {
        log.error("Turno no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({TurnoYaAbiertoException.class})
    public ResponseEntity<String> handleTurnoYaAbiertoException(TurnoYaAbiertoException ex) {
        log.error("Turno ya abierto: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler({TurnoYaCerradoException.class})
    public ResponseEntity<String> handleTurnoYaCerradoException(TurnoYaCerradoException ex) {
        log.error("Turno ya cerrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler({DatosInvalidosException.class})
    public ResponseEntity<String> handleDatosInvalidosException(DatosInvalidosException ex) {
        log.error("Datos inválidos: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
} 