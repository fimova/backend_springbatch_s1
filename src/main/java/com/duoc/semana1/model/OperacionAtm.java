package com.duoc.semana1.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor 
public class OperacionAtm {

    private Long operacionId;
    private Long cuentaId;
    private String tipo;
    private BigDecimal monto;
    private LocalDateTime fecha;

}
