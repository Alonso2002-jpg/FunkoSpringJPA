package org.develop.FunkoSpringJpa.funko.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoResponseDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.funko.mappers.FunkosMapper;
import org.develop.FunkoSpringJpa.funko.services.FunkoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("funkos")
public class FunkoRestController {
        private final FunkoService funkoService;
        private final FunkosMapper funkosMapper;

    @Autowired
    public FunkoRestController(FunkoService funkoService, FunkosMapper funkosMapper) {
        this.funkoService =funkoService;
        this.funkosMapper = funkosMapper;
    }

    @GetMapping
    public ResponseEntity<List<FunkoResponseDto>> getFunkos(
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Long category) {
        return ResponseEntity.ok(funkosMapper.toResponseDtoList(funkoService.getAll(price, category)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FunkoResponseDto> getFunko(@PathVariable Long id) {
        return ResponseEntity.ok(funkosMapper.toResponseDto(funkoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<FunkoResponseDto> postFunkos(@Valid @RequestBody FunkoCreateDto funko) {
        return ResponseEntity.status(HttpStatus.CREATED).body(funkosMapper.toResponseDto(funkoService.save(funko)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FunkoResponseDto> putFunko(@PathVariable long id, @Valid @RequestBody FunkoUpdateDto funko) {
        return ResponseEntity.ok(funkosMapper.toResponseDto(funkoService.update(id,funko)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFunko(@PathVariable long id) {
        funkoService.deleteById(id);
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
