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

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Used to specify which buttons should be shown to users in the message box.
 *
 * @since 0.0.1
 */
@AllArgsConstructor
@Getter
@ToString
public enum MessageBoxButtonType {
    /**
     * A pre-defined {@code ButtonType} that displays "Apply" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.APPLY}.
     */
    APPLY(ButtonType.APPLY),

    /**
     * A pre-defined {@code ButtonType} that displays "OK" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.OK_DONE}.
     */
    OK(ButtonType.OK),

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
     * A pre-defined {@code ButtonType} that displays "Yes" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.YES}.
     */
    YES(ButtonType.YES),

    /**
     * A pre-defined {@code ButtonType} that displays "No" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.NO}.
     */
    NO(ButtonType.NO),

    /**
     * A pre-defined {@code ButtonType} that displays "Finish" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.FINISH}.
     */
    FINISH(ButtonType.FINISH),

    /**
     * A pre-defined {@code ButtonType} that displays "Next" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.NEXT_FORWARD}.
     */
    NEXT(ButtonType.NEXT),

    /**
     * A pre-defined {@code ButtonType} that displays "Previous" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.BACK_PREVIOUS}.
     */
    PREVIOUS(ButtonType.PREVIOUS),

    /**
     * A custom {@code ButtonType} that displays "Abort" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.CANCEL_CLOSE}.
     */
    ABORT(new ButtonType("Abort", ButtonBar.ButtonData.CANCEL_CLOSE)),

    /**
     * A custom {@code ButtonType} that displays "Retry" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.BACK_PREVIOUS}.
     */
    RETRY(new ButtonType("Retry", ButtonBar.ButtonData.BACK_PREVIOUS)),

    /**
     * A custom {@code ButtonType} that displays "Try Again" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.BACK_PREVIOUS}.
     */
    TRYAGAIN(new ButtonType("Try Again", ButtonBar.ButtonData.BACK_PREVIOUS)),

    /**
     * A custom {@code ButtonType} that displays "Ignore" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.NEXT_FORWARD}.
     */
    IGNORE(new ButtonType("Ignore", ButtonBar.ButtonData.NEXT_FORWARD)),

    /**
     * A custom {@code ButtonType} that displays "Continue" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.NEXT_FORWARD}.
     */
    CONTINUE(new ButtonType("Continue", ButtonBar.ButtonData.NEXT_FORWARD)),

    /**
     * A custom {@code ButtonType} that displays "Submit" and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.APPLY}.
     */
    SUBMIT(new ButtonType("Submit", ButtonBar.ButtonData.APPLY)),

    /**
     * A custom {@code ButtonType} that displays no text and has a
     * {@code ButtonBar.ButtonData} of {@code ButtonBar.ButtonData.OTHER}.
     */
    NONE(new ButtonType(""));

    private final ButtonType buttonType;

    /**
     * Returns the matching DialogResult that should be returned if the
     * corresponding button is pressed.
     * @return the matching DialogResult
     */
    public final DialogResult getDialogResult() {
        return switch (this) {
            case APPLY -> DialogResult.APPLY;
            case OK -> DialogResult.OK;
            case CANCEL -> DialogResult.CANCEL;
            case CLOSE -> DialogResult.CLOSE;
            case YES -> DialogResult.YES;
            case NO -> DialogResult.NO;
            case FINISH -> DialogResult.FINISH;
            case NEXT -> DialogResult.NEXT;
            case PREVIOUS -> DialogResult.PREVIOUS;
            case ABORT -> DialogResult.ABORT;
            case RETRY -> DialogResult.RETRY;
            case TRYAGAIN -> DialogResult.TRYAGAIN;
            case IGNORE -> DialogResult.IGNORE;
            case CONTINUE -> DialogResult.CONTINUE;
            case SUBMIT -> DialogResult.SUBMIT;
            case NONE -> DialogResult.NONE;
        };
    }
}
