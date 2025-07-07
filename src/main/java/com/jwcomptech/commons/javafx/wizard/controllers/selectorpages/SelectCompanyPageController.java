package com.jwcomptech.commons.javafx.wizard.controllers.selectorpages;

import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.javafx.wizard.base.AbstractWizardPageController;
import com.jwcomptech.commons.javafx.wizard.data.Company;
import com.jwcomptech.commons.javafx.wizard.services.CompanyService;
import com.jwcomptech.commons.validators.Condition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.jwcomptech.commons.javafx.wizard.base.WizardContext.SELECTED_COMPANY;

@Data
@EqualsAndHashCode(callSuper = true)
public class SelectCompanyPageController extends AbstractWizardPageController {
    @FXML
    private ComboBox<Company> companyComboBox;

    @Override
    public void postInitialize() {
        companyComboBox.setItems(FXCollections.observableArrayList(
                CompanyService.getInstance().getCompanies()
        ));
        companyComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) ->
                        onUserInputChanged());

        addToTrueVals(Condition.of(() -> companyComboBox.getValue() != null,
                "Please select a company."));
    }

    @Override
    protected void afterValidation() { }

    @Override
    public void beforeNext() {
        Company selected = companyComboBox.getValue();
        context.getData().mergeWith(FXData.builder()
                .put(Company.class, SELECTED_COMPANY, selected).build());
        // Optional: switch CSS here using root.getScene().getStylesheets()
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
}
