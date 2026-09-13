package com.duoc.semana1.bff.atm.dto;

import lombok.Data;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

//para solicitar retiro de dinero
@Data
public class AtmRetiroRequest {

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(
            value = "0.01",
            message = "El monto debe ser mayor que 0"
    )
    private BigDecimal monto;
}