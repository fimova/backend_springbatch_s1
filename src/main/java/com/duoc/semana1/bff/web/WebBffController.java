package com.duoc.semana1.bff.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.semana1.bff.web.dto.WebResumenResponse;

@RestController
@RequestMapping("/api/web")
public class WebBffController {

    private final WebBffService webBffService;

    public WebBffController(WebBffService webBffService) {
        this.webBffService = webBffService;
    }

    @GetMapping("/cuentas/{cuentaId}/resumen")
    public WebResumenResponse obtenerResumen(
            @PathVariable Long cuentaId
    ) {
        return webBffService.obtenerResumen(cuentaId);
    }
}
