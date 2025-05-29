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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Objects;

@SuppressWarnings("unused")
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MutableTriple<L, M, R> extends Triple<L, M, R> {
    public static final MutableTriple<?, ?, ?>[] EMPTY_ARRAY = new MutableTriple[0];
    @Serial
    private static final long serialVersionUID = 3036724394104122597L;

    public L left;
    public M middle;
    public R right;

    @SuppressWarnings("unchecked")
    public static <L, M, R> MutableTriple<L, M, R>[] emptyArray() {
        return (MutableTriple<L, M, R>[]) EMPTY_ARRAY;
    }

    @Contract("_, _, _ -> new")
    public static <L, M, R> @NotNull MutableTriple<L, M, R> of(L left, M middle, R right) {
        return new MutableTriple<>(left, middle, right);
    }

    @Contract("_, _, _ -> new")
    public static <L, M, R> @NotNull MutableTriple<L, M, R> ofNonNull(L left, M middle, R right) {
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(middle, "middle"),
                Objects.requireNonNull(right, "right"));
    }
}
