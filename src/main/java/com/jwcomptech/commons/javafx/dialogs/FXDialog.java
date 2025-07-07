package com.jwcomptech.commons.javafx.dialogs;

import com.jwcomptech.commons.annotations.FeatureComplete;
import com.jwcomptech.commons.javafx.controls.FXButtonType;
import com.jwcomptech.commons.javafx.controls.FXButtonTypeGroup;
import com.jwcomptech.commons.javafx.controls.FXControls;
import com.jwcomptech.commons.javafx.controls.FXPaneType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.javafx.JavaFXUtils.*;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FeatureComplete(since = "1.0.0-alpha")
@SuppressWarnings("unused")
public class FXDialog {
    private final Dialog<DialogResult> dialog;
    private final ButtonBar buttonBar;
    private final FXButtonTypeGroup buttonMap;
    private final Map<String, DialogButton> buttons;
    private final FXControls controls;

    public FXDialog(final @NotNull FXButtonTypeGroup buttonMap) {
        checkArgumentNotNull(buttonMap, cannotBeNull("buttonMap"));
        this.buttonMap = buttonMap;

        dialog = new Dialog<>();
        buttonBar = lookupButtonBar();
        controls = new FXControls();
        buttons = new LinkedHashMap<>();

        configureButtons();
    }

    public void setContentText(final String contentText) {
        runLaterIfNeeded(() -> dialog.setContentText(contentText));
    }

    public void setHeaderText(final String headerText) {
        runLaterIfNeeded(() -> dialog.setHeaderText(headerText));
    }

    public void setTitle(final String title) {
        runLaterIfNeeded(() -> dialog.setTitle(title));
    }

    public void setGraphic(final Node graphic) {
        runLaterIfNeeded(() -> dialog.setGraphic(graphic));
    }

    public String getContentText() {
        return dialog.getContentText();
    }

    public String getHeaderText() {
        return dialog.getHeaderText();
    }

    public String getTitle() {
        return dialog.getTitle();
    }

    public Node getGraphic() {
        return dialog.getGraphic();
    }

    public void close() {
        runLaterIfNeeded(dialog::close);
    }

    public void hide() {
        runLaterIfNeeded(dialog::hide);
    }

    public void show() {
        runLaterIfNeeded(dialog::show);
    }

    public Optional<DialogResult> showAndWait() {
        final var result = new SimpleObjectProperty<Optional<DialogResult>>();
        runAndWait(() -> result.set(dialog.showAndWait()));

        return result.get();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public boolean isResizable() {
        return dialog.isResizable();
    }

    public void setResizable(final boolean resizable) {
        runLaterIfNeeded(() -> dialog.setResizable(resizable));
    }

    public double getWidth() {
        return dialog.getWidth();
    }

    public void setWidth(final double width) {
        runLaterIfNeeded(() -> dialog.setWidth(width));
    }

    public double getHeight() {
        return dialog.getHeight();
    }

    public void setHeight(final double height) {
        runLaterIfNeeded(() -> dialog.setHeight(height));
    }

    public Callback<ButtonType, DialogResult> getResultConverter() {
        return dialog.getResultConverter();
    }

    public void setResultConverter(final Callback<ButtonType, DialogResult> value) {
        runLaterIfNeeded(() -> dialog.setResultConverter(value));
    }

    public DialogResult getResult() {
        return dialog.getResult();
    }

    public void setResult(final DialogResult value) {
        runLaterIfNeeded(() -> dialog.setResult(value));
    }

    public double getX() {
        return dialog.getX();
    }

    public void setX(final double x) {
        runLaterIfNeeded(() -> dialog.setX(x));
    }

    public double getY() {
        return dialog.getY();
    }

    public void setY(final double y) {
        runLaterIfNeeded(() -> dialog.setY(y));
    }

    public DialogPane getDialogPane() {
        return dialog.getDialogPane();
    }

    public void setDialogPane(final DialogPane value) {
        runLaterIfNeeded(() -> dialog.setDialogPane(value));
    }

    public Optional<ButtonBar> getButtonBar() {
        return Optional.ofNullable(buttonBar);
    }

    public void setButtonBarButtonOrderToNONE() {
        setButtonBarButtonOrder(ButtonBar.BUTTON_ORDER_NONE);
    }

    public void setButtonBarButtonOrderToLINUX() {
        setButtonBarButtonOrder(ButtonBar.BUTTON_ORDER_LINUX);
    }

    public void setButtonBarButtonOrderToWINDOWS() {
        setButtonBarButtonOrder(ButtonBar.BUTTON_ORDER_WINDOWS);
    }

    public void setButtonBarButtonOrderToMACOS() {
        setButtonBarButtonOrder(ButtonBar.BUTTON_ORDER_MAC_OS);
    }

    public void setButtonBarButtonOrder(final String order) {
        runLaterIfNeeded(() -> getButtonBar().ifPresent(bar ->
                bar.setButtonOrder(order)));
    }

    public DialogButton getButton(final int index) {
        var entryList = new ArrayList<>(buttons.entrySet());
        var entry = entryList.get(1);
        return entry.getValue();
    }

    public DialogButton getButton(final String name) {
        return buttons.get(name);
    }

    /**
     * Finds the {@link ButtonBar} node within the parent's dialog pane if it exists.
     *
     * @return An {@link Optional} containing the {@link ButtonBar}, or empty if not found.
     * @throws IllegalStateException if the parent dialog doesn't define a button bar
     */
    private @NotNull ButtonBar lookupButtonBar() {
        var buttonBar = getDialogPane().lookupAll(".button-bar").stream()
                .filter(node -> node instanceof ButtonBar).findFirst()
                .map(node -> (ButtonBar) node);
        if(buttonBar.isEmpty()) throw new IllegalStateException("Dialog contains no button bar");
        return buttonBar.get();
    }

    protected void configureButtons() {
        dialog.getDialogPane().getButtonTypes().setAll(
                buttonMap.getAllTypes().stream()
                        .map(FXButtonType::getButtonType)
                        .toList()
                        .toArray(new ButtonType[0]));

        buttons.clear();
        buttonMap.getDialogButtons(this)
                        .forEach(button ->
                                buttons.put(button.getText(), button));
        buttons.forEach(controls::addButton);
        setButtonBarButtonOrderToNONE();
    }

    public DialogPaneBuilder getDialogPaneBuilder(final FXPaneType type) {
        return DialogPaneBuilder.create(getDialogPane(), type);
    }
}
