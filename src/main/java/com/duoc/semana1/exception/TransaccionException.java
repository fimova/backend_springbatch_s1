package com.duoc.semana1.exception;

public class TransaccionException extends RuntimeException {

    public TransaccionException (String message){
        super(message);
    }

    public TransaccionException (String message, Throwable cause){
        super(message, cause);
    }
    
}
