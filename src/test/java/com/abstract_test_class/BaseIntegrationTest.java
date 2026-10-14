package com.abstract_test_class;

import javafx.application.HostServices;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public abstract class BaseIntegrationTest<Repository, CreationDTO, UpdateDTO, Service, Entity> {

    @Autowired
    protected Service service;

    @Autowired
    protected Repository repository;

    @MockitoBean
    protected HostServices hostServices;

    protected CreationDTO creationDTO;
    protected UpdateDTO updateDTO;

    @BeforeEach
    protected abstract void init();

    protected abstract Entity createDefaultEntity();

    protected abstract void deleteDefaultEntity();

    protected abstract Entity getFirstEntityOnRepoList();

    protected abstract Entity reloadEntity(Long id);

    protected abstract Integer countEntityOcurrencesOnRepo();

    protected void assertEntityTableIsEmpty() {

        assertEquals(0, countEntityOcurrencesOnRepo());
    }
}
