package com.jwcomptech.commons.javafx.wizard.controllers;

import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.resources.enums.Builtin;
import com.jwcomptech.commons.javafx.wizard.base.AbstractWizardPageController;
import com.jwcomptech.commons.validators.Condition;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import static com.jwcomptech.commons.javafx.wizard.base.WizardContext.USERNAME;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class LoginPageController extends AbstractWizardPageController {
    @FXML
    private CheckBox acceptLicenseCheckBox;
    @FXML
    private Hyperlink viewLicenseLink;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private boolean hasError = false;

    private final Resource redLock = Builtin.Red_Lock_IMG.getResource();

    @Override
    public void postInitialize() {
        final ChangeListener<String> listener =
                (obs, o, n) -> {
            reevaluateAllVals();
            onUserInputChanged();
        };

        usernameField.textProperty().addListener(listener);
        passwordField.textProperty().addListener(listener);

        addToTrueVals(Condition.of(() ->
                        !usernameField.getText().isBlank()
                                && !passwordField.getText().isBlank(),
                "Username and password are required!"));
        addToTrueVals(Condition.of(() -> acceptLicenseCheckBox.isSelected()));
    }

    @Override
    protected void afterValidation() {
        errorLabel.setText(reevaluateAllAndGetFailMessage().orElse(""));
    }

    @Override
    public void beforeNext() {
        context.getData().mergeWith(FXData.builder()
                .put(String.class, USERNAME, usernameField.getText())
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
}
