package com.duoc.semana1.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.listener.SkipListener;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

import com.duoc.semana1.model.MovimientoAnual;

@Component
public class MovimientoAnualStepExecutionListener implements StepExecutionListener,
        SkipListener<MovimientoAnual, MovimientoAnual> {

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

        long memoriaUsada = (runtime.totalMemory() - runtime.freeMemory())
                / (1024 * 1024); //conversion a MB

        long memoriaMaxima = runtime.maxMemory()
                / (1024 * 1024); //conversion a MB

        System.out.println("======================================");
        System.out.println("FIN DEL STEP");
        System.out.println("Step: " + stepExecution.getStepName());
        System.out.println("Estado: " + stepExecution.getStatus());
        System.out.println("Leídos: " + stepExecution.getReadCount());
        System.out.println("Procesados/escritos: " + stepExecution.getWriteCount());
        System.out.println("Omitidos: " + stepExecution.getSkipCount());
        System.out.println("Errores: " + stepExecution.getFailureExceptions().size());
        System.out.println("Tiempo de ejecución: " + tiempoEjecucion + " ms");
        System.out.println("Memoria utilizada: " + memoriaUsada + " MB");
        System.out.println("Memoria máxima disponible: " + memoriaMaxima + " MB");
        System.out.println("======================================");

        return stepExecution.getExitStatus();
    }

    @Override
    public void onSkipInProcess(MovimientoAnual item, Throwable t) {

        System.out.println("REGISTRO OMITIDO");
        System.out.println("Cuenta: " + item.getCuentaId());
        System.out.println("Fecha: " + item.getFecha());
        System.out.println("Transacción: " + item.getTransaccion());
        System.out.println("Monto: " + item.getMonto());
        System.out.println("Descripción: " + item.getDescripcion());
        System.out.println("Motivo: " + t.getMessage());
    }

    @Override
    public void onSkipInRead(Throwable t) {
        System.out.println("Registro omitido durante la lectura.");
        System.out.println("Motivo: " + t.getMessage());
    }

    @Override
    public void onSkipInWrite(MovimientoAnual item, Throwable t) {

        System.out.println("Registro omitido durante la escritura.");
        System.out.println("Cuenta: " + item.getCuentaId());
        System.out.println("Motivo: " + t.getMessage());
    }
}
