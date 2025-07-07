package com.jwcomptech.commons.javafx.wizard;

import com.jwcomptech.commons.javafx.controls.FXButtonTypeGroup;
import com.jwcomptech.commons.javafx.wizard.base.AbstractWizard;
import com.jwcomptech.commons.javafx.wizard.base.WizardPageLoader;
import com.jwcomptech.commons.javafx.wizard.enums.WizardPages;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServerInstallWizard extends AbstractWizard<ServerInstallWizard> {

    @Override
    protected ServerInstallWizard getWizard() {
        return this;
    }

    @Override
    protected FXButtonTypeGroup getInitialButtonTypeGroup() {
        return FXButtonTypeGroup.BackNextCancel;
    }

    @Override
    protected String getWindowTitle() {
        return "Installation Wizard";
    }

    @Override
    public void initPages(final @NotNull WizardPageLoader<ServerInstallWizard> loader) {
        loader.addPage(WizardPages.Welcome)
                .addPage(WizardPages.Login)
                .addPage(WizardPages.SelectCompany)
                .addPage(WizardPages.SelectRegionAndLocation)
//                .addPage(WizardPages.SelectLicense)
//                .addPage(WizardPages.SelectRoles)
//                .addPage(WizardPages.SelectFeatures)
//                .addPage(WizardPages.SelectInstallLocation)
//                .addPage(WizardPages.SelectLogAndDataPaths)
//                .addPage(WizardPages.SelectOptionalSettings)
//                .addPage(WizardPages.Review)
//                .addPage(WizardPages.StartInstall)
                .addPage(WizardPages.Finish);
    }

    @Override
    protected void postInitialize() { }
}
