package com.duoc.semana1.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.duoc.semana1.exception.TransaccionException;
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
            TransaccionStepExecutionListener transaccionStepExecutionListener,
            @Qualifier ("transaccionTaskExecutor") ThreadPoolTaskExecutor taskExecutor
    ) {

        return new StepBuilder("transaccionStep", jobRepository)
                .<Transaccion, Transaccion>chunk(5)
                .reader(transaccionReader)
                .processor(transaccionProcessor)
                .writer(transaccionWriter)
                .faultTolerant()
                .retryLimit(3)
                .retry(CannotAcquireLockException.class)
                .retry(TransientDataAccessException.class)
                .listener(transaccionStepExecutionListener)
                .taskExecutor(taskExecutor)
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

    @Bean(name="transaccionTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor () {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(3);
        executor.setThreadNamePrefix("transaccionThread");
        executor.initialize();
        return executor;
    }
    
}
