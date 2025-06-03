package com.jwcomptech.commons.javafx.dialogs;

/*-
 * #%L
 * JWCT Commons
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

import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.resources.ResourceManager;
import com.jwcomptech.commons.resources.enums.ResourceType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * A Login Dialog implementation for use to authenticate passwords.
 * <p>
 *     Use setTitle to set the title of the window<br>
 *     Use setHeaderText to set the header of the window<br>
 *     Use setIconPath to set the icon of the window, path must be in the resource folder
 * </p>
 *
 * @since 0.0.1
 */
@ToString
final class LoginDialogImpl extends Dialog<Pair<String, String>> {

    /**
     * The path in the resource folder of the window icon.
     */
    @Getter
    private String iconPath = "";

    /**
     * Sets the path in the resource folder of the window icon.
     * @param iconPath the icon path
     */
    void setIconPath(final @NotNull String iconPath) {
        this.iconPath = iconPath;

        final var stage = (Stage) getDialogPane().getScene().getWindow();
        if(!iconPath.trim().isEmpty()) stage.getIcons().add(new Image(iconPath));
    }

    LoginDialogImpl(final String warningText, final boolean redText) {
        final ResourceManager resources = ResourceManager.getInstance();
        final Resource lock_red = resources.addResource(ResourceType.IMG, "Lock-Red.png");
        final Resource data_secure = resources.addResource(ResourceType.IMG, "Data-Secure.png");
        final Resource login_dialog_css = resources.addResource(ResourceType.CSS, "LoginDialog.css");

        setTitle("Login");
        setHeaderText("Please Login to Continue!");

        // Create the username and password labels and fields.
        final var mainGrid = new GridPane();

        final var status = new Label(warningText);
        status.setPadding(new Insets(0, 0, 0, 10));
        status.setFont(new Font("Arial", 16));
        if(redText) status.setTextFill(Color.RED);
        if(redText) status.setGraphic(new ImageView(lock_red.getAsImage()));
        mainGrid.add(status, 0, 0);

        final var buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setPadding(new Insets(0, 100, 10, 10));

        buttonGrid.getStylesheets().add(login_dialog_css.getURLString());

        // Set the icon (must be included in the project).
        setGraphic(new ImageView(data_secure.getAsImage()));

        final var usernameLabel = new Label("Username:");
        usernameLabel.setFont(new Font("Arial", 16));
        final var username = new TextField();
        username.setId("txtUsername");
        username.setPromptText("Username");
        username.setFont(new Font("Arial", 16));

        final var passwordLabel = new Label("Password:");
        passwordLabel.setFont(new Font("Arial", 16));
        final var password = new PasswordField();
        password.setId("txtPassword");
        password.setPromptText("Password");
        password.setFont(new Font("Arial", 16));

        buttonGrid.add(usernameLabel, 0, 1);
        buttonGrid.add(username, 1, 1);
        buttonGrid.add(passwordLabel, 0, 2);
        buttonGrid.add(password, 1, 2);

        mainGrid.add(buttonGrid, 0, 1);

        getDialogPane().setContent(mainGrid);

        //Set dialog result data for login button
        final var loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        //By default, disable the login button
        final var loginButton = getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Enable/Disable login button depending on whether a username was entered.
        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) ->
                loginButton.setDisable(newValue.trim().isEmpty()));

        // Request focus on the username field by default.
        Platform.runLater(username::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });
    }
}
