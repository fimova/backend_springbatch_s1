package com.duoc.semana1.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.duoc.semana1.config.ExecutorShutdown;

@Component
public class JobCompletionListener implements JobExecutionListener {

    private static final Logger logger =
            LoggerFactory.getLogger(JobCompletionListener.class);

    private final ExecutorShutdown executorShutdown;

    public JobCompletionListener(ExecutorShutdown executorShutdown) {
        this.executorShutdown = executorShutdown;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

        logger.info(
                "Inicio del Job. Job={}, JobExecutionId={}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId()
        );
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        logger.info(
                "Fin del Job. Job={}, JobExecutionId={}, Estado={}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId(),
                jobExecution.getStatus()
        );

        if (jobExecution.getStatus() == BatchStatus.FAILED) {

            logger.error(
                    "El Job finalizó con errores. Job={}, JobExecutionId={}, Estado={}, Errores={}",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getId(),
                    jobExecution.getStatus(),
                    jobExecution.getAllFailureExceptions().size()
            );
        }

        executorShutdown.shutdown();
    }
}