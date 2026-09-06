package com.duoc.semana1.bff.atm.dto;

import lombok.Data;

import java.math.BigDecimal;

//respuesta del retiro de dinero
@Data
public class AtmRetiroResponse {

    private Long cuentaId;
    private BigDecimal montoRetirado;
    private BigDecimal saldoDisponible;
    private String mensaje;
}
