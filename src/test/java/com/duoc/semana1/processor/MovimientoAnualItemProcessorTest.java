package com.duoc.semana1.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.duoc.semana1.exception.MovimientoAnualException;
import com.duoc.semana1.model.MovimientoAnual;

class MovimientoAnualItemProcessorTest {

    private MovimientoAnualItemProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new MovimientoAnualItemProcessor();
    }

    @Test
    void deberiaProcesarMovimientoValido() {

        MovimientoAnual movimiento = new MovimientoAnual();

        movimiento.setCuentaId(101L);
        movimiento.setFecha(LocalDate.of(2024, 1, 1));
        movimiento.setTransaccion("DEPOSITO");
        movimiento.setMonto(new BigDecimal("1000"));
        movimiento.setDescripcion(" Ingreso mensual ");

        MovimientoAnual resultado = processor.process(movimiento);

        assertNotNull(resultado);

        assertEquals("deposito", resultado.getTransaccion());
        assertEquals("Ingreso mensual", resultado.getDescripcion());
    }

    @Test
    void deberiaRechazarCuentaIdNula() {

        MovimientoAnual movimiento = new MovimientoAnual();

        movimiento.setCuentaId(null);
        movimiento.setFecha(LocalDate.of(2024, 1, 1));
        movimiento.setTransaccion("deposito");
        movimiento.setMonto(new BigDecimal("1000"));
        movimiento.setDescripcion("Ingreso mensual");

        assertThrows(
            MovimientoAnualException.class,
            () -> processor.process(movimiento)
        );
    }

    @Test
    void deberiaRechazarDepositoConMontoCero() {

        MovimientoAnual movimiento = new MovimientoAnual();

        movimiento.setCuentaId(107L);
        movimiento.setFecha(LocalDate.of(2024, 12, 25));
        movimiento.setTransaccion("deposito");
        movimiento.setMonto(BigDecimal.ZERO);
        movimiento.setDescripcion("Ingreso navideño");

        assertThrows(
            MovimientoAnualException.class,
            () -> processor.process(movimiento)
        );
    }

    @Test
    void deberiaRechazarTipoTransaccionInvalido() {

        MovimientoAnual movimiento = new MovimientoAnual();

        movimiento.setCuentaId(101L);
        movimiento.setFecha(LocalDate.of(2024, 1, 1));
        movimiento.setTransaccion("transferencia");
        movimiento.setMonto(new BigDecimal("1000"));
        movimiento.setDescripcion("Transferencia bancaria");

        assertThrows(
            MovimientoAnualException.class,
            () -> processor.process(movimiento)
        );
    }
}

