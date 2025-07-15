package com.jwcomptech.commons.javafx.wizard.base;

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

import com.jwcomptech.commons.javafx.HeadlessFX;
import com.jwcomptech.commons.javafx.controls.FXButton;
import com.jwcomptech.commons.javafx.controls.FXPaneType;
import com.jwcomptech.commons.javafx.controls.FXButtonType;
import com.jwcomptech.commons.javafx.controls.FXButtonTypeGroup;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Data;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Data
public abstract class AbstractWizard<T extends AbstractWizard<T>> {
    private final BorderPane content = new BorderPane();
    private final WizardPageLoader<T> pageLoader = new WizardPageLoader<>();
    private final WizardNavigator<T> navigator = new WizardNavigator<>();
    private final AtomicReference<WizardContext<T>> context = new AtomicReference<>();
    private Stage primaryStage;

    private CountDownLatch startupLatch = new CountDownLatch(1);

    public AbstractWizard() {

        HeadlessFX.initializeWithUI(primaryStage -> {
            this.primaryStage = primaryStage;

            // Set wizard instance safely here (subclass fully initialized)
            context.set(WizardContext.of(getWizard()));
            pageLoader.setParent(getWizard());
            navigator.setParent(getWizard());

            initialize();

            primaryStage.setTitle(getWindowTitle());
            primaryStage.setScene(new Scene(content));
            startupLatch.countDown();
        });
    }

    @SneakyThrows
    public void show() {
        startupLatch.await();

        HeadlessFX.runLaterIfNeeded(primaryStage::show);
    }

    public final void initialize() {
        configureButtons(getInitialButtonTypeGroup());

        initPages(pageLoader);

        navigator.start();

        postInitialize();
    }

    protected final void configureButtons(final @NotNull FXButtonTypeGroup group) {
        Optional<FXButtonType> backButtonType = group.getButton1IfExists();
        Optional<FXButtonType> nextButtonType = group.getButton2IfExists();
        Optional<FXButtonType> cancelButtonType = group.getButton3IfExists();

        AtomicReference<FXButton> backButton = new AtomicReference<>();
        AtomicReference<FXButton> nextButton = new AtomicReference<>();
        AtomicReference<FXButton> cancelButton = new AtomicReference<>();

        backButtonType.ifPresent(fxButtonType ->
                backButton.set(new FXButton(group, fxButtonType,
                        FXPaneType.BORDER_PANE, content)));
        context.get().setBackButton(backButton.get());

        backButton.get().setOnAction(event -> navigator.onPrevious());

        nextButtonType.ifPresent(fxButtonType ->
                nextButton.set(new FXButton(group, fxButtonType,
                        FXPaneType.BORDER_PANE, content)));
        context.get().setNextButton(nextButton.get());

        nextButton.get().setOnAction(event -> navigator.onNext());

        cancelButtonType.ifPresent(fxButtonType ->
                cancelButton.set(new FXButton(group, fxButtonType,
                        FXPaneType.BORDER_PANE, content)));
        context.get().setCancelButton(cancelButton.get());

        cancelButton.get().setOnAction(event -> navigator.onCancel());
    }

    protected abstract FXButtonTypeGroup getInitialButtonTypeGroup();

    protected abstract String getWindowTitle();

    protected abstract T getWizard();

    protected abstract void initPages(final WizardPageLoader<T> loader);

    protected abstract void postInitialize();

    public WizardContext<T> getContext() {
        return context.get();
    }
}
