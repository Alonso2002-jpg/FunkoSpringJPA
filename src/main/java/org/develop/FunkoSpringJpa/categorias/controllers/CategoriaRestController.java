package org.develop.FunkoSpringJpa.categorias.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaCreateDto;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaResponseDto;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaUpdateDto;
import org.develop.FunkoSpringJpa.categorias.mapper.CategoriaMapper;
import org.develop.FunkoSpringJpa.categorias.services.CategoriaService;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("funkos/categorias")
public class CategoriaRestController {
    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;
    @Autowired
    public CategoriaRestController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDto>> getCategorias() {
        return ResponseEntity.ok(categoriaMapper.toResponseDtoList(categoriaService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> getCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaMapper.toResponseDto(categoriaService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> postFunko(@Valid @RequestBody CategoriaCreateDto categoriaCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMapper.toResponseDto(categoriaService.save(categoriaCreateDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> putFunko(@PathVariable long id, @Valid @RequestBody CategoriaUpdateDto categoriaUpdateDto) {
        return ResponseEntity.ok(categoriaMapper.toResponseDto(categoriaService.update(id,categoriaUpdateDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFunko(@PathVariable long id) {
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

     @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
