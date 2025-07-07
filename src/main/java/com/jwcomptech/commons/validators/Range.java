package com.jwcomptech.commons.validators;

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

import lombok.NonNull;
import lombok.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

@Value
public class Range<T extends Comparable<T>> implements Serializable {
    @NonNull T lowerBound;
    @NonNull T upperBound;
    boolean lowerInclusive;
    boolean upperInclusive;

    @Serial
    private static final long serialVersionUID = 7689560007927336082L;

    static <T extends Comparable<T>> @NotNull Range<T> create(final T lowerBound,
                                                              final T upperBound,
                                                              final boolean lowerInclusive,
                                                              final boolean upperInclusive) {
        checkArgumentNotNull(lowerBound, cannotBeNull("lowerBound"));
        checkArgumentNotNull(upperBound, cannotBeNull("upperBound"));

        if (lowerBound.compareTo(upperBound) > 0) {
            throw new IllegalArgumentException("Lower bound cannot be greater than upper bound.");
        }

        return new Range<>(lowerBound, upperBound, lowerInclusive, upperInclusive);
    }

    @Contract("_, _ -> new")
    public static <T extends Comparable<T>> @NotNull Range<T> open(final T lower, final T upper) {
        return create(lower, upper, false, false); // exclusive
    }

    @Contract("_, _ -> new")
    public static <T extends Comparable<T>> @NotNull Range<T> closed(final T lower, final T upper) {
        return create(lower, upper, true, true); // inclusive
    }

    @Contract("_, _ -> new")
    public static <T extends Comparable<T>> @NotNull Range<T> openClosed(final T lower,
                                                                   final T upper) {
        return create(lower, upper, false, true);
    }

    @Contract("_, _ -> new")
    public static <T extends Comparable<T>> @NotNull Range<T> closedOpen(final T lower,
                                                                   final T upper) {
        return create(lower, upper, true, false);
    }

    public boolean contains(final T value) {
        checkArgumentNotNull(value, cannotBeNull("value"));

        final boolean lowerCheck;
        final boolean upperCheck;

        if (lowerInclusive) {
            lowerCheck = lowerBound.compareTo(value) <= 0;
        } else {
            lowerCheck = lowerBound.compareTo(value) < 0;
        }

        if (upperInclusive) {
            upperCheck = upperBound.compareTo(value) >= 0;
        } else {
            upperCheck = upperBound.compareTo(value) > 0;
        }

        return lowerCheck && upperCheck;
    }

    public boolean containsAll(@NotNull final Iterable<? extends T> values) {
        for (final T value : values) {
            if (!contains(value)) {
                return false; // immediately fail
            }
        }
        return true;
    }

    public boolean containsNone(@NotNull final Iterable<? extends T> values) {
        for (final T value : values) {
            if (contains(value)) {
                return false; // one match means not "none"
            }
        }
        return true;
    }
}
