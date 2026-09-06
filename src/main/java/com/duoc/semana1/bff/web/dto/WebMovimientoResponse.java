package com.duoc.semana1.bff.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

//datos de movimientos/cuentas anuales
@Data
public class WebMovimientoResponse {

    private LocalDate fecha;
    private String transaccion;
    private BigDecimal monto;
    private String descripcion;
}
