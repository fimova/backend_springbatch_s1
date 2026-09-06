package com.duoc.semana1.bff.atm.dto;

import lombok.Data;

import java.math.BigDecimal;

//consulta de saldo en la cuenta
@Data
public class AtmSaldoResponse {

    private Long cuentaId;
    private BigDecimal saldo;
}
