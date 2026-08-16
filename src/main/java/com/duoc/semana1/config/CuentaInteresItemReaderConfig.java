package com.duoc.semana1.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.duoc.semana1.model.CuentaInteres;

@Configuration
public class CuentaInteresItemReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<CuentaInteres> cuentaInteresReader(
            @Value("${app.input.intereses}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<CuentaInteres>()
                .name("cuentaInteresReader")
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
                .fieldSetMapper(cuentaInteresFieldSetMapper())
                .build();
    }

    private FieldSetMapper<CuentaInteres> cuentaInteresFieldSetMapper() {

        return fieldSet -> {

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
        };
    }
}
