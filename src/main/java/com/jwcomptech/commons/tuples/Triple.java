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
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class Triple<L, M, R> implements Comparable<Triple<L, M, R>>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public static final Triple<?, ?, ?>[] EMPTY_ARRAY = new Triple[0];

    protected Triple() { }

    @SuppressWarnings("unchecked")
    public static <L, M, R> Triple<L, M, R>[] emptyArray() {
        return (Triple<L, M, R>[]) EMPTY_ARRAY;
    }

    public static <L, M, R> Triple<L, M, R> of(final L left, final M middle, final R right) {
        return ImmutableTriple.of(left, middle, right);
    }

    public static <L, M, R> Triple<L, M, R> ofNonNull(final L left, final M middle, final R right) {
        return ImmutableTriple.ofNonNull(left, middle, right);
    }

    public int compareTo(@NotNull final Triple<L, M, R> other) {
        return (new CompareToBuilder()).append(this.getLeft(), other.getLeft()).append(this.getMiddle(),
                other.getMiddle()).append(this.getRight(), other.getRight()).toComparison();
    }

    @SuppressWarnings("rawtypes")
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Triple)) {
            return false;
        } else {
            final Triple<?, ?, ?> other = (Triple)obj;
            return Objects.equals(this.getLeft(), other.getLeft()) && Objects.equals(this.getMiddle(),
                    other.getMiddle()) && Objects.equals(this.getRight(), other.getRight());
        }
    }

    public abstract L getLeft();

    public abstract M getMiddle();

    public abstract R getRight();

    public int hashCode() {
        return Objects.hashCode(this.getLeft())
                ^ Objects.hashCode(this.getMiddle())
                ^ Objects.hashCode(this.getRight());
    }

    public String toString() {
        return "(" + this.getLeft() + "," + this.getMiddle() + "," + this.getRight() + ")";
    }

    public String toString(final String format) {
        return String.format(format, this.getLeft(), this.getMiddle(), this.getRight());
    }
}
