package com.duoc.semana1.bff.mobile.dto;

import lombok.Data;
import java.math.BigDecimal;

//info basica de la cuenta
@Data
public class MobileCuentaResponse {

    private Long cuentaId;
    private String nombre;
    private BigDecimal saldo;
}
