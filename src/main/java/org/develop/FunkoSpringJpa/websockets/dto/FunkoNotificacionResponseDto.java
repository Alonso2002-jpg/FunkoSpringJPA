package org.develop.FunkoSpringJpa.websockets.dto;

import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;

public record FunkoNotificacionResponseDto(
        String name,
        Double price,
        Integer quantity,
        String image,
        String categoria,
        String createdAt,
        String updatedAt
) {
}
