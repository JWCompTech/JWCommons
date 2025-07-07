package com.jwcomptech.commons.javafx.controls.locations;

import javafx.geometry.Insets;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

@Value
@EqualsAndHashCode(callSuper = true)
public class AnchorPaneNodeLocation extends NodeLocation<Double> {
    public AnchorPaneNodeLocation(final double top,
                                  final double right,
                                  final double bottom,
                                  final double left) {
        put("top", top);
        put("right", right);
        put("bottom", bottom);
        put("left", left);
    }

    public AnchorPaneNodeLocation(final @NotNull Insets insets) {
        checkArgumentNotNull(insets, cannotBeNull("insets"));
        put("top", insets.getTop());
        put("right", insets.getRight());
        put("bottom", insets.getBottom());
        put("left", insets.getLeft());
    }

    public Optional<Double> getTop() {
        return Optional.ofNullable(get("top"));
    }

    public Optional<Double> getRight() {
        return Optional.ofNullable(get("right"));
    }

    public Optional<Double> getBottom() {
        return Optional.ofNullable(get("bottom"));
    }

    public Optional<Double> getLeft() {
        return Optional.ofNullable(get("left"));
    }
}
