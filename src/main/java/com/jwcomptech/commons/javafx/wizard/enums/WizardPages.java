package com.jwcomptech.commons.javafx.wizard.enums;

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

import com.jwcomptech.commons.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public enum WizardPages implements BaseEnum<String> {
    Finish("FinishPage.fxml"),
    Login("LoginPage.fxml"),
    Review("ReviewPage.fxml"),
    SelectCompany("SelectCompanyPage.fxml"),
    SelectFeatures("SelectFeaturesPage.fxml"),
    SelectInstallLocation("SelectInstallLocationPage.fxml"),
    SelectLicense("SelectLicensePage.fxml"),
    SelectLogAndDataPaths("SelectLogAndDataPathsPage.fxml"),
    SelectOptionalSettings("SelectOptionalSettingsPage.fxml"),
    SelectRegionAndLocation("SelectRegionAndLocationPage.fxml"),
    SelectRoles("SelectRolesPage.fxml"),
    StartInstall("StartInstallPage.fxml"),
    LocationValidationError("LocationValidationErrorPage.fxml"),
    Welcome("WelcomePage.fxml"),
    CreateLocation("CreateLocationPage.fxml");

    private final String value;

    public String getFXMLPath() {
        return value;
    }
}
