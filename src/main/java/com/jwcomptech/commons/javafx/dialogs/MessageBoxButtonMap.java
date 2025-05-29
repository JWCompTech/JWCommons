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
import lombok.Builder;
import lombok.Data;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the mapping of MessageBox dialog buttons to their corresponding DialogResult.
 *
 * @see MessageBox
 * @see DialogResult
 * @since 0.0.1
 */
@Data
@Builder
public class MessageBoxButtonMap {
    @Builder.Default
    private MessageBoxButtonType button1 = MessageBoxButtonType.OK;
    private MessageBoxButtonType button2;
    private MessageBoxButtonType button3;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Map<ButtonBar.ButtonData, DialogResult> generateMap() {
        if(button3 != null && button2 != null && button1 != null) {
            return Map.ofEntries(
                    getButton1Data().get(),
                    getButton2Data().get(),
                    getButton3Data().get()
            );
        } else if(button1 != null && button2 != null) {
            return Map.ofEntries(
                    getButton1Data().get(),
                    getButton2Data().get()
            );
        } else if(button1 != null) {
            return Map.ofEntries(
                    getButton1Data().get()
            );
        } else {
            MessageBoxButtonType button = MessageBoxButtonType.OK;

            return Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(
                            button.getButtonType().getButtonData(),
                            button.getDialogResult()
                    )
            );
        }
    }

    public Optional<AbstractMap.SimpleEntry<ButtonBar.ButtonData, DialogResult>> getButton1Data() {
        return getButtonEntry(button1);
    }

    public Optional<AbstractMap.SimpleEntry<ButtonBar.ButtonData, DialogResult>> getButton2Data() {
        return getButtonEntry(button2);
    }

    public Optional<AbstractMap.SimpleEntry<ButtonBar.ButtonData, DialogResult>> getButton3Data() {
        return getButtonEntry(button3);
    }

    private Optional<AbstractMap.SimpleEntry<ButtonBar.ButtonData, DialogResult>> getButtonEntry(
            final MessageBoxButtonType button) {
        if(button == null) return Optional.empty();
        else {
            final var entry = new AbstractMap.SimpleEntry<>(
                    button.getButtonType().getButtonData(),
                    button.getDialogResult()
            );
            return Optional.of(entry);
        }
    }
}
