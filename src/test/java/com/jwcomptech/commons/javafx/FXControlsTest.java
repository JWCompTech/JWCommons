package com.jwcomptech.commons.javafx;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jwcomptech.commons.javafx.controls.FXControls;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.control.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.Map;

@ExtendWith(ApplicationExtension.class)
public class FXControlsTest {

    private FXControls fxControls;

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> { /* noop */ });
    }

    @BeforeEach
    void setup() {
        fxControls = new FXControls();
    }

    @Test
    void testAddAndGetControl() {
        Button button = new Button("Click me");
        fxControls.addButton("btn1", button);

        assertThat(fxControls.getButton("btn1")).contains(button);
        assertThat(fxControls.getControl(Button.class, "btn1")).contains(button);
    }

    @Test
    void testAddControlDuplicateThrows() {
        fxControls.addButton("btn1", new Button("Original"));

        assertThatThrownBy(() -> fxControls.addButton("btn1", new Button("Duplicate")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicate control name");
    }

    @Test
    void testAddControlIgnoreExistingSkipsDuplicates() {
        fxControls.addLabelIgnoreExisting("label1", new Label("One"));
        fxControls.addLabelIgnoreExisting("label1", new Label("Should not replace"));

        assertThat(fxControls.getLabel("label1").get().getText()).isEqualTo("One");
    }

    @Test
    void testRemoveControl() {
        fxControls.addTextField("field1", new TextField("initial"));
        assertThat(fxControls.getTextField("field1")).isPresent();

        fxControls.removeControl(TextField.class, "field1");
        assertThat(fxControls.getTextField("field1")).isEmpty();
    }

    @Test
    void testGetControlsReturnsUnmodifiableMap() {
        fxControls.addPasswordField("secret", new PasswordField());

        Map<String, PasswordField> map = fxControls.getPasswordFields();
        assertThat(map).containsKey("secret");

        assertThatThrownBy(() -> map.put("new", new PasswordField()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testGetControlReturnsEmptyForUnknown() {
        assertThat(fxControls.getCheckBox("nonexistent")).isEmpty();
    }

    @Test
    void testGetControlsReturnsEmptyMapWhenNone() {
        assertThat(fxControls.getButtons()).isNotNull().isEmpty();
    }

    @Test
    void testStreamControlsOfTypeReturnsCorrectEntries() {
        fxControls.addTextField("username", new TextField());
        fxControls.addTextField("email", new TextField());
        fxControls.addLabel("header", new Label("Header"));

        var keys = fxControls.streamControlsOfType(TextField.class)
                .map(Map.Entry::getKey)
                .toList();

        assertThat(keys).containsExactlyInAnyOrder("username", "email");
    }

    @Test
    void testStreamControlsOfTypeReturnsEmptyIfNoneRegistered() {
        assertThat(fxControls.streamControlsOfType(ComboBox.class)).isEmpty();
    }

    @Test
    void testGetControlRejectsNullKey() {
        assertThatThrownBy(() -> fxControls.getControl(TextField.class, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testRemoveControlIsNoOpForMissing() {
        fxControls.removeControl(CheckBox.class, "ghost");
        assertThat(fxControls.getCheckBox("ghost")).isEmpty(); // Shouldn’t throw
    }
}

