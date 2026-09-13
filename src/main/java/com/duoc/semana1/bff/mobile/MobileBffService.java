package com.duoc.semana1.bff.mobile;

import org.springframework.stereotype.Service;

import com.duoc.semana1.bff.mobile.dto.MobileCuentaResponse;
import com.duoc.semana1.bff.mobile.dto.MobileMovimientoResponse;
import com.duoc.semana1.bff.mobile.dto.MobileResumenResponse;
import com.duoc.semana1.exception.ResourceNotFoundException;
import com.duoc.semana1.model.CuentaInteres;
import com.duoc.semana1.model.MovimientoAnual;
import com.duoc.semana1.services.CuentaInteresService;
import com.duoc.semana1.services.MovimientoAnualService;

import java.util.List;

//orquesta y compone info para mostrar a mobile
@Service
public class MobileBffService {

        private final CuentaInteresService cuentaInteresService;
        private final MovimientoAnualService movimientoAnualService;

        public MobileBffService(
                        CuentaInteresService cuentaInteresService,
                        MovimientoAnualService movimientoAnualService) {
                this.cuentaInteresService = cuentaInteresService;
                this.movimientoAnualService = movimientoAnualService;
        }

        public MobileResumenResponse obtenerResumen(Long cuentaId) {

                List<CuentaInteres> cuentas = cuentaInteresService.obtenerPorCuenta(cuentaId);

                MobileResumenResponse response = new MobileResumenResponse();

                response.setCuenta(mapearCuenta(cuentas, cuentaId));

                if (!cuentas.isEmpty()) {
                        CuentaInteres cuenta = cuentas.get(0);

                        response.setInteresAplicado(
                                        cuenta.getInteresAplicado());

                        response.setPeriodo(
                                        cuenta.getPeriodo());
                }

                response.setMovimientos(
                                obtenerMovimientos(cuentaId));

                return response;
        }

        public List<MobileMovimientoResponse> obtenerMovimientos(
                        Long cuentaId) {

                List<MovimientoAnual> movimientos = movimientoAnualService.obtenerPorCuenta(cuentaId);

                List<MovimientoAnual> movimientosRecientes = movimientos.stream()
                                .limit(3)
                                .toList();

                return mapearMovimientos(movimientosRecientes);
        }

        private MobileCuentaResponse mapearCuenta(
                        List<CuentaInteres> cuentas,
                        Long cuentaId) {

                if (cuentas.isEmpty()) {
                        throw new ResourceNotFoundException(
                                        "No se encontró la cuenta con id: " + cuentaId);
                }

                CuentaInteres cuenta = cuentas.get(0);

                MobileCuentaResponse response = new MobileCuentaResponse();

                response.setCuentaId(cuenta.getCuentaId());
                response.setNombre(cuenta.getNombre());
                response.setSaldo(cuenta.getSaldo());

                return response;
        }

        private List<MobileMovimientoResponse> mapearMovimientos(
                        List<MovimientoAnual> movimientos) {

                return movimientos.stream()
                                .map(movimiento -> {

                                        MobileMovimientoResponse response = new MobileMovimientoResponse();

                                        response.setFecha(
                                                        movimiento.getFecha());

                                        response.setTransaccion(
                                                        movimiento.getTransaccion());

                                        response.setMonto(
                                                        movimiento.getMonto());

                                        response.setDescripcion(
                                                        movimiento.getDescripcion());

                                        return response;
                                })
                                .toList();
        }
}
