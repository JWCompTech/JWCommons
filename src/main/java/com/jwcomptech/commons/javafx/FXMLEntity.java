package com.jwcomptech.commons.javafx;

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

import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.resources.ResourceManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import lombok.Data;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Optional;

import static com.jwcomptech.commons.javafx.JavaFXUtils.hasFxControllerTag;

/**
 * Wraps a FXML resource to make loading with {@code FXMLLoader} easier for both
 * regular FXML files and ones that are marked as a dialog.
 *
 * @param <C> the controller type
 * @since 1.0.0-alpha
 */
@Data
public class FXMLEntity<C> {
    private final Resource fxmlFile;
    private final FXMLLoader loader;
    private final C controller;
    private final Parent root;
    private final Dialog<?> dialog;

    /**
     * Creates a new FXMLEntity representing the specified FXML {@link Resource}.
     *
     * @param fxmlFile the FXML file
     * @apiNote @{@link SneakyThrows} is used to avoid spreading throws {@link IOException}
     * from {@link FXMLLoader#load()} all over the library.
     */
    @SneakyThrows
    private FXMLEntity(final @NotNull Resource fxmlFile) {
        this.fxmlFile = fxmlFile;

        loader = new FXMLLoader(fxmlFile.getURL());

        final Object obj = loader.load();

        root = obj instanceof Parent ? (Parent) obj : null;

        dialog = obj instanceof Dialog ? (Dialog<?>) obj : null;

        controller = loader.getController();
    }

    /**
     * Creates a new FXMLEntity representing the specified FXML {@link Resource},
     * using the specified controller.
     *
     * @param fxmlFile the FXML file
     * @param controller the controller
     * @apiNote @{@link SneakyThrows} is used to avoid spreading throws {@link IOException}
     * from {@link FXMLLoader#load()} all over the library.
     */
    @SneakyThrows
    private FXMLEntity(final @NotNull Resource fxmlFile,
                       final C controller) {
        this.fxmlFile = fxmlFile;

        if (controller != null && hasFxControllerTag(fxmlFile)) {
            throw new IllegalStateException("FXML file must not use fx:controller when manually injecting a controller.");
        }

        loader = new FXMLLoader(fxmlFile.getURL());
        loader.setController(controller); // inject the controller manually

        final Object obj = loader.load();

        root = obj instanceof Parent ? (Parent) obj : null;

        dialog = obj instanceof Dialog ? (Dialog<?>) obj : null;

        this.controller = controller;
    }

    /**
     * Creates a new instance with the specified FXML {@link Resource}
     * and loads the file with {@link FXMLLoader}.
     *
     * @param fxmlFile the file to wrap
     * @param <C> the controller type
     * @return a new instance
     */
    @Contract("_ -> new")
    public static <C> @NotNull FXMLEntity<C> of(@NotNull final Resource fxmlFile) {
        return new FXMLEntity<>(fxmlFile);
    }

    /**
     * Creates a new instance with the specified FXML {@link Resource}
     * and loads the file with {@link FXMLLoader} injecting the specified controller.
     *
     * @param fxmlFile the file to wrap
     * @param controller the controller to inject, can be null
     * @param <C> the controller type, can be null
     * @return a new instance
     */
    @SneakyThrows
    @Contract("_, _ -> new")
    public static <C> @NotNull FXMLEntity<C> of(@NotNull final Resource fxmlFile,
                                                @Nullable final C controller) {
        return new FXMLEntity<>(fxmlFile, controller);
    }

    /**
     * Creates a new instance with the specified FXML resource name to be retrieved
     * from the {@link ResourceManager} and loads the file with {@link FXMLLoader}.
     *
     * @param resourceName the name of the resource file to wrap
     * @param <C> the controller type
     * @return a new instance
     */
    @Contract("_ -> new")
    public static <C> @NotNull FXMLEntity<C> of(@NotNull final String resourceName) {
        return of(resourceName, null);
    }

    /**
     * Creates a new instance with the specified FXML resource name to be retrieved
     * from the {@link ResourceManager} and loads the file with {@link FXMLLoader}
     * injecting the specified controller.
     *
     * @param resourceName the name of the resource file to wrap
     * @param <C> the controller type
     * @param controller the controller to inject, can be null
     * @return a new instance
     */
    @Contract("_, _ -> new")
    public static <C> @NotNull FXMLEntity<C> of(@NotNull final String resourceName,
                                                @Nullable final C controller) {
        final ResourceManager resources = ResourceManager.getInstance();
        Optional<Resource> resource = resources.getResource(resourceName);

        if (resource.isEmpty()) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }

        return new FXMLEntity<>(resource.get(), controller);
    }

    /**
     * Returns the {@link Dialog<R>} cast to the entity's result type.
     *
     * @param <R> The expected result type.
     * @return An Optional containing the typed dialog, or empty if the entity is not a dialog object.
     */
    @SuppressWarnings("unchecked")
    public <R> Optional<Dialog<R>> getDialog() {
        return isDialog() ? Optional.of((Dialog<R>) dialog) : Optional.empty();
    }

    /**
     * Returns the {@link Dialog<R>} cast to the specified result type.
     * <p>
     * This method performs a runtime check of the dialog's result to ensure type safety.
     *
     * @param resultType The class representing the expected result type.
     * @param <R> The expected result type.
     * @return An Optional containing the typed dialog, or empty if the entity is not a dialog object.
     * @throws ClassCastException if the dialog's result is not of the specified type.
     */
    @SuppressWarnings({"unchecked", "unused"})
    public <R> Optional<Dialog<R>> getDialog(final Class<R> resultType) {
        if (!isDialog()) return Optional.empty();

        Object result = dialog.getResult();

        if (result != null && !resultType.isInstance(result)) {
            throw new ClassCastException("Expected dialog result of type " + resultType.getName() +
                    ", but found " + result.getClass().getName());
        }

        return Optional.of((Dialog<R>) dialog);
    }

    /**
     * Returns the entity as a {@link Parent} root object if it is not a {@link Dialog} object.
     * @return the entity as a {@link Parent} root object if it is not a {@link Dialog} object
     */
    public Optional<Parent> getRoot() {
        return !isDialog() ? Optional.of(root) : Optional.empty();
    }

    /**
     * Returns true if the entity is a {@link Dialog} and not a {@link Parent} root object.
     * @return true if the entity is a {@link Dialog} and not a {@link Parent} root object
     */
    public boolean isDialog() {
        return dialog != null;
    }
}
