package org.develop.FunkoSpringJpa.funko.mappers;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoResponseDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.funko.commons.mainUse.model.Funko;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FunkosMapper {

    public Funko toFunko(FunkoCreateDto funkoCreateDto){
        return Funko.builder()
                .name(funkoCreateDto.name())
                .price(funkoCreateDto.price())
                .quantity(funkoCreateDto.quantity())
                .category(funkoCreateDto.category())
                .image(funkoCreateDto.image())
                .build();
    }

    public FunkoCreateDto toCreate(Funko funko){
        return FunkoCreateDto.builder()
                .name(funko.getName())
                .price(funko.getPrice())
                .quantity(funko.getQuantity())
                .category(funko.getCategory())
                .image(funko.getImage())
                .build();
    }
    public Funko toFunko(FunkoUpdateDto funkoUpdateDto, Funko funko){
        return Funko.builder()
                .id(funko.getId())
                .name(funkoUpdateDto.name() == null ? funko.getName() : funkoUpdateDto.name())
                .price(funkoUpdateDto.price() == null ? funko.getPrice() : funkoUpdateDto.price())
                .quantity(funkoUpdateDto.quantity() == null ? funko.getQuantity() : funkoUpdateDto.quantity())
                .image(funkoUpdateDto.image() == null ? funko.getImage() : funkoUpdateDto.image())
                .category(funkoUpdateDto.category() == null ? funko.getCategory() : funkoUpdateDto.category())
                .createdAt(funko.getCreatedAt())
                .build();
    }

    public FunkoUpdateDto toUpdate(Funko funko){
        return FunkoUpdateDto.builder()
                .name(funko.getName())
                .price(funko.getPrice())
                .quantity(funko.getQuantity())
                .image(funko.getImage())
                .category(funko.getCategory())
                .build();
    }
    public Funko toFunko(FunkoResponseDto funkoResponseDto, Long id){
        return Funko.builder()
                .id(id)
                .name(funkoResponseDto.name())
                .price(funkoResponseDto.price())
                .quantity(funkoResponseDto.quantity())
                .image(funkoResponseDto.image())
                .category(funkoResponseDto.category())
                .build();
    }
    public FunkoResponseDto toResponseDto(Funko funko){
        return FunkoResponseDto.builder()
                .name(funko.getName())
                .price(funko.getPrice())
                .quantity(funko.getQuantity())
                .image(funko.getImage())
                .category(funko.getCategory())
                .build();
    }

    public List<FunkoResponseDto> toResponseDtoList(List<Funko> funkos){
        return funkos.stream().map(this::toResponseDto).toList();
    }

}
