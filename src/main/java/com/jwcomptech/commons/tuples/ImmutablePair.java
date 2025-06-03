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

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

@Value
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ImmutablePair<L, R> extends Pair<L, R> {
    public static final ImmutablePair<?, ?>[] EMPTY_ARRAY = new ImmutablePair[0];
    @SuppressWarnings("rawtypes")
    private static final ImmutablePair NULL = new ImmutablePair<>(null, null);
    @Serial
    private static final long serialVersionUID = -2715604964494254662L;

    L left;
    R right;

    public R setValue(final R value) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <L, R> ImmutablePair<L, R>[] emptyArray() {
        return (ImmutablePair<L, R>[]) EMPTY_ARRAY;
    }

    public static <L, R> Pair<L, R> left(final L left) {
        return of(left, null);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> ImmutablePair<L, R> nullPair() {
        return NULL;
    }

    public static <L, R> ImmutablePair<L, R> of(final L left, final R right) {
        return left == null && right == null ? nullPair() : new ImmutablePair<>(left, right);
    }

    public static <L, R> ImmutablePair<L, R> of(final Map.Entry<L, R> pair) {
        return pair != null ? new ImmutablePair<>(pair.getKey(), pair.getValue()) : nullPair();
    }

    public static <L, R> ImmutablePair<L, R> ofNonNull(final L left, final R right) {
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(right, "right"));
    }

    public static <L, R> Pair<L, R> right(final R right) {
        return of(null, right);
    }
}
