package com.jwcomptech.commons.javafx.controls;

import com.jwcomptech.commons.annotations.FeatureComplete;
import com.jwcomptech.commons.javafx.dialogs.DialogButton;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jwcomptech.commons.consts.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;

/**
 * A central registry for managing JavaFX UI controls by type and name.
 * <p>
 * This class provides type-safe storage and retrieval of {@link Node} controls,
 * such as {@link Label}, {@link Button}, and others,
 * allowing them to be accessed by class type and a unique string name.
 * <p>
 * All control lookups are generic and validated against the expected type, ensuring safe casting
 * at runtime. Type-specific helpers are also available for common controls.
 * <p>
 * This registry is primarily intended for internal UI wiring, controller organization, or frameworks
 * that benefit from central access to reusable controls.
 */
@Data
@FeatureComplete(since = "1.0.0-alpha")
public class FXControls {
    private final Map<Class<?>, Map<String, ?>> controlsMap = new LinkedHashMap<>();
    private final Map<String, DialogButton> dialogButtonMap = new LinkedHashMap<>();

    /**
     * Retrieves a control of the specified type and name from the registry.
     *
     * @param type the class of the control to retrieve
     * @param name the name identifier of the control
     * @param <T> the type of JavaFX {@link Node} being retrieved
     * @return an {@link Optional} containing the control if found and of the correct type; otherwise, an empty Optional
     */
    public <T extends Node> Optional<T> getControl(final Class<T> type,
                                       final String name) {
        checkArgumentNotNull(type, cannotBeNullOrEmpty("type"));
        checkArgumentNotNullOrEmpty(name, cannotBeNullOrEmpty("name"));
        final var map = controlsMap.get(type);
        if (map == null) return Optional.empty();

        final var controlObj = Optional.ofNullable(map.get(name));
        if (controlObj.isPresent() && type.isInstance(controlObj.get())) {
            try {
                return Optional.of(type.cast(controlObj.get()));
            } catch (ClassCastException e) {
                throw new IllegalStateException("Control type mismatch for name: " + name, e);
            }
        } else return Optional.empty();
    }

    /**
     * Returns an unmodifiable view of the internal controls map.
     * <p>
     * This method is primarily intended for testing purposes and should not
     * be used as the standard way to access controls. Use {@link #getControl(Class, String)}
     * for type-safe control retrieval instead.
     * </p>
     * <p>
     * The returned map is unmodifiable; attempts to modify it will throw
     * {@link UnsupportedOperationException}.
     * </p>
     *
     * @return an unmodifiable {@code Map<Class<?>, Map<String, ?>>} of all registered controls
     */
    @UnmodifiableView
    @VisibleForTesting
    @SuppressWarnings("unused")
    public Map<Class<?>, Map<String, ?>> getControls() {
        return Collections.unmodifiableMap(controlsMap);
    }

    /**
     * Returns an unmodifiable view of the internal {@link DialogButton} map.
     * <p>
     * This method is primarily intended for testing purposes and should not
     * be used as the standard way to access {@link DialogButton}s. Use {@link #getDialogButton(String)}
     * for type-safe control retrieval instead.
     * </p>
     * <p>
     * The returned map is unmodifiable; attempts to modify it will throw
     * {@link UnsupportedOperationException}.
     * </p>
     *
     * @return an unmodifiable {@code Map<String, DialogButton>} of all registered {@link DialogButton}s
     */
    @VisibleForTesting
    @SuppressWarnings("unused")
    public Map<String, DialogButton> getDialogButtons() {
        return Collections.unmodifiableMap(dialogButtonMap);
    }

    /**
     * Returns a stream of control entries (name to control) for the specified control type.
     *
     * @param type the JavaFX control type
     * @param <T>  the control subtype
     * @return a stream of {@code Map.Entry<String, T>} representing controls of the given type
     */
    public <T> Stream<Map.Entry<String, T>> streamControlsOfType(Class<T> type) {
        Map<String, ?> raw = controlsMap.get(type);
        if (raw == null) return Stream.empty();

        try {
            // Safe cast using Class<T>
            Map<String, T> casted = raw.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> type.cast(e.getValue()),
                            (a, b) -> b,
                            LinkedHashMap::new
                    ));

            return casted.entrySet().stream();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Control map contains mismatched types for: " + type.getSimpleName(), e);
        }
    }


    /**
     * Adds a new control to the registry under the specified class type and name.
     * <p>
     * If a control with the same name already exists for the given class type,
     * an {@link IllegalArgumentException} will be thrown to prevent overwriting.
     *
     * @param type   the class of the control to add
     * @param name    the unique name identifier for the control
     * @param control the control instance to register
     * @param <T>     the type of JavaFX {@link Node} being added
     * @throws IllegalArgumentException if a control with the given name already exists for the specified type
     */
    @SuppressWarnings("unchecked")
    public <T extends Node> void addControl(final Class<T> type,
                                            final String name,
                                            final T control) {
        checkArgumentNotNull(type, cannotBeNullOrEmpty("type"));
        checkArgumentNotNullOrEmpty(name, cannotBeNullOrEmpty("name"));
        var map = (Map<String, T>) controlsMap.computeIfAbsent(
                type,
                k -> new LinkedHashMap<>()
        );

        if (map.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate control name: " + name);
        }

        map.put(name, control);
    }

    /**
     * Adds a control to the registry under the specified class type and name,
     * ignoring the request if a control with the same name already exists.
     *
     * @param type   the class of the control to add
     * @param name    the unique name identifier for the control
     * @param control the control instance to register
     * @param <T>     the type of JavaFX {@link Node} being added
     */
    @SuppressWarnings("unchecked")
    public <T extends Node> void addControlIgnoreExisting(final Class<T> type,
                               final String name,
                               final T control) {
        checkArgumentNotNull(type, cannotBeNullOrEmpty("type"));
        checkArgumentNotNullOrEmpty(name, cannotBeNullOrEmpty("name"));
        var map = (Map<String, T>) controlsMap.computeIfAbsent(
                type,
                k -> new LinkedHashMap<>()
        );

        if (!map.containsKey(name)) map.put(name, control);
    }

    /**
     * Retrieves an unmodifiable map of all controls registered under the specified type.
     * <p>
     * If no controls are registered under the given class type, a new empty map is created and returned.
     *
     * @param type the class type of controls to retrieve
     * @param <T>   the type of JavaFX {@link Node}
     * @return an unmodifiable {@link Map} of control names to control instances
     */
    @SuppressWarnings("unchecked")
    public <T extends Node> Map<String, T> getControls(final Class<T> type) {
        checkArgumentNotNull(type, cannotBeNullOrEmpty("type"));
        var map = (Map<String, T>) controlsMap.computeIfAbsent(
                type,
                k -> new LinkedHashMap<>()
        );
        return Collections.unmodifiableMap(map);
    }

    /**
     * Removes a control of the specified type and name from the registry.
     *
     * @param type the class type of the control to remove
     * @param name  the name identifier of the control
     * @param <T>   the type of JavaFX {@link Node} being removed
     */
    public <T extends Node> void removeControl(final Class<T> type, final String name) {
        checkArgumentNotNull(type, cannotBeNullOrEmpty("type"));
        checkArgumentNotNullOrEmpty(name, cannotBeNullOrEmpty("name"));
        final var map = controlsMap.get(type);
        if (map == null) return;
        map.remove(name);
    }

    /**
     * Removes a DialogButton with the specified name from the registry.
     *
     * @param name  the name identifier of the button
     */
    public void removeDialogButton(final String name) {
        checkArgumentNotNullOrEmpty(name, cannotBeNullOrEmpty("name"));
        final var map = controlsMap.get(Button.class);
        if (map == null) return;
        map.remove(name);
        dialogButtonMap.remove(name);
    }

    /**
     * Retrieves a {@link Label} control by its name.
     *
     * @param name the name identifier of the label
     * @return an {@link Optional} containing the label if found; otherwise, an empty Optional
     */
    public Optional<Label> getLabel(final String name) {
        return getControl(Label.class, name);
    }

    /**
     * Retrieves a {@link Button} control by its name.
     *
     * @param name the name identifier of the button
     * @return an {@link Optional} containing the button if found; otherwise, an empty Optional
     */
    public Optional<Button> getButton(final String name) {
        return getControl(Button.class, name);
    }

    /**
     * Retrieves a {@link DialogButton} control by its name.
     *
     * @param name the name identifier of the button
     * @return an {@link Optional} containing the button if found; otherwise, an empty Optional
     */
    public Optional<DialogButton> getDialogButton(final String name) {
        return Optional.ofNullable(dialogButtonMap.get(name));
    }

    /**
     * Retrieves a {@link TextField} control by its name.
     *
     * @param name the name identifier of the text field
     * @return an {@link Optional} containing the text field if found; otherwise, an empty Optional
     */
    public Optional<TextField> getTextField(final String name) {
        return getControl(TextField.class, name);
    }

    /**
     * Retrieves a {@link PasswordField} control by its name.
     *
     * @param name the name identifier of the password field
     * @return an {@link Optional} containing the password field if found; otherwise, an empty Optional
     */
    public Optional<PasswordField> getPasswordField(final String name) {
        return getControl(PasswordField.class, name);
    }

    /**
     * Retrieves a {@link CheckBox} control by its name.
     *
     * @param name the name identifier of the checkbox
     * @return an {@link Optional} containing the checkbox if found; otherwise, an empty Optional
     */
    public Optional<CheckBox> getCheckBox(final String name) {
        return getControl(CheckBox.class, name);
    }

    /**
     * Registers a {@link Label} control under the given name.
     *
     * @param name  the unique name identifier for the label
     * @param value the {@link Label} instance to register
     * @throws IllegalArgumentException if a label with the given name already exists
     */
    public void addLabel(final String name, final Label value) {
        addControl(Label.class, name, value);
    }

    /**
     * Registers a {@link Label} control under the given name,
     * ignoring the operation if a label with the same name already exists.
     *
     * @param name  the unique name identifier for the label
     * @param label the {@link Label} instance to register
     */
    public void addLabelIgnoreExisting(final String name, final Label label) {
        addControlIgnoreExisting(Label.class, name, label);
    }

    /**
     * Registers a {@link Button} control under the given name.
     *
     * @param name   the unique name identifier for the button
     * @param button the {@link Button} instance to register
     * @throws IllegalArgumentException if a button with the given name already exists
     */
    public void addButton(final String name, final Button button) {
        addControl(Button.class, name, button);
    }

    /**
     * Registers a {@link Button} control under the given name.
     *
     * @param name   the unique name identifier for the button
     * @param button the {@link DialogButton} instance to register
     * @throws IllegalArgumentException if a button with the given name already exists
     */
    public void addButton(final String name, final @NotNull DialogButton button) {
        addControl(Button.class, name, button.getButton());
        dialogButtonMap.put(name, button);
    }

    /**
     * Registers a {@link Button} control using the button text as the identifier.
     *
     * @param button the {@link DialogButton} instance to register
     * @throws IllegalArgumentException if a button with the given name already exists
     */
    public void addButton(final @NotNull DialogButton button) {
        addControl(Button.class, button.getText(), button.getButton());
        dialogButtonMap.put(button.getText(), button);
    }

    /**
     * Registers a {@link Button} control under the given name,
     * ignoring the operation if a button with the same name already exists.
     *
     * @param name   the unique name identifier for the button
     * @param button the {@link Button} instance to register
     */
    public void addButtonIgnoreExisting(final String name, final Button button) {
        addControlIgnoreExisting(Button.class, name, button);
    }

    /**
     * Registers a {@link Button} control under the given name,
     * ignoring the operation if a button with the same name already exists.
     *
     * @param name   the unique name identifier for the button
     * @param button the {@link DialogButton} instance to register
     */
    public void addButtonIgnoreExisting(final String name, @NotNull final DialogButton button) {
        addControlIgnoreExisting(Button.class, name, button.getButton());
        if(!dialogButtonMap.containsKey(name)) {
            dialogButtonMap.put(name, button);
        }
    }

    /**
     * Registers a {@link Button} control using the button text as the identifier,
     * ignoring the operation if a button with the same name already exists.
     *
     * @param button the {@link Button} instance to register
     */
    public void addButtonIgnoreExisting(@NotNull final DialogButton button) {
        addControlIgnoreExisting(Button.class, button.getText(), button.getButton());
        if(!dialogButtonMap.containsKey(button.getText())) {
            dialogButtonMap.put(button.getText(), button);
        }
    }

    /**
     * Registers a {@link TextField} control under the given name.
     *
     * @param name   the unique name identifier for the text field
     * @param textField the {@link TextField} instance to register
     * @throws IllegalArgumentException if a text field with the given name already exists
     */
    public void addTextField(final String name, final TextField textField) {
        addControl(TextField.class, name, textField);
    }

    /**
     * Registers a {@link TextField} control under the given name,
     * ignoring the operation if a text field with the same name already exists.
     *
     * @param name   the unique name identifier for the text field
     * @param textField the {@link TextField} instance to register
     */
    public void addTextFieldIgnoreExisting(final String name, final TextField textField) {
        addControlIgnoreExisting(TextField.class, name, textField);
    }

    /**
     * Registers a {@link PasswordField} control under the given name.
     *
     * @param name   the unique name identifier for the password field
     * @param passwordField the {@link PasswordField} instance to register
     * @throws IllegalArgumentException if a password field with the given name already exists
     */
    public void addPasswordField(final String name, final PasswordField passwordField) {
        addControl(PasswordField.class, name, passwordField);
    }

    /**
     * Registers a {@link PasswordField} control under the given name,
     * ignoring the operation if a password field with the same name already exists.
     *
     * @param name   the unique name identifier for the password field
     * @param passwordField the {@link PasswordField} instance to register
     */
    public void addPasswordFieldIgnoreExisting(final String name, final PasswordField passwordField) {
        addControlIgnoreExisting(PasswordField.class, name, passwordField);
    }

    /**
     * Registers a {@link CheckBox} control under the given name.
     *
     * @param name   the unique name identifier for the checkbox
     * @param checkBox the {@link CheckBox} instance to register
     * @throws IllegalArgumentException if a checkbox with the given name already exists
     */
    public void addCheckBox(final String name, final CheckBox checkBox) {
        addControl(CheckBox.class, name, checkBox);
    }

    /**
     * Registers a {@link CheckBox} control under the given name,
     * ignoring the operation if a checkbox with the same name already exists.
     *
     * @param name   the unique name identifier for the checkbox
     * @param checkBox the {@link CheckBox} instance to register
     */
    public void addCheckBoxIgnoreExisting(final String name, final CheckBox checkBox) {
        addControlIgnoreExisting(CheckBox.class, name, checkBox);
    }

    /**
     * Returns an unmodifiable map of all registered {@link Label} controls.
     * <p>
     * The map keys are control names, and the values are the corresponding {@link Label} instances.
     *
     * @return an unmodifiable {@link Map} of control names to {@link Label} instances
     */
    public Map<String, Label> getLabels() {
        return getControls(Label.class);
    }

    /**
     * Returns an unmodifiable map of all registered {@link Button} controls.
     * <p>
     * The map keys are control names, and the values are the corresponding {@link Button} instances.
     *
     * @return an unmodifiable {@link Map} of control names to {@link Button} instances
     */
    public Map<String, Button> getButtons() {
        return getControls(Button.class);
    }

    /**
     * Returns an unmodifiable map of all registered {@link TextField} controls.
     * <p>
     * The map keys are control names, and the values are the corresponding {@link TextField} instances.
     *
     * @return an unmodifiable {@link Map} of control names to {@link TextField} instances
     */
    public Map<String, TextField> getTextFields() {
        return getControls(TextField.class);
    }

    /**
     * Returns an unmodifiable map of all registered {@link PasswordField} controls.
     * <p>
     * The map keys are control names, and the values are the corresponding {@link PasswordField} instances.
     *
     * @return an unmodifiable {@link Map} of control names to {@link PasswordField} instances
     */
    public Map<String, PasswordField> getPasswordFields() {
        return getControls(PasswordField.class);
    }

    /**
     * Returns an unmodifiable map of all registered {@link CheckBox} controls.
     * <p>
     * The map keys are control names, and the values are the corresponding {@link CheckBox} instances.
     *
     * @return an unmodifiable {@link Map} of control names to {@link CheckBox} instances
     */
    public Map<String, CheckBox> getCheckBoxes() {
        return getControls(CheckBox.class);
    }
}
