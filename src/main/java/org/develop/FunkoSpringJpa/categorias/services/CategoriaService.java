package org.develop.FunkoSpringJpa.categorias.services;

import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaCreateDto;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaUpdateDto;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;

import java.util.List;

public interface CategoriaService {

    List<Categoria> getAll();
    Categoria getById(Long id);
    Categoria save(CategoriaCreateDto t);

    void deleteById(Long id);
    Categoria update(Long id, CategoriaUpdateDto funko);

    void deleteAll();
}
