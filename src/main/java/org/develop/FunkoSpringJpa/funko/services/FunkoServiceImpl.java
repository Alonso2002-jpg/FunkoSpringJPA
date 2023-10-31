package org.develop.FunkoSpringJpa.funko.services;

import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.funko.exceptions.FunkoNotFound;
import org.develop.FunkoSpringJpa.funko.mappers.FunkosMapper;
import org.develop.FunkoSpringJpa.funko.repositories.FunkoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "funkos")
public class FunkoServiceImpl implements FunkoService{
    private final FunkoRepository funkoRepository;
    private final FunkosMapper funkosMapper;

    @Autowired
    public FunkoServiceImpl(FunkoRepository funkoRepository,FunkosMapper funkosMapper) {
    this.funkoRepository = funkoRepository;
    this.funkosMapper = funkosMapper;
    }
    @Override
    public List<Funko> getAll(Double price, String category) {
        if (category != null && price != null){
            return funkoRepository.findAllByCategoryAndPrice(category, price);
        }else if (price != null) {
            return funkoRepository.findAllByPrice(price);
        }else if (category != null) {
            return funkoRepository.findAllByCategory(category);
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
        return funkoRepository.save(funkosMapper.toFunko(t));
    }


    @Override
    public void deleteAll() {
        funkoRepository.deleteAll();
    }

    @Override
    @CacheEvict
    public void deleteById(Long id) {
        findById(id);
        funkoRepository.deleteById(id);
    }

    @Override
    @CachePut
    public Funko update(Long id, FunkoUpdateDto funko) {
        return funkoRepository.save(funkosMapper.toFunko(funko,findById(id)));
    }

}
