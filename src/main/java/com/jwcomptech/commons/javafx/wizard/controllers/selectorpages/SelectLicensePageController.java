package com.jwcomptech.commons.javafx.wizard.controllers.selectorpages;

import com.jwcomptech.commons.javafx.wizard.base.AbstractWizardPageController;
import com.jwcomptech.commons.validators.Condition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SelectLicensePageController extends AbstractWizardPageController {

    @Override
    public void postInitialize() { }

    @Override
    protected void afterValidation() { }

    @Override
    public void beforeNext() { }

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
