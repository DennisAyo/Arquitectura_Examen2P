package com.banquito.analisis.model;

import lombok.*;

import com.banquito.analisis.enums.EstadoTurno;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@ToString
@Document(collection = "turnos_caja")
public class TurnosCaja {

    @Id
    private String codigoTurno;

    @Field("codigo_caja")
    private String codigoCaja;

    @Field("codigo_cajero")
    private String codigoCajero;

    @Field("inicio_turno")
    private LocalDateTime inicioTurno;

    @Field("monto_inicial")
    private BigDecimal montoInicial;

    @Field("fin_turno")
    private LocalDateTime finTurno;

    @Field("monto_final")
    private BigDecimal montoFinal;

    @Field("estado")
    private EstadoTurno estado;

    public TurnosCaja(String codigoTurno) {
        this.codigoTurno = codigoTurno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnosCaja turnosCaja = (TurnosCaja) o;
        return Objects.equals(codigoTurno, turnosCaja.codigoTurno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoTurno);
    }
} 