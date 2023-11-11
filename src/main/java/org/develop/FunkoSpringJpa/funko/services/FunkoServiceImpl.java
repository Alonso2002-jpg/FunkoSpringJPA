package org.develop.FunkoSpringJpa.funko.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;
import org.develop.FunkoSpringJpa.categorias.repositories.CategoriaRepository;
import org.develop.FunkoSpringJpa.categorias.services.CategoriaService;
import org.develop.FunkoSpringJpa.config.websockets.WebSocketConfig;
import org.develop.FunkoSpringJpa.config.websockets.WebSocketHandler;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.funko.exceptions.FunkoNotFound;
import org.develop.FunkoSpringJpa.funko.mappers.FunkosMapper;
import org.develop.FunkoSpringJpa.funko.repositories.FunkoRepository;
import org.develop.FunkoSpringJpa.storage.service.StorageService;
import org.develop.FunkoSpringJpa.websockets.dto.FunkoNotificacionResponseDto;
import org.develop.FunkoSpringJpa.websockets.mapper.FunkoNotificacionMapper;
import org.develop.FunkoSpringJpa.websockets.model.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "funkos")
public class FunkoServiceImpl implements FunkoService{
    private final FunkoRepository funkoRepository;
    private final CategoriaService categoriaService;
    private final StorageService storageService;

    private final WebSocketConfig webSocketConfig;
    private final FunkoNotificacionMapper funkoNotificacionMapper;
    private final FunkosMapper funkosMapper;
    private final ObjectMapper mapper;
    private WebSocketHandler webSocketService;

    @Autowired
    public FunkoServiceImpl(FunkoRepository funkoRepository,
                            FunkosMapper funkosMapper,
                            CategoriaService categoriaService,
                            StorageService storageService,
                            WebSocketConfig webSocketConfig,
                            FunkoNotificacionMapper funkoNotificacionMapper
    ) {
    this.funkoRepository = funkoRepository;
    this.funkosMapper = funkosMapper;
    this.categoriaService = categoriaService;
    this.storageService = storageService;
    this.webSocketConfig = webSocketConfig;
    this.funkoNotificacionMapper = funkoNotificacionMapper;

    webSocketService = webSocketConfig.webSocketFunkosHandler();
    mapper = new ObjectMapper();
    }
    @Override
    public List<Funko> getAll(Double price, Long category) {
        if (category != null && price != null){
            return funkoRepository.findAllByPriceAndCategory(price,categoriaService.getById(category));
        }else if (price != null) {
            return funkoRepository.findAllByPrice(price);
        }else if (category != null) {
            return funkoRepository.findAllByCategory(categoriaService.getById(category));
        }else {
           return funkoRepository.findAll();
        }
    }


    @Override
    @Cacheable
    public Funko findById(Long id) {
        return funkoRepository.findById(id).orElseThrow(() -> new FunkoNotFound(id));
    }

    @Override
    @CachePut
    public Funko save(FunkoCreateDto t) {
        var saved = funkoRepository.save(funkosMapper.toFunko(t, categoriaService.getById(t.category())));
        onChange(Notificacion.Tipo.CREATE,saved);
        return saved;
    }


    @Override
    public void deleteAll() {
        funkoRepository.deleteAll();
    }

    @Override
    @CacheEvict
    public void deleteById(Long id) {
        var funkoID = findById(id);
        funkoRepository.deleteById(id);
        onChange(Notificacion.Tipo.DELETE, funkoID);
    }

    @Override
    @CachePut
    public Funko update(Long id, FunkoUpdateDto funko) {
        var funkoUpd = funkoRepository.save(
                funkosMapper.toFunko(
        funko,
        findById(id),
        funko.category() == null ? null : categoriaService.getById(funko.category())));
        onChange(Notificacion.Tipo.UPDATE, funkoUpd);
        return funkoUpd;
    }

    void onChange(Notificacion.Tipo tipo, Funko data) {
    log.debug("Servicio de productos onChange con tipo: " + tipo + " y datos: " + data);

    if (webSocketService == null) {
        log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
        webSocketService = this.webSocketConfig.webSocketFunkosHandler();
    }

    try {
        Notificacion<FunkoNotificacionResponseDto> notificacion = new Notificacion<>(
                "FUNKOS",
                tipo,
                funkoNotificacionMapper.toFunkoNotificacionMapper(data),
                LocalDateTime.now().toString()
        );

        String json = mapper.writeValueAsString((notificacion));

        log.info("Enviando mensaje a los clientes ws");
        // Enviamos el mensaje a los clientes ws con un hilo, si hay muchos clientes, puede tardar
        // no bloqueamos el hilo principal que atiende las peticiones http
        Thread senderThread = new Thread(() -> {
            try {
                webSocketService.sendMessage(json);
            } catch (Exception e) {
                log.error("Error al enviar el mensaje a través del servicio WebSocket", e);
            }
        });
        senderThread.start();
    } catch (JsonProcessingException e) {
        log.error("Error al convertir la notificación a JSON", e);
    }
}
}
