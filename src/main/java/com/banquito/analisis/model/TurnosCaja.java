package com.banquito.analisis.model;

import lombok.*;


import com.banquito.analisis.enums.EstadoTurno;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "turnos_caja")
public class TurnosCaja {

    @Id
    @Column(name = "codigo_turno", length = 50)
    private String codigoTurno;

    @Column(name = "codigo_caja", length = 10, nullable = false)
    private String codigoCaja;

    @Column(name = "codigo_cajero", length = 10, nullable = false)
    private String codigoCajero;

    @Column(name = "inicio_turno", nullable = false)
    private LocalDateTime inicioTurno;

    @Column(name = "monto_inicial", precision = 15, scale = 2, nullable = false)
    private BigDecimal montoInicial;

    @Column(name = "fin_turno")
    private LocalDateTime finTurno;

    @Column(name = "monto_final", precision = 15, scale = 2)
    private BigDecimal montoFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 10, nullable = false)
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