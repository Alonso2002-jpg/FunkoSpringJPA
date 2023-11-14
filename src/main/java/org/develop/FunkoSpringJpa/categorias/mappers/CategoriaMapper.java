package org.develop.FunkoSpringJpa.categorias.mappers;

import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaCreateDto;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaResponseDto;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaUpdateDto;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriaMapper {

    public Categoria toCategoria(CategoriaCreateDto categoriaCreateDto){
        return Categoria.builder()
                .nameCategory(categoriaCreateDto.nameCategory())
                .isActive(categoriaCreateDto.isActive())
                .build();
    }

    public CategoriaCreateDto toCreate(Categoria categoria){
        return CategoriaCreateDto.builder()
                .nameCategory(categoria.getNameCategory())
                .build();
    }

    public Categoria toCategoria(CategoriaUpdateDto categoriaUpdateDto, Categoria categoria){
        return Categoria.builder()
                .id(categoria.getId())
                .nameCategory(categoriaUpdateDto.nameCategory() == null ? categoria.getNameCategory() : categoriaUpdateDto.nameCategory())
                .isActive(categoriaUpdateDto.isActive() == null ? categoria.isActive() : categoriaUpdateDto.isActive())
                .build();
    }

    public CategoriaUpdateDto toUpdateDto(Categoria categoria){
        return CategoriaUpdateDto.builder()
                .nameCategory(categoria.getNameCategory())
                .isActive(categoria.isActive())
                .build();
    }

    public Categoria toCategoria(CategoriaResponseDto categoriaResponseDto, Long id){
        return Categoria.builder()
                .id(id)
                .nameCategory(categoriaResponseDto.nameCategory())
                .build();
    }

    public CategoriaResponseDto toResponseDto(Categoria categoria){
        return CategoriaResponseDto.builder()
                .nameCategory(categoria.getNameCategory())
                .isActive(categoria.isActive())
                .build();
    }

    public List<CategoriaResponseDto> toResponseDtoList(List<Categoria> categorias){
        return categorias.stream().map(this::toResponseDto).toList();
    }

    public Page<CategoriaResponseDto> toPageResponse(Page<Categoria> pageCategoria){
        return pageCategoria.map(this::toResponseDto);
    }
}
