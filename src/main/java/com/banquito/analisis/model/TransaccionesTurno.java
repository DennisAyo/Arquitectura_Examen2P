package com.banquito.analisis.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

import com.banquito.analisis.enums.TipoTransaccion;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "transacciones_turno")
public class TransaccionesTurno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo_caja", length = 10, nullable = false)
    private String codigoCaja;

    @Column(name = "codigo_cajero", length = 10, nullable = false)
    private String codigoCajero;

    @Column(name = "codigo_turno", length = 50, nullable = false)
    private String codigoTurno;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transaccion", length = 20, nullable = false)
    private TipoTransaccion tipoTransaccion;

    @Column(name = "monto_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal montoTotal;

    @Column(name = "fecha_transaccion", nullable = false)
    private LocalDateTime fechaTransaccion;

    @ElementCollection
    @CollectionTable(name = "denominaciones_transaccion", 
                    joinColumns = @JoinColumn(name = "transaccion_id"))
    private List<DenominacionBilletes> denominaciones;

    public TransaccionesTurno(Long id) {
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