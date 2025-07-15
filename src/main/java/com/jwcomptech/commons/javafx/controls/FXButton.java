package com.jwcomptech.commons.javafx.controls;

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

import com.jwcomptech.commons.annotations.FeatureComplete;
import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.javafx.JavaFXUtils;
import com.jwcomptech.commons.javafx.controls.locations.BorderPanePosition;
import com.jwcomptech.commons.javafx.controls.locations.NodeLocation;
import com.jwcomptech.commons.javafx.dialogs.DialogResult;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jwcomptech.commons.javafx.JavaFXUtils.runLaterIfNeeded;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

@SuppressWarnings("unused")
@Data
@FeatureComplete(since = "1.0.0-alpha")
public class FXButton {
    private final FXButtonTypeGroup parentButtonMap;
    private final FXButtonType buttonType;
    private final Button button;
    private final Pane parentPane;
    private final FXPaneType parentPaneType;
    private final ButtonBar parentButtonBar;

    private boolean addedToParent = false;

    /**
     * Creates a new FXButton instance wrapping a JavaFX {@link Button}.
     *
     * @param parentButtonMap the parent button map
     * @param buttonType the button type
     * @param button the button instance
     * @param parentPaneType the parent pane type
     * @param parentPane the parent pane instance
     */
    public FXButton(final @NotNull FXButtonTypeGroup parentButtonMap,
                    final @NotNull FXButtonType buttonType,
                    final @NotNull Button button,
                    final @NotNull FXPaneType parentPaneType,
                    final @NotNull Pane parentPane) {
        this.parentButtonMap = parentButtonMap;
        this.buttonType = buttonType;
        this.parentPaneType = parentPaneType;
        this.parentPane = parentPane;
        this.parentButtonBar = lookupButtonBar();

        this.button = button;
    }

    /**
     * Creates a new FXButton instance wrapping a JavaFX {@link Button}.
     *
     * @param parentButtonMap the parent button map
     * @param buttonType the button type
     * @param parentPaneType the parent pane type
     * @param parentPane the parent pane instance
     */
    public FXButton(final @NotNull FXButtonTypeGroup parentButtonMap,
                    final @NotNull FXButtonType buttonType,
                    final @NotNull FXPaneType parentPaneType,
                    final @NotNull Pane parentPane) {
        this.parentButtonMap = parentButtonMap;
        this.buttonType = buttonType;
        this.parentPaneType = parentPaneType;
        this.parentPane = parentPane;
        this.parentButtonBar = lookupButtonBar();

        this.button = new Button(buttonType.getText());
    }

    /**
     * Creates a new FXButton instance wrapping a JavaFX {@link Button}.
     *
     * @param parentButtonMap the parent button map
     * @param buttonType the button type
     * @param graphic the button graphic
     * @param parentPaneType the parent pane type
     * @param parentPane the parent pane instance
     */
    public FXButton(final @NotNull FXButtonTypeGroup parentButtonMap,
                        final @NotNull FXButtonType buttonType,
                        final @Nullable Node graphic,
                        final @NotNull FXPaneType parentPaneType,
                        final @NotNull Pane parentPane) {
        this.parentButtonMap = parentButtonMap;
        this.buttonType = buttonType;
        this.parentPaneType = parentPaneType;
        this.parentPane = parentPane;
        this.parentButtonBar = lookupButtonBar();

        if(graphic == null) this.button = new Button(buttonType.getText());
        else this.button = new Button(buttonType.getText(), graphic);
    }

    /**
     * Adds the button to the parent pane at the specified location.
     *
     * @param location the location to add the button
     * @return the button instance for convenience
     */
    public Button addToParentPane(final NodeLocation<?> location) {
        if(!addedToParent) {
            switch(parentPaneType) {
                case ANCHOR_PANE -> {
                    if(parentPane instanceof AnchorPane) {
                        checkArgumentNotNull(location,
                                "Location must be supplied to " +
                                        "add to AnchorPane!");

                        final var top = (Double) location.get("top");
                        final var left = (Double) location.get("left");
                        final var bottom = (Double) location.get("bottom");
                        final var right = (Double) location.get("right");

                        JavaFXUtils.setAnchors(button, top, right, bottom, left);
                        parentPane.getChildren().add(button);
                    }
                }
                case BORDER_PANE -> {
                    if(parentPane instanceof BorderPane pane) {
                        checkArgumentNotNull(location,
                                "Location must be supplied to " +
                                        "add to BorderPane!");
                        switch ((BorderPanePosition) location.get("position")) {
                            case TOP -> pane.setTop(button);
                            case RIGHT -> pane.setRight(button);
                            case BOTTOM -> pane.setBottom(button);
                            case LEFT -> pane.setLeft(button);
                            case CENTER -> pane.setCenter(button);
                        }
                    }
                }
                case GRID_PANE -> {
                    if(parentPane instanceof GridPane pane) {
                        checkArgumentNotNull(location,
                                "Location must be supplied to " +
                                        "add to GridPane!");
                        var columnIndex = (Integer) location.get("columnIndex");
                        var rowIndex = (Integer) location.get("rowIndex");
                        var colspan = (Integer) location.get("colspan");
                        var rowspan = (Integer) location.get("rowspan");

                        if(columnIndex == null) columnIndex = 0;
                        if(rowIndex == null) rowIndex = 0;
                        if(colspan == null) colspan = 1;
                        if(rowspan == null) rowspan = 1;

                        pane.add(button, columnIndex, rowIndex, colspan, rowspan);
                    }
                }
                default -> parentPane.getChildren().add(button);
            }

            addedToParent = true;
        }

        return button;
    }

    /**
     * Gets the displayed text on the button.
     *
     * @return The current text of the button.
     */
    public String getText() {
        return button.getText();
    }

    /**
     * Updates the text displayed on the button.
     *
     * @param value The new text to display.
     */
    public void setText(final String value) {
        runLaterIfNeeded(() -> button.setText(value));
    }

    /**
     * Sets whether this button is the default (Enter key triggers it).
     *
     * @param value {@code true} to make this the default button.
     */
    public void setDefaultButton(final boolean value) {
        runLaterIfNeeded(() -> button.setDefaultButton(value));
    }

    /**
     * Checks if this is the default button.
     */
    public boolean isDefaultButton() {
        return button.isDefaultButton();
    }

    /**
     * Sets whether this button is the cancel button (Esc key triggers it).
     *
     * @param value {@code true} to make this the cancel button.
     */
    public void setCancelButton(final boolean value) {
        runLaterIfNeeded(() -> button.setCancelButton(value));
    }

    /**
     * Checks if this is the cancel button.
     */
    public boolean isCancelButton() {
        return button.isCancelButton();
    }

    /**
     * Disables or enables the button.
     *
     * @param value {@code true} to disable; {@code false} to enable.
     */
    public void setDisable(final boolean value) {
        runLaterIfNeeded(() -> button.setDisable(value));
    }

    /**
     * @return {@code true} if the button is disabled.
     */
    public boolean isDisable() {
        return button.isDisable();
    }

    /**
     * Alias for {@link #isDisable()}.
     *
     * @return {@code true} if disabled.
     */
    public boolean isDisabled() {
        return isDisable();
    }

    /**
     * @return {@code true} if the button is enabled.
     */
    public boolean isEnabled() {
        return !button.isDisable();
    }

    /**
     * Enables the button.
     */
    public void enable() {
        setDisable(false);
    }

    /**
     * Disables the button.
     */
    public void disable() {
        setDisable(true);
    }

    public void setOnAction(final EventHandler<ActionEvent> value) {
        button.setOnAction(value);
    }

    public EventHandler<ActionEvent> getOnAction() {
        return button.getOnAction();
    }

    /**
     * Finds the {@link ButtonBar} node within the dialog pane.
     *
     * @return The ButtonBar instance.
     * @throws IllegalStateException if no ButtonBar is found.
     */
    @NotNull
    protected ButtonBar lookupButtonBar() {
        return parentPane.lookupAll(".button-bar").stream()
                .filter(ButtonBar.class::isInstance)
                .map(ButtonBar.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Dialog contains no button bar"));
    }

    /**
     * Converts this button to its corresponding {@link DialogResult}.
     *
     * @return The result corresponding to this button.
     */
    public @NotNull DialogResult toDialogResult() {
        return toDialogResult(null);
    }

    /**
     * Converts this button to a {@link DialogResult} and attaches optional data.
     *
     * @param data Optional FXData to merge into the result.
     * @return A new {@link DialogResult}.
     */
    public @NotNull DialogResult toDialogResult(final FXData data) {
        return new DialogResult(buttonType, data);
    }
}
