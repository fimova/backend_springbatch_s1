package com.duoc.semana1.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.duoc.semana1.model.MovimientoAnual;

@SpringBootTest(properties = {
    "spring.sql.init.mode=never"
})
class MovimientoAnualItemWriterTest {

    @Autowired
    private ItemWriter<MovimientoAnual> writer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void deberiaEscribirMovimientoEnBaseDeDatos() throws Exception {

        MovimientoAnual movimiento = new MovimientoAnual();

        movimiento.setCuentaId(999L);
        movimiento.setFecha(LocalDate.of(2024, 1, 1));
        movimiento.setTransaccion("deposito");
        movimiento.setMonto(new BigDecimal("1000"));
        movimiento.setDescripcion("Prueba Writer");

        writer.write(new Chunk<>(List.of(movimiento)));

        Integer cantidad = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM movimientos_anuales
                WHERE cuenta_id = 999
                """,
                Integer.class
        );

        assertEquals(1, cantidad);
    }
}