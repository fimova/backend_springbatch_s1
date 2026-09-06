package com.duoc.semana1.bff.atm.dto;

import lombok.Data;

import java.math.BigDecimal;

//depositar dinero en la cuenta
@Data
public class AtmDepositoRequest {

    private Long cuentaId;
    private BigDecimal monto;
}
