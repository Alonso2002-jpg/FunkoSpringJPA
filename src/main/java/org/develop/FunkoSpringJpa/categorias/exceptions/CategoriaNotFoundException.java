package org.develop.FunkoSpringJpa.categorias.exceptions;

public class CategoriaNotFoundException extends CategoriaException{
    public CategoriaNotFoundException(Long id) {
        super("Categoria not found with ID: " + id);
    }
}
