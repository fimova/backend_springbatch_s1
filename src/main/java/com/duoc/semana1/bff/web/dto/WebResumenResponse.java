package com.duoc.semana1.bff.web.dto;

import lombok.Data;

import java.util.List;

//similar a mobile pero más completo
@Data
public class WebResumenResponse {

    private WebCuentaResponse cuenta;
    private List<WebInteresResponse> intereses;
    private List<WebMovimientoResponse> movimientos;
    private List<WebTransaccionResponse> transacciones;
}
