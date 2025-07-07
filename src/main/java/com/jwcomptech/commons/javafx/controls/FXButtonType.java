package com.jwcomptech.commons.javafx.controls;

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

import com.jwcomptech.commons.annotations.FeatureComplete;
import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.javafx.dialogs.DialogResult;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a standardized set of button types that can be shown in dialogs.
 * <p>
 * Each enum instance wraps a JavaFX {@link ButtonType} and provides convenient
 * conversion and parsing utilities for dialog interaction.
 *
 * @since 1.0.0-alpha
 */
@AllArgsConstructor
@Getter
@ToString
@FeatureComplete(since = "1.0.0-alpha")
public enum FXButtonType {
    /**
     * A custom {@code ButtonType} that displays "Abort" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.CANCEL_CLOSE}.
     */
    ABORT(new ButtonType("Abort", ButtonBar.ButtonData.CANCEL_CLOSE)),

    /**
     * A pre-defined {@code ButtonType} that displays "Apply" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.APPLY}.
     */
    APPLY(ButtonType.APPLY),

    /**
     * A custom {@code ButtonType} that displays "Back" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.BACK_PREVIOUS}.
     */
    BACK(new ButtonType("Back", ButtonBar.ButtonData.BACK_PREVIOUS)),

    /**
     * A pre-defined {@code ButtonType} that displays "Cancel" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.CANCEL_CLOSE}.
     */
    CANCEL(ButtonType.CANCEL),

    /**
     * A pre-defined {@code ButtonType} that displays "Close" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.CANCEL_CLOSE}.
     */
    CLOSE(ButtonType.CLOSE),

    /**
     * A custom {@code ButtonType} that displays "Continue" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.NEXT_FORWARD}.
     */
    CONTINUE(new ButtonType("Continue", ButtonBar.ButtonData.NEXT_FORWARD)),

    /**
     * A pre-defined {@code ButtonType} that displays "Finish" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.FINISH}.
     */
    FINISH(ButtonType.FINISH),

    /**
     * A custom {@code ButtonType} that displays "Ignore" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.NEXT_FORWARD}.
     */
    IGNORE(new ButtonType("Ignore", ButtonBar.ButtonData.NEXT_FORWARD)),

    /**
     * A custom {@code ButtonType} that displays "Install" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.APPLY}.
     */
    INSTALL(new ButtonType("Install", ButtonBar.ButtonData.APPLY)),

    /**
     * A custom {@code ButtonType} that displays "Login" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.APPLY}.
     */
    LOGIN(new ButtonType("Login", ButtonBar.ButtonData.APPLY)),

    /**
     * A pre-defined {@code ButtonType} that displays "Next" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.NEXT_FORWARD}.
     */
    NEXT(ButtonType.NEXT),

    /**
     * A pre-defined {@code ButtonType} that displays "No" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.NO}.
     */
    NO(ButtonType.NO),

    /**
     * A custom {@code ButtonType} that displays no text and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.OTHER}.
     */
    NONE(new ButtonType("", ButtonBar.ButtonData.OTHER)),

    /**
     * A pre-defined {@code ButtonType} that displays "OK" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.OK_DONE}.
     */
    OK(ButtonType.OK),

    /**
     * A pre-defined {@code ButtonType} that displays "Previous" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.BACK_PREVIOUS}.
     */
    PREVIOUS(ButtonType.PREVIOUS),

    /**
     * A custom {@code ButtonType} that displays "Retry" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.BACK_PREVIOUS}.
     */
    RETRY(new ButtonType("Retry", ButtonBar.ButtonData.BACK_PREVIOUS)),

    /**
     * A custom {@code ButtonType} that displays "Submit" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.APPLY}.
     */
    SUBMIT(new ButtonType("Submit", ButtonBar.ButtonData.APPLY)),

    /**
     * A custom {@code ButtonType} that displays "Try Again" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.BACK_PREVIOUS}.
     */
    TRYAGAIN(new ButtonType("Try Again", ButtonBar.ButtonData.BACK_PREVIOUS)),

    /**
     * A pre-defined {@code ButtonType} that displays "Yes" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.YES}.
     */
    YES(ButtonType.YES);

    private final ButtonType buttonType;

    public String getText() {
        return buttonType.getText();
    }

    /**
     * Attempts to find a {@code FXButtonType} matching the provided {@link ButtonType}.
     * This compares the semantic values (label and button data) for equality.
     *
     * @param buttonType The {@link ButtonType} to match.
     * @return An {@link Optional} of the corresponding {@code FXButtonType}, or empty if not found.
     */
    public static Optional<FXButtonType> parse(final ButtonType buttonType) {
        for (final FXButtonType FXButtonType : FXButtonType.values()) {
            if (FXButtonType.buttonType.equals(buttonType)) {
                return Optional.of(FXButtonType);
            }
        }

        return Optional.empty();
    }

    /**
     * Creates a {@link DialogResult} associated with this {@code FXButtonType}.
     *
     * @return A new {@link DialogResult} with no additional data.
     */
    @Contract(value = " -> new", pure = true)
    public @NotNull DialogResult toDialogResult() {
        return new DialogResult(this);
    }

    /**
     * Creates a {@link DialogResult} associated with this {@code FXButtonType},
     * embedding the specified {@link FXData}.
     *
     * @param data The data to attach to the dialog result.
     * @return A new {@link DialogResult} containing the given data.
     */
    public @NotNull DialogResult toDialogResult(final FXData data) {
        return new DialogResult(this, data);
    }
}
