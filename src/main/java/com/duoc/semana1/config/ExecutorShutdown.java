package com.duoc.semana1.config;

import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Component
public class ExecutorShutdown {

    private final ThreadPoolTaskExecutor movimientoAnualTaskExecutor;
    private final ThreadPoolTaskExecutor cuentaInteresTaskExecutor;
    private final ThreadPoolTaskExecutor transaccionTaskExecutor;

    public ExecutorShutdown(

            @Qualifier("movimientoAnualTaskExecutor") ThreadPoolTaskExecutor movimientoAnualTaskExecutor,
            @Qualifier("cuentaInteresTaskExecutor") ThreadPoolTaskExecutor cuentaInteresTaskExecutor,
            @Qualifier("transaccionTaskExecutor") ThreadPoolTaskExecutor transaccionTaskExecutor) {

        this.movimientoAnualTaskExecutor = movimientoAnualTaskExecutor;
        this.cuentaInteresTaskExecutor = cuentaInteresTaskExecutor;
        this.transaccionTaskExecutor = transaccionTaskExecutor;
    }

    @PreDestroy
    public void shutdown() {

        movimientoAnualTaskExecutor.shutdown();
        cuentaInteresTaskExecutor.shutdown();
        transaccionTaskExecutor.shutdown();

        System.out.println("======================================");
        System.out.println("EXECUTORS CERRADOS");
        System.out.println("======================================");
    }
}
