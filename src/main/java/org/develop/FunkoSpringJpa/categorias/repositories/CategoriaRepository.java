package org.develop.FunkoSpringJpa.categorias.repositories;

import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
}
