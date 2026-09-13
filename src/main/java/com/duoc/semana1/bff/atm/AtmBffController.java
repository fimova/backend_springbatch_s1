package com.duoc.semana1.bff.atm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.semana1.bff.atm.dto.AtmDepositoRequest;
import com.duoc.semana1.bff.atm.dto.AtmDepositoResponse;
import com.duoc.semana1.bff.atm.dto.AtmSaldoResponse;

import jakarta.validation.Valid;

import com.duoc.semana1.bff.atm.dto.AtmRetiroRequest;
import com.duoc.semana1.bff.atm.dto.AtmRetiroResponse;

@RestController
@RequestMapping("/api/atm")
public class AtmBffController {

        private final AtmBffService atmBffService;

        public AtmBffController(AtmBffService atmBffService) {
                this.atmBffService = atmBffService;
        }

        @GetMapping("/cuentas/{cuentaId}/saldo")
        public AtmSaldoResponse obtenerSaldo(
                        @PathVariable Long cuentaId) {
                return atmBffService.obtenerSaldo(cuentaId);
        }

        // deposito en cajero
        @PostMapping("/cuentas/{cuentaId}/depositos")
        public AtmDepositoResponse depositar(
                        @PathVariable Long cuentaId,
                        @Valid @RequestBody AtmDepositoRequest request) {
                atmBffService.registrarDeposito(
                                cuentaId,
                                request.getMonto());

                AtmSaldoResponse saldo = atmBffService.obtenerSaldo(cuentaId);

                AtmDepositoResponse response = new AtmDepositoResponse();

                response.setCuentaId(cuentaId);
                response.setMontoDepositado(request.getMonto());
                response.setSaldoDisponible(saldo.getSaldo());
                response.setMensaje("Depósito realizado correctamente");

                return response;
        }

        //retiro en cajero
        @PostMapping("/cuentas/{cuentaId}/retiros")
        public AtmRetiroResponse retirar(
                        @PathVariable Long cuentaId,
                        @Valid @RequestBody AtmRetiroRequest request) {
                atmBffService.registrarRetiro(
                                cuentaId,
                                request.getMonto());

                AtmSaldoResponse saldo = atmBffService.obtenerSaldo(cuentaId);

                AtmRetiroResponse response = new AtmRetiroResponse();

                response.setCuentaId(cuentaId);
                response.setMontoRetirado(request.getMonto());
                response.setSaldoDisponible(saldo.getSaldo());
                response.setMensaje("Retiro realizado correctamente");

                return response;
        }
}
