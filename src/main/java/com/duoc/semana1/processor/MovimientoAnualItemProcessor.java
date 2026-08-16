package com.duoc.semana1.processor;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.duoc.semana1.exception.MovimientoAnualException;
import com.duoc.semana1.model.MovimientoAnual;

@Component
public class MovimientoAnualItemProcessor
        implements ItemProcessor<MovimientoAnual, MovimientoAnual> {

    @Override
    public MovimientoAnual process(MovimientoAnual movimientoAnual) {

        // Validación de datos
        if (movimientoAnual.getCuentaId() == null || movimientoAnual.getCuentaId() <= 0) {
            throw new MovimientoAnualException("La cuentaId es obligatoria y debe ser mayor a cero.");
        }

        if (movimientoAnual.getFecha() == null) {
            throw new MovimientoAnualException("La fecha es obligatoria.");
        }

        if (movimientoAnual.getMonto() == null) {
            throw new MovimientoAnualException("El monto es obligatorio.");
        }

        if (movimientoAnual.getTransaccion() == null || movimientoAnual.getTransaccion().isBlank()) {
            throw new MovimientoAnualException("El tipo de transacción es obligatorio.");
        }

        if (movimientoAnual.getDescripcion() == null || movimientoAnual.getDescripcion().isBlank()) {
            throw new MovimientoAnualException("La descripción es obligatoria.");
        }

        // Normalización
        String transaccion = movimientoAnual.getTransaccion()
                .trim()
                .toLowerCase();

        movimientoAnual.setTransaccion(transaccion);

        movimientoAnual.setDescripcion(movimientoAnual.getDescripcion().trim()
        );

        // Validación del tipo de transacción
        if (!transaccion.equals("deposito")
                && !transaccion.equals("retiro")
                && !transaccion.equals("compra")) {

            throw new MovimientoAnualException("Tipo de transacción no válido: " + transaccion);
        }

        // Validación del monto según el tipo de transacción
        if (transaccion.equals("deposito")
                && movimientoAnual.getMonto().signum() <= 0) {

            throw new MovimientoAnualException("Un depósito debe tener un monto mayor a cero.");
        }

        if ((transaccion.equals("retiro")
                || transaccion.equals("compra"))
                && movimientoAnual.getMonto().signum() >= 0) {

            throw new MovimientoAnualException("Un retiro o compra debe tener un monto menor a cero.");
        }

        return movimientoAnual;
    }
}
