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

import com.jwcomptech.commons.javafx.controls.FXButtonType;
import com.jwcomptech.commons.javafx.controls.FXButtonTypeGroup;
import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.validators.Condition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableObjectValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.EnumMap;
import java.util.Optional;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.javafx.JavaFXUtils.*;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * Displays message box with specified options.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, setterPrefix = "with")
public final class MessageBox {
    @Builder.Default
    private String text = "";
    @Builder.Default
    private String title = "";
    @Builder.Default
    private String headerText = "";
    @Builder.Default
    private FXButtonTypeGroup buttons = FXButtonTypeGroup.OK;
    @Builder.Default
    private MessageBoxIcon icon = MessageBoxIcon.NONE;
    @Builder.Default
    private MessageBoxDefaultButton defaultButton = MessageBoxDefaultButton.Button1;
    @Builder.Default
    private Resource stylesheet = null;

    //Must be ObjectProperty to be excluded from builder
    @Getter(AccessLevel.PRIVATE)
    private final ObjectProperty<Alert> alert = new SimpleObjectProperty<>();

    private final EnumMap<FXButtonType, Runnable> resultHandlers =
            new EnumMap<>(FXButtonType.class);

    @VisibleForTesting
    MessageBox setAlert(Alert alert) {
        this.alert.set(alert);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MessageBox setOnResult(FXButtonType type, Runnable action) {
        checkArgumentNotNull(type, cannotBeNull("type"));

        if(action != null) resultHandlers.put(type, action);

        return this;
    }


    /**
     * Shows the message box and does not wait for a result.
     */
    public void show() {
        if(buttons == FXButtonTypeGroup.OK && icon == MessageBoxIcon.CONFIRMATION) {
            buttons = FXButtonTypeGroup.OKCancel;
        }

        buildDialog();
        runLaterIfNeeded(() -> alert.get().show());
    }

    /**
     * Shows the message box and waits for the result and returns it as a DialogResult object.
     * @return the result of the message box
     */
    public DialogResult showAndWait() {
        if(buttons == FXButtonTypeGroup.OK && icon == MessageBoxIcon.CONFIRMATION) {
            buttons = FXButtonTypeGroup.OKCancel;
        }

        buildDialog();

        //Must be ObjectProperty type to work with lambda
        final var dialogResult = new SimpleObjectProperty<DialogResult>();
        //Must be ObjectProperty type to work with lambda
        final WritableObjectValue<Optional<ButtonType>> result = new SimpleObjectProperty<>();

        final Runnable runnable = () -> {
            result.set(alert.get().showAndWait());

            result.get().ifPresentOrElse(
                    buttonType -> dialogResult.set(buttons.toDialogResult(buttonType)),
                    () -> dialogResult.set(DialogResult.NONE)
            );

            Condition.of(() -> dialogResult.get() != null).waitTillEvalTrue();

            Runnable handler = resultHandlers.get(dialogResult.get().getButtonType());
            if (handler != null) {
                handler.run();
            }
        };

        runLaterIfNeeded(runnable);

        return dialogResult.get();
    }

    private void buildDialog() {
        final var defaultButton_ = defaultButton == null ? MessageBoxDefaultButton.Button1 : defaultButton;

        final var button1 = buttons.getButton1IfExists();
        final var button2 = buttons.getButton2IfExists();
        final var button3 = buttons.getButton3IfExists();

        if(button1.isPresent() && button2.isPresent() && button3.isPresent()) {
            final var button1_ = button1.get();
            final var button2_ = button2.get();
            final var button3_ = button3.get();

            if(alert.get() == null) alert.set(new Alert(icon.getValue()));

            alert.get().getButtonTypes().setAll(
                    button1_.getButtonType(),
                    button2_.getButtonType(),
                    button3_.getButtonType()
            );

            switch (defaultButton_) {
                case Button1 -> setAlertDefaultButtons(button1_);
                case Button2 -> setAlertDefaultButtons(button2_);
                case Button3 -> setAlertDefaultButtons(button3_);
            }
        } else if(button1.isPresent() && button2.isPresent()) {
            final var button1_ = button1.get();
            final var button2_ = button2.get();

            if(alert.get() == null) runLaterIfNeeded(() -> alert.set(new Alert(icon.getValue())));

            alert.get().getButtonTypes().setAll(
                    button1_.getButtonType(),
                    button2_.getButtonType()
            );

            switch (defaultButton_) {
                case Button1, Button2 -> setAlertDefaultButtons(button1_);
                case Button3 -> setAlertDefaultButtons(button2_);
            }
        } else if(button1.isPresent()) {
            final var button1_ = button1.get();

            if(alert.get() == null) alert.set(new Alert(icon.getValue()));

            alert.get().getButtonTypes().setAll(
                    button1_.getButtonType()
            );

            switch (defaultButton_) {
                case Button1, Button2, Button3 -> setAlertDefaultButtons(button1_);
            }
        } else throw new IllegalStateException(cannotBeNull("Button1"));

        if (stylesheet != null) alert.get().getDialogPane().getStylesheets().add(stylesheet.getURLString());

        alert.get().setTitle(title);
        alert.get().setHeaderText(headerText);
        alert.get().setContentText(text);

        Window.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst().ifPresent(owner -> alert.get().initOwner(owner));
    }

    private void setAlertDefaultButtons(final FXButtonType type) {
        final var pane = alert.get().getDialogPane();
        for (final var t : alert.get().getButtonTypes())
            ((Button) pane.lookupButton(t)).setDefaultButton(t.equals(type.getButtonType()));
    }

    //region Overloads

    /**
     * Displays message box with specified text, title, header text, buttons, icon and default button.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param headerText The text to display in the header section of the message box
     * @param buttons One of the MessageBoxButtons values that specifies which buttons to display in the message box
     * @param icon One of the MessageBoxIcons values that specifies which icon to display in the message box
     * @param defaultButton One of the MessageBoxDefaultButton values that specifies the default button
     *                      for the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final String headerText,
                                    final FXButtonTypeGroup buttons, final MessageBoxIcon icon,
                                    final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withHeaderText(headerText)
                .withButtons(buttons)
                .withIcon(icon)
                .withDefaultButton(defaultButton)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title, header text, buttons and icon.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param headerText The text to display in the header section of the message box
     * @param buttons One of the MessageBoxButtons values that specifies which buttons to display in the message box
     * @param icon One of the MessageBoxIcons values that specifies which icon to display in the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final String headerText,
                                    final FXButtonTypeGroup buttons, final MessageBoxIcon icon) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withHeaderText(headerText)
                .withButtons(buttons)
                .withIcon(icon)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title, header text, buttons and default button.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param headerText The text to display in the header section of the message box
     * @param buttons One of the MessageBoxButtons values that specifies which buttons to display in the message box
     * @param defaultButton One of the MessageBoxDefaultButton values that specifies the default button
     *                      for the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final String headerText,
                                    final FXButtonTypeGroup buttons, final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withHeaderText(headerText)
                .withButtons(buttons)
                .withDefaultButton(defaultButton)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title, header text, icon and default button.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param headerText The text to display in the header section of the message box
     * @param icon One of the MessageBoxIcons values that specifies which icon to display in the message box
     * @param defaultButton One of the MessageBoxDefaultButton values that specifies the default button
     *                      for the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final String headerText,
                                          final @NotNull MessageBoxIcon icon, final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withHeaderText(headerText)
                .withIcon(icon)
                .withDefaultButton(defaultButton)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title, buttons, icon and default button.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param buttons One of the MessageBoxButtons values that specifies which buttons to display in the message box
     * @param icon One of the MessageBoxIcons values that specifies which icon to display in the message box
     * @param defaultButton One of the MessageBoxDefaultButton values that specifies the default button
     *                      for the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final FXButtonTypeGroup buttons,
                                          final MessageBoxIcon icon, final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withButtons(buttons)
                .withIcon(icon)
                .withDefaultButton(defaultButton)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title, buttons and default button.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param buttons One of the MessageBoxButtons values that specifies which buttons to display in the message box
     * @param defaultButton One of the MessageBoxDefaultButton values that specifies the default button
     *                      for the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final FXButtonTypeGroup buttons,
                                          final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withButtons(buttons)
                .withDefaultButton(defaultButton)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title, icon and default button.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param icon One of the MessageBoxIcons values that specifies which icon to display in the message box
     * @param defaultButton One of the MessageBoxDefaultButton values that specifies the default button
     *                      for the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final @NotNull MessageBoxIcon icon,
                                          final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withIcon(icon)
                .withDefaultButton(defaultButton)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title, header text and buttons.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param headerText The text to display in the header section of the message box
     * @param buttons One of the MessageBoxButtons values that specifies which buttons to display in the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title,
                                          final String headerText, final FXButtonTypeGroup buttons) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withHeaderText(headerText)
                .withButtons(buttons)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title, header text and icon.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param headerText The text to display in the header section of the message box
     * @param icon One of the MessageBoxIcons values that specifies which icon to display in the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title,
                                          final String headerText, final @NotNull MessageBoxIcon icon) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withHeaderText(headerText)
                .withIcon(icon)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title and buttons.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param buttons One of the MessageBoxButtons values that specifies which buttons to display in the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final FXButtonTypeGroup buttons) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withButtons(buttons)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title and icon.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param icon One of the MessageBoxIcons values that specifies which icon to display in the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final MessageBoxIcon icon) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withIcon(icon)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title and header text.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param headerText The text to display in the header section of the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final String headerText) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .withHeaderText(headerText)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text and title.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title) {
        return MessageBox.builder()
                .withText(text)
                .withTitle(title)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text.
     * @param text The text to display in the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text) {
        return MessageBox.builder()
                .withText(text)
                .build().showAndWait();
    }

    //endregion Overloads
}
