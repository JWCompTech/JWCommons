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
import com.jwcomptech.commons.javafx.resources.Resource;
import com.jwcomptech.commons.javafx.resources.ResourceManager;
import com.jwcomptech.commons.javafx.resources.enums.ResourceType;
import com.jwcomptech.commons.tuples.ImmutablePair;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Data;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.jwcomptech.commons.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;

/**
 * Loads a dialog using the specified JavaFX fxml file.
 *
 * @param <R> the class type to be returned when the dialog is closed
 * @param <C> the controller class to be used for the dialog
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Value
public class FXMLDialogWrapper<R, C extends Initializable> {
    /**
     * The {@code Dialog} as a {@code Dialog} object.
     */
    Dialog<R> dialog;

    /**
     * The {@code FXMLLoader} that was used to load the {@code Dialog}.
     */
    FXMLLoader loader;

    /**
     * Creates an instance of a dialog and sets the specified title, icon, owner and fxml file path.
     * @param title the title
     * @param icon the icon image to be used in the window decorations and when minimized
     * @param owner the owner of the Window
     * @param fxmlPath the path of the fxml file
     */
    private FXMLDialogWrapper(final String title, final Image icon, final Stage owner, final String fxmlPath) {
        checkArgumentNotNullOrEmpty(fxmlPath, cannotBeNullOrEmpty("fxmlPath"));

        final ResourceManager resources = ResourceManager.getInstance();
        final Optional<String> fxmlFilename = resources.parseResourceURL(fxmlPath).getValue();

        if(fxmlFilename.isEmpty()) {
            throw new IllegalArgumentException("Invalid fxml path, does not start " +
                    "with known resource type: " + fxmlPath);
        }

        final Resource fxmlResource = resources.addResource(ResourceType.FXML, fxmlFilename.get());

        final ImmutablePair<FXMLLoader, Dialog<R>> dialogPair = fxmlResource.getAsDialog();
        loader = dialogPair.getKey();
        dialog = dialogPair.getValue();

        final var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.setResizable(false);
        dialogStage.initOwner(owner);

        if(title != null) {
            checkArgumentNotNullOrEmpty(title, cannotBeNullOrEmpty(title));
            dialogStage.setTitle(title);
        }
        if(title != null && icon != null) {
            if(icon.isError()) throw new IllegalArgumentException("Icon cannot have loading errors.");
            else dialogStage.getIcons().add(icon);
        }
    }

    /**
     * Returns the controller associated with the {@code Dialog}.
     * @return the {@code Controller} object
     */
    public C getController() { return loader.getController(); }

    /**
     * Defines the controller associated with the {@code Dialog}.
     * @param controller the {@code Controller} object
     */
    public void setController(final Initializable controller) { loader.setController(controller); }

    /**
     * Shows the dialog and waits for the user response (in other words, brings
     * up a blocking dialog, with the returned value the users input).
     * <p>
     * This method must be called on the JavaFX Application thread.
     * Additionally, it must either be called from an input event handler or
     * From the run method of a Runnable passed to
     * {@link javafx.application.Platform#runLater Platform.runLater}.
     * It must not be called during animation or layout processing.
     * </p>
     *
     * @return An {@link Optional} that contains the result.
     *         Refer to the {@link Dialog} class documentation for more detail.
     * @throws IllegalStateException if this method is called on a thread
     *     other than the JavaFX Application Thread.
     * @throws IllegalStateException if this method is called during
     *     animation or layout processing.
     */
    public Optional<R> showAndWait() { return dialog.showAndWait(); }

    /**
     * Returns the title of the {@code Dialog}.
     * @return the title
     */
    public String getTitle() {
        final var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        return dialogStage.getTitle();
    }

    /**
     * Defines the title of the {@code Dialog}.
     * @param value the title to set.
     */
    public void setTitle(final String value) {
        checkArgumentNotNullOrEmpty(value, cannotBeNullOrEmpty("value"));

        final var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.setTitle(value);
    }

    /**
     * Returns the icon image for the {@code Dialog} that is used in the window decorations and when minimized.
     * @return the icon image, null if no icon exists
     */
    public @Nullable Image getIcon() {
        final var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        return dialogStage.getIcons().isEmpty() ? null : dialogStage.getIcons().getFirst();
    }

    /**
     * Defines the icon image for the {@code Dialog} to be used in the window decorations and when minimized.
     * @param value the icon image
     */
    public void setIcon(final Image value) {
        checkArgumentNotNull(value, cannotBeNullOrEmpty("value"));
        if(value.isError()) throw new IllegalArgumentException(
                "Icon cannot have loading errors.");

        final var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(value);
    }

    /**
     * Returns the owner Window for this {@code Dialog}, or null for a top-level,
     * unowned stage.
     * @return the owner for this dialog.
     */
    public Stage getOwner() {
        final var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        return (Stage) dialogStage.getOwner();
    }

    /**
     * Defines the owner Window for this {@code Dialog}, or null for a top-level,
     * unowned stage. This must be done prior to making the stage visible.
     * @param value the owner for this dialog.
     * @throws IllegalStateException if this property is set after the stage
     * has ever been made visible.
     * @throws IllegalStateException if this stage is the primary stage.
     */
    public void setOwner(final Stage value) {
        final var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.initOwner(value);
    }

    /**
     * Returns whether this {@code Dialog} is kept on top of other windows.
     * <p>
     * If some other window is already always-on-top then the
     * relative order between these windows is unspecified (depends on
     * platform).
     * </p>
     * <p>
     * There are differences in behavior between applications if a security
     * manager is present. Applications with permissions are allowed to set
     * "always on top" flag on a Stage. In applications without the proper
     * permissions, an attempt to set the flag will be ignored and the property
     * value will be restored to "false".
     * </p>
     * @return if dialog is kept on top of other windows
     */
    public boolean isAlwaysOnTop() {
        final var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        return dialogStage.isAlwaysOnTop();
    }

    /**
     * Defines whether this {@code Dialog} is kept on top of other windows.
     * <p>
     * If some other window is already always-on-top then the
     * relative order between these windows is unspecified (depends on
     * platform).
     * </p>
     * <p>
     * There are differences in behavior between applications if a security
     * manager is present. Applications with permissions are allowed to set
     * "always on top" flag on a Stage. In applications without the proper
     * permissions, an attempt to set the flag will be ignored and the property
     * value will be restored to "false".
     * </p>
     * @param value the value to set.
     */
    public void setAlwaysOnTop(final boolean value) {
        final var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.setAlwaysOnTop(value);
    }

    @Data
    public class Builder implements Buildable<FXMLDialogWrapper<R, C>> {
        /**
         * The dialog title.
         */
        private final String title;
        /**
         * The icon image to be used in the window decorations and when minimized.
         */
        private final Image icon;
        /**
         * The owner of the Window.
         */
        private final Stage owner;
        /**
         * The path of the fxml file.
         */
        private final String fxmlPath;

        public FXMLDialogWrapper<R, C> build() {
            return new FXMLDialogWrapper<>(title, icon, owner, fxmlPath);
        }
    }
}
