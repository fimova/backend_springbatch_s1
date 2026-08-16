package com.duoc.semana1.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.duoc.semana1.model.Transaccion;

@SpringBootTest
class TransaccionItemWriterTest {

    @Autowired
    private ItemWriter<Transaccion> transaccionWriter;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void deberiaEscribirTransaccion() throws Exception {

        // 1. Crear transacción de prueba
        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionId(999L);
        transaccion.setFecha(LocalDate.of(2024, 1, 10));
        transaccion.setMonto(new BigDecimal("1500"));
        transaccion.setTipo("credito");
        transaccion.setAnomalia(false);
        transaccion.setMotivoAnomalia(null);

        // 2. Ejecutar writer
        transaccionWriter.write(
            new Chunk<>(List.of(transaccion))
        );

        // 3. Consultar BD
        Map<String, Object> resultado = jdbcTemplate.queryForMap(
            """
            SELECT transaccion_id, fecha, monto, tipo, anomalia, motivo_anomalia
            FROM transacciones_diarias
            WHERE transaccion_id = 999
            """
        );

        // 4. Verificar
        assertEquals(999L, ((Number) resultado.get("TRANSACCION_ID")).longValue());
        assertEquals(new BigDecimal("1500"), resultado.get("MONTO"));
        assertEquals("credito", resultado.get("TIPO"));
        assertEquals(0, ((Number) resultado.get("ANOMALIA")).intValue());
        assertNull(resultado.get("MOTIVO_ANOMALIA"));
    }
}
