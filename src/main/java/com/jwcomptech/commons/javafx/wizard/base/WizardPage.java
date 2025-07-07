package com.jwcomptech.commons.javafx.wizard.base;

import javafx.fxml.FXML;

public interface WizardPage {
    @FXML
    void initialize();

    void beforeNext();

    void beforePrevious();

    void afterNext();

    void afterPrevious();

    void onCancel();

    void afterFinished();
}
