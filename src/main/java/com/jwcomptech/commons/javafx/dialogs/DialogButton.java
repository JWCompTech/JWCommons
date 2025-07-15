package com.jwcomptech.commons.javafx.dialogs;

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
import com.jwcomptech.commons.javafx.controls.FXButton;
import com.jwcomptech.commons.javafx.controls.FXButtonType;
import com.jwcomptech.commons.javafx.controls.FXButtonTypeGroup;
import com.jwcomptech.commons.javafx.controls.FXPaneType;
import javafx.scene.control.*;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a button in a {@link FXDialog}, encapsulating its type, behavior,
 * and the JavaFX {@link Button} node within a {@link ButtonBar}.
 *
 * @since 1.0.0-alpha
 */

@Value
@EqualsAndHashCode(callSuper = true)
@FeatureComplete(since = "1.0.0-alpha")
@SuppressWarnings("unused")
public class DialogButton extends FXButton {

    FXDialog parentDialog;
    ButtonBar parentButtonBar;

    /**
     * Constructs a new {@code DialogButton} for the given dialog and button type.
     *
     * @param parentButtonMap The button group this button belongs to.
     * @param buttonType      The logical button type.
     * @param parentPaneType  The pane type.
     * @param parentDialog    The dialog containing the button.
     */
    public DialogButton(final @NotNull FXButtonTypeGroup parentButtonMap,
                        final @NotNull FXButtonType buttonType,
                        final @NotNull FXPaneType parentPaneType,
                        final @NotNull FXDialog parentDialog) {
        super(parentButtonMap,
                buttonType,
                (Button) parentDialog.getDialogPane().lookupButton(buttonType.getButtonType()),
                parentPaneType,
                parentDialog.getDialogPane());
        this.parentDialog = parentDialog;
        this.parentButtonBar = lookupButtonBar();
    }
}
