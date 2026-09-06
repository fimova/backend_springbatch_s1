package com.duoc.semana1.bff.mobile.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

//movimientos recientes
@Data
public class MobileMovimientoResponse {

    private LocalDate fecha;
    private String transaccion;
    private BigDecimal monto;
    private String descripcion;
}
