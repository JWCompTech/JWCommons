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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.util.Objects;

@SuppressWarnings("unused")
@Value
@EqualsAndHashCode(callSuper=false)
public class ImmutableTriple<L, M, R> extends Triple<L, M, R> {
    public static final ImmutableTriple<?, ?, ?>[] EMPTY_ARRAY = new ImmutableTriple[0];
    @SuppressWarnings("rawtypes")
    private static final ImmutableTriple NULL = new ImmutableTriple<>(null, null, null);
    @Serial
    private static final long serialVersionUID = 8326051134216836762L;

    L left;
    M middle;
    R right;

    @SuppressWarnings("unchecked")
    public static <L, M, R> ImmutableTriple<L, M, R>[] emptyArray() {
        return (ImmutableTriple<L, M, R>[]) EMPTY_ARRAY;
    }

    @SuppressWarnings("unchecked")
    public static <L, M, R> ImmutableTriple<L, M, R> nullTriple() {
        return NULL;
    }

    public static <L, M, R> ImmutableTriple<L, M, R> of(L left, M middle, R right) {
        return !(null != left || null != middle) && null == right
                ? nullTriple()
                : new ImmutableTriple<>(left, middle, right);
    }

    public static <L, M, R> ImmutableTriple<L, M, R> ofNonNull(L left, M middle, R right) {
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(middle, "middle"),
                Objects.requireNonNull(right, "right"));
    }
}
