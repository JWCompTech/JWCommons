package com.jwcomptech.commons.javafx.wizard.base;

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
