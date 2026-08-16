package com.duoc.semana1.config;

import java.time.LocalDate;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.duoc.semana1.model.Transaccion;

@Configuration
public class TransaccionItemReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<Transaccion> transaccion (
        @Value("${app.input.transacciones}") Resource inputFile) {

            return new FlatFileItemReaderBuilder<Transaccion>()
                .name("transaccionReader")
                .resource(inputFile)
                .encoding("UTF-8")
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names(
                    "transaccionId",
                    "fecha",
                    "monto",
                    "tipo"
                )
                .fieldSetMapper(transaccionFieldSetMapper())
                .build();
        }
    
    private FieldSetMapper<Transaccion> transaccionFieldSetMapper(){

        return fieldSet -> {
            
            Transaccion transaccion = new Transaccion();

            transaccion.setTransaccionId(
                fieldSet.readLong("transaccionId")
            );

            transaccion.setFecha(
                convertirFecha(
                    fieldSet.readString("fecha")
                )
            );

            transaccion.setMonto(
                fieldSet.readBigDecimal("monto")
            );

            transaccion.setTipo(
                fieldSet.readString("tipo")
            );

            return transaccion;
        };
    }

    private LocalDate convertirFecha(String valor) {
        if (valor==null || valor.isBlank()) {
            return null;
        }

        return LocalDate.parse(valor.trim());
    }
}
