package com.jwcomptech.commons.javafx.controls.locations;

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

import javafx.geometry.Insets;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

@SuppressWarnings("unused")
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
