package org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CategoriaUpdateDto(
        @Pattern(regexp = "^(MARVEL|DISNEY|ANIME|OTROS)$", message = "Category must be MARVEL, DISNEY, ANIME or OTROS")
        String nameCategory,
        Boolean isActive
) {
}
