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

import com.jwcomptech.commons.Condition;
import com.jwcomptech.commons.interfaces.Buildable;
import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.resources.ResourceManager;
import com.jwcomptech.commons.resources.enums.ResourceType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Dialog;
import javafx.util.Pair;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Optional;

import static com.jwcomptech.commons.javafx.JavaFXUtils.*;

/**
 * Creates a Login Dialog for use to authenticate passwords.
 * <p>
 *     Use setTitle to set the title of the window<br>
 *     Use setHeaderText to set the header of the window<br>
 *     Use setIconPath to set the icon of the window, path must be in the resource folder
 * </p>
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Data
public final class LoginDialog {
    private String warningText = "Invalid username or password. Please try again.";
    private boolean redText = true;

    private final ResourceManager resources = ResourceManager.getInstance();

    private final ObjectProperty<LoginDialogImpl> dialog = new SimpleObjectProperty<>();

    private LoginDialog() {
        ifFxThreadElseCheck(() -> dialog.set(new LoginDialogImpl(warningText, redText)),
                () -> {},
                () -> Condition.of(() -> dialog.get() != null).waitTillTrue()
        );
    }

    /**
     * Shows the dialog and waits for the user response (in other words, brings
     * up a blocking dialog, with the returned value the users input).
     * The first string value is the username and the second is the password.
     * @return An {@link Optional} that contains the result.
     *         Refer to the {@link Dialog} class documentation for more detail.
     * @throws IllegalStateException if this method is called during
     *     animation or layout processing.
     */
    public @NotNull Optional<Pair<String, String>> showAndWait() {
        final var result = new SimpleObjectProperty<Optional<Pair<String, String>>>();
        ifFxThreadElseCheck(() -> result.set(dialog.get().showAndWait()),
                () -> {},
                () -> Condition.of(() -> result.get().isPresent()).waitTillTrue()
        );

        return result.get();
    }

    /**
     * Returns the path in the resource folder of the window icon.
     * @return the icon path
     */
    public String getIconPath() { return dialog.get().getIconPath(); }

    /**
     * Returns the Resource object representing the window icon.
     * @return the icon Resource
     */
    public Optional<Resource> getIconResource() {
        return resources.getResource(getIconPath());
    }

    /**
     * Sets the filename of an image in the resource folder to use for the window icon.
     * @param resourceName the icon filename
     */
    public void setIcon(final String resourceName) {
        if(resourceName == null || resourceName.isEmpty()) {
            runLaterCheck(() -> dialog.get().setIconPath(""));
        } else {
            final Resource icon = resources.addResource(ResourceType.IMG, resourceName);
            runLaterCheck(() -> dialog.get().setIconPath(icon.getURLString()));
        }
    }

    /**
     * Sets the url of an image in the resource folder to use for the window icon.
     * @param resourceURL the icon path
     */
    public void setIconURL(final String resourceURL) {
        if(resourceURL != null && !resourceURL.isEmpty()) {
            final Optional<Resource> icon = resources.addResource(resourceURL);
            icon.ifPresent(resource ->
                    runLaterCheck(() -> dialog.get().setIconPath(resource.getURLString())));
        }

        runLaterCheck(() -> dialog.get().setIconPath(""));
    }

    /** Closes the dialog. */
    public void close() {
        runLaterCheck(() -> dialog.get().close());
    }

    /** Hides the dialog. */
    public void hide() {
        runLaterCheck(() -> dialog.get().hide());
    }

    /**
     * Return the title of the dialog.
     * @return the title of the dialog
     */
    public String getTitle() { return dialog.get().getTitle(); }

    /**
     * Change the title of the dialog.
     * @param title the title to set
     */
    public void setTitle(final String title) {
        runLaterCheck(() -> dialog.get().setTitle(title));
    }

    /**
     * Returns the string to show in the dialog header area.
     * @return the string to show in the dialog header area
     */
    public String getHeaderText() { return dialog.get().getHeaderText(); }

    /**
     * Sets the string to show in the dialog header area.
     * @param headerText the header text to set
     */
    public void setHeaderText(final String headerText) {
        runLaterCheck(() -> dialog.get().setHeaderText(headerText));
    }

    /**
     * Returns true if the dialog is showing.
     * @return true if the dialog is showing
     */
    public boolean isShowing() { return dialog.get().isShowing(); }


    /**
     * Returns the LoginDialog Dialog implementation object.
     * @return the LoginDialog Dialog implementation object
     */
    @SuppressWarnings("ClassEscapesDefinedScope")
    @VisibleForTesting
    public LoginDialogImpl getDialog() { return dialog.get(); }

    /**
     * Returns a new instance of the LoginDialog builder.
     * @return a new instance of the LoginDialog builder
     */
    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    @Data
    public static class Builder implements Buildable<LoginDialog> {
        private String warningText = "Invalid username or password. Please try again.";
        private boolean redText = true;
        private String title = "";
        private String headerText = "";
        private String iconURL = "";
        private String iconResourceName = "";

        public LoginDialog build() {
            final LoginDialog newDialog = new LoginDialog();

            ifFxThreadElseCheck(() -> newDialog.dialog.set(new LoginDialogImpl(warningText, redText)),
                    () -> {},
                    () -> Condition.of(() -> newDialog.getDialog() != null).waitTillTrue()
            );

            if(title != null && !title.isBlank()) {
                newDialog.setTitle(title);
            }

            if(headerText != null && !headerText.isBlank()) {
                newDialog.setHeaderText(headerText);
            }

            if(iconResourceName == null || iconResourceName.isEmpty()) {
                newDialog.setIcon(iconResourceName);
            }

            if(iconURL != null && !iconURL.isBlank()) {
                newDialog.setIconURL(iconURL);
            }

            return newDialog;
        }
    }
}
