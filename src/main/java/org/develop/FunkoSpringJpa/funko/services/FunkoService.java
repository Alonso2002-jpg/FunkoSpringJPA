package org.develop.FunkoSpringJpa.funko.services;

import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.funko.commons.mainUse.model.Funko;

import java.util.List;

public interface FunkoService {
    List<Funko> getAll(Double price, Long category);
    Funko findById(Long id);
    Funko save(FunkoCreateDto t);

    void deleteById(Long id);
    Funko update(Long id, FunkoUpdateDto funko);

    void deleteAll();
}
