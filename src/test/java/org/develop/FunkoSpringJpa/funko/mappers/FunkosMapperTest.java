package org.develop.FunkoSpringJpa.funko.mappers;

import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoResponseDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.funko.commons.mainUse.model.Funko;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FunkosMapperTest {
    private FunkosMapper funkosMapper;
    private Funko funko1;
    private Categoria categoria;
    private FunkoCreateDto funkoCreateDto;
    private FunkoUpdateDto funkoUpdateDto;
    private FunkoResponseDto funkoResponseDto;

    @BeforeEach
    void setUp() {
    funkosMapper = new FunkosMapper();
    categoria = Categoria.builder()
            .nameCategory("MARVEL")
            .isActive(true)
            .build();

    funko1 = Funko.builder()
                .id(1L)
                .name("Funko 1")
                .price(1.1)
                .quantity(5)
                .category(categoria)
                .image("ola.jpg")
                .build();

    funkoCreateDto = FunkoCreateDto.builder()
            .name(funko1.getName())
            .price(funko1.getPrice())
            .image(funko1.getImage())
            .category(1L)
            .build();

    funkoUpdateDto = FunkoUpdateDto.builder()
            .name("Funko UPD")
            .price(funko1.getPrice())
            .quantity(funko1.getQuantity())
            .image(funko1.getImage())
            .category(2L)
            .build();

    funkoResponseDto = FunkoResponseDto.builder()
            .name("Funko Create")
            .price(funko1.getPrice())
            .quantity(funko1.getQuantity())
            .image(funko1.getImage())
            .category(funko1.getCategory())
            .build();
    }

    @Test
    void toFunkoCreate() {
    Funko funko = funkosMapper.toFunko(funkoCreateDto,categoria);

    assertAll(
            () -> assertNotNull(funko),
            () -> assertNull(funko.getId()),
            () -> assertEquals(funkoCreateDto.name(), funko.getName())

    );
    }


    @Test
    void ToFunkoUpdate() {
    Funko funko = funkosMapper.toFunko(funkoUpdateDto, funko1,categoria);

    assertAll(
            () -> assertNotNull(funko),
            () -> assertNotNull(funko.getQuantity()),
            () -> assertEquals(funkoUpdateDto.name(), funko.getName()),
            () -> assertEquals(funko.getQuantity(), funko1.getQuantity())
    );
    }


    @Test
    void ToFunkoResponse() {
        Funko funko = funkosMapper.toFunko(funkoResponseDto, 1L);

        assertAll(
            () -> assertNotNull(funko),
            () -> assertNotNull(funko.getId()),
            () -> assertEquals(funkoResponseDto.name(), funko.getName()),
            () -> assertEquals(1L, funko.getId())
        );
    }

    @Test
    void toResponseDto() {
        FunkoResponseDto fkDto = funkosMapper.toResponseDto(funko1);
         assertAll(
            () -> assertNotNull(fkDto),
            () -> assertEquals(funko1.getName(), fkDto.name())
        );
    }
}