package org.develop.FunkoSpringJpa.categorias.commons.mainUse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CATEGORIAS")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^(MARVEL|DISNEY|ANIME|OTROS)$", message = "Category must be MARVEL, DISNEY, ANIME or OTROS")
    private String nameCategory;
    @Builder.Default
    private LocalDate createdAt = LocalDate.now();
    @Builder.Default
    private LocalDate updatedAt = LocalDate.now();
    @Builder.Default
    private boolean isActive = true;
}
