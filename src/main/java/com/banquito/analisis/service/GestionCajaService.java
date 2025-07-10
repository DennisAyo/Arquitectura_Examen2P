package com.banquito.analisis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.analisis.controller.dto.AlertaCierreDTO;
import com.banquito.analisis.controller.dto.DenominacionBilletesDTO;
import com.banquito.analisis.controller.dto.FinalizarTurnoDTO;
import com.banquito.analisis.controller.dto.IniciarTurnoDTO;
import com.banquito.analisis.controller.dto.ProcesarTransaccionDTO;
import com.banquito.analisis.controller.dto.TurnoCajaDTO;
import com.banquito.analisis.controller.dto.TransaccionesTurnoDTO;
import com.banquito.analisis.controller.mapper.TurnosCajaMapper;
import com.banquito.analisis.controller.mapper.TransaccionesTurnoMapper;
import com.banquito.analisis.controller.mapper.DenominacionBilletesMapper;
import com.banquito.analisis.enums.EstadoTurno;
import com.banquito.analisis.enums.TipoTransaccion;
import com.banquito.analisis.exception.DatosInvalidosException;
import com.banquito.analisis.exception.TurnoNotFoundException;
import com.banquito.analisis.exception.TurnoYaAbiertoException;
import com.banquito.analisis.exception.TurnoYaCerradoException;
import com.banquito.analisis.model.DenominacionBilletes;
import com.banquito.analisis.model.TransaccionesTurno;
import com.banquito.analisis.model.TurnosCaja;
import com.banquito.analisis.repository.TransaccionesTurnoRepository;
import com.banquito.analisis.repository.TurnosCajaRepository;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Slf4j
public class GestionCajaService {

    private final TurnosCajaRepository turnosCajaRepository;
    private final TransaccionesTurnoRepository transaccionesTurnoRepository;
    private final TurnosCajaMapper turnosCajaMapper;
    private final TransaccionesTurnoMapper transaccionesTurnoMapper;
    private final DenominacionBilletesMapper denominacionBilletesMapper;

    public GestionCajaService(TurnosCajaRepository turnosCajaRepository,
                           TransaccionesTurnoRepository transaccionesTurnoRepository,
                           TurnosCajaMapper turnosCajaMapper,
                           TransaccionesTurnoMapper transaccionesTurnoMapper,
                           DenominacionBilletesMapper denominacionBilletesMapper) {
        this.turnosCajaRepository = turnosCajaRepository;
        this.transaccionesTurnoRepository = transaccionesTurnoRepository;
        this.turnosCajaMapper = turnosCajaMapper;
        this.transaccionesTurnoMapper = transaccionesTurnoMapper;
        this.denominacionBilletesMapper = denominacionBilletesMapper;
    }

    @Transactional
    public TurnoCajaDTO abrirTurno(IniciarTurnoDTO iniciarTurnoDTO) {
        log.info("Abriendo turno para caja: {} y cajero: {}", 
                iniciarTurnoDTO.getCodigoCaja(), iniciarTurnoDTO.getCodigoCajero());

        // Verificar que no exista un turno abierto
        Optional<TurnosCaja> turnoExistente = turnosCajaRepository.findByCodigoCajaAndCodigoCajeroAndEstado(
                iniciarTurnoDTO.getCodigoCaja(), iniciarTurnoDTO.getCodigoCajero(), EstadoTurno.ABIERTO);
        
        if (turnoExistente.isPresent()) {
            throw new TurnoYaAbiertoException(iniciarTurnoDTO.getCodigoCaja(), iniciarTurnoDTO.getCodigoCajero());
        }

        // Calcular monto inicial
        BigDecimal montoInicial = calcularMontoTotal(iniciarTurnoDTO.getDenominaciones());

        // Generar código de turno
        String codigoTurno = generarCodigoTurno(iniciarTurnoDTO.getCodigoCaja(), iniciarTurnoDTO.getCodigoCajero());

        // Crear turno
        TurnosCaja turno = new TurnosCaja();
        turno.setCodigoTurno(codigoTurno);
        turno.setCodigoCaja(iniciarTurnoDTO.getCodigoCaja());
        turno.setCodigoCajero(iniciarTurnoDTO.getCodigoCajero());
        turno.setInicioTurno(LocalDateTime.now());
        turno.setMontoInicial(montoInicial);
        turno.setEstado(EstadoTurno.ABIERTO);

        TurnosCaja turnoGuardado = turnosCajaRepository.save(turno);

        // Registrar transacción de inicio
        registrarTransaccionInicio(turnoGuardado, iniciarTurnoDTO.getDenominaciones());

        log.info("Turno abierto exitosamente con código: {}", codigoTurno);
        return turnosCajaMapper.toDTO(turnoGuardado);
    }

    @Transactional
    public TransaccionesTurnoDTO procesarTransaccion(ProcesarTransaccionDTO procesarTransaccionDTO) {
        log.info("Procesando transacción para turno: {}", procesarTransaccionDTO.getCodigoTurno());

        // Verificar que el turno existe y está abierto
        TurnosCaja turno = turnosCajaRepository.findById(procesarTransaccionDTO.getCodigoTurno())
                .orElseThrow(() -> new TurnoNotFoundException(procesarTransaccionDTO.getCodigoTurno()));

        if (turno.getEstado() == EstadoTurno.CERRADO) {
            throw new TurnoYaCerradoException(procesarTransaccionDTO.getCodigoTurno());
        }

        // Calcular monto total
        BigDecimal montoTotal = calcularMontoTotal(procesarTransaccionDTO.getDenominaciones());

        // Crear transacción
        TransaccionesTurno transaccion = new TransaccionesTurno();
        transaccion.setCodigoCaja(turno.getCodigoCaja());
        transaccion.setCodigoCajero(turno.getCodigoCajero());
        transaccion.setCodigoTurno(procesarTransaccionDTO.getCodigoTurno());
        transaccion.setTipoTransaccion(procesarTransaccionDTO.getTipoTransaccion());
        transaccion.setMontoTotal(montoTotal);
        transaccion.setFechaTransaccion(LocalDateTime.now());
        
        // Convertir denominaciones
        List<DenominacionBilletes> denominaciones = new ArrayList<>();
        for (DenominacionBilletesDTO dto : procesarTransaccionDTO.getDenominaciones()) {
            denominaciones.add(denominacionBilletesMapper.toModel(dto));
        }
        transaccion.setDenominaciones(denominaciones);

        TransaccionesTurno transaccionGuardada = transaccionesTurnoRepository.save(transaccion);

        log.info("Transacción procesada exitosamente con ID: {}", transaccionGuardada.getId());
        return transaccionesTurnoMapper.toDTO(transaccionGuardada);
    }

    @Transactional
    public AlertaCierreDTO cerrarTurno(FinalizarTurnoDTO finalizarTurnoDTO) {
        log.info("Cerrando turno: {}", finalizarTurnoDTO.getCodigoTurno());

        // Verificar que el turno existe y está abierto
        TurnosCaja turno = turnosCajaRepository.findById(finalizarTurnoDTO.getCodigoTurno())
                .orElseThrow(() -> new TurnoNotFoundException(finalizarTurnoDTO.getCodigoTurno()));

        if (turno.getEstado() == EstadoTurno.CERRADO) {
            throw new TurnoYaCerradoException(finalizarTurnoDTO.getCodigoTurno());
        }

        // Calcular monto declarado
        BigDecimal montoDeclarado = calcularMontoTotal(finalizarTurnoDTO.getDenominacionesFinales());

        // Calcular monto calculado basado en transacciones
        BigDecimal montoCalculado = calcularMontoCalculado(finalizarTurnoDTO.getCodigoTurno());

        // Registrar transacción de cierre
        registrarTransaccionCierre(turno, finalizarTurnoDTO.getDenominacionesFinales());

        // Actualizar turno
        turno.setFinTurno(LocalDateTime.now());
        turno.setMontoFinal(montoDeclarado);
        turno.setEstado(EstadoTurno.CERRADO);
        turnosCajaRepository.save(turno);

        log.info("Turno cerrado exitosamente: {}", finalizarTurnoDTO.getCodigoTurno());
        return new AlertaCierreDTO(finalizarTurnoDTO.getCodigoTurno(), montoCalculado, montoDeclarado);
    }

    public List<TurnoCajaDTO> obtenerTurnosPorCajero(String codigoCajero) {
        List<TurnosCaja> turnos = turnosCajaRepository.findByCodigoCajero(codigoCajero);
        List<TurnoCajaDTO> dtos = new ArrayList<>();
        for (TurnosCaja turno : turnos) {
            dtos.add(turnosCajaMapper.toDTO(turno));
        }
        return dtos;
    }

    public List<TransaccionesTurnoDTO> obtenerTransaccionesPorTurno(String codigoTurno) {
        List<TransaccionesTurno> transacciones = transaccionesTurnoRepository.findByCodigoTurno(codigoTurno);
        List<TransaccionesTurnoDTO> dtos = new ArrayList<>();
        for (TransaccionesTurno transaccion : transacciones) {
            dtos.add(transaccionesTurnoMapper.toDTO(transaccion));
        }
        return dtos;
    }

    public TurnoCajaDTO obtenerTurnoPorCodigo(String codigoTurno) {
        TurnosCaja turno = turnosCajaRepository.findById(codigoTurno)
                .orElseThrow(() -> new TurnoNotFoundException(codigoTurno));
        return turnosCajaMapper.toDTO(turno);
    }

    private String generarCodigoTurno(String codigoCaja, String codigoCajero) {
        LocalDate fecha = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return String.format("%s-%s-%s", codigoCaja, codigoCajero, fecha.format(formatter));
    }

    private BigDecimal calcularMontoTotal(List<DenominacionBilletesDTO> denominaciones) {
        BigDecimal total = BigDecimal.ZERO;
        for (DenominacionBilletesDTO denominacion : denominaciones) {
            if (denominacion.getCantidadBilletes() < 0) {
                throw new DatosInvalidosException("cantidadBilletes", denominacion.getCantidadBilletes().toString());
            }
            BigDecimal monto = denominacion.getBillete().getValor()
                    .multiply(new BigDecimal(denominacion.getCantidadBilletes()));
            total = total.add(monto);
        }
        return total;
    }

    private BigDecimal calcularMontoCalculado(String codigoTurno) {
        List<TransaccionesTurno> transacciones = transaccionesTurnoRepository.findByCodigoTurno(codigoTurno);
        BigDecimal montoCalculado = BigDecimal.ZERO;

        for (TransaccionesTurno transaccion : transacciones) {
            if (transaccion.getTipoTransaccion() == TipoTransaccion.INICIO) {
                montoCalculado = montoCalculado.add(transaccion.getMontoTotal());
            } else if (transaccion.getTipoTransaccion() == TipoTransaccion.DEPOSITO) {
                montoCalculado = montoCalculado.add(transaccion.getMontoTotal());
            } else if (transaccion.getTipoTransaccion() == TipoTransaccion.AHORRO) {
                montoCalculado = montoCalculado.subtract(transaccion.getMontoTotal());
            }
        }

        return montoCalculado;
    }

    private void registrarTransaccionInicio(TurnosCaja turno, List<DenominacionBilletesDTO> denominaciones) {
        TransaccionesTurno transaccion = new TransaccionesTurno();
        transaccion.setCodigoCaja(turno.getCodigoCaja());
        transaccion.setCodigoCajero(turno.getCodigoCajero());
        transaccion.setCodigoTurno(turno.getCodigoTurno());
        transaccion.setTipoTransaccion(TipoTransaccion.INICIO);
        transaccion.setMontoTotal(turno.getMontoInicial());
        transaccion.setFechaTransaccion(LocalDateTime.now());

        List<DenominacionBilletes> denominacionesBilletes = new ArrayList<>();
        for (DenominacionBilletesDTO dto : denominaciones) {
            denominacionesBilletes.add(denominacionBilletesMapper.toModel(dto));
        }
        transaccion.setDenominaciones(denominacionesBilletes);

        transaccionesTurnoRepository.save(transaccion);
    }

    private void registrarTransaccionCierre(TurnosCaja turno, List<DenominacionBilletesDTO> denominaciones) {
        BigDecimal montoTotal = calcularMontoTotal(denominaciones);
        
        TransaccionesTurno transaccion = new TransaccionesTurno();
        transaccion.setCodigoCaja(turno.getCodigoCaja());
        transaccion.setCodigoCajero(turno.getCodigoCajero());
        transaccion.setCodigoTurno(turno.getCodigoTurno());
        transaccion.setTipoTransaccion(TipoTransaccion.CIERRE);
        transaccion.setMontoTotal(montoTotal);
        transaccion.setFechaTransaccion(LocalDateTime.now());

        List<DenominacionBilletes> denominacionesBilletes = new ArrayList<>();
        for (DenominacionBilletesDTO dto : denominaciones) {
            denominacionesBilletes.add(denominacionBilletesMapper.toModel(dto));
        }
        transaccion.setDenominaciones(denominacionesBilletes);

        transaccionesTurnoRepository.save(transaccion);
    }
} 