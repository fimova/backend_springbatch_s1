package com.duoc.semana1.bff.web;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.semana1.bff.web.dto.WebCuentaResponse;
import com.duoc.semana1.bff.web.dto.WebInteresResponse;
import com.duoc.semana1.bff.web.dto.WebMovimientoResponse;
import com.duoc.semana1.bff.web.dto.WebResumenResponse;
import com.duoc.semana1.bff.web.dto.WebTransaccionResponse;
import com.duoc.semana1.exception.ResourceNotFoundException;
import com.duoc.semana1.model.CuentaInteres;
import com.duoc.semana1.model.MovimientoAnual;
import com.duoc.semana1.model.Transaccion;
import com.duoc.semana1.services.CuentaInteresService;
import com.duoc.semana1.services.MovimientoAnualService;
import com.duoc.semana1.services.TransaccionService;

//orquestar y componer info para la web
@Service
public class WebBffService {

        private final CuentaInteresService cuentaInteresService;
        private final MovimientoAnualService movimientoAnualService;
        private final TransaccionService transaccionService;

        public WebBffService(
                        CuentaInteresService cuentaInteresService,
                        MovimientoAnualService movimientoAnualService,
                        TransaccionService transaccionService) {
                this.cuentaInteresService = cuentaInteresService;
                this.movimientoAnualService = movimientoAnualService;
                this.transaccionService = transaccionService;
        }

        public WebResumenResponse obtenerResumen(Long cuentaId) {

                List<CuentaInteres> cuentas = cuentaInteresService.obtenerPorCuenta(cuentaId);

                List<MovimientoAnual> movimientos = movimientoAnualService.obtenerPorCuenta(cuentaId);

                List<Transaccion> transacciones = transaccionService.obtenerTodas();

                WebResumenResponse response = new WebResumenResponse();

                response.setCuenta(mapearCuenta(cuentas, cuentaId));
                response.setIntereses(mapearIntereses(cuentas));
                response.setMovimientos(mapearMovimientos(movimientos));
                response.setTransacciones(mapearTransacciones(transacciones));

                return response;
        }

        private WebCuentaResponse mapearCuenta(
                        List<CuentaInteres> cuentas,
                        Long cuentaId) {

                if (cuentas.isEmpty()) {
                        throw new ResourceNotFoundException(
                                        "No se encontró la cuenta con id: " + cuentaId);
                }

                CuentaInteres cuenta = cuentas.get(0);

                WebCuentaResponse response = new WebCuentaResponse();

                response.setCuentaId(cuenta.getCuentaId());
                response.setNombre(cuenta.getNombre());
                response.setSaldo(cuenta.getSaldo());
                response.setEdad(cuenta.getEdad());

                return response;
        }

        private List<WebInteresResponse> mapearIntereses(
                        List<CuentaInteres> cuentas) {

                return cuentas.stream()
                                .map(cuenta -> {
                                        WebInteresResponse response = new WebInteresResponse();

                                        response.setCuentaId(cuenta.getCuentaId());
                                        response.setSaldoInicial(cuenta.getSaldo());
                                        response.setInteresAplicado(
                                                        cuenta.getInteresAplicado());
                                        response.setSaldoFinal(
                                                        cuenta.getSaldoFinal());
                                        response.setPeriodo(
                                                        cuenta.getPeriodo());
                                        response.setTipo(
                                                        cuenta.getTipo());

                                        return response;
                                })
                                .toList();
        }

        private List<WebMovimientoResponse> mapearMovimientos(
                        List<MovimientoAnual> movimientos) {

                return movimientos.stream()
                                .map(movimiento -> {
                                        WebMovimientoResponse response = new WebMovimientoResponse();

                                        response.setFecha(movimiento.getFecha());
                                        response.setTransaccion(
                                                        movimiento.getTransaccion());
                                        response.setMonto(movimiento.getMonto());
                                        response.setDescripcion(
                                                        movimiento.getDescripcion());

                                        return response;
                                })
                                .toList();
        }

        private List<WebTransaccionResponse> mapearTransacciones(
                        List<Transaccion> transacciones) {

                return transacciones.stream()
                                .map(transaccion -> {
                                        WebTransaccionResponse response = new WebTransaccionResponse();

                                        response.setTransaccionId(
                                                        transaccion.getTransaccionId());
                                        response.setFecha(transaccion.getFecha());
                                        response.setMonto(transaccion.getMonto());
                                        response.setTipo(transaccion.getTipo());
                                        response.setAnomalia(transaccion.isAnomalia());
                                        response.setMotivoAnomalia(
                                                        transaccion.getMotivoAnomalia());

                                        return response;
                                })
                                .toList();
        }

        public List<WebMovimientoResponse> obtenerMovimientos(Long cuentaId) {

                List<MovimientoAnual> movimientos = movimientoAnualService.obtenerPorCuenta(cuentaId);

                return mapearMovimientos(movimientos);
        }

        public List<WebInteresResponse> obtenerIntereses(Long cuentaId) {

                List<CuentaInteres> cuentas = cuentaInteresService.obtenerPorCuenta(cuentaId);

                return mapearIntereses(cuentas);
        }

        public List<WebTransaccionResponse> obtenerTransacciones() {

                List<Transaccion> transacciones = transaccionService.obtenerTodas();

                return mapearTransacciones(transacciones);
        }
}