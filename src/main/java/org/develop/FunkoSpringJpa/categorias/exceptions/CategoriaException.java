package org.develop.FunkoSpringJpa.categorias.exceptions;

public abstract class CategoriaException extends RuntimeException{

    public CategoriaException(String message){
        super(message);
    }
}
