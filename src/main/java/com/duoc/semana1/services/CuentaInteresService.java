package com.duoc.semana1.services;

import org.springframework.stereotype.Service;

import com.duoc.semana1.model.CuentaInteres;
import com.duoc.semana1.repository.CuentaInteresRepository;

import java.util.List;

@Service
public class CuentaInteresService {

    private final CuentaInteresRepository cuentaInteresRepository;

    public CuentaInteresService(
            CuentaInteresRepository cuentaInteresRepository
    ) {
        this.cuentaInteresRepository = cuentaInteresRepository;
    }

    public List<CuentaInteres> obtenerPorCuenta(Long cuentaId) {

        return cuentaInteresRepository.findByCuentaId(cuentaId);
    }
}
