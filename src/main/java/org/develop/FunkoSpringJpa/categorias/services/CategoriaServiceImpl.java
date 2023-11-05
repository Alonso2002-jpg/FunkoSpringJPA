package org.develop.FunkoSpringJpa.categorias.services;

import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaCreateDto;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.dto.CategoriaUpdateDto;
import org.develop.FunkoSpringJpa.categorias.commons.mainUse.model.Categoria;
import org.develop.FunkoSpringJpa.categorias.exceptions.CategoriaExistException;
import org.develop.FunkoSpringJpa.categorias.exceptions.CategoriaNotFoundException;
import org.develop.FunkoSpringJpa.categorias.mapper.CategoriaMapper;
import org.develop.FunkoSpringJpa.categorias.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "categorias")
public class CategoriaServiceImpl implements CategoriaService {
    private CategoriaRepository categoriaRepository;
    private CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }


    @Override
    public List<Categoria> getAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria getById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
    }

    @Override
    public Categoria save(CategoriaCreateDto t) {
        if (categoriaRepository.findByNameCategoryIgnoreCase(t.nameCategory()).isPresent()){
            throw new CategoriaExistException(t.nameCategory());
        }
        return categoriaRepository.save(categoriaMapper.toCategoria(t));
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        categoriaRepository.deleteById(id);
    }

    @Override
    public Categoria update(Long id, CategoriaUpdateDto funko) {
        return categoriaRepository.save(categoriaMapper.toCategoria(funko, getById(id)));
    }

    @Override
    public void deleteAll() {
        categoriaRepository.deleteAll();
    }
}
