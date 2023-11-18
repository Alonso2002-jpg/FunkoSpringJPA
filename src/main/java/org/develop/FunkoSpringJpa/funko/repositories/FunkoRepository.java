package org.develop.FunkoSpringJpa.funko.repositories;

import org.develop.FunkoSpringJpa.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.funko.commons.mainUse.model.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FunkoRepository extends JpaRepository<Funko,Long>, JpaSpecificationExecutor<Funko> {
    List<Funko> findAllByPriceAndCategory(Double price, Categoria categoria);
    List<Funko> findAllByCategory(Categoria category);
    List<Funko> findAllByPrice(Double price);

}
