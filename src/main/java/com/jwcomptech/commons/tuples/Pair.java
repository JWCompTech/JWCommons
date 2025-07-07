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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.function.FailableBiConsumer;
import org.apache.commons.lang3.function.FailableBiFunction;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class Pair<L, R> implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {
    @Serial
    private static final long serialVersionUID = 4954918890077093841L;
    public static final Pair<?, ?>[] EMPTY_ARRAY = new Pair[0];

    protected Pair() { }

    @SuppressWarnings("unchecked")
    public static <L, R> Pair<L, R>[] emptyArray() {
        return (Pair<L, R>[]) EMPTY_ARRAY;
    }

    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return ImmutablePair.of(left, right);
    }

    public static <L, R> Pair<L, R> of(final Map.Entry<L, R> pair) {
        return ImmutablePair.of(pair);
    }

    public static <L, R> Pair<L, R> ofNonNull(final L left, final R right) {
        return ImmutablePair.ofNonNull(left, right);
    }

    public <E extends Throwable> void accept(@NotNull final FailableBiConsumer<L, R, E> consumer) throws E {
        consumer.accept(this.getKey(), this.getValue());
    }

    public <V, E extends Throwable> V apply(@NotNull final FailableBiFunction<L, R, V, E> function) throws E {
        return function.apply(this.getKey(), this.getValue());
    }

    public int compareTo(@NotNull final Pair<L, R> other) {
        return (new CompareToBuilder()).append(this.getLeft(), other.getLeft()).append(this.getRight(),
                other.getRight()).toComparison();
    }

    @SuppressWarnings("rawtypes")
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Map.Entry)) {
            return false;
        } else {
            final Map.Entry<?, ?> other = (Map.Entry)obj;
            return Objects.equals(this.getKey(), other.getKey()) && Objects.equals(this.getValue(), other.getValue());
        }
    }

    public final L getKey() {
        return this.getLeft();
    }

    public abstract L getLeft();

    public abstract R getRight();

    public R getValue() {
        return this.getRight();
    }

    public int hashCode() {
        return Objects.hashCode(this.getKey()) ^ Objects.hashCode(this.getValue());
    }

    public String toString() {
        return "(" + this.getLeft() + ',' + this.getRight() + ')';
    }

    public String toString(final String format) {
        return String.format(format, this.getLeft(), this.getRight());
    }
}
