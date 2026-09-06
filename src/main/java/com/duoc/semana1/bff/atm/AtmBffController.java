package com.duoc.semana1.bff.atm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.semana1.bff.atm.dto.AtmSaldoResponse;

@RestController
@RequestMapping("/api/atm")
public class AtmBffController {

    private final AtmBffService atmBffService;

    public AtmBffController(AtmBffService atmBffService) {
        this.atmBffService = atmBffService;
    }

    @GetMapping("/cuentas/{cuentaId}/saldo")
    public AtmSaldoResponse obtenerSaldo(
            @PathVariable Long cuentaId
    ) {
        return atmBffService.obtenerSaldo(cuentaId);
    }
}
