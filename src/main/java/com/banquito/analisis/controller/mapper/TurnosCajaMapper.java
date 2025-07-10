package com.banquito.analisis.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.analisis.controller.dto.TurnoCajaDTO;
import com.banquito.analisis.model.TurnosCaja;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TurnosCajaMapper {

    TurnoCajaDTO toDTO(TurnosCaja model);
    
    TurnosCaja toModel(TurnoCajaDTO dto);
} 