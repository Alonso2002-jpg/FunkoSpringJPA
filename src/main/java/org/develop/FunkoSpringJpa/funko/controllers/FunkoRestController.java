package org.develop.FunkoSpringJpa.funko.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoResponseDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.funko.mappers.FunkosMapper;
import org.develop.FunkoSpringJpa.funko.services.FunkoService;
import org.develop.FunkoSpringJpa.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("funkos")
public class FunkoRestController {
        private final FunkoService funkoService;
        private final FunkosMapper funkosMapper;
        private final StorageService storageService;

    @Autowired
    public FunkoRestController(FunkoService funkoService, FunkosMapper funkosMapper,StorageService storageService) {
        this.funkoService =funkoService;
        this.funkosMapper = funkosMapper;
        this.storageService = storageService;
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

    @PatchMapping(value = "imagen/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FunkoResponseDto> putImagen(@PathVariable long id, @RequestPart("file") MultipartFile file){

        if (!file.isEmpty()){
            String img =storageService.store(file);
            String urlName = storageService.getUrl(img).replace(" ","%");

             var funko = funkoService.findById(id);
             funko.setImage(urlName);
             funkoService.update(id,funkosMapper.toUpdateDto(funko));
             return ResponseEntity.ok(funkosMapper.toResponseDto(funko));
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagen no enviada");
        }
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
