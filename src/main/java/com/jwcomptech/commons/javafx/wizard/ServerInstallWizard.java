package com.jwcomptech.commons.javafx.wizard;

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
