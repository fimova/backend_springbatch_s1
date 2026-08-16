package com.duoc.semana1.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

@Component
public class CuentaInteresStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {

        System.out.println("======================================");
        System.out.println("INICIO DEL STEP");
        System.out.println("Step: " + stepExecution.getStepName());
        System.out.println("======================================");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        System.out.println("======================================");
        System.out.println("FIN DEL STEP");
        System.out.println("Step: " + stepExecution.getStepName());
        System.out.println("Estado: " + stepExecution.getStatus());
        System.out.println("Leídos: " + stepExecution.getReadCount());
        System.out.println("Procesados/escritos: " + stepExecution.getWriteCount());
        System.out.println("Omitidos: " + stepExecution.getSkipCount());
        System.out.println("Errores: " + stepExecution.getFailureExceptions().size());
        System.out.println("======================================");

        return stepExecution.getExitStatus();
    }
}
