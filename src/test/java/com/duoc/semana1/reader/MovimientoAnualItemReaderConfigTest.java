package com.duoc.semana1.reader;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.duoc.semana1.config.MovimientoAnualItemReaderConfig;
import com.duoc.semana1.model.MovimientoAnual;

/*class MovimientoAnualItemReaderConfigTest {

    private FlatFileItemReader<MovimientoAnual> reader;

    @BeforeEach
    void setUp() {
        MovimientoAnualItemReaderConfig config =
                new MovimientoAnualItemReaderConfig();

        Resource resource =
                new ClassPathResource("cuentas_anuales.csv");

        reader = config.movimientoAnualReader(resource);

        reader.open(new ExecutionContext());
    }

    @AfterEach
    void tearDown() {
        reader.close();
    }

    @Test
    void deberiaLeerPrimerMovimiento() throws Exception {

        MovimientoAnual movimiento = reader.read();

        assertNotNull(movimiento);

        assertEquals(101L, movimiento.getCuentaId());
        assertEquals(
                LocalDate.of(2024, 1, 1),
                movimiento.getFecha()
        );
        assertEquals("deposito", movimiento.getTransaccion());
        assertEquals(
                new BigDecimal("1000"),
                movimiento.getMonto()
        );
        assertEquals(
                "Ingreso mensual",
                movimiento.getDescripcion()
        );
    }

    @Test
    void deberiaLeerTodasLasTransacciones() throws Exception {

        int cantidad = 0;
        MovimientoAnual movimiento;

        while ((movimiento = reader.read()) != null) {
            cantidad++;
        }

        assertEquals(9, cantidad);
    }
}
    */
