package com.duoc.semana1.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.duoc.semana1.exception.CuentaInteresException;
import com.duoc.semana1.model.CuentaInteres;

class CuentaInteresItemProcessorTest {

    private CuentaInteresItemProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new CuentaInteresItemProcessor();
    }

    @Test
    void deberiaCalcularInteresDeCuentaAhorro() throws Exception {

        CuentaInteres cuenta = new CuentaInteres();

        cuenta.setCuentaId(101L);
        cuenta.setNombre("John Doe");
        cuenta.setSaldo(new BigDecimal("5000"));
        cuenta.setEdad(30);
        cuenta.setTipo("ahorro");

        CuentaInteres resultado = processor.process(cuenta);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("100.00"), resultado.getInteresAplicado());
        assertEquals(new BigDecimal("5100.00"), resultado.getSaldoFinal());
    }

    @Test
    void deberiaCalcularInteresDePrestamo() throws Exception {

        CuentaInteres cuenta = new CuentaInteres();

        cuenta.setCuentaId(102L);
        cuenta.setNombre("Jane Smith");
        cuenta.setSaldo(new BigDecimal("8000"));
        cuenta.setEdad(25);
        cuenta.setTipo("prestamo");

        CuentaInteres resultado = processor.process(cuenta);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("400.00"), resultado.getInteresAplicado());
        assertEquals(new BigDecimal("8400.00"), resultado.getSaldoFinal());
    }

    @Test
    void deberiaNormalizarNombreYTipo() throws Exception {

        CuentaInteres cuenta = new CuentaInteres();

        cuenta.setCuentaId(103L);
        cuenta.setNombre("  Bob Johnson  ");
        cuenta.setSaldo(new BigDecimal("12000"));
        cuenta.setEdad(30);
        cuenta.setTipo("  AHORRO  ");

        CuentaInteres resultado = processor.process(cuenta);

        assertEquals("Bob Johnson", resultado.getNombre());
        assertEquals("ahorro", resultado.getTipo());
    }

    @Test
    void deberiaRechazarSaldoNegativo() {

        CuentaInteres cuenta = new CuentaInteres();

        cuenta.setCuentaId(104L);
        cuenta.setNombre("Alice Brown");
        cuenta.setSaldo(new BigDecimal("-100"));
        cuenta.setEdad(45);
        cuenta.setTipo("ahorro");

        assertThrows(
                CuentaInteresException.class,
                () -> processor.process(cuenta)
        );
    }

    @Test
    void deberiaRechazarTipoHipoteca() {

        CuentaInteres cuenta = new CuentaInteres();

        cuenta.setCuentaId(105L);
        cuenta.setNombre("Charlie Green");
        cuenta.setSaldo(new BigDecimal("7000"));
        cuenta.setEdad(35);
        cuenta.setTipo("hipoteca");

        assertThrows(
                CuentaInteresException.class,
                () -> processor.process(cuenta)
        );
    }

    @Test
    void deberiaRechazarNombreVacio() {

        CuentaInteres cuenta = new CuentaInteres();

        cuenta.setCuentaId(106L);
        cuenta.setNombre("   ");
        cuenta.setSaldo(new BigDecimal("5000"));
        cuenta.setEdad(30);
        cuenta.setTipo("ahorro");

        assertThrows(
                CuentaInteresException.class,
                () -> processor.process(cuenta)
        );
    }
}
