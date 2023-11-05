package org.develop.FunkoSpringJpa.funko.repositories;

import org.develop.Funkos.funko.commons.mainUse.model.Funko;
import org.develop.Funkos.funko.commons.mainUse.model.MyIDGenerator;
import org.develop.Funkos.funko.repositories.funko.FunkoRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = "spring.sql.init.mode = never")
@DataJpaTest
class FunkoRepositoryImplTest {

    private FunkoRepository funkoRepository;
    private Funko funko1, funko2;;
    @BeforeEach
    public void setUp() throws Exception {
        funkoRepository = new FunkoRepositoryImpl(MyIDGenerator.getInstance());
        funkoRepository.deleteAll();
        funko1 = Funko.builder()
                .id(1L)
                .name("Funko 1")
                .price(1.1)
                .quantity(5)
                .category("OTROS")
                .image("ola.jpg")
                .build();
        funko2 = Funko.builder()
                .id(2L)
                .name("Funko 2")
                .price(2.2)
                .quantity(10)
                .category("OTROS")
                .image("ola2.jpg")
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        funkoRepository.deleteAll();
    }

    @Test
    void getAll() {
        funkoRepository.save(funko1);
        funkoRepository.save(funko2);

        List<Funko> allfunks = funkoRepository.getAll();

        assertAll(
                () -> assertEquals(2, allfunks.size()),
                () -> assertTrue(allfunks.contains(funko1)),
                () -> assertTrue(allfunks.contains(funko2))
        );

    }

    @Test
    void findById() {
        funkoRepository.save(funko1);
        funkoRepository.save(funko2);

        Optional<Funko> funko = funkoRepository.findById(1L);

        assertAll(
                () -> assertTrue(funko.isPresent()),
                () -> assertEquals(funko1, funko.get())
        );
    }

    @Test
    void findByIdError(){
        funkoRepository.save(funko1);
        funkoRepository.save(funko2);
        Optional<Funko> funko = funkoRepository.findById(3L);
        assertTrue(funko.isEmpty());
    }
    @Test
    void save() {
        Funko funko = funkoRepository.save(funko1);
        List<Funko> funkos = funkoRepository.getAll();

        assertAll(
                () -> assertNotNull(funko),
                () -> assertEquals(funko1.getId(), funko.getId()),
                () -> assertFalse(funkos.isEmpty())
        );
    }

    @Test
    void deleteById() {
        funkoRepository.save(funko1);
        funkoRepository.save(funko2);
        funkoRepository.deleteById(funko1.getId());

        assertAll(
                () -> assertEquals(1, funkoRepository.getAll().size()),
                () -> assertTrue(funkoRepository.getAll().contains(funko2))
        );
    }

    @Test
    void deleteAll(){
        funkoRepository.save(funko1);
        funkoRepository.save(funko2);
        funkoRepository.deleteAll();

        assertEquals(0, funkoRepository.getAll().size());
    }
}