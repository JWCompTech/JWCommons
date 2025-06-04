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

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Used to specify what buttons are displayed on a message box.
 *
 * @since 0.0.1
 */
@AllArgsConstructor
@ToString
public enum MessageBoxButtons {
    AbortRetryIgnore(MessageBoxButtonType.ABORT,
            MessageBoxButtonType.RETRY,
            MessageBoxButtonType.CANCEL),
    CancelTryAgainContinue(MessageBoxButtonType.CANCEL,
            MessageBoxButtonType.TRYAGAIN,
            MessageBoxButtonType.CONTINUE),
    OK(MessageBoxButtonType.OK, null, null),
    OKCancel(MessageBoxButtonType.OK, MessageBoxButtonType.CANCEL, null),
    RetryCancel(MessageBoxButtonType.RETRY, MessageBoxButtonType.CANCEL, null),
    YesNo(MessageBoxButtonType.YES, MessageBoxButtonType.NO, null),
    YesNoCancel(MessageBoxButtonType.YES,
            MessageBoxButtonType.NO,
            MessageBoxButtonType.CANCEL),
    Close(MessageBoxButtonType.CLOSE, null, null),
    SubmitCancel(MessageBoxButtonType.SUBMIT, MessageBoxButtonType.CANCEL, null),
    NextPrevious(MessageBoxButtonType.NEXT, MessageBoxButtonType.PREVIOUS, null),
    NextPreviousCancel(MessageBoxButtonType.NEXT,
            MessageBoxButtonType.PREVIOUS,
            MessageBoxButtonType.CANCEL),
    Finish(MessageBoxButtonType.FINISH, null, null),
    FinishCancel(MessageBoxButtonType.FINISH, MessageBoxButtonType.CANCEL, null),
    Apply(MessageBoxButtonType.APPLY, null, null),
    ApplyCancel(MessageBoxButtonType.APPLY, MessageBoxButtonType.CANCEL, null),;

    private final MessageBoxButtonType button1;
    private final MessageBoxButtonType button2;
    private final MessageBoxButtonType button3;

    @Contract(pure = true)
    public @NotNull Optional<MessageBoxButtonType> getButton1() {
        return Optional.ofNullable(button1);
    }

    @Contract(pure = true)
    public @NotNull Optional<MessageBoxButtonType> getButton2() {
        return Optional.ofNullable(button2);
    }

    @Contract(pure = true)
    public @NotNull Optional<MessageBoxButtonType> getButton3() {
        return Optional.ofNullable(button3);
    }
}
