package org.develop.FunkoSpringJpa.funko.repositories;

import org.develop.FunkoSpringJpa.funko.commons.mainUse.model.Funko;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunkoRepository extends JpaRepository<Funko,Long> {
    List<Funko> findAllByCategoryAndPrice(String category, Double price);
    List<Funko> findAllByCategory(String category);
    List<Funko> findAllByPrice(Double price);

}
