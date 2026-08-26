package com.presentation.controller.client;

import com.dto.client.ClientInfoDTO;
import com.enums.ClientNotesFilter;
import com.enums.RegisteredPhoneFilter;
import com.enums.RegistrationDateRange;
import com.presentation.controller.BaseCatalogViewController;
import com.service.interfaces.ClientService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.HostServices;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_CLIENT_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.*;
import static com.presentation.constants.StringResource.StatMessageSuffix.*;
import static com.presentation.constants.StringResource.URLs.WHATSPAPP_API_URL;
import static com.presentation.constants.ViewPath.*;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.resetComboBoxFilter;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;
import static com.presentation.support.view.FXMLViewLoader.loadViewOnPane;
import static com.presentation.support.view.FXMLViewLoader.loadViewWithControllerPane;

@Component
@RequiredArgsConstructor
public class ClientViewController extends BaseCatalogViewController<ClientInfoDTO> {

    private final ApplicationContext applicationContext;
    private final ClientService clientService;
    private final HostServices hostServices;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label
            totalClientsCount,
            newClientsThisMonth,
            clientsWithAtLeastOnePhoneNumber,
            clientsWithNoPhoneNumber,
            newClientsVsLastMonthPercentage,
            totalClientsRegisteredLastMonth,
            totalClientsWithNotes,
            clientsWithNotesPercentage,
            listCount;

    @FXML
    private MFXButton
            cleanFiltersButton,
            createClientButton;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<RegistrationDateRange> registrationDateFilter;

    @FXML
    private ComboBox<RegisteredPhoneFilter> registeredPhoneFilter;

    @FXML
    private ComboBox<ClientNotesFilter> clientNotesFilter;

    @FXML
    private VBox clientsListVBox;

    @Override
    protected ObservableValue<?>[] getSearchProperties() {

        return new ObservableValue<?>[]{
                searchField.textProperty(),
                registrationDateFilter.valueProperty(),
                registeredPhoneFilter.valueProperty(),
                clientNotesFilter.valueProperty()
        };
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(cleanFiltersButton, this::resetSearchFilter),
                Map.entry(createClientButton, this::goToRegisterNewClientView)
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void loadGlobalStats() {

        loadTotalClientsStats();
        loadPhoneRegistrationStats();
        loadClientRegistrationTrend();
        loadClientNotesStats();
    }

    @Override
    protected Label getResultsCountLabel() {

        return listCount;
    }

    @Override
    protected List<ClientInfoDTO> searchCatalog() {

        String clientName = searchField.getText();

        RegistrationDateRange registrationDateRange = registrationDateFilter.getValue();
        RegisteredPhoneFilter phoneFilter = registeredPhoneFilter.getValue();
        ClientNotesFilter notesFilter = clientNotesFilter.getValue();

        return clientService.liveSearch(clientName, registrationDateRange, phoneFilter, notesFilter);
    }

    @Override
    protected VBox getItemListContainer() {

        return clientsListVBox;
    }

    @Override
    protected void loadItemsOnView(List<ClientInfoDTO> items) {

        loadItemsOnController(
                items,
                clientsListVBox,
                ClientItemController.class,
                CLIENT_ITEM_VIEW_PATH,
                EMPTY_CLIENT_LIST_MESSAGE,
                CLIENT_ITEM_VIEW_LOADING_FAILED,
                itemController -> {

                    itemController.setOnEditCallback(this::goToClientEditionView);
                    itemController.setOnSendMessageCallback(this::sendWhatsAppMessage);
                }
        );
    }

    @Override
    protected void clearFilterNodes() {

        setBlankTextfield(searchField);
        resetComboBoxFilter(registrationDateFilter, registeredPhoneFilter, clientNotesFilter);
    }

    @Override
    protected void initializeListContent() {

        List<ClientInfoDTO> clients = clientService.getClientList();
        loadItemsOnView(clients);

        loadEnumsOnComboBox(registrationDateFilter, RegistrationDateRange.values());
        loadEnumsOnComboBox(registeredPhoneFilter, RegisteredPhoneFilter.values());
        loadEnumsOnComboBox(clientNotesFilter, ClientNotesFilter.values());

        setStringConverter(registrationDateFilter, RegistrationDateRange.TODOS);
        setStringConverter(registeredPhoneFilter, RegisteredPhoneFilter.TODOS);
        setStringConverter(clientNotesFilter, ClientNotesFilter.TODOS);
    }

    private void loadTotalClientsStats() {

        executeAsyncTask(
                clientService::getTotalClientsStats,
                totalClientsStatsDTO -> {
                    setTextOnLabel(totalClientsCount, parseNumberValueToText(totalClientsStatsDTO.getTotalClientsCount()));
                    setTextOnLabel(newClientsThisMonth, parseNumberValueToText(totalClientsStatsDTO.getClientsRegisteredThisMonth()) + NEW_THIS_MONTH);
                }
        );
    }

    private void loadPhoneRegistrationStats() {

        executeAsyncTask(
                clientService::getPhoneNumberRegistrationStats,
                clientPhoneNumberStatsDTO -> {
                    setTextOnLabel(clientsWithAtLeastOnePhoneNumber, parseNumberValueToText(clientPhoneNumberStatsDTO.getClientsWithAtLeastOnePhoneNumber()));
                    setTextOnLabel(clientsWithNoPhoneNumber, parseNumberValueToText(clientPhoneNumberStatsDTO.getClientsWithoutPhoneNumber()) + NO_PHONE_REGISTERED);
                }
        );
    }

    private void loadClientRegistrationTrend() {

        executeAsyncTask(
                clientService::getClientRegistrationTrendStats,
                clientRegistrationTrendStatDTO -> {
                    setTextOnLabel(newClientsVsLastMonthPercentage, formatAsPercentage(clientRegistrationTrendStatDTO.getTrendPercentage()));
                    setTextOnLabel(totalClientsRegisteredLastMonth, parseNumberValueToText(clientRegistrationTrendStatDTO.getClientsRegisteredDuringTheLastMonth()) + IN_THE_LAST_MONTH);
                }
        );
    }

    private void loadClientNotesStats() {

        executeAsyncTask(
                clientService::getClientNotesStats,
                clientNotesStatsDTO -> {
                    setTextOnLabel(totalClientsWithNotes, parseNumberValueToText(clientNotesStatsDTO.getClientsWithNotes()));
                    setTextOnLabel(clientsWithNotesPercentage, formatAsPercentage(clientNotesStatsDTO.getClientsWithNotesPercentage()) + OUT_OF_TOTAL);
                }
        );
    }

    private void sendWhatsAppMessage(ClientInfoDTO clientInfoDTO) {

        String mainPhone = clientInfoDTO.getPhoneNumbersList().getFirst();

        hostServices.showDocument(WHATSPAPP_API_URL + mainPhone.replaceAll( "\\D", ""));
    }

    private void goToRegisterNewClientView() {

        loadViewOnPane(CLIENT_CREATION_VIEW_PATH, applicationContext, CLIENT_CREATION_VIEW_LOADING_FAILED, anchorPane);
    }

    private void goToClientEditionView(ClientInfoDTO infoDTO) {

        loadViewWithControllerPane(
                CLIENT_EDITION_VIEW_PATH,
                applicationContext,
                CLIENT_EDITION_VIEW_LOADING_FAILED,
                anchorPane,
                ClientEditionController.class,
                editionController -> editionController.initialize(infoDTO)
        );
    }
}