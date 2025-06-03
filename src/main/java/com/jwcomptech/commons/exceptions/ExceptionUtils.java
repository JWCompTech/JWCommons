package com.jwcomptech.commons.exceptions;

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

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * Contains methods for dealing with exceptions.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public final class ExceptionUtils {
    /**
     * Asserts that the specified runnable throws IllegalArgumentException
     * and if not throws an {@link AssertionError}. This is suggested to only
     * be used in unit tests.
     *
     * @param runnable the runnable to verify
     */
    public static void assertThrownIllegalArgumentException(@NotNull final Runnable runnable) {
        assertThrownException(runnable, IllegalArgumentException.class);
    }

    /**
     * Asserts that the specified runnable throws IllegalStateException
     * and if not throws an {@link AssertionError}. This is suggested to only
     * be used in unit tests.
     *
     * @param runnable the runnable to verify
     */
    public static void assertThrownIllegalStateException(@NotNull final Runnable runnable) {
        assertThrownException(runnable, IllegalStateException.class);
    }

    /**
     * Asserts that the specified runnable throws RuntimeException
     * and if not throws an {@link AssertionError}. This is suggested to only
     * be used in unit tests.
     *
     * @param runnable the runnable to verify
     */
    public static void assertThrownRuntimeException(@NotNull final Runnable runnable) {
        assertThrownException(runnable, RuntimeException.class);
    }

    /**
     * Asserts that the specified runnable throws NumberFormatException
     * and if not throws an {@link AssertionError}. This is suggested to only
     * be used in unit tests.
     *
     * @param runnable the runnable to verify
     */
    public static void assertThrownNumberFormatException(@NotNull final Runnable runnable) {
        assertThrownException(runnable, NumberFormatException.class);
    }

    /**
     * Asserts that the specified runnable throws UnsupportedOperationException
     * and if not throws an {@link AssertionError}. This is suggested to only
     * be used in unit tests.
     *
     * @param runnable the runnable to verify
     */
    public static void assertThrownUnsupportedOperationException(@NotNull final Runnable runnable) {
        assertThrownException(runnable, UnsupportedOperationException.class);
    }

    /**
     * Asserts that the specified runnable throws the specified throwable
     * and if not throws an {@link AssertionError}. This is suggested to only
     * be used in unit tests.
     *
     * @param runnable the runnable to verify
     * @param expectedType the class of the throwable type
     * @param <T> the throwable type
     * @throws AssertionError if no throwable or a different throwable
     * was thrown during execution of the runnable
     */
    @SuppressWarnings("OverlyBroadCatchBlock")
    public static <T extends Throwable> void assertThrownException(@NotNull final Runnable runnable,
                                                                   final Class<T> expectedType) {
        try {
            runnable.run();
            throw new AssertionError("Expected %s to be thrown, but nothing was thrown."
                    .formatted(expectedType.getSimpleName()));
        } catch (final Throwable e) {
            if(!expectedType.isAssignableFrom(e.getClass())) {
                throw new AssertionError("Expected %s to be thrown, but %s was thrown instead."
                        .formatted(expectedType.getName(), e.getClass().getName()), e);
            }
        }
    }

    /**
     * Throws an UnsupportedOperationException for use in utility class
     * constructors with the message
     * "This is a utility class and cannot be instantiated!".
     *
     * @throws UnsupportedOperationException every time method is run
     */
    public static void throwUnsupportedExForUtilityCls() {
        throw new java.lang.UnsupportedOperationException(
                "This is a utility class and cannot be instantiated!");
    }

    /**
     * Claims a Throwable is another Throwable type using type erasure. This
     * hides a checked exception from the Java compiler, allowing a checked
     * exception to be thrown without having the exception in the method's throw
     * clause.
     *
     * @param <T> the type of the throwable to be thrown
     * @param throwable the throwable to throw
     * @throws T if specified throwable is not null
     * @throws IllegalArgumentException if specified throwable is null
     */
    @Contract(value = "_ -> fail", pure = true)
    public static <T extends Throwable> void throwUnchecked(final @NotNull T throwable) throws T {
        checkArgumentNotNull(throwable, cannotBeNull("throwable"));
        throw throwable;
    }

    /**
     * Checks if a throwable represents a checked exception
     *
     * @param throwable The throwable to check.
     * @return True if the given Throwable is a checked exception.
     */
    public static boolean isChecked(final Throwable throwable) {
        return throwable != null && !(throwable instanceof Error) && !(throwable instanceof RuntimeException);
    }

    /**
     * Checks if a throwable represents an unchecked exception
     *
     * @param throwable The throwable to check.
     * @return True if the given Throwable is an unchecked exception.
     */
    public static boolean isUnchecked(final Throwable throwable) {
        return throwable instanceof Error || throwable instanceof RuntimeException;
    }

    /** Prevents instantiation of this utility class. */
    private ExceptionUtils() { throwUnsupportedExForUtilityCls(); }
}
