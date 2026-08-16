package com.duoc.semana1.reader;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.core.io.ByteArrayResource;

import com.duoc.semana1.config.TransaccionItemReaderConfig;
import com.duoc.semana1.model.Transaccion;

class TransaccionItemReaderTest {

    @Test
    void deberiaLeerTransaccionCorrectamente() throws Exception {

        // 1. CSV pequeño para la prueba
        String csv = """
                id,fecha,monto,tipo
                1,2024-01-01,1000,debito
                """;

        // 2. Convertir el CSV en un Resource
        ByteArrayResource resource =
                new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));

        // 3. Crear el reader usando el mismo código de producción
        TransaccionItemReaderConfig config =
                new TransaccionItemReaderConfig();

        FlatFileItemReader<Transaccion> reader =
                config.transaccion(resource);

        // 4. Abrir el reader
        reader.open(new ExecutionContext());

        // 5. Leer el primer registro
        Transaccion transaccion = reader.read();

        // 6. Comprobar que los datos fueron convertidos correctamente
        assertNotNull(transaccion);

        assertEquals(1L, transaccion.getTransaccionId());
        assertEquals(
                java.time.LocalDate.of(2024, 1, 1),
                transaccion.getFecha()
        );
        assertEquals(
                new java.math.BigDecimal("1000"),
                transaccion.getMonto()
        );
        assertEquals("debito", transaccion.getTipo());

        // 7. El segundo read debe devolver null porque solo hay un registro
        assertNull(reader.read());

        // 8. Cerrar el reader
        reader.close();
    }
}
