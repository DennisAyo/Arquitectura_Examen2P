package com.banquito.analisis.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.analisis.controller.dto.DenominacionBilletesDTO;
import com.banquito.analisis.model.DenominacionBilletes;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DenominacionBilletesMapper {

    DenominacionBilletesDTO toDTO(DenominacionBilletes model);
    
    DenominacionBilletes toModel(DenominacionBilletesDTO dto);
} 