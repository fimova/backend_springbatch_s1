package com.duoc.semana1.services;

import org.springframework.stereotype.Service;

import com.duoc.semana1.model.Transaccion;
import com.duoc.semana1.repository.TransaccionRepository;

import java.util.List;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionService(
            TransaccionRepository transaccionRepository
    ) {
        this.transaccionRepository = transaccionRepository;
    }

    public List<Transaccion> obtenerTodas() {

        return transaccionRepository.findAll();
    }
}