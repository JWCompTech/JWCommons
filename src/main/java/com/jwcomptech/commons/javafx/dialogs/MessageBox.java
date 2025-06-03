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
import com.jwcomptech.commons.Literals;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableObjectValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

import static com.jwcomptech.commons.javafx.JavaFXUtils.*;

/**
 * Displays message box with specified options.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Data
@Builder(toBuilder = true)
public final class MessageBox {
    @Builder.Default
    private String text = "";
    @Builder.Default
    private String title = "";
    @Builder.Default
    private String headerText = "";
    @Builder.Default
    private MessageBoxButtons buttons = MessageBoxButtons.OK;
    @Builder.Default
    private MessageBoxIcon icon = MessageBoxIcon.NONE;
    @Builder.Default
    private MessageBoxDefaultButton defaultButton = MessageBoxDefaultButton.Button1;

    //Must be ObjectProperty to be excluded from builder
    @Getter(AccessLevel.PRIVATE)
    private final ObjectProperty<Alert> alert = new SimpleObjectProperty<>();

    /**
     * Shows the message box and does not wait for a result.
     */
    public void show() {
        if(buttons == MessageBoxButtons.OK && icon == MessageBoxIcon.CONFIRMATION) {
            buttons = MessageBoxButtons.OKCancel;
        }

        buildDialog();
        runLaterCheck(() -> alert.get().show());
    }

    /**
     * Shows the message box and waits for the result and returns it as a DialogResult object.
     * @return the result of the message box
     */
    public DialogResult showAndWait() {
        if(buttons == MessageBoxButtons.OK && icon == MessageBoxIcon.CONFIRMATION) {
            buttons = MessageBoxButtons.OKCancel;
        }

        buildDialog();

        //Must be ObjectProperty type to work with lambda
        final var dialogResult = new SimpleObjectProperty<DialogResult>();
        //Must be ObjectProperty type to work with lambda
        final WritableObjectValue<Optional<ButtonType>> result = new SimpleObjectProperty<>();

        final Runnable runnable = () -> {
            result.set(alert.get().showAndWait());

            result.get().ifPresentOrElse(
                    buttonType -> {
                        for(final var type : buildResultMap().entrySet()) {
                            if(type != null) {
                                if(buttonType.getButtonData() == type.getKey()) {
                                    dialogResult.set(type.getValue());
                                    break;
                                }
                            }
                        }

                        //This shouldn't ever happen if things are mapped correctly
                        if(dialogResult.get() == null) dialogResult.set(DialogResult.NONE);
                    },
                    () -> dialogResult.set(DialogResult.NONE)
            );

            Condition.of(() -> dialogResult.get() != null).waitTillTrue();
        };

        runLaterCheck(runnable);
        return dialogResult.get();
    }

    private void buildDialog() {
        final var defaultButton_ = defaultButton == null ? MessageBoxDefaultButton.Button1 : defaultButton;

        final var button1 = buttons.getButton1();
        final var button2 = buttons.getButton2();
        final var button3 = buttons.getButton3();

        if(button1.isPresent() && button2.isPresent() && button3.isPresent()) {
            alert.set(new Alert(icon.getValue(), text,
                    button1.get().getButtonType(), button2.get().getButtonType(), button3.get().getButtonType()));

            switch (defaultButton_) {
                case Button1 -> setAlertDefaultButtons(button1.get());
                case Button2 -> setAlertDefaultButtons(button2.get());
                case Button3 -> setAlertDefaultButtons(button3.get());
            }
        } else if(button1.isPresent() && button2.isPresent()) {
            alert.set(new Alert(icon.getValue(), text,
                    button1.get().getButtonType(), button2.get().getButtonType()));

            switch (defaultButton_) {
                case Button1, Button2 -> setAlertDefaultButtons(button1.get());
                case Button3 -> setAlertDefaultButtons(button2.get());
            }
        } else if(button1.isPresent()) {
            alert.set(new Alert(icon.getValue(), text, button1.get().getButtonType()));

            switch (defaultButton_) {
                case Button1, Button2, Button3 -> setAlertDefaultButtons(button1.get());
            }
        } else throw new IllegalStateException(Literals.cannotBeNull("Button1"));

        alert.get().setHeaderText(headerText);
        alert.get().setTitle(title);
    }

    private Map<ButtonBar.ButtonData, DialogResult> buildResultMap() {
        final var defaultButton_ = defaultButton == null ? MessageBoxDefaultButton.Button1 : defaultButton;

        final var button1 = buttons.getButton1();
        final var button2 = buttons.getButton2();
        final var button3 = buttons.getButton3();

        final Map<ButtonBar.ButtonData, DialogResult> resultMap;

        if(button1.isPresent() && button2.isPresent() && button3.isPresent()) {
            resultMap = MessageBoxButtonMap.builder()
                    .button1(button1.get())
                    .button2(button2.get())
                    .button3(button3.get())
                    .build().generateMap();
        } else if(button1.isPresent() && button2.isPresent()) {
            resultMap = MessageBoxButtonMap.builder()
                    .button1(button1.get())
                    .button2(button2.get())
                    .build().generateMap();
        } else if(button1.isPresent()) {
            resultMap = MessageBoxButtonMap.builder()
                    .button1(button1.get())
                    .build().generateMap();
        } else throw new IllegalStateException(Literals.cannotBeNull("Button1"));

        return resultMap;
    }

    private void setAlertDefaultButtons(final MessageBoxButtonType type) {
        final var pane = alert.get().getDialogPane();
        for (final var t : alert.get().getButtonTypes())
            ((Button) pane.lookupButton(t)).setDefaultButton(t.equals(type.getButtonType()));
    }

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
                                    final MessageBoxButtons buttons, final MessageBoxIcon icon,
                                    final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .text(text)
                .title(title)
                .headerText(headerText)
                .buttons(buttons)
                .icon(icon)
                .defaultButton(defaultButton)
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
                                    final MessageBoxButtons buttons, final MessageBoxIcon icon) {
        return MessageBox.builder()
                .text(text)
                .title(title)
                .headerText(headerText)
                .buttons(buttons)
                .icon(icon)
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
                                    final MessageBoxButtons buttons, final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .text(text)
                .title(title)
                .headerText(headerText)
                .buttons(buttons)
                .defaultButton(defaultButton)
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
                .text(text)
                .title(title)
                .headerText(headerText)
                .icon(icon)
                .defaultButton(defaultButton)
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
    public static DialogResult show(final String text, final String title, final MessageBoxButtons buttons,
                                    final MessageBoxIcon icon, final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .text(text)
                .title(title)
                .buttons(buttons)
                .icon(icon)
                .defaultButton(defaultButton)
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
    public static DialogResult show(final String text, final String title, final MessageBoxButtons buttons,
                                    final MessageBoxDefaultButton defaultButton) {
        return MessageBox.builder()
                .text(text)
                .title(title)
                .buttons(buttons)
                .defaultButton(defaultButton)
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
                .text(text)
                .title(title)
                .icon(icon)
                .defaultButton(defaultButton)
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
                                    final String headerText, final MessageBoxButtons buttons) {
        return MessageBox.builder()
                .text(text)
                .title(title)
                .headerText(headerText)
                .buttons(buttons)
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
                .text(text)
                .title(title)
                .headerText(headerText)
                .icon(icon)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text, title and buttons.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @param buttons One of the MessageBoxButtons values that specifies which buttons to display in the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title, final MessageBoxButtons buttons) {
        return MessageBox.builder()
                .text(text)
                .title(title)
                .buttons(buttons)
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
                .text(text)
                .title(title)
                .icon(icon)
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
                .text(text)
                .title(title)
                .headerText(headerText)
                .build().showAndWait();
    }

    /**
     * Displays message box with specified text and title.
     * @param text The text to display in the message box
     * @param title The text to display in the title bar of the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text, final String title) {
        return MessageBox.builder().text(text).title(title).build().showAndWait();
    }

    /**
     * Displays message box with specified text.
     * @param text The text to display in the message box
     * @return DialogResult representing the return value of the message box
     */
    public static DialogResult show(final String text) {
        return MessageBox.builder().text(text).build().showAndWait();
    }
}
