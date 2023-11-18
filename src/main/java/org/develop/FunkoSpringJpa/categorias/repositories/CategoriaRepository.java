package org.develop.FunkoSpringJpa.categorias.repositories;

import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria,Long>, JpaSpecificationExecutor<Categoria> {

    Optional<Categoria> findByNameCategoryIgnoreCase(String nameCategory);
}
