package org.develop.FunkoSpringJpa.funko.exceptions;

public abstract class FunkoException extends RuntimeException{
    FunkoException(String message){
        super(message);
    }
}
