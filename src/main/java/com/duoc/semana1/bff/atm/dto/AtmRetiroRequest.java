package com.duoc.semana1.bff.atm.dto;

import lombok.Data;

import java.math.BigDecimal;

//para solicitar retiro de dinero
@Data
public class AtmRetiroRequest {

    private Long cuentaId;
    private BigDecimal monto;
}