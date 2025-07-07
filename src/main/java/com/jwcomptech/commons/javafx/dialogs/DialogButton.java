package com.jwcomptech.commons.javafx.dialogs;

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
