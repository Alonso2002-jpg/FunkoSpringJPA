package org.develop.FunkoSpringJpa.categorias.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNotFoundException extends CategoriaException{
    public CategoriaNotFoundException(Long id) {
        super("Categoria not found with ID: " + id);
    }
}
