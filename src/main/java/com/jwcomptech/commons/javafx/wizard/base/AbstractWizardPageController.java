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

import com.jwcomptech.commons.validators.Validated;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@SuppressWarnings("unused")
@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractWizardPageController
        extends Validated implements WizardPage {
    @Setter(AccessLevel.PACKAGE)
    protected WizardContext<?> context;

    @Setter(AccessLevel.PACKAGE)
    private WizardNavigator<?> navigator;

    @Setter(AccessLevel.PACKAGE)
    private Parent root;

    @FXML
    public final void initialize() {
        navigator = context.getNavigator();

        postInitialize();

        setNextButtonEnabled(evaluateAllVals().isValid());

        afterValidation();
    }

    protected final void onUserInputChanged() {
        reevaluateAllVals();
        afterValidation();
    }

    protected final void setNextButtonEnabled(boolean enabled) {
        navigator.getNextButton().setDisable(!enabled);
    }

    protected final void disableNextButton() {
        navigator.getNextButton().disable();
    }

    protected final void enableNextButton() {
        navigator.getNextButton().enable();
    }

    protected abstract void postInitialize();

    protected abstract void afterValidation();

    public abstract void beforeNext();

    public abstract void beforePrevious();

    public abstract void afterNext();

    public abstract void afterPrevious();

    public abstract void onCancel();

    public abstract void afterFinished();
}
