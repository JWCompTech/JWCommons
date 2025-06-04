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

import com.jwcomptech.commons.functions.Function1;
import com.jwcomptech.commons.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.jwcomptech.commons.consts.Literals.*;
import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;
import static com.jwcomptech.commons.utils.StringUtils.*;

/**
 * Allows different conditions to be checked at runtime.
 */
@SuppressWarnings("unused")
public final class Preconditions {
    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalArgumentException if {@code expression} is false
     */
    @SuppressWarnings("BooleanParameter")
    public static void checkArgument(final boolean expression)
            throws IllegalArgumentException {
        if(!expression) {
            throw new IllegalArgumentException("Invalid argument specified!");
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessage the message to use for the exception if thrown
     * @throws IllegalArgumentException if {@code expression} is false
     * or if {@code errorMessage} is null or empty
     */
    @SuppressWarnings("BooleanParameter")
    public static void checkArgument(final boolean expression,
                                     final Object errorMessage)
            throws IllegalArgumentException {
        if(errorMessage == null || isBlank(String.valueOf(errorMessage))) {
            throw new IllegalArgumentException(cannotBeNullOrEmpty("errorMessage"));
        }
        if(!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param exceptionSupplier the exception supplier to specify the exception to throw
     * @throws RuntimeException the supplied exception if {@code expression} is false
     * @throws IllegalArgumentException if {@code expression} is false
     * or if {@code exceptionSupplier} is null
     */
    @SuppressWarnings("BooleanParameter")
    public static void checkArgument(final boolean expression,
                                     final Supplier<? extends RuntimeException> exceptionSupplier) {
        if(exceptionSupplier == null) throw new IllegalArgumentException(cannotBeNull("exceptionSupplier"));
        
        if (!expression) throw exceptionSupplier.get();
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param <T> the input type of the function
     * @param <R> the return type of the function
     * @param reference the object to evaluate
     * @param conditionFunction a function that checks a condition against the reference
     * @param exceptionSupplier the exception supplier to specify the exception to throw
     *                          if the condition function returns false
     * @param transformFunction the function to use to transform {@code reference}
     * @throws RuntimeException the supplied exception if {@code conditionFunction} returns false
     * @return the transformed reference if the condition function returns true
     * @throws IllegalArgumentException if any of the arguments are null
     */
    @SuppressWarnings("BooleanParameter")
    public static <T, R> R checkArgumentAndTransform(final T reference,
                                                     final @NotNull Function1<T, Boolean> conditionFunction,
                                                     final Supplier<? extends RuntimeException> exceptionSupplier,
                                                     final @NotNull Function1<T, R> transformFunction) {
        checkArgumentNotNull(reference, cannotBeNull("reference"));
        checkArgumentNotNull(conditionFunction, cannotBeNull("conditionFunction"));
        checkArgumentNotNull(exceptionSupplier, cannotBeNull("exceptionSupplier"));
        checkArgumentNotNull(transformFunction, cannotBeNull("transformFunction"));
        
        if (!conditionFunction.apply(reference)) throw exceptionSupplier.get();
        else return transformFunction.apply(reference);
    }

    /**
     * Ensures that {@code reference} is non-null,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     *
     * @param <T> the type of the reference
     * @param reference the object to evaluate
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @return {@code reference}, guaranteed to be non-null, for convenience
     * @throws IllegalArgumentException if {@code reference} is null
     * or if {@code errorMessage} is null or empty
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <T> T checkArgumentNotNull(final T reference,
                                             final Object errorMessage) {
        checkErrorMessageNotNullOrBlank(errorMessage);
        checkArgument(reference != null,
                () -> new IllegalArgumentException(String.valueOf(errorMessage)));
        return reference;
    }

    /**
     * Ensures that {@code reference} is non-null,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * If no exception is thrown, {@code reference} is then passed to the specified consumer.
     *
     * @param <T> the type of the reference
     * @param reference the object to evaluate
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @param runnable the consumer to run, supplying {@code reference}, guaranteed to be non-null, for convenience
     * @throws IllegalArgumentException if {@code reference} or {@code runnable} are null
     * or if {@code errorMessage} is null or empty
     */
    public static <T> void checkArgumentNotNullAndRun(final T reference,
                                                      final Object errorMessage,
                                                      final @NotNull Consumer<T> runnable)
            throws IllegalArgumentException {
        checkErrorMessageNotNullOrBlank(errorMessage);
        checkArgumentNotNull(runnable, cannotBeNull("runnable"));
        checkArgument(reference != null,
                () -> new IllegalArgumentException(String.valueOf(errorMessage)));
        runnable.accept(reference);
    }

    /**
     * Ensures that {@code reference} is non-null,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * If no exception is thrown, {@code reference} is then passed to the specified function.
     *
     * @param <T> the type of the reference
     * @param <R> the return type of the function
     * @param reference the object to evaluate
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @param function the function to use to transform {@code reference}
     * @return the result of the specified function
     * @throws IllegalArgumentException if {@code reference} or {@code function} is null
     * or if {@code errorMessage} is null or empty
     */
    public static <T, R> R checkArgumentNotNullAndTransform(final T reference,
                                                      final Object errorMessage,
                                                      final @NotNull Function1<T, R> function)
            throws IllegalArgumentException {
        checkErrorMessageNotNullOrBlank(errorMessage);
        checkArgumentNotNull(function, cannotBeNull("function"));
        checkArgument(reference != null,
                () -> new IllegalArgumentException(String.valueOf(errorMessage)));
        return function.apply(reference);
    }

    /**
     * Ensures that {@code reference} is non-null and non-empty,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     *
     * @apiNote This method calls {@link StringUtils#isNotBlank(CharSequence)} on the specified reference.
     *
     * @param reference the object to evaluate
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code reference} or {@code errorMessage} are null or empty
     */
    @SuppressWarnings("UnusedReturnValue")
    public static void checkArgumentNotNullOrEmpty(final CharSequence reference,
                                                     final Object errorMessage)
            throws IllegalArgumentException {
        checkErrorMessageNotNullOrBlank(errorMessage);

        checkArgument(isNotBlank(reference),
                () -> new IllegalArgumentException(String.valueOf(errorMessage)));
    }

    /**
     * Ensures that {@code reference} is non-null and non-empty,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * If no exception is thrown, {@code reference} is then passed to the specified consumer.
     *
     * @apiNote This method calls {@link StringUtils#isNotBlank(CharSequence)} on the specified reference.
     *
     * @param reference the object to evaluate
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @param consumer the consumer to run, supplying {@code reference}, guaranteed to be non-null, for convenience
     * @throws IllegalArgumentException if {@code reference} or {@code errorMessage} are null or empty
     * or if {@code consumer} is null
     */
    public static void checkArgumentNotNullOrEmptyAndRun(final CharSequence reference,
                                                           final Object errorMessage,
                                                           final @NotNull Consumer<CharSequence> consumer)
            throws IllegalArgumentException {
        checkErrorMessageNotNullOrBlank(errorMessage);
        checkArgumentNotNull(consumer, cannotBeNull("consumer"));
        checkArgument(isNotBlank(reference),
                () -> new IllegalArgumentException(String.valueOf(errorMessage)));
        consumer.accept(reference);
    }

    /**
     * Ensures that {@code reference} is non-null and non-empty,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * If no exception is thrown, {@code reference} is then passed to the specified function.
     *
     * @apiNote This method calls {@link StringUtils#isNotBlank(CharSequence)} on the specified reference.
     *
     * @param <R> the return type of the function
     * @param reference    the object to evaluate
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @param function     the function to use to transform {@code reference}
     * @return the result of the specified function
     * @throws IllegalArgumentException if {@code reference} or {@code errorMessage} are null or empty
     * or if {@code function} is null
     */
    public static <R> R checkArgumentNotNullOrEmptyAndTransform(final CharSequence reference,
                                                            final Object errorMessage,
                                                            final @NotNull Function1<CharSequence, R> function)
            throws IllegalArgumentException {
        checkArgumentNotNull(reference, cannotBeNull("reference"));
        checkErrorMessageNotNullOrBlank(errorMessage);
        checkArgumentNotNull(function, cannotBeNull("function"));

        checkArgument(isNotBlank(reference),
                () -> new IllegalArgumentException(String.valueOf(errorMessage)));
        return function.apply(reference);
    }

    /** Ensures that {@code expression} is true,
     * throwing an {@code IllegalStateException} with a custom message otherwise.
     * @param expression the expression to evaluate
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code expression} is false
     * @throws IllegalArgumentException if {@code errorMessage} is null or empty
     */
    public static void checkState(final boolean expression, final Object errorMessage) {
        checkArgumentNotNull(expression, cannotBeNull("expression"));
        checkErrorMessageNotNullOrBlank(errorMessage);

        checkArgument(!expression,
                () -> new IllegalStateException(String.valueOf(errorMessage)));
    }

    /**
     * Ensures that {@code reference} falls between the specified range
     * and may equal the min or max values,
     * throwing an {@code IllegalStateException} with a custom message otherwise.
     * @param reference the number to evaluate
     * @param min the minimum value the reference can be
     * @param max the maximum value the reference can be
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code reference} is not within specified range
     * @throws IllegalArgumentException if any of the arguments are null
     * or if {@code errorMessage} is null or empty
     */
    public static void checkArgumentInRangeInclusive(final Number reference,
                                            final Number min,
                                            final Number max,
                                            final Object errorMessage) {
        checkArgumentNotNull(reference, cannotBeNull("reference"));
        checkArgumentNotNull(min, cannotBeNull("min"));
        checkArgumentNotNull(max, cannotBeNull("max"));
        checkErrorMessageNotNullOrBlank(errorMessage);


    }

    /**
     * Ensures that {@code reference} falls between the specified range,
     * but does not equal the min or max values,
     * throwing an {@code IllegalStateException} with a custom message otherwise.
     * @param reference the number to evaluate
     * @param min the minimum value the reference can be
     * @param max the maximum value the reference can be
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code reference} is not within specified range
     * @throws IllegalArgumentException if any of the arguments are null
     * or if {@code errorMessage} is null or empty
     */
    public static void checkArgumentInRangeExclusive(final Number reference,
                                                     final Number min,
                                                     final Number max,
                                                     final Object errorMessage) {
        checkArgumentNotNull(reference, cannotBeNull("reference"));
        checkArgumentNotNull(min, cannotBeNull("min"));
        checkArgumentNotNull(max, cannotBeNull("max"));
        checkErrorMessageNotNullOrBlank(errorMessage);

    }

    /**
     * Ensures that {@code collection} is empty,
     * throwing an {@code IllegalStateException} with a custom message otherwise.
     * @param collection the collection to evaluate
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code collection} is null
     * @throws IllegalStateException if {@code collection} is empty
     */
    public static void checkCollectionIsEmpty(final Collection<?> collection,
                                                 final Object errorMessage) {
        checkArgumentNotNull(collection, cannotBeNull("collection"));
        checkErrorMessageNotNullOrBlank(errorMessage);

        checkArgument(collection.isEmpty(),
                () -> new IllegalStateException("Collection must be be empty!"));
    }

    /**
     * Ensures that {@code collection} is NOT empty,
     * throwing an {@code IllegalStateException} with a custom message otherwise.
     * @param collection the collection to evaluate
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code collection} is null
     * @throws IllegalStateException if {@code collection} is NOT empty
     */
    public static void checkCollectionIsNotEmpty(final Collection<?> collection,
                                                 final Object errorMessage) {
        checkArgumentNotNull(collection, cannotBeNull("collection"));
        checkErrorMessageNotNullOrBlank(errorMessage);

        checkArgument(!collection.isEmpty(),
                () -> new IllegalStateException("Collection cannot be empty!"));
    }

    /**
     * Ensures that {@code errorMessage} is not null or, when converted to a
     * string using {@link String#valueOf(Object)}, is not blank.
     *
     * @param errorMessage the error message object to evaluate
     * @throws IllegalArgumentException if {@code errorMessage} is null
     * or converts to a blank string
     */
    public static void checkErrorMessageNotNullOrBlank(final Object errorMessage) {
        checkArgument(errorMessage != null && isNotBlank(String.valueOf(errorMessage)),
                () -> new IllegalArgumentException(cannotBeNull("errorMessage")));
    }

    /**
     * Checks if a string can be converted to a Boolean.
     *
     * @apiNote The following strings are considered true boolean values:
     *          "true", "t", "yes", "y", "1", "succeeded", "succeed", "enabled", "on".
     *          The following strings are considered false boolean values:
     *          "false", "f", "no", "n", "0", "-1", "failed", "fail", "disabled", "off".
     *
     * @param input string to check
     * @return true if string matches a boolean, false if string does not match or is null
     */
    public static boolean isBoolean(final String input) {
        return isBoolean(input, Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     * Checks if a string can be converted to a Boolean.
     *
     * @apiNote The following strings are considered true boolean values:
     *          "true", "t", "yes", "y", "1", "succeeded", "succeed", "enabled", "on".
     *          The following strings are considered false boolean values:
     *          "false", "f", "no", "n", "0", "-1", "failed", "fail", "disabled", "off".
     *
     * @param input string to check
     * @param locale the locale to use
     * @return true if string matches a boolean, false if string does not match or is null
     * @throws IllegalArgumentException if locale is null
     */
    public static boolean isBoolean(final String input, final Locale locale) {
        if(input == null) return false;
        checkArgumentNotNull(locale, LOCALE_CANNOT_BE_NULL);
        final String value = strip(input.toLowerCase(locale));
        return switch (value) {
            case "true", "t", "yes", "y", "1", "succeeded", "succeed", "enabled", "false", "f", "no", "n", "0", "-1",
                 "failed", "fail", "disabled" -> true;
            default -> false;
        };
    }

    /** Prevents instantiation of this utility class. */
    private Preconditions() { throwUnsupportedExForUtilityCls(); }
}
