package com.duoc.semana1.listener;

import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionListener implements JobExecutionListener {

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
    }
}
