package com.jwcomptech.commons.javafx.controls;

import com.jwcomptech.commons.annotations.FeatureComplete;
import com.jwcomptech.commons.enums.BaseEnum;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.*;
import javafx.stage.Window;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

/**
 * An enumeration of commonly used JavaFX {@link Pane} types,
 * each associated with its corresponding class and utility methods.
 * <p>
 * This enum can be used for dynamic pane creation and type-checking.
 *
 * @since 1.0.0-alpha
 */
@AllArgsConstructor
@Getter
@ToString
@FeatureComplete(since = "1.0.0-alpha")
public enum FXPaneType implements BaseEnum<Class<?>> {
    /**
     * A basic {@link Pane} — a layout container with no built-in layout policy.
     */
    PANE(Pane.class),

    /**
     * An {@link AnchorPane} — allows nodes to be anchored to its edges.
     */
    ANCHOR_PANE(AnchorPane.class),

    /**
     * A {@link BorderPane} — arranges nodes in top, bottom, left, right, and center positions.
     */
    BORDER_PANE(BorderPane.class),

    /**
     * A {@link DialogPane} — the root pane displayed in a {@link Dialog} {@link Window}.
     */
    DIALOG_PANE(DialogPane.class),

    /**
     * A {@link FlowPane} — lays out its children in a flow that wraps at the container’s edge.
     */
    FLOW_PANE(FlowPane.class),

    /**
     * A {@link GridPane} — lays out its children in a flexible grid of rows and columns.
     */
    GRID_PANE(GridPane.class),

    /**
     * An {@link HBox} — arranges its children in a single horizontal row.
     */
    HBOX(HBox.class),

    /**
     * A {@link StackPane} — stacks all children on top of each other.
     */
    STACK_PANE(StackPane.class),

    /**
     * A {@link TilePane} — arranges its children in uniformly sized tiles.
     */
    TILE_PANE(TilePane.class),

    /**
     * A {@link VBox} — arranges its children in a single vertical column.
     */
    VBOX(VBox.class);

    /**
     * The actual JavaFX pane class this enum represents.
     */
    private final Class<? extends Pane> value;

    /**
     * Tries to match a class to one of the defined {@link FXPaneType} values.
     * This method supports subclass matching.
     *
     * @param cls the class to match
     * @return an {@link Optional} containing the matched {@link FXPaneType}, if any
     */
    @SuppressWarnings("unused")
    public static @NotNull Optional<FXPaneType> fromClass(final Class<?> cls) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().isAssignableFrom(cls))
                .findFirst();
    }

    /**
     * Creates a new instance of the pane represented by this enum value.
     *
     * @return a new instance of the corresponding {@link Pane} subclass
     * @throws RuntimeException if instantiation fails (e.g., no default constructor)
     */
    public @NotNull Pane newInstance() {
        try {
            return value.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate pane type: " + value.getSimpleName(), e);
        }
    }
}
