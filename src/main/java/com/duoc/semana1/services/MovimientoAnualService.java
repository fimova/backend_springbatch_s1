package com.duoc.semana1.services;

import org.springframework.stereotype.Service;

import com.duoc.semana1.model.MovimientoAnual;
import com.duoc.semana1.repository.MovimientoAnualRepository;

import java.util.List;

@Service
public class MovimientoAnualService {

    private final MovimientoAnualRepository movimientoAnualRepository;

    public MovimientoAnualService(
            MovimientoAnualRepository movimientoAnualRepository
    ) {
        this.movimientoAnualRepository = movimientoAnualRepository;
    }

    public List<MovimientoAnual> obtenerPorCuenta(Long cuentaId) {

        return movimientoAnualRepository.findByCuentaId(cuentaId);
    }
}
