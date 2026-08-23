package com.duoc.semana1.listener;

import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

import com.duoc.semana1.config.ExecutorShutdown;

@Component
public class JobCompletionListener implements JobExecutionListener {

    private final ExecutorShutdown executorShutdown;

    public JobCompletionListener(ExecutorShutdown executorShutdown) {
        this.executorShutdown = executorShutdown;
    }
    

    @Override
    public void beforeJob(JobExecution jobExecution) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("INICIO DEL JOB");
        System.out.println("Job: " + jobExecution.getJobInstance().getJobName());
        System.out.println("======================================");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        System.out.println("======================================");
        System.out.println("FIN DEL JOB");
        System.out.println("Job: " + jobExecution.getJobInstance().getJobName());
        System.out.println("Estado: " + jobExecution.getStatus());
        System.out.println("======================================");

        executorShutdown.shutdown();
    }
}
