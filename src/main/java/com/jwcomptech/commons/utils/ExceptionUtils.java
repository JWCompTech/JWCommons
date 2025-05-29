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

import org.jetbrains.annotations.NotNull;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Contains methods for dealing with exceptions.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public final class ExceptionUtils {
    /**
     * Asserts that the specified runnable throws IllegalArgumentException
     * and if not throws a junit assertion failure.
     * @param runnable the runnable to verify
     */
    public static void assertThrownIllegalArgumentException(@NotNull final Runnable runnable) {
        try {
            runnable.run();
            fail( "This method should have thrown IllegalArgumentException!" );
        } catch (final IllegalArgumentException ignore) { }
    }

    /**
     * Asserts that the specified runnable throws IllegalStateException
     * and if not throws a junit assertion failure.
     * @param runnable the runnable to verify
     */
    public static void assertThrownIllegalStateException(@NotNull final Runnable runnable) {
        try {
            runnable.run();
            fail( "This method should have thrown IllegalStateException!" );
        } catch (final IllegalStateException ignore) { }
    }

    /**
     * Asserts that the specified runnable throws RuntimeException
     * and if not throws a junit assertion failure.
     * @param runnable the runnable to verify
     */
    public static void assertThrownRuntimeException(@NotNull final Runnable runnable) {
        try {
            runnable.run();
            fail( "This method should have thrown RuntimeException!" );
        } catch (final RuntimeException ignore) { }
    }

    /** Prevents instantiation of this utility class. */
    private ExceptionUtils() { }
}
