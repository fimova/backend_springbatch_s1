package com.duoc.semana1.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.duoc.semana1.listener.JobCompletionListener;
import com.duoc.semana1.listener.TransaccionStepExecutionListener;
import com.duoc.semana1.model.Transaccion;

@Configuration
public class TransaccionBatchConfig {

    @Bean
    public Step transaccionStep(
            JobRepository jobRepository,
            ItemReader<Transaccion> transaccionReader,
            ItemProcessor<Transaccion, Transaccion> transaccionProcessor,
            ItemWriter<Transaccion> transaccionWriter,
            TransaccionStepExecutionListener transaccionStepExecutionListener
    ) {

        return new StepBuilder("transaccionStep", jobRepository)
                .<Transaccion, Transaccion>chunk(10)
                .reader(transaccionReader)
                .processor(transaccionProcessor)
                .writer(transaccionWriter)
                .listener(transaccionStepExecutionListener)
                .build();
    }

    @Bean
    public Job transaccionJob(
            JobRepository jobRepository,
            Step transaccionStep,
            JobCompletionListener jobCompletionListener
    ) {
        return new JobBuilder("transaccionJob", jobRepository)
                .start(transaccionStep)
                .listener(jobCompletionListener)
                .build();
    }
    
}
