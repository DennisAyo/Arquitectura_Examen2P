package com.banquito.analisis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.analisis.model.TurnoCaja;
import com.banquito.analisis.enums.EstadoTurno;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoCajaRepository extends JpaRepository<TurnoCaja, String> {

    List<TurnoCaja> findByCodigoCajaAndCodigoCajero(String codigoCaja, String codigoCajero);
    
    List<TurnoCaja> findByEstado(EstadoTurno estado);
    
    Optional<TurnoCaja> findByCodigoCajaAndCodigoCajeroAndEstado(String codigoCaja, String codigoCajero, EstadoTurno estado);
    
    List<TurnoCaja> findByCodigoCajero(String codigoCajero);
    
    List<TurnoCaja> findByCodigoCaja(String codigoCaja);
} 