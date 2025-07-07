package com.jwcomptech.commons.javafx.wizard.base;

import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.javafx.controls.FXButton;
import com.jwcomptech.commons.javafx.wizard.enums.WizardPages;
import com.jwcomptech.commons.resources.ResourceManager;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class WizardContext<T extends AbstractWizard<T>> {
    private FXButton backButton;
    private FXButton nextButton;
    private FXButton cancelButton;
    public T parent = null;
    public FXData data = FXData.builder().build();
    private final ResourceManager resources = ResourceManager.getInstance();

    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String SELECTED_COMPANY = "selectedCompany";
    public static String SELECTED_REGION = "selectedRegion";
    public static String SELECTED_LOCATION = "selectedLocation";

    public WizardContext(final @NotNull AbstractWizard<T> wizard) {
        this.parent = wizard.getWizard();
    }

    public WizardNavigator<T> getNavigator() {
        return parent.getNavigator();
    }

    public void navigateToNext() {
        getNavigator().nextPage();
    }

    public void navigateToPrevious() {
        getNavigator().previousPage();
    }

    public void navigateTo(final WizardPages page) {
        getNavigator().navigateTo(page);
    }

    @Contract("_ -> new")
    public static <T extends AbstractWizard<T>> @NotNull WizardContext<T> of(final AbstractWizard<T> wizard) {
        return new WizardContext<>(wizard);
    }
}
