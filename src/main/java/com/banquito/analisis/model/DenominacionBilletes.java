package com.banquito.analisis.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.banquito.analisis.enums.Denominacion;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ToString
@Embeddable
public class DenominacionBilletes {

    @Enumerated(EnumType.STRING)
    @Column(name = "billete", length = 20, nullable = false)
    private Denominacion billete;

    @Column(name = "cantidad_billetes", nullable = false)
    private Integer cantidadBilletes;

    @Column(name = "monto", precision = 15, scale = 2, nullable = false)
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