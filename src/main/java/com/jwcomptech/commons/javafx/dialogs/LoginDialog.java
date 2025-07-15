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

import com.jwcomptech.commons.interfaces.Buildable;
import com.jwcomptech.commons.javafx.controls.FXButtonType;
import com.jwcomptech.commons.javafx.controls.FXButtonTypeGroup;
import com.jwcomptech.commons.javafx.controls.FXControls;
import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.javafx.controls.FXPaneType;
import com.jwcomptech.commons.resources.enums.Builtin;
import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.resources.ResourceManager;
import com.jwcomptech.commons.resources.enums.ResourceDir;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.jwcomptech.commons.javafx.JavaFXUtils.*;
import static com.jwcomptech.commons.utils.StringUtils.fallbackIfBlank;

/**
 * Creates a Login Dialog for use to authenticate passwords.
 * <p>
 *     Use setTitle to set the title of the window<br>
 *     Use setHeaderText to set the header of the window<br>
 *     Use setIconPath to set the icon of the window, path must be in the resource folder
 * </p>
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Data
public class LoginDialog {
    private String warningText = "Invalid username or password. Please try again.";
    private boolean redText = true;
    private FXControls controls = new FXControls();

    private static final ResourceManager resources = ResourceManager.getInstance();

    private final ObjectProperty<FXDialog> dialog = new SimpleObjectProperty<>();

    private static final String DEFAULT_FONT_FAMILY = "Arial";
    private static final double DEFAULT_FONT_SIZE = 16.0;

    private static final Insets FIELD_PANE_PADDING = new Insets(0, 100, 10, 10);
    private static final double FIELD_PANE_HGAP = 10;
    private static final double FIELD_PANE_VGAP = 10;

    /**
     * Needs to be private to force object creation via the Builder class.
     */
    private LoginDialog() { }

    /**
     * Shows the dialog and waits for the user response (in other words, brings
     * up a blocking dialog, with the returned value the users input).
     * The first string value is the username and the second is the password.
     * @return An {@link Optional} that contains the result.
     *         Refer to the {@link Dialog} class documentation for more detail.
     * @throws IllegalStateException if this method is called during
     *     animation or layout processing.
     */
    public @NotNull Optional<DialogResult> showAndWait() {
        final var result = new SimpleObjectProperty<Optional<DialogResult>>();
        runAndWait(() -> result.set(getDialogOrThrow().showAndWait()));

        return result.get();
    }

    /**
     * Returns the {@link Resource} objects used for the window icon.
     * @return the icon {@link Resource} objects
     */
    public List<Image> getIconPaths() {
        final var stage = (Stage) getDialogOrThrow().getDialogPane().getScene().getWindow();
        return stage.getIcons();
    }

    /**
     * Returns the {@link Resource} object used for the window icon.
     * @return the icon {@link Resource}
     */
    public @NotNull List<Resource> getIconResource() {
        final List<Image> iconImages = getIconPaths();
        final List<Resource> iconResources = new ArrayList<>();

        for (final var iconPath : iconImages) {
            resources.getResource(iconPath.getUrl()).ifPresent(iconResources::add);
        }
        return iconResources;
    }

    /**
     * Sets the {@link Resource} object to use for the window icon.
     * @param icon the icon {@link Resource}
     */
    public void addIcon(final @NotNull Resource icon) {
        final ObjectProperty<Image> iconImage = new SimpleObjectProperty<>();

        safeImage(icon).ifPresent(iconImage::set);

        runLaterIfNeeded(() -> {
            final var stage = (Stage) getDialogOrThrow().getDialogPane().getScene().getWindow();
            if (iconImage.get() != null) stage.getIcons().add(iconImage.get());
        });
    }

    /**
     * Sets the {@link Resource} objects to use for the window icon.
     *
     * @param icons the icon {@link Resource} objects
     */
    public void setIcons(final @NotNull List<Resource> icons) {
        final List<Image> iconImages = new ArrayList<>();

        for(final var icon : icons) {
            safeImage(icon).ifPresent(iconImages::add);
        }

        runLaterIfNeeded(() -> {
            final var stage = (Stage) getDialogOrThrow().getDialogPane().getScene().getWindow();
            if (!iconImages.isEmpty()) stage.getIcons().addAll(iconImages);
        });
    }

    /** Closes the dialog. */
    public void close() {
        runLaterIfNeeded(() -> getDialogOrThrow().close());
    }

    /** Hides the dialog. */
    public void hide() {
        runLaterIfNeeded(() -> getDialogOrThrow().hide());
    }

    /**
     * Returns the title of the dialog.
     *
     * @return the title of the dialog
     */
    public String getTitle() { return getDialogOrThrow().getTitle(); }

    /**
     * Defines the title of the dialog.
     *
     * @param title the title to set
     */
    public void setTitle(final String title) {
        runLaterIfNeeded(() -> getDialogOrThrow().setTitle(title));
    }

    /**
     * Returns the string to show in the dialog header area.
     *
     * @return the string to show in the dialog header area
     */
    public String getHeaderText() { return getDialogOrThrow().getHeaderText(); }

    /**
     * Defines the string to show in the dialog header area.
     *
     * @param headerText the header text to set
     */
    public void setHeaderText(final String headerText) {
        runLaterIfNeeded(() -> getDialogOrThrow().setHeaderText(headerText));
    }

    /**
     * Returns true if the dialog is showing.
     *
     * @return true if the dialog is showing
     */
    public boolean isShowing() { return getDialogOrThrow().isShowing(); }

    /**
     * Returns the base {@link FXDialog} that is wrapped by this object.
     *
     * @return the base {@link FXDialog} that is wrapped by this object
     */
    public FXDialog getDialog() { return getDialogOrThrow(); }

    private @NotNull FXDialog getDialogOrThrow() {
        if (dialog.get() == null) {
            throw new IllegalStateException("Dialog has not been initialized. " +
                    "Use LoginDialog.builder().build() to create the dialog instance.");

        }
        return dialog.get();
    }

    private static Optional<Image> safeImage(@NotNull Resource resource) {
        ResourceManager.getInstance().addResource(ResourceDir.IMAGE, resource);
        return resource.asImage()
                .filter(image -> !image.isError());
    }

    protected @NotNull FXDialog createDialog(final String title,
                                                             final String headerText,
                                                             final String warningText,
                                                             final boolean redText) {
        final FXDialog dialog = new FXDialog(FXButtonTypeGroup.LoginCancel);

        final ResourceManager resources = ResourceManager.getInstance();
        final String title_ = "Login";
        final String headerText_ = "Please Login to Continue!";

        dialog.setTitle(fallbackIfBlank(title, title_));

        dialog.setHeaderText(fallbackIfBlank(headerText, headerText_));

        // Set the icon.
        Builtin.Data_Secure_IMG.getResource().asImage()
                .ifPresent(image -> dialog.setGraphic(new ImageView(image)));

        configureDialogButtons(dialog);

        //Set dialog result data for login button
        final var loginButtonObj = controls.getDialogButton("Login");
        if(loginButtonObj.isEmpty()) {
            throw new IllegalStateException("Login button not loaded!");
        }
        final var loginButton = loginButtonObj.get();

        final var cancelButtonObj = controls.getDialogButton("Cancel");
        if(cancelButtonObj.isEmpty()) {
            throw new IllegalStateException("Cancel button not loaded!");
        }
        final var cancelButton = cancelButtonObj.get();

        configureDialogPane(dialog);

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        final var username = controls.getTextField("username").get();
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        final var password = controls.getPasswordField("password").get();

        configureControlValidations(controls);

        // Request focus on the username field by default.
        runLaterIfNeeded(username::requestFocus);

        configureResultConverter(dialog, username, password);

        return dialog;
    }

    protected void configureControlValidations(final @NotNull FXControls controls) {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        final var username = controls.getTextField("username").get();

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        final var loginButton = controls.getDialogButton("Login").get();

        // Enable/Disable login button depending on whether a username was entered.
        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) ->
                loginButton.setDisable(newValue.trim().isEmpty()));
    }

    protected void configureDialogPane(final @NotNull FXDialog dialog) {
        // Create the base grid pane
        final var builder = dialog.getDialogPaneBuilder(FXPaneType.GRID_PANE);
        builder.addToGridPane(configureStatusLabel(
                        controls, warningText, redText, dialog),
                0, 0);

        builder.withGrid(pane ->
                controls.addControl(GridPane.class, "MainGrid", pane));

        // Create the username and password labels and fields.
        final var pane = configureFieldPane(new GridPane());

        if(configureFieldPaneCSS() != null) {
            pane.getStylesheets().add(configureFieldPaneCSS().getURLString());
        }

        final TextField username = configureUsernameField(controls);
        final PasswordField password = configurePasswordField(controls);

        pane.add(configureUsernameLabel(controls), 0, 1);
        pane.add(username, 1, 1);
        pane.add(configurePasswordLabel(controls), 0, 2);
        pane.add(password, 1, 2);

        builder.addToGridPane(pane,0, 1);

        builder.buildAndUpdate();
    }

    protected void configureResultConverter(final @NotNull FXDialog dialog,
                                            final @NotNull TextField username,
                                            final @NotNull PasswordField password) {
        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == FXButtonType.LOGIN.getButtonType()) {
                final FXData data = FXData.builder()
                        .putString("username", username.getText())
                        .putString("password", password.getText())
                        .build();
                return FXButtonType.LOGIN.toDialogResult(data);
            }
            return null;
        });
    }

    protected void configureDialogButtons(final @NotNull FXDialog dialog) {
        final DialogButton loginButton = dialog.getButton(0);
        final DialogButton cancelButton = dialog.getButton(1);

        // Configures Login Button
        if(loginButton != null) {
            //By default, disable the login button
            loginButton.disable();
            loginButton.setDefaultButton(true);
        }

        // Configures Cancel Button
        //noinspection StatementWithEmptyBody
        if(cancelButton != null) {
            // Put Cancel button config here
        }
    }

    /**
     * Allows for subclasses to customize the {@link GridPane} that holds all the login
     * fields. This by default sets the HGap and VGap to 10
     * and the padding to [0, 100, 10, 10].
     *
     * @param pane the {@link GridPane} passed in during object creation
     * @return the modified {@link GridPane}
     * @apiNote This method should never be called directly and is only called
     * during object creation. This method should only be overridden during subclassing.
     */
    @Contract("_ -> param1")
    protected @NotNull GridPane configureFieldPane(final @NotNull GridPane pane) {
        pane.setHgap(FIELD_PANE_HGAP);
        pane.setVgap(FIELD_PANE_VGAP);
        pane.setPadding(FIELD_PANE_PADDING);

        return pane;
    }

    /**
     * Allows for subclasses to customize the {@link GridPane} CSS that holds all the
     * login fields, default is null.
     *
     * @return the CSS resource to use
     * @apiNote This method should never be called directly and is only called
     * during object creation. This method should only be overridden during subclassing.
     */
    protected Resource configureFieldPaneCSS() {
        //return resources.getResource(Builtin.Login_Dialog_CSS);
        return null;
    }

    /**
     * Allows for subclasses to customize the status {@link Label} that is used to
     * display error messages and the default calls
     * {@link LoginDialog#configureLabelFont()}
     * to set the font for the label text.
     *
     * @param controls an object that contains all controls currently in use and
     *                 any new controls created in this method should be added
     *                 to this object
     * @param warningText the warning text to set to the label
     * @param redText if true should display the text with the {@link Color#RED}
     * @param dialog the new {@link Dialog}
     * @return the modified status {@link Label}
     * @apiNote This method should never be called directly and is only called
     * during object creation. This method should only be overridden during subclassing.
     */
    protected @NotNull Label configureStatusLabel(final @NotNull FXControls controls,
                                                         final String warningText,
                                                         final boolean redText,
                                                         final FXDialog dialog) {
        final var label = new Label(warningText);
        //Adds space to make room for the icon, not needed if icon is removed
        label.setPadding(new Insets(0, 0, 0, 10));
        //Sets the text font, can be changed manually if you want to customize further
        label.setFont(configureLabelFont());
        controls.addLabel("status", label);

        //Checks if red text is enabled and sets the text color and icon
        if(redText) {
            label.setTextFill(Color.RED);
            Builtin.Red_Lock_IMG.getResource().asImage()
                    .ifPresent(image -> dialog.setGraphic(new ImageView(image)));
        }

        return label;
    }

    /**
     * Allows for subclasses to customize the username {@link Label} that is used
     * to describe the username field and the default calls
     * {@link LoginDialog#configureLabelFont()}
     * to set the font for the label text.
     *
     * @param controls an object that contains all controls currently in use and
     *                 any new controls created in this method should be added
     *                 to this object
     * @return the modified username {@link Label}
     * @apiNote This method should never be called directly and is only called
     * during object creation. This method should only be overridden during subclassing.
     */
    protected @NotNull Label configureUsernameLabel(final @NotNull FXControls controls) {
        final var label = new Label("Username:");
        //Sets the text font, can be changed manually if you want to customize further
        label.setFont(configureLabelFont());
        controls.addLabel("username", label);

        return label;
    }

    /**
     * Allows for subclasses to customize the username {@link TextField} and the
     * default calls {@link LoginDialog#configureTextFieldFont()} to set the font
     * for the field text.
     * @param controls an object that contains all controls currently in use and
     *                 any new controls created in this method should be added
     *                 to this object
     * @return the modified username {@link TextField}
     * @apiNote This method should never be called directly and is only called
     * during object creation. This method should only be overridden during subclassing.
     */
    protected @NotNull TextField configureUsernameField(final @NotNull FXControls controls) {
        final var textField = new TextField();
        textField.setId("txtUsername");
        textField.setPromptText("Username");
        //Sets the text font, can be changed manually if you want to customize further
        textField.setFont(configureTextFieldFont());

        Resource image = Builtin.User_Login_IMG.getResource();

        textField.setStyle(
                "-fx-background-image: " + image.getCSSPath() + ";" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: 180px center;"
        );

        controls.addTextField("username", textField);

        return textField;
    }

    /**
     * Allows for subclasses to customize the password {@link Label} that is used
     * to describe the password field and the default calls
     * {@link LoginDialog#configureLabelFont()}
     * to set the font for the label text.
     *
     * @param controls an object that contains all controls currently in use and
     *                 any new controls created in this method should be added
     *                 to this object
     * @return the modified password {@link Label}
     * @apiNote This method should never be called directly and is only called
     * during object creation. This method should only be overridden during subclassing.
     */
    protected @NotNull Label configurePasswordLabel(final @NotNull FXControls controls) {
        final var label = new Label("Password:");
        //Sets the text font, can be changed manually if you want to customize further
        label.setFont(configureLabelFont());
        controls.addLabel("password", label);

        return label;
    }

    /**
     * Allows for subclasses to customize the password {@link PasswordField} and the
     * default calls {@link LoginDialog#configureTextFieldFont()} to set the font
     * for the field text.
     * @param controls an object that contains all controls currently in use and
     *                 any new controls created in this method should be added
     *                 to this object
     * @return the modified password {@link PasswordField}
     * @apiNote This method should never be called directly and is only called
     * during object creation. This method should only be overridden during subclassing.
     */
    protected @NotNull PasswordField configurePasswordField(final @NotNull FXControls controls) {
        final var passwordField = new PasswordField();
        passwordField.setId("txtPassword");
        passwordField.setPromptText("Password");
        //Sets the text font, can be changed manually if you want to customize further
        passwordField.setFont(configureTextFieldFont());

        Resource image = Builtin.Key_IMG.getResource();

        passwordField.setStyle(
                "-fx-background-image: " + image.getCSSPath() + ";" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: 180px center;"
        );

        controls.addPasswordField("password", passwordField);

        return passwordField;
    }

    /**
     * Allows for subclasses to customize the label {@link Font} and the
     * default sets the font to "Arial" with the size of "16".
     *
     * @return the {@link Font} to use for all labels
     * @apiNote This method should never be called directly and is only called
     * during object creation. This method should only be overridden during subclassing.
     */
    @Contract(" -> new")
    protected static @NotNull Font configureLabelFont() {
        return new Font(DEFAULT_FONT_FAMILY, DEFAULT_FONT_SIZE);
    }

    /**
     * Allows for subclasses to customize the field text {@link Font} and the
     * default sets the font to "Arial" with the size of "16".
     *
     * @return the {@link Font} to use for all field text
     * @apiNote This method should never be called directly and is only called
     * during object creation. This method should only be overridden during subclassing.
     */
    @Contract(" -> new")
    protected static @NotNull Font configureTextFieldFont() {
        return new Font(DEFAULT_FONT_FAMILY, DEFAULT_FONT_SIZE);
    }

    /**
     * Returns a new instance of the LoginDialog builder.
     * @return a new instance of the LoginDialog builder
     */
    @Contract(" -> new")
    public static @NotNull LoginDialog.Builder builder() {
        return new Builder();
    }

    public static @NotNull Buildable<LoginDialog> buildable() {
        return new Builder();
    }

    /**
     * The builder class for the LoginDialog.
     */
    @Data
    public static class Builder implements Buildable<LoginDialog> {
        private String warningText = "";
        private boolean redText = false;
        private String title = "";
        private String headerText = "";
        private List<Resource> icons = new ArrayList<>();

        /**
         * Sets the warning text to use for the status label to
         * the default ("Invalid username or password. Please try again.").
         *
         * @return this instance to for method chaining
         */
        public Builder withDefaultWarningText() {
            this.warningText = "Invalid username or password. Please try again.";
            return this;
        }

        /**
         * Sets the warning text to use for the status label.
         *
         * @param warningText the text to set
         * @return this instance to for method chaining
         */
        public Builder withWarningText(final String warningText) {
            this.warningText = warningText;
            return this;
        }

        /**
         * Sets if the status label should use red text.
         *
         * @param redText if true the status label text will be red
         * @return this instance to for method chaining
         */
        public Builder withRedText(final boolean redText) {
            this.redText = redText;
            return this;
        }

        /**
         * Sets if the title of the dialog window.
         *
         * @param title the window title
         * @return this instance to for method chaining
         */
        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets if the header text of the dialog window.
         *
         * @param headerText the window header text
         * @return this instance to for method chaining
         */
        public Builder withHeaderText(final String headerText) {
            this.headerText = headerText;
            return this;
        }

        /**
         * Sets the main icon of the dialog window.
         *
         * @param icon the window icon
         * @return this instance to for method chaining
         */
        public Builder withIcon(final Resource icon) {
            this.icons.add(icon);
            return this;
        }

        /**
         * Sets the icons of the dialog window.
         *
         * @param icons the window icons
         * @return this instance to for method chaining
         */
        public Builder withIcons(final List<Resource> icons) {
            if(icons != null) this.icons = icons;
            return this;
        }

        /**
         * Builds a new LoginDialog instance with the set parameters.
         *
         * @return a new LoginDialog instance
         */
        public LoginDialog build() {
            final LoginDialog newDialog = new LoginDialog();
            final FXDialog newBaseDialog =
                    newDialog.createDialog(title, headerText, warningText, redText);

            runLaterIfNeeded(() -> {
                newDialog.dialog.set(newBaseDialog);
                newDialog.setIcons(icons);
            });

            return newDialog;
        }
    }
}
