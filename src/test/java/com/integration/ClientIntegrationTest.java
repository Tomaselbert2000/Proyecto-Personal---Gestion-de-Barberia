package com.integration;

import com.abstract_test_class.BaseIntegrationTest;
import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.dto.stats.*;
import com.enums.ClientNotesFilter;
import com.enums.RegisteredPhoneFilter;
import com.enums.RegistrationDateRange;
import com.model.Client;
import com.repository.ClientRepository;
import com.service.interfaces.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.factory.ClientTestDataFactory.buildValidClientCreationDTO;
import static com.factory.ClientTestDataFactory.buildValidClientUpdateDTO;
import static org.junit.jupiter.api.Assertions.*;

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
    protected ClientInfoDTO getInfoDTO(Long id) {

        return service.getClientInfo(id);
    }

    @Test
    @DisplayName("Dado un cliente registrado, al obtener estadísticas de adquisición vs último mes, deberá retornar los datos correctamente")
    void givenStatsVsLastMonth_WhenRetrieving_ThenReturnsAcquisitionStats() {

        service.registerNewClient(creationDTO);

        ClientAcquisitionStatsDTO stats = service.getClientStatsVsLastMonth();

        assertAll(
                () -> assertNotNull(stats, "Stats DTO no es null"),
                () -> assertNotNull(stats.getNewClientsThisMonth(), "New clients this month no es null"),
                () -> assertNotNull(stats.getNewClientsLastMonth(), "New clients last month no es null"),
                () -> assertNotNull(stats.getPercentageVsLastMonth(), "Percentage vs last month no es null")
        );

        long expectedThisMonth = countEntityOcurrencesOnRepo();
        assertEquals(expectedThisMonth, stats.getNewClientsThisMonth());

        assertAll(
                () -> assertTrue(stats.getPercentageVsLastMonth() >= 0.0, "Porcentaje está entre 0% y 100%"),
                () -> assertTrue(stats.getPercentageVsLastMonth() <= 100.0, "Porcentaje no supera el 100%")
        );
    }

    @Test
    @DisplayName("Dado un cliente registrado, al obtener estadísticas totales de clientes, deberá retornar los datos correctamente")
    void givenStats_WhenGettingTotalClientsStats_ThenReturnsStats() {

        TotalClientsStatsDTO stats = service.getTotalClientsStats();

        assertAll(
                () -> assertNotNull(stats, "Total clients stats DTO no es null"),
                () -> assertNotNull(stats.getTotalClientsCount(), "Total clients count no es null"),
                () -> assertNotNull(stats.getClientsRegisteredThisMonth(), "Clients registered this month no es null")
        );

        assertAll(
                () -> assertEquals(countEntityOcurrencesOnRepo().intValue(), stats.getTotalClientsCount(), "Total coincide con repo"),
                () -> assertTrue(stats.getClientsRegisteredThisMonth() <= stats.getTotalClientsCount(), "Este mes <= total"),
                () -> assertTrue(stats.getClientsRegisteredThisMonth() >= 0, "Este mes >= 0")
        );
    }

    @Test
    @DisplayName("Dado un cliente registrado, al obtener estadísticas de registro de teléfonos, deberá retornar los datos correctamente")
    void givenStats_WhenGettingPhoneNumberStats_ThenReturnsStats() {

        ClientPhoneNumberStatsDTO stats = service.getPhoneNumberRegistrationStats();

        assertAll(
                () -> assertNotNull(stats, "Phone number stats DTO no es null"),
                () -> assertNotNull(stats.getClientsWithAtLeastOnePhoneNumber(), "Clients with phone no es null"),
                () -> assertNotNull(stats.getClientsWithoutPhoneNumber(), "Clients without phone no es null")
        );

        long totalClients = countEntityOcurrencesOnRepo();
        assertAll(
                () -> assertEquals(totalClients, stats.getClientsWithAtLeastOnePhoneNumber() + stats.getClientsWithoutPhoneNumber(), "Suma coincide con total repo"),
                () -> assertTrue(stats.getClientsWithAtLeastOnePhoneNumber() >= 0, "Con teléfono >= 0"),
                () -> assertTrue(stats.getClientsWithoutPhoneNumber() >= 0, "Sin teléfono >= 0")
        );
    }

    @Test
    @DisplayName("Dado un cliente registrado, al obtener estadísticas de tendencia de registro, deberá retornar los datos correctamente")
    void givenStats_WhenGettingRegistrationTrendStats_ThenReturnsStats() {

        ClientRegistrationTrendStatDTO stats = service.getClientRegistrationTrendStats();

        assertAll(
                () -> assertNotNull(stats, "Registration trend stats DTO no es null"),
                () -> assertNotNull(stats.getClientsRegisteredDuringThisMonth(), "Clients this month no es null"),
                () -> assertNotNull(stats.getClientsRegisteredDuringTheLastMonth(), "Clients last month no es null"),
                () -> assertNotNull(stats.getTrendPercentage(), "Trend percentage no es null")
        );

        assertAll(
                () -> assertEquals(countEntityOcurrencesOnRepo().intValue(), stats.getClientsRegisteredDuringThisMonth(), "Este mes coincide con repo"),
                () -> assertTrue(stats.getClientsRegisteredDuringTheLastMonth() >= 0, "Mes pasado >= 0"),
                () -> assertTrue(stats.getTrendPercentage() >= -100.0 && stats.getTrendPercentage() <= 100.0, "Porcentaje en rango válido")
        );
    }

    @Test
    @DisplayName("Dado un cliente registrado, al obtener estadísticas de observaciones, deberá retornar los datos correctamente")
    void givenStats_WhenGettingNotesStats_ThenReturnsStats() {

        ClientNotesStatsDTO stats = service.getClientNotesStats();

        assertAll(
                () -> assertNotNull(stats, "Notes stats DTO no es null"),
                () -> assertNotNull(stats.getClientsWithNotes(), "Clients with notes no es null"),
                () -> assertNotNull(stats.getClientsWithoutNotes(), "Clients without notes no es null"),
                () -> assertNotNull(stats.getClientsWithNotesPercentage(), "With notes percentage no es null")
        );

        long total = stats.getClientsWithNotes() + stats.getClientsWithoutNotes();
        assertAll(
                () -> assertEquals(countEntityOcurrencesOnRepo().intValue(), total, "Total coincide con repo"),
                () -> assertTrue(stats.getClientsWithNotesPercentage() >= 0.0, "Porcentaje >= 0%"),
                () -> assertTrue(stats.getClientsWithNotesPercentage() <= 100.0, "Porcentaje <= 100%")
        );
    }

    @Test
    @DisplayName("Dado un nombre de cliente, filtros de fecha, teléfono y notas, la búsqueda en vivo deberá retornar resultados filtrados")
    void givenFilters_WhenLiveSearching_ThenReturnsFilteredResults() {

        List<ClientInfoDTO> results = service.liveSearch(
                creationDTO.getFirstName(),
                RegistrationDateRange.ULTIMOS_30_DIAS,
                RegisteredPhoneFilter.CON_TELEFONO_REGISTRADO,
                ClientNotesFilter.TODOS
        );

        assertAll(
                () -> assertNotNull(results, "Live search results no es null")
        );

        if (!results.isEmpty()) {
            assertAll(
                    () -> assertTrue(results.size() <= countEntityOcurrencesOnRepo(), "No más resultados que clientes totales"),
                    () -> assertEquals(creationDTO.getFirstName(), results.getFirst().getFirstName(), "Nombre coincide con DTO")
            );
        }
    }

    @Test
    @DisplayName("Dado una lista de clientes, al obtener la lista completa, deberá retornar todos los clientes")
    void givenClientList_WhenRetrievingAllClients_ThenReturnsList() {

        createDefaultEntity();

        List<ClientInfoDTO> results = service.getClientList();

        assertAll(
                () -> assertNotNull(results, "Client list no es null"),
                () -> assertEquals(countEntityOcurrencesOnRepo(), results.size(), "Tamaño coincide con repo")
        );

        if (!results.isEmpty()) {
            assertAll(
                    () -> assertEquals(getFirstEntityOnRepoList().getFirstName(), results.getFirst().getFirstName(), "Primer nombre coincide con repo"),
                    () -> assertEquals(getFirstEntityOnRepoList().getLastName(), results.getFirst().getLastName(), "Primer apellido coincide con repo")
            );
        }
    }

    @Test
    @DisplayName("Dado un cliente registrado, al buscar por nombre en vivo, deberá retornar resultados")
    void givenClientName_WhenLiveSearchingByName_ThenReturnsResults() {

        createDefaultEntity();

        List<ClientInfoDTO> results = service.clientLiveSearchByName(creationDTO.getFirstName());

        assertAll(
                () -> assertNotNull(results, "Client live search results no es null"),
                () -> assertFalse(results.isEmpty(), "No debe estar vacío"),
                () -> assertEquals(1, results.size(), "Debe haber exactamente 1 resultado")
        );

        assertAll(
                () -> assertEquals(getFirstEntityOnRepoList().getFirstName(), results.getFirst().getFirstName(), "Nombre coincide con repo"),
                () -> assertEquals(getFirstEntityOnRepoList().getLastName(), results.getFirst().getLastName(), "Apellido coincide con repo")
        );
    }
}
