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
import com.jwcomptech.commons.javafx.dialogs.DialogButton;
import com.jwcomptech.commons.javafx.dialogs.DialogResult;
import com.jwcomptech.commons.javafx.dialogs.FXDialog;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.*;

/**
 * Represents predefined button configurations for use in dialogs.
 * Each group defines up to three {@link FXButtonType} values, which are used
 * to create JavaFX {@link Button} instances and map user interactions to {@link DialogResult}.
 *
 * @since 1.0.0-alpha
 */
@Getter
@ToString
@FeatureComplete(since = "1.0.0-alpha")
@SuppressWarnings("unused")
public enum FXButtonTypeGroup {

    AbortRetryIgnore(FXButtonType.ABORT, FXButtonType.RETRY, FXButtonType.CANCEL),
    Apply(FXButtonType.APPLY, null, null),
    ApplyCancel(FXButtonType.APPLY, FXButtonType.CANCEL, null),
    BackApply(FXButtonType.BACK, FXButtonType.APPLY, null),
    BackApplyCancel(FXButtonType.BACK, FXButtonType.CANCEL, null),
    BackFinish(FXButtonType.BACK, FXButtonType.FINISH, null),
    BackFinishCancel(FXButtonType.BACK, FXButtonType.CANCEL, null),
    BackInstall(FXButtonType.BACK, FXButtonType.INSTALL, null),
    BackInstallCancel(FXButtonType.BACK, FXButtonType.INSTALL, FXButtonType.CANCEL),
    BackNext(FXButtonType.BACK, FXButtonType.NEXT, null),
    BackNextCancel(FXButtonType.BACK, FXButtonType.NEXT, FXButtonType.CANCEL),
    BackRetry(FXButtonType.BACK, FXButtonType.RETRY, null),
    BackRetryCancel(FXButtonType.BACK, FXButtonType.RETRY, FXButtonType.CANCEL),
    BackSubmit(FXButtonType.BACK, FXButtonType.SUBMIT, null),
    BackSubmitCancel(FXButtonType.BACK, FXButtonType.SUBMIT, FXButtonType.CANCEL),
    CancelTryAgainContinue(FXButtonType.CANCEL, FXButtonType.TRYAGAIN, FXButtonType.CONTINUE),
    Close(FXButtonType.CLOSE, null, null),
    Finish(FXButtonType.FINISH, null, null),
    FinishCancel(FXButtonType.FINISH, FXButtonType.CANCEL, null),
    Install(FXButtonType.INSTALL, null, null),
    InstallCancel(FXButtonType.INSTALL, FXButtonType.CANCEL, null),
    Login(FXButtonType.LOGIN, null, null),
    LoginCancel(FXButtonType.LOGIN, FXButtonType.CANCEL, null),
    NextPrevious(FXButtonType.NEXT, FXButtonType.PREVIOUS, null),
    NextPreviousCancel(FXButtonType.NEXT, FXButtonType.PREVIOUS, FXButtonType.CANCEL),
    OK(FXButtonType.OK, null, null),
    OKCancel(FXButtonType.OK, FXButtonType.CANCEL, null),
    PreviousApply(FXButtonType.PREVIOUS, FXButtonType.APPLY, null),
    PreviousApplyCancel(FXButtonType.PREVIOUS, FXButtonType.APPLY, FXButtonType.CANCEL),
    PreviousFinish(FXButtonType.PREVIOUS, FXButtonType.FINISH, null),
    PreviousFinishCancel(FXButtonType.PREVIOUS, FXButtonType.FINISH, FXButtonType.CANCEL),
    PreviousInstall(FXButtonType.PREVIOUS, FXButtonType.INSTALL, null),
    PreviousInstallCancel(FXButtonType.PREVIOUS, FXButtonType.INSTALL, FXButtonType.CANCEL),
    PreviousRetry(FXButtonType.PREVIOUS, FXButtonType.RETRY, null),
    PreviousRetryCancel(FXButtonType.PREVIOUS, FXButtonType.RETRY, FXButtonType.CANCEL),
    PreviousNext(FXButtonType.PREVIOUS, FXButtonType.NEXT, null),
    PreviousNextCancel(FXButtonType.PREVIOUS, FXButtonType.NEXT, FXButtonType.CANCEL),
    PreviousSubmit(FXButtonType.PREVIOUS, FXButtonType.SUBMIT, null),
    PreviousSubmitCancel(FXButtonType.PREVIOUS, FXButtonType.SUBMIT, FXButtonType.CANCEL),
    Retry(FXButtonType.RETRY, null, null),
    RetryCancel(FXButtonType.RETRY, FXButtonType.CANCEL, null),
    Submit(FXButtonType.SUBMIT, null, null),
    SubmitCancel(FXButtonType.SUBMIT, FXButtonType.CANCEL, null),
    YesNo(FXButtonType.YES, FXButtonType.NO, null),
    YesNoCancel(FXButtonType.YES, FXButtonType.NO, FXButtonType.CANCEL),

    ;

    private final FXButtonType button1;
    private final FXButtonType button2;
    private final FXButtonType button3;
    private final Map<ButtonBar.ButtonData, DialogResult> cachedResultMap;

    FXButtonTypeGroup(FXButtonType button1,
                      FXButtonType button2,
                      FXButtonType button3) {
        this.button1 = button1;
        this.button2 = button2;
        this.button3 = button3;
        this.cachedResultMap = buildResultMap();
    }

    /**
     * Returns whether this group defines a first button.
     */
    public boolean hasButton1() { return button1 != null; }

    /**
     * Returns whether this group defines a second button.
     */
    public boolean hasButton2() { return button2 != null; }

    /**
     * Returns whether this group defines a third button.
     */
    public boolean hasButton3() { return button3 != null; }

    /**
     * Returns the first button type.
     *
     * @throws IllegalStateException if the first button is not present.
     */
    public @NotNull FXButtonType getButton1() {
        checkState(hasButton1(), cannotBeNull("button1"));
        //noinspection DataFlowIssue
        return button1;
    }

    /**
     * Returns the second button type.
     *
     * @throws UnsupportedOperationException if the second button is not present.
     */
    public @NotNull FXButtonType getButton2() {
        checkOperation(hasButton2(), name() + " does not have a second button.");
        //noinspection DataFlowIssue
        return button2;
    }

    /**
     * Returns the third button type.
     *
     * @throws UnsupportedOperationException if the third button is not present.
     */
    public @NotNull FXButtonType getButton3() {
        checkOperation(hasButton3(), name() + " does not have a third button.");
        //noinspection DataFlowIssue
        return button3;
    }

    /**
     * Returns an optional containing the first button type, if present.
     */
    @Contract(pure = true)
    public @NotNull Optional<FXButtonType> getButton1IfExists() {
        return Optional.ofNullable(button1);
    }

    /**
     * Returns an optional containing the second button type, if present.
     */
    @Contract(pure = true)
    public @NotNull Optional<FXButtonType> getButton2IfExists() {
        return Optional.ofNullable(button2);
    }

    /**
     * Returns an optional containing the third button type, if present.
     */
    @Contract(pure = true)
    public @NotNull Optional<FXButtonType> getButton3IfExists() {
        return Optional.ofNullable(button3);
    }

    /**
     * Returns all defined {@link FXButtonType}s in declared order.
     */
    public @NotNull List<FXButtonType> getAllTypes() {
        return Stream.of(button1, button2, button3)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Constructs a list of {@link DialogButton} instances for use with the given dialog.
     *
     * @param dialog The dialog instance.
     * @return A list of associated dialog buttons.
     */
    public @NotNull List<DialogButton> getDialogButtons(@NotNull FXDialog dialog) {
        return getAllTypes().stream()
                .map(type -> new DialogButton(this, type, FXPaneType.DIALOG_PANE, dialog))
                .toList();
    }

    /**
     * Attempts to find the {@link ButtonBar} in the dialog.
     */
    public static @NotNull Optional<ButtonBar> getButtonBar(@NotNull Dialog<?> dialog) {
        return dialog.getDialogPane().lookupAll(".button-bar").stream()
                .filter(ButtonBar.class::isInstance)
                .map(ButtonBar.class::cast)
                .findFirst();
    }

    /**
     * Converts a {@link ButtonType} to a {@link DialogResult}, returning {@link DialogResult#NONE} if unmatched.
     */
    public @NotNull DialogResult toDialogResult(ButtonType button) {
        return toDialogResult(button, null);
    }

    /**
     * Converts a {@link ButtonType} to a {@link DialogResult}, optionally attaching extra data.
     */
    public @NotNull DialogResult toDialogResult(ButtonType button, FXData data) {
        checkArgumentNotNull(button, cannotBeNull("button"));
        DialogResult result = cachedResultMap.getOrDefault(
                button.getButtonData(), DialogResult.NONE);

        if (data != null) {
            if (result.isImmutable()) {
                result = result.copy();
            }
            result.mergeWith(data);
        }

        return result;
    }

    /**
     * Returns a cached mapping of {@link ButtonBar.ButtonData} to {@link DialogResult}.
     */
    public @NotNull @UnmodifiableView Map<ButtonBar.ButtonData, DialogResult> getResultMap() {
        return cachedResultMap;
    }

    private @NotNull @Unmodifiable Map<ButtonBar.ButtonData, DialogResult> buildResultMap() {
        return getAllTypes().stream()
                .map(type -> new AbstractMap.SimpleEntry<>(
                        type.getButtonType().getButtonData(),
                        type.toDialogResult()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Returns an entry mapping {@link ButtonBar.ButtonData} to {@link DialogResult} for button1.
     */
    public @NotNull Optional<AbstractMap.Entry<ButtonBar.ButtonData, DialogResult>> getButton1DataMap() {
        return getButtonDataMap(button1);
    }

    /**
     * Returns an entry mapping {@link ButtonBar.ButtonData} to {@link DialogResult} for button2.
     */
    public @NotNull Optional<AbstractMap.Entry<ButtonBar.ButtonData, DialogResult>> getButton2DataMap() {
        return getButtonDataMap(button2);
    }

    /**
     * Returns an entry mapping {@link ButtonBar.ButtonData} to {@link DialogResult} for button3.
     */
    public @NotNull Optional<AbstractMap.Entry<ButtonBar.ButtonData, DialogResult>> getButton3DataMap() {
        return getButtonDataMap(button3);
    }

    private Optional<AbstractMap.Entry<ButtonBar.ButtonData, DialogResult>> getButtonDataMap(FXButtonType type) {
        if (type == null) return Optional.empty();
        var data = cachedResultMap.get(type.getButtonType().getButtonData());
        return data != null
                ? Optional.of(new AbstractMap.SimpleEntry<>(type.getButtonType().getButtonData(), data))
                : Optional.empty();
    }
}
