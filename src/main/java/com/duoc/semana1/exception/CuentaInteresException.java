package com.duoc.semana1.exception;

public class CuentaInteresException extends RuntimeException{

    public CuentaInteresException (String message) {
        super (message);
    }

    public CuentaInteresException (String message, Throwable cause){
        super(message, cause);
    }
    
}
