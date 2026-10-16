package com.abstract_test_class;

import javafx.application.HostServices;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public abstract class BaseIntegrationTest<Repository extends JpaRepository<Entity, Long>, CreationDTO, UpdateDTO, InfoDTO, Service, Entity> {

    @Autowired
    protected Service service;

    @Autowired
    protected Repository repository;

    @MockitoBean
    protected HostServices hostServices;

    protected CreationDTO creationDTO;
    protected UpdateDTO updateDTO;
    protected InfoDTO infoDTO;

    @BeforeEach
    protected abstract void init();

    protected abstract void createDefaultEntity();

    protected abstract void deleteDefaultEntity(Long id);

    protected abstract void updateEntity(Long id, UpdateDTO dto);

    protected Entity getFirstEntityOnRepoList() {

        return repository.findAll().getFirst();
    }

    protected Entity reloadEntity(Long id){

        return repository.findById(id).orElseThrow();
    }

    protected abstract InfoDTO getInfoDTO(Long id);

    protected Integer countEntityOcurrencesOnRepo() {

        return repository.findAll().size();
    }

    protected void assertEntityTableIsEmpty() {

        assertEquals(0, countEntityOcurrencesOnRepo());
    }
}
