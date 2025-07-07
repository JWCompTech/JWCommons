package com.jwcomptech.commons.javafx.dialogs;

import com.jwcomptech.commons.annotations.FeatureComplete;
import com.jwcomptech.commons.javafx.JavaFXUtils;
import com.jwcomptech.commons.javafx.controls.FXPaneType;
import com.jwcomptech.commons.javafx.controls.locations.BorderPanePosition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.*;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A builder for creating and configuring a {@link DialogPane} using a selected JavaFX {@link Pane} as its content.
 * This builder supports multiple types of layout panes such as {@link GridPane}, {@link VBox}, {@link HBox},
 * {@link StackPane}, {@link FlowPane}, {@link TilePane}, {@link AnchorPane}, and {@link BorderPane}.
 *
 * @since 1.0.0-alpha
 */
@Data
@FeatureComplete(since = "1.0.0-alpha")
@SuppressWarnings("unused")
public final class DialogPaneBuilder {
    private final DialogPane dialogPane;
    private final FXPaneType mainPaneType;
    private final Pane mainPane;

    /**
     * Creates a new builder with the given dialog pane and content pane type.
     *
     * @param dialogPane the dialog pane to configure
     * @param type       the type of content pane to use
     */
    private DialogPaneBuilder(final DialogPane dialogPane, final FXPaneType type) {
        this.dialogPane = dialogPane;
        mainPaneType = type != null ? type : FXPaneType.PANE;
        mainPane = mainPaneType.newInstance();
    }

    /**
     * Static factory method for creating a new builder.
     *
     * @param dialogPane the dialog pane to configure
     * @param type       the content pane type to use
     * @return a new {@link DialogPaneBuilder}
     */
    @Contract("_, _ -> new")
    public static @NotNull DialogPaneBuilder create(final DialogPane dialogPane,
                                                    final FXPaneType type) {
        return new DialogPaneBuilder(dialogPane, type);
    }

    /**
     * Adds a node to a {@link GridPane} at the specified coordinates.
     *
     * @param node        the node to add
     * @param columnIndex the column index
     * @param rowIndex    the row index
     * @return this builder instance
     * @throws IllegalStateException if the main pane is not a {@link GridPane}
     */
    @SuppressWarnings("UnusedReturnValue")
    public DialogPaneBuilder addToGridPane(final Node node,
                                           final int columnIndex,
                                           final int rowIndex) {
        return withGrid(pane -> pane.add(node, columnIndex, rowIndex));
    }

    /**
     * Adds a node to a {@link GridPane} at the specified coordinates with span.
     *
     * @param node        the node to add
     * @param columnIndex the column index
     * @param rowIndex    the row index
     * @param colspan     the number of columns to span
     * @param rowspan     the number of rows to span
     * @return this builder instance
     * @throws IllegalStateException if the main pane is not a {@link GridPane}
     */
    public DialogPaneBuilder addToGridPane(final Node node,
                                           final int columnIndex,
                                           final int rowIndex,
                                           final int colspan,
                                           final int rowspan) {
        return withGrid(pane ->
                pane.add(node, columnIndex, rowIndex, colspan, rowspan));
    }

    /**
     * Adds nodes to a {@link VBox}.
     *
     * @param nodes the nodes to add
     * @return this builder instance
     * @throws IllegalStateException if the main pane is not a {@link VBox}
     */
    public DialogPaneBuilder addToVBox(Node... nodes) {
        return withVBox(pane -> pane.getChildren().addAll(nodes));
    }

    /**
     * Adds nodes to a {@link HBox}.
     *
     * @param nodes the nodes to add
     * @return this builder instance
     * @throws IllegalStateException if the main pane is not a {@link HBox}
     */
    public DialogPaneBuilder addToHBox(Node... nodes) {
        return withHBox(pane -> pane.getChildren().addAll(nodes));
    }

    /**
     * Adds nodes to a {@link FlowPane}.
     *
     * @param nodes the nodes to add
     * @return this builder instance
     * @throws IllegalStateException if the main pane is not a {@link FlowPane}
     */
    public DialogPaneBuilder addToFlowPane(final Node... nodes) {
        return withFlow(pane -> pane.getChildren().addAll(nodes));
    }

    /**
     * Adds nodes to a {@link StackPane}.
     *
     * @param nodes the nodes to add
     * @return this builder instance
     * @throws IllegalStateException if the main pane is not a {@link StackPane}
     */
    public DialogPaneBuilder addToStackPane(final Node... nodes) {
        return withStack(pane -> pane.getChildren().addAll(nodes));
    }

    /**
     * Adds nodes to a {@link TilePane}.
     *
     * @param nodes the nodes to add
     * @return this builder instance
     * @throws IllegalStateException if the main pane is not a {@link TilePane}
     */
    public DialogPaneBuilder addToTilePane(final Node... nodes) {
        return withTile(pane -> pane.getChildren().addAll(nodes));
    }

    /**
     * Adds a node to an {@link AnchorPane} and sets its anchors.
     * Pass -1 to skip an anchor side.
     *
     * @param node   the node to anchor and add
     * @param top the distance from the top edge, or -1 to ignore
     * @param right the distance from the right edge, or -1 to ignore
     * @param bottom the distance from the bottom edge, or -1 to ignore
     * @param left the distance from the left edge, or -1 to ignore
     * @return this builder instance
     * @throws IllegalStateException if the main pane is not an {@link AnchorPane}
     */
    public DialogPaneBuilder addToAnchorPane(final Node node,
                                             final double top,
                                             final double right,
                                             final double bottom,
                                             final double left) {
        return withAnchor(pane -> {
            JavaFXUtils.setAnchors(node, top, right, bottom, left);
            pane.getChildren().add(node);
        });
    }

    /**
     * Adds a node to the given position of a {@link BorderPane}.
     *
     * @param node     the node to place
     * @param position the {@link BorderPanePosition} to place the node in
     * @return this builder instance
     * @throws IllegalStateException if the main pane is not a {@link BorderPane}
     */
    public DialogPaneBuilder addToBorderPane(final Node node,
                                             final BorderPanePosition position) {
        return withBorder(pane -> {
            switch (position) {
                case TOP -> pane.setTop(node);
                case RIGHT -> pane.setRight(node);
                case BOTTOM -> pane.setBottom(node);
                case LEFT -> pane.setLeft(node);
                case CENTER -> pane.setCenter(node);
            }
        });
    }

    /**
     * Applies a custom operation on the internal pane.
     *
     * @param consumer a {@link PaneConsumer} using the generic base {@link Pane} type
     * @return this builder instance
     */
    @Contract("_ -> this")
    public DialogPaneBuilder with(final @NotNull PaneConsumer<Pane> consumer) {
        consumer.accept(mainPane);
        return this;
    }

    /**
     * Applies a custom operation on the internal pane.
     *
     * @param consumer a {@link PaneConsumer} using the {@link GridPane} type
     * @return this builder instance
     */
    public DialogPaneBuilder withGrid(final @NotNull PaneConsumer<GridPane> consumer) {
        if (mainPane instanceof GridPane grid) {
            consumer.accept(grid);
        } else {
            throw new IllegalStateException("Selected pane is not a GridPane");
        }
        return this;
    }

    /**
     * Applies a custom operation on the internal pane.
     *
     * @param consumer a {@link PaneConsumer} using the {@link HBox} type
     * @return this builder instance
     */
    public DialogPaneBuilder withHBox(final @NotNull PaneConsumer<HBox> consumer) {
        if (mainPane instanceof HBox grid) {
            consumer.accept(grid);
        } else {
            throw new IllegalStateException("Selected pane is not a HBox");
        }
        return this;
    }

    /**
     * Applies a custom operation on the internal pane.
     *
     * @param consumer a {@link PaneConsumer} using the {@link VBox} type
     * @return this builder instance
     */
    public DialogPaneBuilder withVBox(final @NotNull PaneConsumer<VBox> consumer) {
        if (mainPane instanceof VBox grid) {
            consumer.accept(grid);
        } else {
            throw new IllegalStateException("Selected pane is not a VBox");
        }
        return this;
    }

    /**
     * Applies a custom operation on the internal pane.
     *
     * @param consumer a {@link PaneConsumer} using the {@link StackPane} type
     * @return this builder instance
     */
    public DialogPaneBuilder withStack(final @NotNull PaneConsumer<StackPane> consumer) {
        if (mainPane instanceof StackPane pane) consumer.accept(pane);
        else throw new IllegalStateException("Selected pane is not a StackPane");
        return this;
    }

    /**
     * Applies a custom operation on the internal pane.
     *
     * @param consumer a {@link PaneConsumer} using the {@link FlowPane} type
     * @return this builder instance
     */
    public DialogPaneBuilder withFlow(final @NotNull PaneConsumer<FlowPane> consumer) {
        if (mainPane instanceof FlowPane pane) consumer.accept(pane);
        else throw new IllegalStateException("Selected pane is not a FlowPane");
        return this;
    }

    /**
     * Applies a custom operation on the internal pane.
     *
     * @param consumer a {@link PaneConsumer} using the {@link AnchorPane} type
     * @return this builder instance
     */
    public DialogPaneBuilder withAnchor(final @NotNull PaneConsumer<AnchorPane> consumer) {
        if (mainPane instanceof AnchorPane pane) consumer.accept(pane);
        else throw new IllegalStateException("Selected pane is not an AnchorPane");
        return this;
    }

    /**
     * Applies a custom operation on the internal pane.
     *
     * @param consumer a {@link PaneConsumer} using the {@link TilePane} type
     * @return this builder instance
     */
    public DialogPaneBuilder withTile(final @NotNull PaneConsumer<TilePane> consumer) {
        if (mainPane instanceof TilePane pane) consumer.accept(pane);
        else throw new IllegalStateException("Selected pane is not a TilePane");
        return this;
    }

    /**
     * Applies a custom operation on the internal pane.
     *
     * @param consumer a {@link PaneConsumer} using the {@link BorderPane} type
     * @return this builder instance
     */
    public DialogPaneBuilder withBorder(final @NotNull PaneConsumer<BorderPane> consumer) {
        if (mainPane instanceof BorderPane pane) consumer.accept(pane);
        else throw new IllegalStateException("Selected pane is not a BorderPane");
        return this;
    }

    /**
     * Sets the padding of the internal pane.
     *
     * @param insets the padding to set
     * @return this builder instance
     */
    public DialogPaneBuilder setPadding(final Insets insets) {
        mainPane.setPadding(insets);
        return this;
    }

    /**
     * Sets spacing for supported pane types such as {@link VBox} and {@link HBox}.
     *
     * @param spacing the spacing between children
     * @return this builder instance
     */
    public DialogPaneBuilder setSpacing(final double spacing) {
        if (mainPane instanceof VBox vbox) vbox.setSpacing(spacing);
        if (mainPane instanceof HBox hbox) hbox.setSpacing(spacing);
        return this;
    }

    /**
     * Finalizes the builder and returns the configured {@link DialogPane}.
     *
     * @return the resulting dialog pane
     */
    @SuppressWarnings("UnusedReturnValue")
    public DialogPane buildAndUpdate() {
        dialogPane.setContent(mainPane);
        return dialogPane;
    }

    /**
     * A generic consumer for pane types.
     *
     * @param <T> a specific subclass of {@link Pane}
     */
    @FunctionalInterface
    public interface PaneConsumer<T extends Pane> {
        void accept(T pane);
    }
}
