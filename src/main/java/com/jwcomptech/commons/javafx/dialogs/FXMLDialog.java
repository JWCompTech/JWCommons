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
import com.jwcomptech.commons.javafx.FXMLEntity;
import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.resources.ResourceManager;
import com.jwcomptech.commons.resources.enums.ResourceDir;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.consts.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;

/**
 * Loads a dialog using the specified JavaFX fxml file.
 *
 * @param <R> the type of the value to returned when the dialog is closed
 * @param <C> the controller type
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Getter
@EqualsAndHashCode
@ToString
public class FXMLDialog<R, C> {
    private static final ResourceManager resources = ResourceManager.getInstance();

    /**
     * The JavaFX {@link Dialog} object.
     */
    private final Dialog<R> dialog;

    /**
     * The JavaFX {@link Stage} object.
     */
    private final Stage stage;

    /**
     * The {@link Resource} object that was represents the FXML file.
     */
    private final Resource resource;

    /**
     * The {@link FXMLEntity} object that was represents the loaded FXML file.
     */
    private final FXMLEntity<C> entity;

    /**
     * The FXML file path.
     */
    private final String fxmlPath;

    /**
     * The dialog title, can be null.
     */
    private String title = "";
    /**
     * The icon image to be used in the window decorations
     * and when minimized, or null for no icon.
     */
    private final List<Image> icons = new ArrayList<>();
    /**
     * The owner of the Window.
     */
    private Stage owner;

    /**
     * Creates an instance and sets the specified
     * fxml file path, title, icon, owner and controller.
     *
     * @param fxmlResource the resource representing the fxml file
     * @param title the title
     * @param icons the icon images to be used in the window decorations
     *             and when minimized, or null or empty for no icons
     * @param owner the owner of the Window
     * @param controller the controller to inject, can be null
     * @throws IllegalArgumentException if {@code fxmlResource} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    private FXMLDialog(@NotNull final Resource fxmlResource,
                       @Nullable final C controller,
                       @Nullable final String title,
                       @Nullable final List<Image> icons,
                       @Nullable final Stage owner) {
        checkArgumentNotNull(fxmlResource, cannotBeNull("fxmlResource"));

        this.resource = fxmlResource;

        fxmlPath = resource.getURLString();

        resources.addResource(ResourceDir.FXML, resource);

        Optional<FXMLEntity<C>> entityOpt = resource.asFXML(controller);

        if(entityOpt.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid fxml path, not a FXML file: " + resource.getURLString());
        }

        this.entity = entityOpt.get();

        if(!entity.isDialog() || entity.getDialog().isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid fxml path, not a dialog: " + resource.getURLString());
        }

        //noinspection unchecked
        this.dialog = (Dialog<R>) entity.getDialog().get();

        this.stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setResizable(false);

        this.owner = owner;
        stage.initOwner(owner);

        if(title != null) {
            this.title = title;
            stage.setTitle(title);
        }

        if(icons != null && !icons.isEmpty()) {
            for (final Image icon : icons) {
                if(icon.isError()) throw new IllegalArgumentException("Icon cannot have loading errors.");
                else {
                    this.icons.add(icon);
                    stage.getIcons().add(icon);
                }
            }
        }
    }

    /**
     * Creates an instance and sets the specified
     * fxml file path, title, icon, owner and controller.
     *
     * @param fxmlEntity the {@link FXMLEntity} representing the fxml file
     * @param title the title
     * @param icons the icon images to be used in the window decorations
     *             and when minimized, or null or empty for no icons
     * @param owner the owner of the Window
     * @throws IllegalArgumentException if {@code fxmlResource} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    private FXMLDialog(@NotNull final FXMLEntity<C> fxmlEntity,
                       @Nullable final String title,
                       @Nullable final List<Image> icons,
                       @Nullable final Stage owner) {
        checkArgumentNotNull(fxmlEntity, cannotBeNull("fxmlEntity"));

        this.entity = fxmlEntity;

        this.resource = fxmlEntity.getFxmlFile();

        this.fxmlPath = resource.getURLString();

        final Resource resource = entity.getFxmlFile();

        resources.addResource(ResourceDir.FXML, resource);

        if(!fxmlEntity.isDialog() || fxmlEntity.getDialog().isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid fxml path, not a dialog: " + resource.getURLString());
        }

        //noinspection unchecked
        this.dialog = (Dialog<R>) fxmlEntity.getDialog().get();

        this.stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setResizable(false);

        this.owner = owner;
        stage.initOwner(owner);

        if(title != null) {
            this.title = title;
            stage.setTitle(title);
        }

        if(icons != null && !icons.isEmpty()) {
            for (final Image icon : icons) {
                if(icon.isError()) throw new IllegalArgumentException("Icon cannot have loading errors.");
                else {
                    this.icons.add(icon);
                    stage.getIcons().add(icon);
                }
            }
        }
    }

    /**
     * Creates a new instance and sets the specified title, icon, owner and fxml file path.
     *
     * @param fxmlResource the resource representing the fxml file
     * @param title the title
     * @param icon the icon image to be used in the window decorations and when minimized, or null for no icon
     * @param owner the owner of the Window
     * @throws IllegalArgumentException if {@code fxmlResource} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    @Contract("_, _, _, _ -> new")
    public static <R, C> @NotNull FXMLDialog<R, C> of(@NotNull final Resource fxmlResource,
                                                       @Nullable final String title,
                                                       @Nullable final Image icon,
                                                       @NotNull final Stage owner) {
        return of(fxmlResource, title, Collections.singletonList(icon), owner, null);
    }

    /**
     * Creates a new instance and sets the specified title, icons, owner and fxml file path.
     *
     * @param fxmlResource the resource representing the fxml file
     * @param title the title
     * @param icons the icon images to be used in the window decorations
     *              and when minimized, or null or empty for no icon
     * @param owner the owner of the Window
     * @throws IllegalArgumentException if {@code fxmlResource} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    @Contract("_, _, _, _ -> new")
    public static <R, C> @NotNull FXMLDialog<R, C> of(@NotNull final Resource fxmlResource,
                                                       @Nullable final String title,
                                                       @Nullable final List<Image> icons,
                                                       @NotNull final Stage owner) {
        return new FXMLDialog<>( fxmlResource, null, title, icons, owner);
    }

    /**
     * Creates a new instance and sets the specified title, icon, owner and fxml file path
     * and sets the controller instance to be injected.
     *
     * @param <R> the return type of the dialog
     * @param <C> the controller type
     * @param fxmlResource the resource representing the fxml file
     * @param title the title
     * @param icon the icon image to be used in the window decorations and when minimized, or null for no icon
     * @param owner the owner of the Window
     * @param controller the controller to inject, can be null
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     * @throws IllegalArgumentException if {@code fxmlResource} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    @Contract("_, _, _, _, _ -> new")
    public static <R, C> @NotNull FXMLDialog<R, C> of(@NotNull final Resource fxmlResource,
                                                       @Nullable final String title,
                                                       @Nullable final Image icon,
                                                       @NotNull final Stage owner,
                                                       @Nullable final C controller) {
        return of(fxmlResource, title, Collections.singletonList(icon), owner, controller);
    }

    /**
     * Creates a new instance and sets the specified title, icons, owner and fxml file path
     * and sets the controller instance to be injected.
     *
     * @param <R> the return type of the dialog
     * @param <C> the controller type
     * @param fxmlResource the resource representing the fxml file
     * @param title the title
     * @param icons the icon images to be used in the window decorations
     *              and when minimized, or null or empty for no icon
     * @param owner the owner of the Window
     * @param controller the controller to inject, can be null
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     * @throws IllegalArgumentException if {@code fxmlResource} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    @Contract("_, _, _, _, _ -> new")
    public static <R, C> @NotNull FXMLDialog<R, C> of(@NotNull final Resource fxmlResource,
                                                       @Nullable final String title,
                                                       @Nullable final List<Image> icons,
                                                       @Nullable final Stage owner,
                                                       @Nullable final C controller) {
        return new FXMLDialog<>(fxmlResource, controller, title, icons, owner);
    }

    /**
     * Creates a new instance and sets the specified title, icon, owner and fxml file path.
     *
     * @param <R> the return type of the dialog
     * @param <C> the controller type
     * @param fxmlPath the path of the fxml file
     * @param title the title
     * @param icon the icon image to be used in the window decorations and when minimized, or null for no icon
     * @param owner the owner of the Window
     * @throws IllegalArgumentException if {@code fxmlPath} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    @Contract("_, _, _, _ -> new")
    public static <R, C> @NotNull FXMLDialog<R, C> of(@NotNull final String fxmlPath,
                                                       @Nullable final String title,
                                                       @Nullable final Image icon,
                                                       @Nullable final Stage owner) {
        return of(fxmlPath, title, icon, owner, null);
    }

    /**
     * Creates a new instance and sets the specified title, icons, owner and fxml file path.
     *
     * @param <R> the return type of the dialog
     * @param <C> the controller type
     * @param fxmlPath the path of the fxml file
     * @param title the title
     * @param icons the icon images to be used in the window decorations
     *              and when minimized, or null or empty for no icon
     * @param owner the owner of the Window
     * @throws IllegalArgumentException if {@code fxmlPath} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    @Contract("_, _, _, _ -> new")
    public static <R, C> @NotNull FXMLDialog<R, C> of(@NotNull final String fxmlPath,
                                                       @Nullable final String title,
                                                       @Nullable final List<Image> icons,
                                                       @Nullable final Stage owner) {
        return of(fxmlPath, title, icons, owner, null);
    }

    /**
     * Creates a new instance and sets the specified title, icon, owner and fxml file path.
     *
     * @param <R> the return type of the dialog
     * @param <C> the controller type
     * @param fxmlPath the path of the fxml file
     * @param title the title
     * @param icon the icon image to be used in the window decorations and when minimized, or null for no icon
     * @param owner the owner of the Window
     * @param controller the controller to inject, can be null
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     * @throws IllegalArgumentException if {@code fxmlPath} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    @SuppressWarnings("SameParameterValue")
    @Contract("_, _, _, _, _ -> new")
    public static <R, C> @NotNull FXMLDialog<R, C> of(@NotNull final String fxmlPath,
                                                       @Nullable final String title,
                                                       @Nullable final Image icon,
                                                       @Nullable final Stage owner,
                                                       @Nullable final C controller) {
        return of(fxmlPath, title, Collections.singletonList(icon), owner, controller);
    }

    /**
     * Creates a new instance and sets the specified title, icons, owner and fxml file path.
     *
     * @param <R> the return type of the dialog
     * @param <C> the controller type
     * @param fxmlPath the path of the fxml file
     * @param title the title
     * @param icons the icon images to be used in the window decorations
     *              and when minimized, or null or empty for no icons
     * @param owner the owner of the Window
     * @param controller the controller to inject, can be null
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     * @throws IllegalArgumentException if {@code fxmlPath} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    @SuppressWarnings("SameParameterValue")
    @Contract("_, _, _, _, _ -> new")
    public static <R, C> @NotNull FXMLDialog<R, C> of(@NotNull final String fxmlPath,
                                                       @Nullable final String title,
                                                       @Nullable final List<Image> icons,
                                                       @Nullable final Stage owner,
                                                       @Nullable final C controller) {
        checkArgumentNotNullOrEmpty(fxmlPath, cannotBeNullOrEmpty("fxmlPath"));

        final Optional<String> fxmlFilename = resources.parseResourceURL(fxmlPath).getValue();

        if(fxmlFilename.isEmpty()) {
            throw new IllegalArgumentException("Invalid fxml path, does not start " +
                    "with known resource type: " + fxmlPath);
        }

        FXMLEntity<C> entity = FXMLEntity.of(fxmlPath, controller);

        return new FXMLDialog<>(entity, title, icons, owner);
    }

    /**
     * Creates a new instance and sets the specified title, icons, owner and fxml file path.
     *
     * @param <R> the return type of the dialog
     * @param <C> the controller type
     * @param fxmlEntity the {@code FXMLEntity} that represents the fxml file
     * @param title the title
     * @param icons the icon images to be used in the window decorations
     *              and when minimized, or null or empty for no icons
     * @param owner the owner of the Window
     * @throws IllegalArgumentException if {@code fxmlPath} is null,
     * not a FXML file, or if it doesn't represent a {@link Dialog}
     * or if {@code icon} has loading errors
     */
    @SuppressWarnings("SameParameterValue")
    @Contract("_, _, _, _ -> new")
    public static <R, C> @NotNull FXMLDialog<R, C> of(@NotNull final FXMLEntity<C> fxmlEntity,
                                                       @Nullable final String title,
                                                       @Nullable final List<Image> icons,
                                                       @Nullable final Stage owner) {
        return new FXMLDialog<>(fxmlEntity, title, icons, owner);
    }

    /**
     * Returns the controller associated with the {@code Dialog}.
     *
     * @return the {@code Controller} object
     */
    public C getController() { return entity.getController(); }

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
     * @return An {@link Optional<R>} that contains the result.
     *         Refer to the {@link Dialog} class documentation for more detail.
     * @throws IllegalStateException if this method is called on a thread
     *     other than the JavaFX Application Thread.
     * @throws IllegalStateException if this method is called during
     *     animation or layout processing.
     */
    public Optional<R> showAndWait() { return dialog.showAndWait(); }

    /**
     * Returns the title of the {@link Dialog}.
     *
     * @return the title of the {@link Dialog}
     */
    public String getTitle() {
        return stage.getTitle();
    }

    /**
     * Defines the title of the {@link Dialog}.
     *
     * @param value the title to set, may be empty string.
     * @throws IllegalArgumentException if value is null
     */
    public void setTitle(final String value) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        this.title = value;
        stage.setTitle(value);
    }

    /**
     * Returns the icon image for the {@link Dialog} that is
     * used in the window decorations and when minimized.
     *
     * @return the icon {@link Image}, null if no icon exists
     */
    public @Nullable Image getIcon() {
        return stage.getIcons().isEmpty() ? null : stage.getIcons().getFirst();
    }

    /**
     * Defines an icon {@link Image} for the {@link Dialog} to be
     * used in the window decorations and when minimized.
     *
     * @param value the icon {@link Image}, or null for no icon
     * @throws IllegalArgumentException if the specified {@link Image} has loading errors
     */
    public void addIcon(@Nullable final Image value) {
        if(value != null) {
            if(value.isError()) throw new IllegalArgumentException(
                    "Icon cannot have loading errors.");
            this.icons.add(value);
            stage.getIcons().add(value);
        } else {
            this.icons.clear();
            stage.getIcons().clear();
        }
    }

    /**
     * Defines the icon {@link Image} objects for the {@link Dialog} to be
     * used in the window decorations and when minimized.
     *
     * @param value the icon {@link Image} objects, or null for no icons
     * @throws IllegalArgumentException if the specified {@link Image} has loading errors
     */
    public void setIcons(@Nullable final Image... value) {
        if(value != null) {
            for(Image image : value) {
                if(image != null) {
                    if (image.isError()) throw new IllegalArgumentException(
                            "Icon cannot have loading errors.");
                    this.icons.add(image);
                    stage.getIcons().add(image);
                }
            }
        } else {
            this.icons.clear();
            stage.getIcons().clear();
        }
    }

    /**
     * Returns the owner {@link Window} for this {@link Dialog}, or null for a top-level,
     * unowned {@link Stage}.
     *
     * @return the owner for this dialog.
     */
    public Stage getOwner() {
        return (Stage) stage.getOwner();
    }

    /**
     * Defines the owner {@link Window} for this {@link Dialog}, or null for a top-level,
     * unowned {@link Stage}. This must be done prior to making the stage visible.
     *
     * @param value the owner for this dialog.
     * @throws IllegalStateException if this property is set after the {@link Stage}
     * has ever been made visible or if this {@link Stage} is the primary {@link Stage}.
     */
    public void setOwner(final Stage value) {
        this.owner = value;
        stage.initOwner(value);
    }

    /**
     * Returns whether this {@link Dialog} is kept on top of other windows.
     * <p>
     * If some other {@link Window} is already always-on-top then the
     * relative order between these windows is unspecified (depends on
     * platform).
     * </p>
     * <p>
     * There are differences in behavior between applications if a security
     * manager is present. Applications with permissions are allowed to set
     * "always on top" flag on a {@link Stage}. In applications without the proper
     * permissions, an attempt to set the flag will be ignored and the property
     * value will be restored to "false".
     * </p>
     *
     * @return if dialog is kept on top of other windows
     */
    public boolean isAlwaysOnTop() {
        return stage.isAlwaysOnTop();
    }

    /**
     * Defines whether this {@link Dialog} is kept on top of other windows.
     * <p>
     * If some other {@link Window} is already always-on-top then the
     * relative order between these windows is unspecified (depends on
     * platform).
     * </p>
     * <p>
     * There are differences in behavior between applications if a security
     * manager is present. Applications with permissions are allowed to set
     * "always on top" flag on a {@link Stage}. In applications without the proper
     * permissions, an attempt to set the flag will be ignored and the property
     * value will be restored to "false".
     * </p>
     *
     * @param value the value to set.
     */
    public void setAlwaysOnTop(final boolean value) {
        stage.setAlwaysOnTop(value);
    }

    /**
     * Creates a new instance of the FXMLDialog builder.
     * @param <R> the type of the value to returned when the {@link Dialog} is closed
     * @param <C> the controller type
     * @return a new instance of the FXMLDialog builder
     */
    @Contract(" -> new")
    public static <R, C> @NotNull Builder<R, C> builder() {
        return new Builder<>();
    }

    public static <R, C> @NotNull Buildable<FXMLDialog<R, C>> buildable() {
        return new Builder<>();
    }

    /**
     * A builder class for the FXMLDialog object.
     * @apiNote When defining the FXML file only one of three ways must
     * be set: a String filePath, a {@link Resource} object, or the preferred way
     * a {@link FXMLEntity} object. Supplying more than one of these will throw
     * an {@link IllegalArgumentException}.
     * @param <R> the type of the value to returned when the {@link Dialog} is closed
     * @param <C> the controller type
     */
    @Data
    public static class Builder<R, C> implements Buildable<FXMLDialog<R, C>> {
        /**
         * The path of the fxml file, cannot be null.
         */
        private String fxmlPath = null;
        /**
         * The {@link Resource} object representing the fxml file, cannot be null.
         */
        private Resource fxmlResource = null;
        /**
         * The {@link FXMLEntity} object representing the fxml file, cannot be null.
         */
        private FXMLEntity<C> fxmlEntity = null;
        /**
         * The dialog title, can be null.
         */
        private String title = "";
        /**
         * The icon {@link Image} to be used in the window decorations
         * and when minimized, or null for no icon.
         */
        private List<Image> icons = null;
        /**
         * The owner {@link Window}.
         */
        private Stage owner = null;
        /**
         * The controller to inject, can be null;
         */
        private C controller = null;

        /**
         * Sets the title to be used for the {@link Dialog} object.
         * @param title the title to set
         * @return this instance for method chaining
         * @throws IllegalArgumentException if value is null
         */
        public Builder<R, C> withTitle(final String title) {
            this.title = title;

            return this;
        }

        /**
         * Defines an icon image for the {@link Dialog} to be
         * used in the window decorations and when minimized.
         *
         * @param icon the icon {@link Image}, or null for no icon
         * @return this instance for method chaining
         * @throws IllegalArgumentException if the specified image has loading errors
         */
        public Builder<R, C> withIcon(final Image icon) {
            this.icons = new ArrayList<>();
            this.icons.add(icon);

            return this;
        }

        /**
         * Defines the icon images for the {@link Dialog} to be
         * used in the window decorations and when minimized.
         *
         * @param icons the icon images, or null for no icons
         * @return this instance for method chaining
         * @throws IllegalArgumentException if the specified image has loading errors
         */
        public Builder<R, C> withIcon(final List<Image> icons) {
            this.icons = icons;

            return this;
        }

        /**
         * Defines the owner {@link Window} for this {@link Dialog}, or null for a top-level,
         * unowned {@link Stage}. This must be done prior to making the {@link Stage} visible.
         *
         * @param owner the owner for this dialog.
         * @return this instance for method chaining
         * @throws IllegalStateException if this property is set after the {@link Stage}
         * has ever been made visible or if this {@link Stage} is the primary {@link Stage}.
         */
        public Builder<R, C> withOwner(final Stage owner) {
            this.owner = owner;

            return this;
        }

        /**
         * Defines the controller associated with the {@link Dialog}.
         * @param controller the controller to set, can be null
         * @return this instance for method chaining
         * @apiNote If controller is null then this method will attempt to load the controller from
         * the fxml file via the fx:controller tag. If this tag is already defined and controller
         * is not null then an IllegalStateException will be thrown. One or the other must be used
         * but not both.
         */
        public Builder<R, C> withController(final C controller) {
            if(this.fxmlEntity != null) {
                throw new IllegalArgumentException("Controller cannot be set " +
                        "if FXML entity is already set.");
            }

            this.controller = controller;

            return this;
        }

        /**
         * Sets the FXML path for the FXML file.
         *
         * @param fxmlPath the path to set
         * @throws IllegalArgumentException if fxmlResource or fxmlEntity is already set
         */
        public Builder<R, C> withFxmlPath(final String fxmlPath) {
            if(this.fxmlResource != null || this.fxmlEntity != null) {
                throw new IllegalArgumentException("FXML path cannot be defined" +
                        "if FXML resource or FXML entity is already set.");
            }

            this.fxmlPath = fxmlPath;

            return this;
        }

        /**
         * Sets the FXML resource representing the FXML file.
         *
         * @param fxmlResource the resource to set
         * @throws IllegalArgumentException if fxmlPath or fxmlEntity is already set
         */
        public Builder<R, C> withFxmlResource(final Resource fxmlResource) {
            if(this.fxmlPath != null || this.fxmlEntity != null) {
                throw new IllegalArgumentException("FXML resource cannot be defined" +
                        "if FXML path or FXML entity is already set.");
            }

            this.fxmlResource = fxmlResource;

            return this;
        }

        /**
         * Sets the FXML entity representing the FXML file.
         *
         * @param fxmlEntity the entity to set
         * @throws IllegalArgumentException if fxmlPath or fxmlResource is already set
         */
        public Builder<R, C> withFxmlEntity(final FXMLEntity<C> fxmlEntity) {
            if(this.fxmlPath != null | this.fxmlResource != null) {
                throw new IllegalArgumentException("FXML entity cannot be defined" +
                        "if FXML path or FXML resource is already set.");
            }

            this.fxmlEntity = fxmlEntity;

            return this;
        }

        /**
         * Builds a new instance of FXMLDialogWrapper with the specified builder arguments.
         *
         * @return a new instance
         * @apiNote If controller is null then this method will attempt to load the controller from
         * the fxml file via the fx:controller tag. If this tag is already defined and controller
         * is not null then an IllegalStateException will be thrown. One or the other must be used
         * but not both.
         * @throws IllegalArgumentException if one of {@code fxmlPath},
         * {@code fxmlResource}, or {@code fxmlEntity} are not defined
         * or if the FXML file is null, not a FXML file,
         * or if it doesn't represent a {@link Dialog}
         * or if {@code icon} has loading errors
         */
        public FXMLDialog<R, C> build() {
            if(fxmlPath == null && fxmlResource == null && fxmlEntity == null) {
                throw new IllegalArgumentException(
                        "FXML path or FXML resource or FXML entity must be set.");
            }

            if(fxmlPath != null) {
                return FXMLDialog.of(fxmlPath, title, icons, owner, controller);
            } else if(fxmlResource != null) {
                return FXMLDialog.of(fxmlResource, title, icons, owner, controller);
            } else return FXMLDialog.of(fxmlEntity, title, icons, owner);
        }
    }
}
