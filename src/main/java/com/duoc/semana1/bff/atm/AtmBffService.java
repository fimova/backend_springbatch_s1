package com.duoc.semana1.bff.atm;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.semana1.bff.atm.dto.AtmSaldoResponse;
import com.duoc.semana1.exception.InsufficientBalanceException;
import com.duoc.semana1.exception.ResourceNotFoundException;
import com.duoc.semana1.model.CuentaInteres;
import com.duoc.semana1.model.OperacionAtm;
import com.duoc.semana1.repository.OperacionAtmRepository;
import com.duoc.semana1.services.CuentaInteresService;

//orquesta y compone la info para mostrar en cajero, pero en los datos actuales persistidos en BD post jobs
//no existe información del saldo actual, asi que se considera el saldo incial como el más "actual"
@Service
public class AtmBffService {

        private final CuentaInteresService cuentaInteresService;
        private final OperacionAtmRepository operacionAtmRepository;

        public AtmBffService(
                        CuentaInteresService cuentaInteresService,
                        OperacionAtmRepository operacionAtmRepository) {
                this.cuentaInteresService = cuentaInteresService;
                this.operacionAtmRepository = operacionAtmRepository;
        }

        public AtmSaldoResponse obtenerSaldo(Long cuentaId) {

                List<CuentaInteres> cuentas = cuentaInteresService.obtenerPorCuenta(cuentaId);

                if (cuentas.isEmpty()) {
                        throw new ResourceNotFoundException(
                                        "No se encontró la cuenta con id: " + cuentaId);
                }

                CuentaInteres cuenta = cuentas.stream()
                                .max(Comparator.comparing(CuentaInteres::getPeriodo))
                                .orElseThrow();

                BigDecimal saldo = cuenta.getSaldo();

                List<OperacionAtm> operaciones = operacionAtmRepository.findByCuentaId(cuentaId);

                for (OperacionAtm operacion : operaciones) {

                        if ("DEPOSITO".equals(operacion.getTipo())) {
                                saldo = saldo.add(operacion.getMonto());
                        }

                        if ("RETIRO".equals(operacion.getTipo())) {
                                saldo = saldo.subtract(operacion.getMonto());
                        }
                }

                AtmSaldoResponse response = new AtmSaldoResponse();

                response.setCuentaId(cuenta.getCuentaId());
                response.setSaldo(saldo);

                return response;
        }

        // guarda la operacion en la base de datos, sin diferenciar
        private void registrarOperacion(
                        Long cuentaId,
                        String tipo,
                        BigDecimal monto) {
                OperacionAtm operacion = new OperacionAtm();

                operacion.setCuentaId(cuentaId);
                operacion.setTipo(tipo);
                operacion.setMonto(monto);
                operacion.setFecha(LocalDateTime.now());

                operacionAtmRepository.save(operacion);
        }

        // registra un deposito
        public void registrarDeposito(
                        Long cuentaId,
                        BigDecimal monto) {

                obtenerSaldo(cuentaId);

                registrarOperacion(
                                cuentaId,
                                "DEPOSITO",
                                monto);
        }

        // registra un retiro
        public void registrarRetiro(
                        Long cuentaId,
                        BigDecimal monto) {

                AtmSaldoResponse saldoActual = obtenerSaldo(cuentaId);

                if (monto.compareTo(saldoActual.getSaldo()) > 0) {
                        throw new InsufficientBalanceException(
                                        "Saldo insuficiente para realizar el retiro");
                }

                registrarOperacion(
                                cuentaId,
                                "RETIRO",
                                monto);
        }
}
