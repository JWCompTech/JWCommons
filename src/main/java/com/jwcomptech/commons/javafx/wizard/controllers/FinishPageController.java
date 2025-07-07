package com.jwcomptech.commons.javafx.wizard.controllers;

import com.jwcomptech.commons.javafx.wizard.base.AbstractWizardPageController;
import com.jwcomptech.commons.validators.Condition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FinishPageController extends AbstractWizardPageController {

    public void postInitialize() { }

    @Override
    protected void afterValidation() { }

    @Override
    public void beforeNext() {
        //Run any extra tasks selected by user via check boxes
        //Close Wizard
    }

    @Override
    public void beforePrevious() {
        //Shouldn't do anything because install is complete
        //Back button should actually be disabled so this method is never called
    }

    @Override
    public void afterNext() { }

    @Override
    public void afterPrevious() { }

    @Override
    public void onCancel() { }

    @Override
    public void afterFinished() { }
}
