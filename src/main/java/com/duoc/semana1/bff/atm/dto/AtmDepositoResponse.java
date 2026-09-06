package com.duoc.semana1.bff.atm.dto;

import lombok.Data;

import java.math.BigDecimal;

//respuesta luego de depositar dinero
@Data
public class AtmDepositoResponse {

    private Long cuentaId;
    private BigDecimal montoDepositado;
    private BigDecimal saldoDisponible;
    private String mensaje;
}
