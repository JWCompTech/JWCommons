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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jwcomptech.commons.javafx.controls.FXButtonType;
import com.jwcomptech.commons.javafx.controls.FXButtonTypeGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
public class MessageBoxTests {
//TODO: fixme

//    private Alert mockAlert;
//    private FXButtonTypeGroup mockButtons;
//
//    @BeforeAll
//    public static void initFX() {
//        // Ensures JavaFX is initialized (especially useful in CI/headless)
//        try {
//            javafx.application.Platform.startup(() -> {});
//        } catch (IllegalStateException ignored) {
//            // FX runtime already started
//        }
//    }
//
//    @BeforeEach
//    void setup() {
//        mockAlert = mock(Alert.class);
//        mockButtons = mock(FXButtonTypeGroup.class);
//    }
//
//    @Test
//    void testHandlerIsCalledForOkButton() {
//        when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));
//        when(mockButtons.toDialogResult(ButtonType.OK)).thenReturn(DialogResult.OK);
//
//        MessageBox box = MessageBox.builder()
//                .withButtons(mockButtons)
//                .build()
//                .setAlert(mockAlert);
//
//        AtomicBoolean called = new AtomicBoolean(false);
//        box.setOnResult(FXButtonType.OK, () -> called.set(true));
//
//        box.show();
//
//        assertTrue(called.get(), "OK handler should be called");
//    }
//
//    @Test
//    void testHandlerNotCalledForUnrelatedButton() {
//        when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.CANCEL));
//        when(mockButtons.toDialogResult(ButtonType.CANCEL)).thenReturn(DialogResult.CANCEL);
//
//        MessageBox box = MessageBox.builder()
//                .withButtons(mockButtons)
//                .build()
//                .setAlert(mockAlert);
//
//        AtomicBoolean called = new AtomicBoolean(false);
//        box.setOnResult(FXButtonType.OK, () -> called.set(true));
//
//        box.show();
//
//        assertFalse(called.get(), "Handler for OK should not be called if CANCEL was pressed");
//    }
//
//    @Test
//    void testNoneResultHandlerIsCalledWhenDialogCancelled() {
//        when(mockAlert.showAndWait()).thenReturn(Optional.empty());
//
//        MessageBox box = MessageBox.builder()
//                .withButtons(mockButtons)
//                .build()
//                .setAlert(mockAlert);
//
//        AtomicBoolean noneCalled = new AtomicBoolean(false);
//        box.setOnResult(FXButtonType.NONE, () -> noneCalled.set(true));
//
//        box.show();
//
//        assertTrue(noneCalled.get(), "NONE handler should be called on dialog cancel (empty result)");
//    }
//
//    @Test
//    void testShowDoesNotCrashWhenNoHandlerRegistered() {
//        when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));
//        when(mockButtons.toDialogResult(ButtonType.OK)).thenReturn(DialogResult.OK);
//
//        MessageBox box = MessageBox.builder()
//                .withButtons(mockButtons)
//                .build()
//                .setAlert(mockAlert);
//
//        assertDoesNotThrow(() -> box.show(), "MessageBox.show() should not crash even if no handler is set");
//    }
//
//    @Test
//    void testMultipleHandlersOnlyCorrectOneIsCalled() {
//        when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));
//        when(mockButtons.toDialogResult(ButtonType.OK)).thenReturn(DialogResult.OK);
//
//        MessageBox box = MessageBox.builder()
//                .withButtons(mockButtons)
//                .build()
//                .setAlert(mockAlert);
//
//        AtomicBoolean okCalled = new AtomicBoolean(false);
//        AtomicBoolean cancelCalled = new AtomicBoolean(false);
//
//        box.setOnResult(FXButtonType.OK, () -> okCalled.set(true));
//        box.setOnResult(FXButtonType.CANCEL, () -> cancelCalled.set(true));
//
//        box.show();
//
//        assertTrue(okCalled.get(), "OK handler should be called");
//        assertFalse(cancelCalled.get(), "CANCEL handler should NOT be called");
//    }
}

