package com.duoc.semana1.bff.web.dto;

import lombok.Data;

import java.math.BigDecimal;

//datos de calculo de intereses
@Data
public class WebInteresResponse {

    private Long cuentaId;
    private BigDecimal saldoInicial;
    private BigDecimal interesAplicado;
    private BigDecimal saldoFinal;
    private String periodo;
    private String tipo;
}
