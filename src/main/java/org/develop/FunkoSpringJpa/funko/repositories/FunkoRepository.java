package org.develop.FunkoSpringJpa.funko.repositories;

import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;
import org.develop.FunkoSpringJpa.funko.commons.mainUse.model.Funko;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunkoRepository extends JpaRepository<Funko,Long> {
    List<Funko> findAllByPriceAndCategory(Double price, Categoria categoria);
    List<Funko> findAllByCategory(Categoria category);
    List<Funko> findAllByPrice(Double price);

}
