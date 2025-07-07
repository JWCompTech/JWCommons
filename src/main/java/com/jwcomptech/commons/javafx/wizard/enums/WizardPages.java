package com.jwcomptech.commons.javafx.wizard.enums;

import com.jwcomptech.commons.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
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
