package com.banquito.analisis.model;

import lombok.*;

import com.banquito.analisis.enums.TipoTransaccion;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@ToString
@Document(collection = "transacciones_turno")
public class TransaccionesTurno {

    @Id
    private String id;

    @Field("codigo_caja")
    private String codigoCaja;

    @Field("codigo_cajero")
    private String codigoCajero;

    @Field("codigo_turno")
    private String codigoTurno;

    @Field("tipo_transaccion")
    private TipoTransaccion tipoTransaccion;

    @Field("monto_total")
    private BigDecimal montoTotal;

    @Field("fecha_transaccion")
    private LocalDateTime fechaTransaccion;

    @Field("denominaciones")
    private List<DenominacionBilletes> denominaciones;

    public TransaccionesTurno(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransaccionesTurno that = (TransaccionesTurno) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 