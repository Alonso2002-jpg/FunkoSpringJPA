package org.develop.FunkoSpringJpa.categorias.repositories;

import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

    Optional<Categoria> findByNameCategoryIgnoreCase(String nameCategory);
}
