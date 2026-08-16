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

import com.duoc.semana1.model.MovimientoAnual;

@Configuration
public class MovimientoAnualItemReaderConfig {
    
    //este metodo lee el archivo
    @Bean
    @StepScope
    public FlatFileItemReader<MovimientoAnual> movimientoAnualReader(
        @Value("${app.input.cuentas-anuales}") Resource inputFile) {

            return new FlatFileItemReaderBuilder<MovimientoAnual>()
                .name("movimientoAnualReader")
                .resource(inputFile)
                .encoding("UTF-8")
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names(
                    "cuentaId",
                    "fecha",
                    "transaccion",
                    "monto",
                    "descripcion"
                )
                .fieldSetMapper(movimientoAnualFieldSetMapper())
                .build();
        }

    //este metodo es auxiliar para mapear los datos que se están leyendo
    private FieldSetMapper<MovimientoAnual> movimientoAnualFieldSetMapper(){

        return fieldSet -> {
            
            MovimientoAnual movimientoAnual = new MovimientoAnual();

            movimientoAnual.setCuentaId(
                fieldSet.readLong("cuentaId")
            );

            movimientoAnual.setFecha(
                convertirFecha(
                    fieldSet.readString("fecha")
                )
            );

            movimientoAnual.setTransaccion(
                fieldSet.readString("transaccion")
            );

            movimientoAnual.setMonto(
                fieldSet.readBigDecimal("monto")
            );

            movimientoAnual.setDescripcion(
                fieldSet.readString("descripcion")
            );

            return movimientoAnual;
        };
    }

    private LocalDate convertirFecha(String valor) {
        if (valor==null || valor.isBlank()) {
            return null;
        }

        return LocalDate.parse(valor.trim());
    }
        
}
