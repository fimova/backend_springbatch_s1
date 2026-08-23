package com.duoc.semana1.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

@Component
public class TransaccionStepExecutionListener
        implements StepExecutionListener {

    private long inicio;

    @Override
    public void beforeStep(StepExecution stepExecution) {

        inicio = System.currentTimeMillis();

        System.out.println("======================================");
        System.out.println("INICIO DEL STEP");
        System.out.println("Step: " + stepExecution.getStepName());
        System.out.println("======================================");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        long tiempoEjecucion = System.currentTimeMillis() - inicio;

        Runtime runtime = Runtime.getRuntime();

        long memoriaUsada =
                (runtime.totalMemory() - runtime.freeMemory())
                / (1024 * 1024);

        long memoriaMaxima =
                runtime.maxMemory()
                / (1024 * 1024);

        System.out.println("======================================");
        System.out.println("FIN DEL STEP");
        System.out.println("Step: " + stepExecution.getStepName());
        System.out.println("Estado: " + stepExecution.getStatus());
        System.out.println("Leídos: " + stepExecution.getReadCount());
        System.out.println("Procesados/escritos: " + stepExecution.getWriteCount());
        System.out.println("Omitidos: " + stepExecution.getSkipCount());
        System.out.println("Errores: " +
                stepExecution.getFailureExceptions().size());
        System.out.println("Tiempo de ejecución: " + tiempoEjecucion + " ms");
        System.out.println("Memoria utilizada: " + memoriaUsada + " MB");
        System.out.println("Memoria máxima disponible: " + memoriaMaxima + " MB");
        System.out.println("======================================");

        return stepExecution.getExitStatus();
    }
}
