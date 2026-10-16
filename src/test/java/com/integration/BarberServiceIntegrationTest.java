package com.integration;

import com.abstract_test_class.BaseIntegrationTest;
import com.dto.barberservice.BarberServiceCreationDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.barberservice.BarberServiceUpdateDTO;
import com.dto.stats.BarberServiceActiveOnCatalogStatsDTO;
import com.exceptions.barberservice.DuplicatedBarberServiceNameException;
import com.model.BarberService;
import com.repository.BarberServiceRepository;
import com.service.interfaces.BarberserviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.factory.BarberServiceTestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;

public class BarberServiceIntegrationTest extends BaseIntegrationTest<
        BarberServiceRepository,
        BarberServiceCreationDTO,
        BarberServiceUpdateDTO,
        BarberServiceInfoDTO,
        BarberserviceService,
        BarberService
        > {

    private final BarberService barberService = buildValidBarberService();

    @Override
    @BeforeEach
    protected void init() {

        preloadBarberServiceOnRepo();
        creationDTO = buildValidBarberServiceCreationDTO();
    }

    @Override
    protected void createDefaultEntity() {

        service.registerNewBarberService(creationDTO);
    }

    @Override
    protected void deleteDefaultEntity(Long id) {

        service.deleteBarberservice(id);
    }

    @Override
    protected void updateEntity(Long id, BarberServiceUpdateDTO dto) {

        service.updateService(id, dto);
    }

    @Override
    protected BarberService getFirstEntityOnRepoList() {

        return repository.findAll().getFirst();
    }

    @Override
    protected BarberServiceInfoDTO getInfoDTO(Long id) {

        return service.getBarberServiceInfo(id);
    }

    @Test
    @DisplayName("Dado un nombre de servicio duplicado al crear, se lanza DuplicatedBarberServiceNameException")
    void givenDuplicateName_WhenCreating_ThenThrowsDuplicatedBarberServiceNameException() {

        BarberServiceCreationDTO first = buildValidBarberServiceCreationDTO();

        first.setName("Servicio duplicado para test");

        service.registerNewBarberService(first);

        BarberServiceCreationDTO dup = buildValidBarberServiceCreationDTO();

        dup.setName("Servicio duplicado para test");

        assertThrows(DuplicatedBarberServiceNameException.class, () -> service.registerNewBarberService(dup));
    }

    @Test
    @DisplayName("Dado un servicio existente y DTO de actualización con solo precio cambiado")
    void givenUpdateOnlyPrice_WhenUpdating_ThenIsSuccessfullyPersisted() {

        BarberService existing = repository.findById(barberService.getBarbershopServiceID()).orElseThrow();

        BarberServiceUpdateDTO update = buildValidBarberServiceUpdateDTO();

        update.setName(existing.getName());
        update.setServiceCategory(existing.getServiceCategory());
        update.setInternalNotes(existing.getInternalNotes());

        service.updateService(barberService.getBarbershopServiceID(), update);

        BarberServiceInfoDTO info = getInfoDTO(barberService.getBarbershopServiceID());

        assertEquals(update.getPrice(), info.getPrice());
    }

    @Test
    @DisplayName("Dado un servicio existente y DTO de actualización con solo categoría cambiado")
    void givenUpdateOnlyCategory_WhenUpdating_ThenIsSuccessfullyPersisted() {

        BarberService existing = repository.findById(barberService.getBarbershopServiceID()).orElseThrow();

        BarberServiceUpdateDTO update = buildValidBarberServiceUpdateDTO();

        update.setName(existing.getName());
        update.setPrice(existing.getPrice());
        update.setInternalNotes(existing.getInternalNotes());

        service.updateService(barberService.getBarbershopServiceID(), update);

        BarberServiceInfoDTO info = getInfoDTO(barberService.getBarbershopServiceID());

        assertEquals(update.getServiceCategory(), info.getCategory());
    }

    @Test
    @DisplayName("Dado un servicio existente y DTO de actualización con solo notas internas cambiado")
    void givenUpdateOnlyNotes_WhenUpdating_ThenIsSuccessfullyPersisted() {

        BarberService existing = repository.findById(barberService.getBarbershopServiceID()).orElseThrow();

        BarberServiceUpdateDTO update = buildValidBarberServiceUpdateDTO();

        update.setName(existing.getName());
        update.setPrice(existing.getPrice());
        update.setServiceCategory(existing.getServiceCategory());

        service.updateService(barberService.getBarbershopServiceID(), update);

        BarberServiceInfoDTO info = getInfoDTO(barberService.getBarbershopServiceID());
        assertEquals(update.getInternalNotes(), info.getInternalNotes());
    }

    @Test
    @DisplayName("Dado un servicio existente, su eliminación debe ser exitosa")
    void givenExistingService_WhenDeleting_ThenIsSuccessfullyDeleted() {

        service.deleteBarberservice(barberService.getBarbershopServiceID());

        assertEquals(0, countEntityOcurrencesOnRepo());
    }

    @Test
    @DisplayName("Dado una búsqueda en vivo con filtros válidos, se devuelve lista correcta")
    void givenLiveSearchWithFilters_WhenSearching_ThenReturnsCorrectDTOs() {

        BarberServiceCreationDTO valid = buildValidBarberServiceCreationDTO();

        valid.setName("Búsqueda en vivo para test");

        service.registerNewBarberService(valid);

        List<BarberServiceInfoDTO> result = service.liveSearch(valid.getName(), null, 0.0, 20000.0);

        assertNotNull(result);
        assertEquals(1, result.size());

        BarberServiceInfoDTO dto = result.getFirst();

        assertEquals(valid.getName(), dto.getName());
    }

    @Test
    @DisplayName("Dado que hay servicios activos, se devuelven estadísticas correctas")
    void givenActiveStats_WhenFetching_ThenReturnsCorrectDTO() {

        BarberServiceInfoDTO info = getInfoDTO(barberService.getBarbershopServiceID());

        assertTrue(info.getIsCurrentlyActive());

        BarberServiceActiveOnCatalogStatsDTO stats = service.getActiveOnCatalogStats();

        assertNotNull(stats);
        assertEquals(1, stats.getAmountOfActiveServices());
        assertEquals(1, stats.getAmountOfDifferentCategories());
    }

    private void preloadBarberServiceOnRepo() {

        barberService.setBarbershopServiceID(null);
        repository.save(barberService);
    }
}