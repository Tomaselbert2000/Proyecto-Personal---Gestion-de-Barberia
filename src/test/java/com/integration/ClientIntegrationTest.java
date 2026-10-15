package com.integration;

import com.abstract_test_class.BaseIntegrationTest;
import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.model.Client;
import com.repository.ClientRepository;
import com.service.interfaces.ClientService;
import org.junit.jupiter.api.BeforeEach;

import static com.factory.ClientTestDataFactory.buildValidClientCreationDTO;
import static com.factory.ClientTestDataFactory.buildValidClientUpdateDTO;

public class ClientIntegrationTest extends BaseIntegrationTest<
        ClientRepository,
        ClientCreationDTO,
        ClientUpdateDTO,
        ClientInfoDTO,
        ClientService,
        Client
        > {

    @Override
    @BeforeEach
    protected void init() {

        creationDTO = buildValidClientCreationDTO();
        updateDTO = buildValidClientUpdateDTO();
    }

    @Override
    protected void createDefaultEntity() {

        service.registerNewClient(creationDTO);
    }

    @Override
    protected void deleteDefaultEntity(Long id) {

        service.deleteClient(id);
    }

    @Override
    protected void updateEntity(Long id, ClientUpdateDTO updateDTO) {

        service.updateClient(id, updateDTO);
    }

    @Override
    protected Client getFirstEntityOnRepoList() {

        return repository.findAll().getFirst();
    }

    @Override
    protected Client reloadEntity(Long id) {

        return repository.findById(id).orElseThrow();
    }

    @Override
    protected ClientInfoDTO getInfoDTO(Long id) {

        return service.getClientInfo(id);
    }

    @Override
    protected Integer countEntityOcurrencesOnRepo() {

        return repository.findAll().size();
    }
}
