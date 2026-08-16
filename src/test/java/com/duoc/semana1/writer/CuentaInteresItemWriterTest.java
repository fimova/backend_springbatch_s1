package com.duoc.semana1.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.duoc.semana1.model.CuentaInteres;

@SpringBootTest
class CuentaInteresItemWriterTest {

    @Autowired
    private ItemWriter<CuentaInteres> writer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void deberiaEscribirCuentaInteres() throws Exception {

        CuentaInteres cuenta = new CuentaInteres();

        cuenta.setCuentaId(999L);
        cuenta.setNombre("Prueba Writer");
        cuenta.setSaldo(new BigDecimal("5000"));
        cuenta.setEdad(30);
        cuenta.setTipo("ahorro");
        cuenta.setInteresAplicado(new BigDecimal("100.00"));
        cuenta.setSaldoFinal(new BigDecimal("5100.00"));

        Chunk<CuentaInteres> chunk = new Chunk<>();
        chunk.add(cuenta);

        writer.write(chunk);

        Integer cantidad = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM cuentas_intereses
                WHERE cuenta_id = 999
                """,
                Integer.class
        );

        assertEquals(1, cantidad);
    }
}