package org.develop.FunkoSpringJpa.categorias.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoriaExistException extends CategoriaException{
    public CategoriaExistException(String name) {
        super("Categoria already exists with Name: " + name);
    }
}
