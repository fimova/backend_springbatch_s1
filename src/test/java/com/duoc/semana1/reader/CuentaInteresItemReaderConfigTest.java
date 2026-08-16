package com.duoc.semana1.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.duoc.semana1.model.CuentaInteres;

class CuentaInteresItemReaderConfigTest {

    private FlatFileItemReader<CuentaInteres> reader;

    @BeforeEach
    void setUp() {

        Resource inputFile =
                new ClassPathResource("intereses.csv");

        reader = new FlatFileItemReaderBuilder<CuentaInteres>()
                .name("cuentaInteresReaderTest")
                .resource(inputFile)
                .encoding("UTF-8")
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names(
                        "cuentaId",
                        "nombre",
                        "saldo",
                        "edad",
                        "tipo"
                )
                .fieldSetMapper(fieldSet -> {

                    CuentaInteres cuenta = new CuentaInteres();

                    cuenta.setCuentaId(
                            fieldSet.readLong("cuentaId"));

                    cuenta.setNombre(
                            fieldSet.readString("nombre"));

                    cuenta.setSaldo(
                            fieldSet.readBigDecimal("saldo"));

                    cuenta.setEdad(
                            fieldSet.readInt("edad"));

                    cuenta.setTipo(
                            fieldSet.readString("tipo"));

                    return cuenta;
                })
                .build();

        reader.open(new ExecutionContext());
    }

    @AfterEach
    void tearDown() {
        reader.close();
    }

    @Test
    void deberiaLeerPrimeraCuentaCorrectamente() throws Exception {

        CuentaInteres cuenta = reader.read();

        assertNotNull(cuenta);

        assertEquals(101L, cuenta.getCuentaId());
        assertEquals("John Doe", cuenta.getNombre());
        assertEquals(new BigDecimal("5000"), cuenta.getSaldo());
        assertEquals(30, cuenta.getEdad());
        assertEquals("ahorro", cuenta.getTipo());
    }
}
