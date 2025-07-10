package com.banquito.analisis.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.analisis.controller.dto.TransaccionesTurnoDTO;
import com.banquito.analisis.model.TransaccionesTurno;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TransaccionesTurnoMapper {

    TransaccionesTurnoDTO toDTO(TransaccionesTurno model);
    
    TransaccionesTurno toModel(TransaccionesTurnoDTO dto);
} 