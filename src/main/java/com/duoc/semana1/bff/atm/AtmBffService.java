package com.duoc.semana1.bff.atm;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.semana1.bff.atm.dto.AtmSaldoResponse;
import com.duoc.semana1.model.CuentaInteres;
import com.duoc.semana1.services.CuentaInteresService;

//orquesta y compone la info para mostrar en cajero, pero en los datos actuales persistidos en BD post jobs
//no existe información del saldo actual, asi que se considera el saldo incial como el más "actual"
@Service
public class AtmBffService {

    private final CuentaInteresService cuentaInteresService;

    public AtmBffService(
            CuentaInteresService cuentaInteresService
    ) {
        this.cuentaInteresService = cuentaInteresService;
    }

    public AtmSaldoResponse obtenerSaldo(Long cuentaId) {

        List<CuentaInteres> cuentas =
                cuentaInteresService.obtenerPorCuenta(cuentaId);

        if (cuentas.isEmpty()) {
            return null;
        }

        CuentaInteres cuenta = cuentas.stream()
                .max(
                        Comparator.comparing(
                                CuentaInteres::getPeriodo
                        )
                )
                .orElseThrow();

        AtmSaldoResponse response =
                new AtmSaldoResponse();

        response.setCuentaId(
                cuenta.getCuentaId()
        );

        response.setSaldo(
                cuenta.getSaldo()
        );

        return response;
    }
}
