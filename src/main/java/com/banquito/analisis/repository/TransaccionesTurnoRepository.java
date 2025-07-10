package com.banquito.analisis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.analisis.model.TransaccionesTurno;
import com.banquito.analisis.enums.TipoTransaccion;

import java.util.List;

@Repository
public interface TransaccionesTurnoRepository extends JpaRepository<TransaccionesTurno, Long> {

    List<TransaccionesTurno> findByCodigoTurno(String codigoTurno);
    
    List<TransaccionesTurno> findByCodigoCajaAndCodigoCajero(String codigoCaja, String codigoCajero);
    
    List<TransaccionesTurno> findByCodigoTurnoAndTipoTransaccion(String codigoTurno, TipoTransaccion tipoTransaccion);
    
    List<TransaccionesTurno> findByTipoTransaccion(TipoTransaccion tipoTransaccion);
    
    List<TransaccionesTurno> findByCodigoCajero(String codigoCajero);
    
    List<TransaccionesTurno> findByCodigoCaja(String codigoCaja);
} 