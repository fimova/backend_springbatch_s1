package com.duoc.semana1.bff.mobile.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

//resumen que proviene de distintos servicios
@Data
public class MobileResumenResponse {

    private MobileCuentaResponse cuenta;
    private BigDecimal interesAplicado;
    private String periodo;
    private List<MobileMovimientoResponse> movimientos;
}
