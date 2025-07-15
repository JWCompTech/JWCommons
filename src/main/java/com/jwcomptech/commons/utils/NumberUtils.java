package com.jwcomptech.commons.utils;

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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

/**
 * Contains methods dealing with numbers.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
public final class NumberUtils {
    /**
     * Checks if the specified number is prime.
     * @param input number to check
     * @return true if specified number is prime
     */
    public static boolean isPrime(final long input) {
        // fast even test.
        if(input > 2 && (input & 1) == 0) return false;
        // only odd factors need to be tested up to input^0.5
        return IntStream.iterate(3, i -> (long) i * i <= input, i -> i + 2)
                .noneMatch(i -> input % i == 0);
    }

    /**
     * Checks if the specified number is even.
     * @param input number to check
     * @return true if the specified number is even
     */
    @Contract(pure = true)
    public static @NotNull Boolean isEven(final int input) { return (input & 1) == 0; }

    /**
     * Checks if the specified number is odd.
     * @param input number to check
     * @return true if the specified number is odd
     */
    @Contract(pure = true)
    public static @NotNull Boolean isOdd(final int input) { return (input & 1) != 0; }

    /**
     * Squares the specified number.
     * @param input number to edit
     * @return the number squared
     */
    public static double squared(final int input) { return StrictMath.pow(input, 2); }

    /**
     * Cubes the specified number.
     * @param input number to edit
     * @return the number cubed
     */
    public static double cubed(final int input) { return StrictMath.pow(input, 3); }

    /**
     * Returns the square root of the specified number.
     * @param input number to edit
     * @return the square root of the number
     */
    public static double squareRoot(final int input) { return Math.sqrt(input); }

    /**
     * Checks if the specified number is in range.
     * @param input the number to check
     * @param lower the lower bound
     * @param upper the upper bound
     * @param inclusive if true the upper bound is included
     * @return true if the specified number is in range
     */
    @Contract(pure = true)
    public static @NotNull Boolean isInRange(final int input, final int lower,
                                             final int upper, final boolean inclusive) {
        return lower <= input && (inclusive ? input <= upper : input < upper);
    }

    /** Prevents instantiation of this utility class. */
    private NumberUtils() { throwUnsupportedExForUtilityCls(); }
}
