package com.jwcomptech.commons.javafx.wizard.base;

/*-
 * #%L
 * JWCommons
 * %%
 * Copyright (C) 2025 JWCompTech
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.javafx.controls.FXButton;
import com.jwcomptech.commons.javafx.wizard.enums.WizardPages;
import com.jwcomptech.commons.resources.ResourceManager;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
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
