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

import com.duoc.semana1.exception.CuentaInteresException;
import com.duoc.semana1.listener.CuentaInteresStepExecutionListener;
import com.duoc.semana1.listener.JobCompletionListener;
import com.duoc.semana1.model.CuentaInteres;

@Configuration
public class CuentaInteresBatchConfig {

    @Bean
    public Step cuentaInteresStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<CuentaInteres> cuentaInteresReader,
            ItemProcessor<CuentaInteres, CuentaInteres> cuentaInteresProcessor,
            ItemWriter<CuentaInteres> cuentaInteresWriter,
            CuentaInteresStepExecutionListener cuentaInteresStepExecutionListener,
            @Qualifier ("cuentaInteresTaskExecutor") ThreadPoolTaskExecutor taskExecutor
    ) {

        return new StepBuilder("cuentaInteresStep", jobRepository)
                .<CuentaInteres, CuentaInteres>chunk(5)
                .reader(cuentaInteresReader)
                .processor(cuentaInteresProcessor)
                .writer(cuentaInteresWriter)
                .faultTolerant()
                .skip(CuentaInteresException.class)
                .skipLimit(10)
                .retryLimit(3)
                .retry(CannotAcquireLockException.class)
                .retry(TransientDataAccessException.class)
                .listener(cuentaInteresStepExecutionListener)
                .transactionManager(transactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Job cuentaInteresJob(
            JobRepository jobRepository,
            Step cuentaInteresStep,
            JobCompletionListener jobCompletionListener
    ) {

        return new JobBuilder("cuentaInteresJob", jobRepository)
                .start(cuentaInteresStep)
                .listener(jobCompletionListener)
                .build();
    }

    @Bean(name="cuentaInteresTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor () {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(3);
        executor.setThreadNamePrefix("cuentaInteresThread");
        executor.initialize();
        return executor;
    }
}