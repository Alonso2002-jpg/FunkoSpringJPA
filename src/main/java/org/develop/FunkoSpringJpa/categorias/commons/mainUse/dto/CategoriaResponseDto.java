package org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto;

import lombok.Builder;

@Builder
public record CategoriaResponseDto(
        String nameCategory,
        Boolean isActive
) {
}
