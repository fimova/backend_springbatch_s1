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

import com.duoc.semana1.exception.MovimientoAnualException;
import com.duoc.semana1.listener.JobCompletionListener;
import com.duoc.semana1.listener.MovimientoAnualStepExecutionListener;
import com.duoc.semana1.model.MovimientoAnual;

//este es el batch del movimiento anual
@Configuration
public class BatchConfig {
    
    @Bean
    public Step movimientoAnualStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<MovimientoAnual> movimientoAnualReader,
            ItemProcessor<MovimientoAnual, MovimientoAnual> movimientoAnualProcessor,
            ItemWriter<MovimientoAnual> movimientoAnualWriter,
            MovimientoAnualStepExecutionListener movimientoAnualStepExecutionListener,
            @Qualifier ("movimientoAnualTaskExecutor") ThreadPoolTaskExecutor taskExecutor
    ) {

        return new StepBuilder("movimientoAnualStep", jobRepository)
                .<MovimientoAnual, MovimientoAnual>chunk(5)
                .reader(movimientoAnualReader)
                .processor(movimientoAnualProcessor)
                .writer(movimientoAnualWriter)
                .faultTolerant() //tolerancia hacia ciertos errores
                .skip(MovimientoAnualException.class) //este explica cual tolerancia y cual saltarse
                .skipLimit(10) //max 10 skips permitidos
                .retryLimit(3)
                .retry(CannotAcquireLockException.class)
                .retry(TransientDataAccessException.class)
                .listener(movimientoAnualStepExecutionListener)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Job movimientoAnualJob(
        JobRepository jobRepository,
        Step movimientoAnualStep,
        JobCompletionListener jobCompletionListener
    ){
        return new JobBuilder("movimientoAnualJob", jobRepository)
        .start(movimientoAnualStep)
        .listener(jobCompletionListener)
        .build();
    }

    @Bean(name="movimientoAnualTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor () {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(3);
        executor.setThreadNamePrefix("movimientoAnualThread");
        executor.initialize();
        return executor;
    }
}
