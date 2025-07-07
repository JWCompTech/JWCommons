package com.jwcomptech.commons.javafx.wizard.controllers.selectorpages;

import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.javafx.wizard.enums.WizardPages;
import com.jwcomptech.commons.javafx.wizard.base.AbstractWizardPageController;
import com.jwcomptech.commons.javafx.wizard.data.Company;
import com.jwcomptech.commons.javafx.wizard.data.Location;
import com.jwcomptech.commons.javafx.wizard.data.Region;
import com.jwcomptech.commons.validators.Condition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jwcomptech.commons.javafx.wizard.base.WizardContext.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class SelectRegionAndLocationPageController extends AbstractWizardPageController {
    @FXML private ComboBox<Region> regionComboBox;
    @FXML private ComboBox<Location> locationComboBox;
    @FXML private Label locationWarningLabel;
    @FXML private Hyperlink createLocationLink;
    @FXML private VBox mainContainer;

    @Override
    public void postInitialize() {
        loadRegions();

        regionComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> {
                    onUserInputChanged();
                    updateLocations(n);
        });

        locationComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> {
                    onUserInputChanged();
                    validateLocation(n);
        });

        createLocationLink.setOnAction(event -> navigateToCreateLocationPage());

        addToTrueVals(Condition.of(() ->
                        regionComboBox.getValue() != null
                                && locationComboBox.getValue() != null,
                "Please select a region and a location."));
    }

    @Override
    protected void afterValidation() { }

    @Override
    public void beforeNext() {
        context.getData().mergeWith(FXData.builder()
                .put(Region.class, SELECTED_REGION, regionComboBox.getValue())
                .put(Location.class, SELECTED_LOCATION, locationComboBox.getValue())
                .build());
    }

    @Override
    public void beforePrevious() { }

    @Override
    public void afterNext() { }

    @Override
    public void afterPrevious() { }

    @Override
    public void onCancel() { }

    @Override
    public void afterFinished() { }

    private void loadRegions() {
        Optional<Company> company = context.getData()
                .getDataValue(Company.class, SELECTED_COMPANY);
        if (company.isPresent()) {
            Set<Region> regions = company.get().getLocations().stream()
                    .map(Location::getRegion)
                    .collect(Collectors.toSet());

            List<Region> sortedRegions = regions.stream()
                    .sorted(Comparator.comparing(Region::getFullName))
                    .collect(Collectors.toList());

            if (sortedRegions.isEmpty()) {
                regionComboBox.setDisable(true);
                regionComboBox.setPromptText("No regions available for selected company");
                regionComboBox.setItems(FXCollections.observableArrayList());
                navigateToErrorPage();
            } else {
                regionComboBox.setDisable(false);
                regionComboBox.setPromptText("Select a region");
                regionComboBox.setItems(FXCollections.observableArrayList(sortedRegions));
                regionComboBox.getSelectionModel().selectFirst();
            }
        }
    }

    private void updateLocations(final Region selectedRegion) {
        var selectedCompany = context.getData()
                .getDataValue(Company.class, SELECTED_COMPANY);

        if (selectedRegion != null && selectedCompany.isPresent()) {
            List<Location> matching = selectedCompany.get().getLocations().stream()
                    .filter(loc -> loc.getRegion().equals(selectedRegion))
                    .sorted(Comparator.comparing(Location::getName))
                    .collect(Collectors.toList());

            if (matching.isEmpty()) {
                locationComboBox.setDisable(true);
                locationComboBox.setPromptText("No locations available in this region");
                locationComboBox.setItems(FXCollections.observableArrayList());
            } else {
                locationComboBox.setDisable(false);
                regionComboBox.setPromptText("Select a location");
                locationComboBox.setItems(FXCollections.observableArrayList(matching));
                locationComboBox.getSelectionModel().selectFirst();
            }
        }
    }

    private void validateLocation(Location location) {
        if (location == null) {
            locationWarningLabel.setText("");
            locationWarningLabel.setVisible(false);
            return;
        }

        Optional<String> validationMessage = location.reevaluateAllAndGetFailMessage();
        if (validationMessage.isPresent()) {
            locationWarningLabel.setText(validationMessage.get());
            locationWarningLabel.setVisible(true);
            context.getNextButton().disable();
        } else {
            locationWarningLabel.setText("");
            locationWarningLabel.setVisible(false);
            context.getNextButton().enable();
        }
    }

    private void navigateToCreateLocationPage() {
        // Logic to transition to CreateLocationPage
        getNavigator().navigateTo(WizardPages.CreateLocation);
    }

    private void navigateToErrorPage() {
        // Logic to display alternate error page if no locations exist at all
        getNavigator().navigateTo(WizardPages.LocationValidationError);
    }
}
