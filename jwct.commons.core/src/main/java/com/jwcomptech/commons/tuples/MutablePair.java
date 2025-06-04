package com.jwcomptech.commons.tuples;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class MutablePair<L, R> extends Pair<L, R> {
    public static final MutablePair<?, ?>[] EMPTY_ARRAY = new MutablePair[0];
    @Serial
    private static final long serialVersionUID = 4954918890077093841L;
    private L left;
    private R right;

    @SuppressWarnings("unchecked")
    public static <L, R> MutablePair<L, R>[] emptyArray() {
        return (MutablePair<L, R>[]) EMPTY_ARRAY;
    }

    @Contract("_, _ -> new")
    public static <L, R> @NotNull MutablePair<L, R> of(final L left, final R right) {
        return new MutablePair<>(left, right);
    }

    @Contract("_ -> new")
    public static <L, R> @NotNull MutablePair<L, R> of(final Map.Entry<L, R> pair) {
        final L left;
        final R right;
        if (pair != null) {
            left = pair.getKey();
            right = pair.getValue();
        } else {
            left = null;
            right = null;
        }

        return new MutablePair<>(left, right);
    }

    @Contract("_, _ -> new")
    public static <L, R> @NotNull MutablePair<L, R> ofNonNull(final L left, final R right) {
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(right, "right"));
    }

    public R setValue(final R value) {
        final R result = this.right;
        this.right = value;
        return result;
    }
}
