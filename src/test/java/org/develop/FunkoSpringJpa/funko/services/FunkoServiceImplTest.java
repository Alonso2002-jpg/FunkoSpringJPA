package org.develop.FunkoSpringJpa.funko.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;
import org.develop.FunkoSpringJpa.categorias.services.CategoriaService;
import org.develop.FunkoSpringJpa.config.websockets.WebSocketConfig;
import org.develop.FunkoSpringJpa.config.websockets.WebSocketHandler;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.funko.exceptions.FunkoException;
import org.develop.FunkoSpringJpa.funko.mappers.FunkosMapper;
import org.develop.FunkoSpringJpa.funko.repositories.FunkoRepository;
import org.develop.FunkoSpringJpa.websockets.dto.FunkoNotificacionResponseDto;
import org.develop.FunkoSpringJpa.websockets.mapper.FunkoNotificacionMapper;
import org.develop.FunkoSpringJpa.websockets.model.Notificacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FunkoServiceImplTest {
    private Funko funko1, funko2;
    private Categoria categoria;
    private FunkoCreateDto funkoCreateDto;
    private FunkoUpdateDto funkoUpdateDto;
    @Mock
    FunkoRepository funkoRepository;
    @Mock
    CategoriaService categoriaService;
    @Mock
    FunkosMapper funkosMapper;
    @Mock
    FunkoNotificacionMapper funkoNotificacionMapper;
    WebSocketHandler webSocketHandler = mock(WebSocketHandler.class) ;
    @Mock
    WebSocketConfig webSocketConfig;
    @InjectMocks
    FunkoServiceImpl funkoService;

    @BeforeEach
    void setUp() {
    categoria = Categoria.builder()
            .nameCategory("MARVEL")
            .build();

    funko1 = Funko.builder()
                .id(1L)
                .name("Funko 1")
                .price(10.0)
                .quantity(5)
                .category(Categoria.builder().build())
                .image("ola.jpg")
                .build();
    funko2 = Funko.builder()
                .id(2L)
                .name("Funko 2")
                .price(20.0)
                .quantity(10)
                .category(Categoria.builder().build())
                .image("ola2.jpg")
                .build();

    funkoCreateDto = FunkoCreateDto.builder()
            .name(funko1.getName())
            .price(funko1.getPrice())
            .quantity(funko1.getQuantity())
            .image(funko1.getImage())
            .category(1L)
            .build();

    funkoUpdateDto = FunkoUpdateDto.builder()
            .name(funko1.getName())
            .price(funko1.getPrice())
            .quantity(funko1.getQuantity())
            .image(funko1.getImage())
            .build();
    }

    @AfterEach
    void tearDown() {
        funkoService.deleteAll();
    }
    @Test
    void getAllEmpty() {
        var listFunks = List.of(funko1, funko2);

        when(funkoRepository.findAll()).thenReturn(listFunks);
        var res = funkoService.getAll(null, null);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(2,res.size()),
                ()-> assertTrue(res.contains(funko1)),
                ()-> assertTrue(res.contains(funko2))
        );

        verify(funkoRepository,times(1)).findAll();
    }

    @Test
    void getAllPrice(){
         var listFunks = List.of(funko1, funko2);

        when(funkoRepository.findAllByPrice(15.0)).thenReturn(listFunks);
        var res = funkoService.getAll(15.0, null);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(2,res.size()),
                ()-> assertTrue(res.contains(funko1)),
                ()-> assertTrue(res.contains(funko2))
        );

        verify(funkoRepository,times(1)).findAllByPrice(15.0);
    }

    @Test
    void getAllCategory(){
         var listFunks = List.of(funko1, funko2);
         var categoria = Categoria.builder().nameCategory("OTROS").build();

        when(categoriaService.getById(1L)).thenReturn(categoria);
        when(funkoRepository.findAllByCategory(categoria)).thenReturn(listFunks);

        var res = funkoService.getAll(null, 1L);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(2,res.size()),
                ()-> assertTrue(res.contains(funko2)),
                ()-> assertTrue(res.contains(funko1))
        );

        verify(funkoRepository,times(1)).findAllByCategory(categoria);
    }

    @Test
    void getAllCategoryAndPrice(){
         var listFunks = List.of(
                 funko1,
                 funko2
                 );
        when(categoriaService.getById(1L)).thenReturn(categoria);
        when(funkoRepository.findAllByPriceAndCategory(1.0,categoria)).thenReturn(listFunks);
        var res = funkoService.getAll(1.0, 1L);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(2,res.size())
        );

        verify(funkoRepository,times(1)).findAllByPriceAndCategory(1.0,categoria);
    }
    @Test
    void findById() {
        when(funkoRepository.findById(1L)).thenReturn(Optional.of(funko1));

        var res =funkoService.findById(1L);

         assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(funko1,res)
        );

        verify(funkoRepository,times(1)).findById(1L);
    }

    @Test
    void findByIdError() {
        when(funkoRepository.findById(3L)).thenReturn(Optional.empty());

        var res = assertThrows(FunkoException.class,()-> funkoService.findById(3L));

        assertEquals("Funko not found with ID: " + 3, res.getMessage());

        verify(funkoRepository,times(1)).findById(3L);
    }

    @Test
    void save() throws IOException {
        when(funkoRepository.save(funko1)).thenReturn(funko1);
        when(categoriaService.getById(1L)).thenReturn(categoria);
        when(funkosMapper.toFunko(funkoCreateDto,categoria)).thenReturn(funko1);
        doNothing().when(webSocketHandler).sendMessage(any());
        var res = funkoService.save(funkoCreateDto);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(funko1,res)
        );

        verify(funkoRepository,times(1)).save(funko1);
        verify(funkosMapper,times(1)).toFunko(funkoCreateDto,categoria);
    }

    @Test
    void update() throws IOException {

        when(funkoRepository.findById(1L)).thenReturn(Optional.of(funko1));
        when(funkosMapper.toFunko(funkoUpdateDto,funko1,null)).thenReturn(funko1);
        when(funkoRepository.save(funko1)).thenReturn(funko1);
        doNothing().when(webSocketHandler).sendMessage(any());
        var res = funkoService.update(1L,funkoUpdateDto);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(funko1,res)
        );

        verify(funkoRepository,times(1)).save(funko1);
        verify(funkosMapper,times(1)).toFunko(funkoUpdateDto,funko1,null);
        verify(funkoRepository,times(1)).findById(1L);
    }

    @Test
    void deleteById() throws IOException {
        doNothing().when(funkoRepository).deleteById(1L);
        when(funkoRepository.findById(1L)).thenReturn(Optional.of(funko1));
        funkoService.deleteById(1L);
        doNothing().when(webSocketHandler).sendMessage(any());
        verify(funkoRepository,times(1)).deleteById(1L);
    }

    @Test
    void onChange() throws IOException {
        // Arrange
        doNothing().when(webSocketHandler).sendMessage("Hola");
        // Act
        funkoService.onChange(Notificacion.Tipo.CREATE, funko1);
    }
}