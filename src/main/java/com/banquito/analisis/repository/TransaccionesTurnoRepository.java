package com.banquito.analisis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.banquito.analisis.model.TransaccionesTurno;
import com.banquito.analisis.enums.TipoTransaccion;

import java.util.List;

@Repository
public interface TransaccionesTurnoRepository extends MongoRepository<TransaccionesTurno, String> {

    List<TransaccionesTurno> findByCodigoTurno(String codigoTurno);
    
    List<TransaccionesTurno> findByCodigoCajaAndCodigoCajero(String codigoCaja, String codigoCajero);
    
    List<TransaccionesTurno> findByCodigoTurnoAndTipoTransaccion(String codigoTurno, TipoTransaccion tipoTransaccion);
    
    List<TransaccionesTurno> findByTipoTransaccion(TipoTransaccion tipoTransaccion);
    
    List<TransaccionesTurno> findByCodigoCajero(String codigoCajero);
    
    List<TransaccionesTurno> findByCodigoCaja(String codigoCaja);
} 