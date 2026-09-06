package com.duoc.semana1.bff.web.dto;

import lombok.Data;

import java.math.BigDecimal;

//datos generales de la cuenta
@Data
public class WebCuentaResponse {

    private Long cuentaId;
    private String nombre;
    private BigDecimal saldo;
    private Integer edad;
}
