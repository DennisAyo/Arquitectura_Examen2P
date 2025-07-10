package com.banquito.analisis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.banquito.analisis.model.TurnosCaja;
import com.banquito.analisis.enums.EstadoTurno;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnosCajaRepository extends MongoRepository<TurnosCaja, String> {

    List<TurnosCaja> findByCodigoCajaAndCodigoCajero(String codigoCaja, String codigoCajero);
    
    List<TurnosCaja> findByEstado(EstadoTurno estado);
    
    Optional<TurnosCaja> findByCodigoCajaAndCodigoCajeroAndEstado(String codigoCaja, String codigoCajero, EstadoTurno estado);
    
    List<TurnosCaja> findByCodigoCajero(String codigoCajero);
    
    List<TurnosCaja> findByCodigoCaja(String codigoCaja);
} 