package com.duoc.semana1.bff.mobile;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.semana1.bff.mobile.dto.MobileMovimientoResponse;
import com.duoc.semana1.bff.mobile.dto.MobileResumenResponse;

@RestController
@RequestMapping("/api/mobile")
public class MobileBffController {

    private final MobileBffService mobileBffService;

    public MobileBffController(MobileBffService mobileBffService) {
        this.mobileBffService = mobileBffService;
    }

    @GetMapping("/cuentas/{cuentaId}/resumen")
    public MobileResumenResponse obtenerResumen(
            @PathVariable Long cuentaId
    ) {
        return mobileBffService.obtenerResumen(cuentaId);
    }

    @GetMapping("/cuentas/{cuentaId}/movimientos")
    public List<MobileMovimientoResponse> obtenerMovimientos(
            @PathVariable Long cuentaId
    ) {
        return mobileBffService.obtenerMovimientos(cuentaId);
    }
}
