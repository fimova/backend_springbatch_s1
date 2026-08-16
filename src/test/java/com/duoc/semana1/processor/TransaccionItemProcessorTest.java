package com.duoc.semana1.processor;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.duoc.semana1.exception.TransaccionException;
import com.duoc.semana1.model.Transaccion;

class TransaccionItemProcessorTest {

    private TransaccionItemProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new TransaccionItemProcessor();
    }

    @Test
    void deberiaProcesarTransaccionValida() throws Exception {

        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionId(1L);
        transaccion.setFecha(LocalDate.of(2024, 1, 1));
        transaccion.setMonto(new BigDecimal("1000"));
        transaccion.setTipo("debito");

        Transaccion resultado = processor.process(transaccion);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getTransaccionId());
        assertEquals("debito", resultado.getTipo());
        assertFalse(resultado.isAnomalia());
        assertNull(resultado.getMotivoAnomalia());
    }

    @Test
    void deberiaNormalizarTipo() throws Exception {

        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionId(1L);
        transaccion.setFecha(LocalDate.of(2024, 1, 1));
        transaccion.setMonto(new BigDecimal("1000"));
        transaccion.setTipo("  DEBITO  ");

        Transaccion resultado = processor.process(transaccion);

        assertEquals("debito", resultado.getTipo());
    }

    @Test
    void deberiaDetectarMontoNegativoComoAnomalia() throws Exception {

        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionId(3L);
        transaccion.setFecha(LocalDate.of(2024, 1, 3));
        transaccion.setMonto(new BigDecimal("-200"));
        transaccion.setTipo("debito");

        Transaccion resultado = processor.process(transaccion);

        assertTrue(resultado.isAnomalia());
        assertEquals(
            "El monto debe ser mayor a cero.",
            resultado.getMotivoAnomalia()
        );
    }

    @Test
    void deberiaDetectarMontoCeroComoAnomalia() throws Exception {

        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionId(4L);
        transaccion.setFecha(LocalDate.of(2024, 1, 3));
        transaccion.setMonto(BigDecimal.ZERO);
        transaccion.setTipo("debito");

        Transaccion resultado = processor.process(transaccion);

        assertTrue(resultado.isAnomalia());
        assertEquals(
            "El monto debe ser mayor a cero.",
            resultado.getMotivoAnomalia()
        );
    }

    @Test
    void deberiaLanzarExcepcionSiIdEsInvalido() {

        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionId(0L);
        transaccion.setFecha(LocalDate.of(2024, 1, 1));
        transaccion.setMonto(new BigDecimal("1000"));
        transaccion.setTipo("debito");

        assertThrows(
            TransaccionException.class,
            () -> processor.process(transaccion)
        );
    }

    @Test
    void deberiaLanzarExcepcionSiFechaEsNula() {

        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionId(1L);
        transaccion.setFecha(null);
        transaccion.setMonto(new BigDecimal("1000"));
        transaccion.setTipo("debito");

        assertThrows(
            TransaccionException.class,
            () -> processor.process(transaccion)
        );
    }

    @Test
    void deberiaLanzarExcepcionSiMontoEsNulo() {

        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionId(1L);
        transaccion.setFecha(LocalDate.of(2024, 1, 1));
        transaccion.setMonto(null);
        transaccion.setTipo("debito");

        assertThrows(
            TransaccionException.class,
            () -> processor.process(transaccion)
        );
    }

    @Test
    void deberiaLanzarExcepcionSiTipoEsInvalido() {

        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionId(1L);
        transaccion.setFecha(LocalDate.of(2024, 1, 1));
        transaccion.setMonto(new BigDecimal("1000"));
        transaccion.setTipo("transferencia");

        assertThrows(
            TransaccionException.class,
            () -> processor.process(transaccion)
        );
    }
}
