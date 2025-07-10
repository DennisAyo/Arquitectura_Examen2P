package com.banquito.analisis.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.banquito.analisis.enums.Denominacion;

import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ToString
public class DenominacionBilletes {

    @Field("billete")
    private Denominacion billete;

    @Field("cantidad_billetes")
    private Integer cantidadBilletes;

    @Field("monto")
    private BigDecimal monto;

    public DenominacionBilletes(Denominacion billete, Integer cantidadBilletes) {
        this.billete = billete;
        this.cantidadBilletes = cantidadBilletes;
        this.monto = billete.getValor().multiply(new BigDecimal(cantidadBilletes));
    }

    public void actualizarMonto() {
        this.monto = billete.getValor().multiply(new BigDecimal(cantidadBilletes));
    }
} 