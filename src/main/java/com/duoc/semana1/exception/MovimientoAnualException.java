package com.duoc.semana1.exception;

public class MovimientoAnualException extends RuntimeException{

    public MovimientoAnualException (String message){
        super(message);
    }

    public MovimientoAnualException(String message, Throwable cause){
        super(message, cause);
    }
    
}
