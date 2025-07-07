package com.jwcomptech.commons.javafx.wizard.base;

import com.jwcomptech.commons.base.Validated;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

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
