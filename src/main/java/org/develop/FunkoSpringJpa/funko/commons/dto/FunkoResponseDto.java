package org.develop.FunkoSpringJpa.funko.commons.dto;

import lombok.Builder;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;

@Builder
public record FunkoResponseDto(
        String name,
        Double price,
        Integer quantity,
        String image,
        Categoria category
) {
}
