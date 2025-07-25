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

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

/**
 * This class assists in validating arguments. The validation methods are
 * based along the following principles:
 * <ul>
 *   <li>An invalid {@code null} argument causes a {@link NullPointerException}.</li>
 *   <li>A non-{@code null} argument causes an {@link IllegalArgumentException}.</li>
 *   <li>An invalid index into an array/collection/map/string causes an {@link IndexOutOfBoundsException}.</li>
 * </ul>
 *
 * <p>All exceptions messages are
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#syntax">format strings</a>
 * as defined by the Java platform. For example:
 *
 * <pre>
 * Validate.isTrue(i &gt; 0, "The value must be greater than zero: %d", i);
 * Validate.notNull(surname, "The surname must not be %s", null);
 * </pre>
 *
 * <p>#ThreadSafe#</p>
 *
 * @see String#format(String, Object...)
 * @since 1.0.0-alpha
 */
@SuppressWarnings({"ClassWithTooManyMethods", "OverlyComplexClass", "unused"})
public final class Validate {

    private static final String DEFAULT_NOT_NAN_EX_MESSAGE =
            "The validated value is not a number";
    private static final String DEFAULT_FINITE_EX_MESSAGE =
            "The value is invalid: %s";
    private static final String DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE =
            "The value %s is not in the specified exclusive range of %s to %s";
    private static final String DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE =
            "The value %s is not in the specified inclusive range of %s to %s";
    private static final String DEFAULT_MATCHES_PATTERN_EX = "The string %s does not match the pattern %s";
    private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
    private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";
    private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE =
            "The validated array contains null element";
    private static final String DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE =
            "The validated collection contains null element";
    private static final String DEFAULT_NOT_BLANK_EX_MESSAGE = "The validated character sequence is blank";
    private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
    private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE =
            "The validated character sequence is empty";
    private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
    private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
    private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE =
            "The validated character sequence index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE =
            "The validated collection index is invalid: %d";
    private static final String DEFAULT_VALID_STATE_EX_MESSAGE = "The validated state is false";
    private static final String DEFAULT_IS_ASSIGNABLE_EX_MESSAGE = "Cannot assign a %s to a %s";
    private static final String DEFAULT_IS_INSTANCE_OF_EX_MESSAGE = "Expected type: %s, actual: %s";

    /**
     * Validate that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception.
     *
     * <pre>Validate.exclusiveBetween(0.1, 2.1, 1.1);</pre>
     *
     * @param start the exclusive start value
     * @param end   the exclusive end value
     * @param value the value to validate
     * @throws IllegalArgumentException if the value falls out of the boundaries
     * @since 1.0.0-alpha
     */
    public static void exclusiveBetween(final double start,
                                        final double end,
                                        final double value) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE.formatted(
                    String.valueOf(value), String.valueOf(start), String.valueOf(end)));
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.exclusiveBetween(0.1, 2.1, 1.1, "Not in range");</pre>
     *
     * @param start the exclusive start value
     * @param end   the exclusive end value
     * @param value the value to validate
     * @param message the exception message if invalid, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @since 1.0.0-alpha
     */
    public static void exclusiveBetween(final double start,
                                        final double end,
                                        final double value,
                                        final String message) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception.
     *
     * <pre>Validate.exclusiveBetween(0, 2, 1);</pre>
     *
     * @param start the exclusive start value
     * @param end   the exclusive end value
     * @param value the value to validate
     * @throws IllegalArgumentException if the value falls out of the boundaries
     * @since 1.0.0-alpha
     */
    public static void exclusiveBetween(final long start,
                                        final long end,
                                        final long value) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE.formatted(
                    String.valueOf(value), String.valueOf(start), String.valueOf(end)));
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.exclusiveBetween(0, 2, 1, "Not in range");</pre>
     *
     * @param start the exclusive start value
     * @param end   the exclusive end value
     * @param value the value to validate
     * @param message the exception message if invalid, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @since 1.0.0-alpha
     */
    public static void exclusiveBetween(final long start,
                                        final long end,
                                        final long value,
                                        final String message) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validate that the specified argument object fall between the two
     * exclusive values specified; otherwise, throws an exception.
     *
     * <pre>Validate.exclusiveBetween(0, 2, 1);</pre>
     *
     * @param <T> the type of the argument object
     * @param start  the exclusive start value, not null
     * @param end  the exclusive end value, not null
     * @param value  the object to validate, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @see #exclusiveBetween(Object, Object, Comparable, String, Object...)
     * @since 1.0.0-alpha
     */
    public static <T> void exclusiveBetween(final T start, final T end, final @NotNull Comparable<T> value) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE.formatted(
                    String.valueOf(value), String.valueOf(start), String.valueOf(end)));
        }
    }

    /**
     * Validate that the specified argument object fall between the two
     * exclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.exclusiveBetween(0, 2, 1, "Not in boundaries");</pre>
     *
     * @param <T> the type of the argument object
     * @param start  the exclusive start value, not null
     * @param end  the exclusive end value, not null
     * @param value  the object to validate, not null
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @see #exclusiveBetween(Object, Object, Comparable)
     * @since 1.0.0-alpha
     */
    @FormatMethod
    public static <T> void exclusiveBetween(final T start, final T end, final @NotNull Comparable<T> value,
                                            @FormatString final String message, final Object... values) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
    }

    /**
     * Validates that the specified argument is not infinite or Not-a-Number (NaN);
     * otherwise throwing an exception.
     *
     * <pre>Validate.finite(myDouble);</pre>
     *
     * <p>The message of the exception is &quot;The value is invalid: %f&quot;.</p>
     *
     * @param value  the value to validate
     * @throws IllegalArgumentException if the value is infinite or Not-a-Number (NaN)
     * @see #ensureFinite(double, String, Object...)
     * @since 1.0.0-alpha
     */
    public static void ensureFinite(final double value) {
        ensureFinite(value, DEFAULT_FINITE_EX_MESSAGE, String.valueOf(value));
    }

    /**
     * Validates that the specified argument is not infinite or Not-a-Number (NaN);
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.finite(myDouble, "The argument must contain a numeric value");</pre>
     *
     * @param value the value to validate
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message
     * @throws IllegalArgumentException if the value is infinite or Not-a-Number (NaN)
     * @see #ensureFinite(double)
     * @since 1.0.0-alpha
     */
    @FormatMethod
    public static void ensureFinite(final double value, @FormatString final String message, final Object... values) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
    }

    /**
     * Gets the message using {@link String#format(String, Object...) String.format(message, values)}
     * if the values are not empty, otherwise return the message unformatted.
     * This method exists to allow validation methods declaring a String message and varargs parameters
     * to be used without any message parameters when the message contains special characters,
     * e.g. {@code Validate.isTrue(false, "%Failed%")}.
     *
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values the optional values for the formatted message
     * @return formatted message using {@link String#format(String, Object...) String.format(message, values)}
     * if the values are not empty, otherwise return the unformatted message.
     * @since 1.0.0-alpha
     */
    @FormatMethod
    private static String getMessage(@FormatString final String message, final Object... values) {
        return ArrayUtils.isEmpty(values) ? message : String.format(message, values);
    }

    /**
     * Validate that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception.
     *
     * <pre>Validate.inclusiveBetween(0.1, 2.1, 1.1);</pre>
     *
     * @param start the inclusive start value
     * @param end   the inclusive end value
     * @param value the value to validate
     * @throws IllegalArgumentException if the value falls outside the boundaries (inclusive)
     * @since 1.0.0-alpha
     */
    public static void inclusiveBetween(final double start, final double end, final double value) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE.formatted(
                    String.valueOf(value), String.valueOf(start), String.valueOf(end)));
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.inclusiveBetween(0.1, 2.1, 1.1, "Not in range");</pre>
     *
     * @param start the inclusive start value
     * @param end   the inclusive end value
     * @param value the value to validate
     * @param message the exception message if invalid, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @since 1.0.0-alpha
     */
    public static void inclusiveBetween(final double start, final double end, final double value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception.
     *
     * <pre>Validate.inclusiveBetween(0, 2, 1);</pre>
     *
     * @param start the inclusive start value
     * @param end   the inclusive end value
     * @param value the value to validate
     * @throws IllegalArgumentException if the value falls outside the boundaries (inclusive)
     * @since 1.0.0-alpha
     */
    public static void inclusiveBetween(final long start, final long end, final long value) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE.formatted(
                    String.valueOf(value), String.valueOf(start), String.valueOf(end)));
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.inclusiveBetween(0, 2, 1, "Not in range");</pre>
     *
     * @param start the inclusive start value
     * @param end   the inclusive end value
     * @param value the value to validate
     * @param message the exception message if invalid, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @since 1.0.0-alpha
     */
    public static void inclusiveBetween(final long start, final long end, final long value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validate that the specified argument object fall between the two
     * inclusive values specified; otherwise, throws an exception.
     *
     * <pre>Validate.inclusiveBetween(0, 2, 1);</pre>
     *
     * @param <T> the type of the argument object
     * @param start  the inclusive start value, not null
     * @param end  the inclusive end value, not null
     * @param value  the object to validate, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @see #inclusiveBetween(Object, Object, Comparable, String, Object...)
     * @since 1.0.0-alpha
     */
    public static <T> void inclusiveBetween(final T start, final T end, final @NotNull Comparable<T> value) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE.formatted(
                    String.valueOf(value), String.valueOf(start), String.valueOf(end)));
        }
    }

    /**
     * Validate that the specified argument object fall between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.inclusiveBetween(0, 2, 1, "Not in boundaries");</pre>
     *
     * @param <T> the type of the argument object
     * @param start  the inclusive start value, not null
     * @param end  the inclusive end value, not null
     * @param value  the object to validate, not null
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @see #inclusiveBetween(Object, Object, Comparable)
     * @since 0.0.1
     */
    @FormatMethod
    public static <T> void inclusiveBetween(final T start,
                                            final T end,
                                            final @NotNull Comparable<T> value,
                                            @FormatString final String message,
                                            final Object... values) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
    }

    /**
     * Validates that the argument can be converted to the specified class, if not, throws an exception.
     *
     * <p>This method is useful when validating that there will be no casting errors.</p>
     *
     * <pre>Validate.isAssignableFrom(SuperClass.class, object.getClass());</pre>
     *
     * <p>The message format of the exception is &quot;Cannot assign {type} to {superType}&quot;</p>
     *
     * @param superType  the class must be validated against, not null
     * @param type  the class to check, not null
     * @throws IllegalArgumentException if type argument is not assignable to the specified superType
     * @see #ensureAssignableFrom(Class, Class, String, Object...)
     * @since 0.0.1
     */
    public static void ensureAssignableFrom(final Class<?> superType, final Class<?> type) {
        // TODO when breaking BC, consider returning type
        if (type == null || superType == null || !superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(DEFAULT_IS_ASSIGNABLE_EX_MESSAGE.formatted(
                            ClassUtils.getName(type, "null type"),
                            ClassUtils.getName(superType, "null type")));
        }
    }

    /**
     * Validates that the argument can be converted to the specified class, if not throws an exception.
     *
     * <p>This method is useful when validating if there will be no casting errors.</p>
     *
     * <pre>Validate.isAssignableFrom(SuperClass.class, object.getClass());</pre>
     *
     * <p>The message of the exception is &quot;The validated object can not be converted to the&quot;
     * followed by the name of the class and &quot;class&quot;</p>
     *
     * @param superType  the class must be validated against, not null
     * @param type  the class to check, not null
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if argument can not be converted to the specified class
     * @see #ensureAssignableFrom(Class, Class)
     * @since 0.0.1
     */
    @FormatMethod
    public static void ensureAssignableFrom(final @NotNull Class<?> superType,
                                            final Class<?> type,
                                            @FormatString final String message,
                                            final Object... values) {
        // TODO when breaking BC, consider returning type
        if (!superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
    }

    /**
     * Validates that the argument is an instance of the specified class, if not throws an exception.
     *
     * <p>This method is useful when validating according to an arbitrary class</p>
     *
     * <pre>Validate.isInstanceOf(OkClass.class, object);</pre>
     *
     * <p>The message of the exception is &quot;Expected type: {type}, actual: {obj_type}&quot;</p>
     *
     * @param type  the class the object must be validated against, not null
     * @param obj  the object to check, null throws an exception
     * @throws IllegalArgumentException if argument is not of specified class
     * @see #ensureInstanceOf(Class, Object, String, Object...)
     * @since 0.0.1
     */
    public static void ensureInstanceOf(final @NotNull Class<?> type, final Object obj) {
        // TODO when breaking BC, consider returning obj
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(DEFAULT_IS_INSTANCE_OF_EX_MESSAGE.formatted(
                    type.getName(), ClassUtils.getName(obj, "null")));
        }
    }

    /**
     * Validate that the argument is an instance of the specified class; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary class
     *
     * <pre>Validate.isInstanceOf(OkClass.class, object, "Wrong class, object is of class %s",
     *   object.getClass().getName());</pre>
     *
     * @param type  the class the object must be validated against, not null
     * @param obj  the object to check, null throws an exception
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if argument is not of specified class
     * @see #ensureInstanceOf(Class, Object)
     * @since 0.0.1
     */
    @FormatMethod
    public static void ensureInstanceOf(final @NotNull Class<?> type,
                                        final Object obj,
                                        @FormatString final String message,
                                        final Object... values) {
        // TODO when breaking BC, consider returning obj
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
    }

    /**
     * Validate that the argument condition is {@code true}; otherwise
     * throwing an exception. This method is useful when validating according
     * to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.
     *
     * <pre>
     * Validate.isTrue(i &gt; 0);
     * Validate.isTrue(myObject.isOk());</pre>
     *
     * <p>The message of the exception is &quot;The validated expression is
     * false&quot;.</p>
     *
     * @param expression  the boolean expression to check
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #ensureTrue(boolean, String, long)
     * @see #ensureTrue(boolean, String, double)
     * @see #ensureTrue(boolean, String, Object...)
     * @since 0.0.1
     */
    public static void ensureTrue(final boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException(DEFAULT_IS_TRUE_EX_MESSAGE);
        }
    }

    /**
     * Validate that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.
     *
     * <pre>Validate.isTrue(d &gt; 0.0, "The value must be greater than zero: &#37;s", d);</pre>
     *
     * <p>For performance reasons, the double value is passed as a separate parameter and
     * appended to the exception message only in the case of an error.</p>
     *
     * @param expression  the boolean expression to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param value  the value to append to the message when invalid
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #ensureTrue(boolean)
     * @see #ensureTrue(boolean, String, long)
     * @see #ensureTrue(boolean, String, Object...)
     * @since 0.0.1
     */
    public static void ensureTrue(final boolean expression, final String message, final double value) {
        if (!expression) {
            throw new IllegalArgumentException(message.formatted(
                    String.valueOf(Double.valueOf(value))));
        }
    }

    /**
     * Validate that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.
     *
     * <pre>Validate.isTrue(i &gt; 0.0, "The value must be greater than zero: &#37;d", i);</pre>
     *
     * <p>For performance reasons, the long value is passed as a separate parameter and
     * appended to the exception message only in the case of an error.</p>
     *
     * @param expression  the boolean expression to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param value  the value to append to the message when invalid
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #ensureTrue(boolean)
     * @see #ensureTrue(boolean, String, double)
     * @see #ensureTrue(boolean, String, Object...)
     * @since 0.0.1
     */
    public static void ensureTrue(final boolean expression, final String message, final long value) {
        if (!expression) {
            throw new IllegalArgumentException(message.formatted(value));
        }
    }

    /**
     * Validate that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.
     *
     * <pre>{@code
     * Validate.isTrue(i >= min &amp;&amp; i <= max, "The value must be between %d and %d", min, max);}</pre>
     *
     * @param expression  the boolean expression to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #ensureTrue(boolean)
     * @see #ensureTrue(boolean, String, long)
     * @see #ensureTrue(boolean, String, double)
     * @since 0.0.1
     */
    @FormatMethod
    public static void ensureTrue(final boolean expression,
                                  @FormatString final String message,
                                  final Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
    }

    /**
     * Validate that the specified argument character sequence matches the specified regular
     * expression pattern; otherwise throwing an exception.
     *
     * <pre>Validate.matchesPattern("hi", "[a-z]*");</pre>
     *
     * <p>The syntax of the pattern is the one used in the {@link Pattern} class.</p>
     *
     * @param input  the character sequence to validate, not null
     * @param pattern  the regular expression pattern, not null
     * @throws IllegalArgumentException if the character sequence does not match the pattern
     * @see #ensureMatchesPattern(CharSequence, String, String, Object...)
     * @since 0.0.1
     */
    public static void ensureMatchesPattern(final CharSequence input, final String pattern) {
        // TODO when breaking BC, consider returning input
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(DEFAULT_MATCHES_PATTERN_EX.formatted(input, pattern));
        }
    }

    /**
     * Validate that the specified argument character sequence matches the specified regular
     * expression pattern; otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.matchesPattern("hi", "[a-z]*", "%s does not match %s", "hi" "[a-z]*");</pre>
     *
     * <p>The syntax of the pattern is the one used in the {@link Pattern} class.</p>
     *
     * @param input  the character sequence to validate, not null
     * @param pattern  the regular expression pattern, not null
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if the character sequence does not match the pattern
     * @see #ensureMatchesPattern(CharSequence, String)
     * @since 0.0.1
     */
    @FormatMethod
    public static void ensureMatchesPattern(final CharSequence input,
                                            final String pattern,
                                            @FormatString final String message,
                                            final Object... values) {
        // TODO when breaking BC, consider returning input
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
    }

    /**
     * Validate that the specified argument iterable is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception.
     *
     * <pre>Validate.noNullElements(myCollection);</pre>
     *
     * <p>If the iterable is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.
     *
     * <p>If the array has a {@code null} element, then the message in the
     * exception is &quot;The validated iterable contains null element at index:
     * &quot; followed by the index.</p>
     *
     * @param <T> the iterable type
     * @param iterable  the iterable to check, validated not null by this method
     * @return the validated iterable (never {@code null} method for chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IllegalArgumentException if an element is {@code null}
     * @see #ensureNoNullElements(Iterable, String, Object...)
     * @since 0.0.1
     */
    public static <T extends Iterable<?>> T ensureNoNullElements(final T iterable) {
        return ensureNoNullElements(iterable, DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE);
    }

    /**
     * Validate that the specified argument iterable is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.noNullElements(myCollection, "The collection contains null at position %d");</pre>
     *
     * <p>If the iterable is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.
     *
     * <p>If the iterable has a {@code null} element, then the iteration
     * index of the invalid element is appended to the {@code values}
     * argument.</p>
     *
     * @param <T> the iterable type
     * @param iterable  the iterable to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated iterable (never {@code null} method for chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IllegalArgumentException if an element is {@code null}
     * @see #ensureNoNullElements(Iterable)
     * @since 0.0.1
     */
    @FormatMethod
    public static <T extends Iterable<?>> T ensureNoNullElements(final T iterable,
                                                                 @FormatString final String message,
                                                                 final Object... values) {
        Objects.requireNonNull(iterable, "iterable");
        int i = 0;
        for (final Iterator<?> it = iterable.iterator(); it.hasNext(); i++) {
            if (it.next() == null) {
                final Object[] values2 = ArrayUtils.addAll(values, i);
                throw new IllegalArgumentException(getMessage(message, values2));
            }
        }
        return iterable;
    }

    /**
     * Validate that the specified argument array is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception.
     *
     * <pre>Validate.noNullElements(myArray);</pre>
     *
     * <p>If the array is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * <p>If the array has a {@code null} element, then the message in the
     * exception is &quot;The validated array contains null element at index:
     * &quot; followed by the index.</p>
     *
     * @param <T> the array type
     * @param array  the array to check, validated not null by this method
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IllegalArgumentException if an element is {@code null}
     * @see #ensureNoNullElements(Object[], String, Object...)
     * @since 0.0.1
     */
    public static <T> T[] ensureNoNullElements(final T[] array) {
        return ensureNoNullElements(array, DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE);
    }

    /**
     * Validate that the specified argument array is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.noNullElements(myArray, "The array contain null at position %d");</pre>
     *
     * <p>If the array is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.
     *
     * <p>If the array has a {@code null} element, then the iteration
     * index of the invalid element is appended to the {@code values}
     * argument.</p>
     *
     * @param <T> the array type
     * @param array  the array to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IllegalArgumentException if an element is {@code null}
     * @see #ensureNoNullElements(Object[])
     * @since 0.0.1
     */
    @FormatMethod
    public static <T> T[] ensureNoNullElements(final T[] array,
                                               @FormatString final String message,
                                               final Object... values) {
        Objects.requireNonNull(array, "array");
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                final Object[] values2 = ArrayUtils.add(values, i);
                throw new IllegalArgumentException(getMessage(message, values2));
            }
        }
        return array;
    }

    /**
     * <p>Validate that the specified argument character sequence is
     * neither {@code null}, a length of zero (no characters), empty
     * nor whitespace; otherwise throwing an exception.
     *
     * <pre>Validate.notBlank(myString);</pre>
     *
     * <p>The message in the exception is &quot;The validated character
     * sequence is blank&quot;.
     *
     * @param <T> the character sequence type
     * @param chars  the character sequence to check, validated not null by this method
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is blank
     * @see #ensureNotBlank(CharSequence, String, Object...)
     * @since 0.0.1
     */
    public static <T extends CharSequence> T ensureNotBlank(final T chars) {
        return ensureNotBlank(chars, DEFAULT_NOT_BLANK_EX_MESSAGE);
    }

    /**
     * Validate that the specified argument character sequence is
     * neither {@code null}, a length of zero (no characters), empty
     * nor whitespace; otherwise throwing an exception with the specified
     * message.
     *
     * <pre>Validate.notBlank(myString, "The string must not be blank");</pre>
     *
     * @param <T> the character sequence type
     * @param chars  the character sequence to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is blank
     * @see #ensureNotBlank(CharSequence)
     * @since 0.0.1
     */
    @FormatMethod
    public static <T extends CharSequence> T ensureNotBlank(final T chars,
                                                            @FormatString final String message,
                                                            final Object... values) {
        Objects.requireNonNull(chars, toSupplier(message, values));
        if (StringUtils.isBlank(chars)) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
        return chars;
    }

    /**
     * <p>Validate that the specified argument collection is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception.
     *
     * <pre>Validate.notEmpty(myCollection);</pre>
     *
     * <p>The message in the exception is &quot;The validated collection is
     * empty&quot;.
     *
     * @param <T> the collection type
     * @param collection  the collection to check, validated not null by this method
     * @return the validated collection (never {@code null} method for chaining)
     * @throws NullPointerException if the collection is {@code null}
     * @throws IllegalArgumentException if the collection is empty
     * @see #ensureNotEmpty(Collection, String, Object...)
     * @since 0.0.1
     */
    public static <T extends Collection<?>> @NotNull T ensureNotEmpty(final T collection) {
        return ensureNotEmpty(collection, DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE);
    }

    /**
     * <p>Validate that the specified argument map is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception.
     *
     * <pre>Validate.notEmpty(myMap);</pre>
     *
     * <p>The message in the exception is &quot;The validated map is
     * empty&quot;.
     *
     * @param <T> the map type
     * @param map  the map to check, validated not null by this method
     * @return the validated map (never {@code null} method for chaining)
     * @throws NullPointerException if the map is {@code null}
     * @throws IllegalArgumentException if the map is empty
     * @see #ensureNotEmpty(Map, String, Object...)
     * @since 0.0.1
     */
    public static <T extends Map<?, ?>> @NotNull T ensureNotEmpty(final T map) {
        return ensureNotEmpty(map, DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE);
    }

    /**
     * <p>Validate that the specified argument character sequence is
     * neither {@code null} nor a length of zero (no characters);
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.notEmpty(myString);</pre>
     *
     * <p>The message in the exception is &quot;The validated
     * character sequence is empty&quot;.
     *
     * @param <T> the character sequence type
     * @param chars  the character sequence to check, validated not null by this method
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is empty
     * @see #ensureNotEmpty(CharSequence, String, Object...)
     * @since 0.0.1
     */
    public static <T extends CharSequence> @NotNull T ensureNotEmpty(final T chars) {
        return ensureNotEmpty(chars, DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE);
    }

    /**
     * <p>Validate that the specified argument collection is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception
     * with the specified message.
     *
     * <pre>Validate.notEmpty(myCollection, "The collection must not be empty");</pre>
     *
     * @param <T> the collection type
     * @param collection  the collection to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated collection (never {@code null} method for chaining)
     * @throws NullPointerException if the collection is {@code null}
     * @throws IllegalArgumentException if the collection is empty
     * @see #ensureNotEmpty(Object[])
     * @since 0.0.1
     */
    @Contract("_, _, _ -> param1")
    @FormatMethod
    public static <T extends Collection<?>> @NotNull T ensureNotEmpty(final T collection,
                                                                      @FormatString final String message,
                                                                      final Object... values) {
        Objects.requireNonNull(collection, toSupplier(message, values));
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
        return collection;
    }

    /**
     * Validate that the specified argument map is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception
     * with the specified message.
     *
     * <pre>Validate.notEmpty(myMap, "The map must not be empty");</pre>
     *
     * @param <T> the map type
     * @param map  the map to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated map (never {@code null} method for chaining)
     * @throws NullPointerException if the map is {@code null}
     * @throws IllegalArgumentException if the map is empty
     * @see #ensureNotEmpty(Object[])
     * @since 0.0.1
     */
    @Contract("_, _, _ -> param1")
    @FormatMethod
    public static <T extends Map<?, ?>> @NotNull T ensureNotEmpty(final T map,
                                                                  @FormatString final String message,
                                                                  final Object... values) {
        Objects.requireNonNull(map, toSupplier(message, values));
        if (map.isEmpty()) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
        return map;
    }

    /**
     * Validate that the specified argument character sequence is
     * neither {@code null} nor a length of zero (no characters);
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.notEmpty(myString, "The string must not be empty");</pre>
     *
     * @param <T> the character sequence type
     * @param chars  the character sequence to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is empty
     * @see #ensureNotEmpty(CharSequence)
     * @since 0.0.1
     */
    @Contract("_, _, _ -> param1")
    @FormatMethod
    public static <T extends CharSequence> @NotNull T ensureNotEmpty(final T chars,
                                                                     @FormatString final String message,
                                                                     final Object... values) {
        Objects.requireNonNull(chars, toSupplier(message, values));
        if (chars.isEmpty()) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
        return chars;
    }

    /**
     * <p>Validate that the specified argument array is neither {@code null}
     * nor a length of zero (no elements); otherwise throwing an exception.
     *
     * <pre>Validate.notEmpty(myArray);</pre>
     *
     * <p>The message in the exception is &quot;The validated array is
     * empty&quot;.
     *
     * @param <T> the array type
     * @param array  the array to check, validated not null by this method
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IllegalArgumentException if the array is empty
     * @see #ensureNotEmpty(Object[], String, Object...)
     * @since 0.0.1
     */
    public static <T> T @NotNull [] ensureNotEmpty(final T[] array) {
        return ensureNotEmpty(array, DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE);
    }

    /**
     * <p>Validate that the specified argument array is neither {@code null}
     * nor a length of zero (no elements); otherwise throwing an exception
     * with the specified message.
     *
     * <pre>Validate.notEmpty(myArray, "The array must not be empty");</pre>
     *
     * @param <T> the array type
     * @param array  the array to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IllegalArgumentException if the array is empty
     * @see #ensureNotEmpty(Object[])
     * @since 0.0.1
     */
    @Contract("_, _, _ -> param1")
    @FormatMethod
    public static <T> T @NotNull [] ensureNotEmpty(final T[] array,
                                                   @FormatString final String message,
                                                   final Object... values) {
        Objects.requireNonNull(array, toSupplier(message, values));
        if (array.length == 0) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
        return array;
    }

    /**
     * Validates that the specified argument is not Not-a-Number (NaN); otherwise
     * throwing an exception.
     *
     * <pre>Validate.notNaN(myDouble);</pre>
     *
     * <p>The message of the exception is &quot;The validated value is not a
     * number&quot;.</p>
     *
     * @param value  the value to validate
     * @throws IllegalArgumentException if the value is not a number
     * @see #ensureNotNaN(double, String, Object...)
     * @since 0.0.1
     */
    public static void ensureNotNaN(final double value) {
        ensureNotNaN(value, DEFAULT_NOT_NAN_EX_MESSAGE);
    }

    /**
     * Validates that the specified argument is not Not-a-Number (NaN); otherwise
     * throwing an exception with the specified message.
     *
     * <pre>Validate.notNaN(myDouble, "The value must be a number");</pre>
     *
     * @param value  the value to validate
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message
     * @throws IllegalArgumentException if the value is not a number
     * @see #ensureNotNaN(double)
     * @since 0.0.1
     */
    @FormatMethod
    public static void ensureNotNaN(final double value,
                                    @FormatString final String message,
                                    final Object... values) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
    }

    /**
     * Validate that the specified argument is not {@code null};
     * otherwise throwing an exception.
     *
     * <pre>Validate.notNull(myObject, "The object must not be null");</pre>
     *
     * <p>The message of the exception is &quot;The validated object is
     * null&quot;.
     *
     * @param <T> the object type
     * @param object  the object to check
     * @return the validated object (never {@code null} for method chaining)
     * @throws NullPointerException if the object is {@code null}
     * @see #ensureNotNull(Object, String, Object...)
     * @deprecated Use {@link Objects#requireNonNull(Object)}.
     * @since 0.0.1
     */
    @Deprecated
    public static <T> T ensureNotNull(final T object) {
        return ensureNotNull(object, DEFAULT_IS_NULL_EX_MESSAGE);
    }

    /**
     * Validate that the specified argument is not {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.notNull(myObject, "The object must not be null");</pre>
     *
     * @param <T> the object type
     * @param object  the object to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message
     * @return the validated object (never {@code null} for method chaining)
     * @throws NullPointerException if the object is {@code null}
     * @see Objects#requireNonNull(Object)
     * @since 0.0.1
     */
    @FormatMethod
    public static <T> T ensureNotNull(final T object,
                                      @FormatString final String message,
                                      final Object... values) {
        return Objects.requireNonNull(object, toSupplier(message, values));
    }

    @Contract(pure = true)
    @FormatMethod
    private static @NotNull Supplier<String> toSupplier(@FormatString final String message,
                                                        final Object... values) {
        return () -> getMessage(message, values);
    }

    /**
     * Validates that the index is within the bounds of the argument
     * collection; otherwise throwing an exception.
     *
     * <pre>Validate.validIndex(myCollection, 2);</pre>
     *
     * <p>If the index is invalid, then the message of the exception
     * is &quot;The validated collection index is invalid: &quot;
     * followed by the index.</p>
     *
     * @param <T> the collection type
     * @param collection  the collection to check, validated not null by this method
     * @param index  the index to check
     * @return the validated collection (never {@code null} for method chaining)
     * @throws NullPointerException if the collection is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #ensureValidIndex(Collection, int, String, Object...)
     * @since 0.0.1
     */
    public static <T extends Collection<?>> T ensureValidIndex(final T collection, final int index) {
        return ensureValidIndex(collection, index, DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE, index);
    }

    /**
     * Validates that the index is within the bounds of the argument
     * character sequence; otherwise throwing an exception.
     *
     * <pre>Validate.validIndex(myStr, 2);</pre>
     *
     * <p>If the character sequence is {@code null}, then the message
     * of the exception is &quot;The validated object is
     * null&quot;.</p>
     *
     * <p>If the index is invalid, then the message of the exception
     * is &quot;The validated character sequence index is invalid: &quot;
     * followed by the index.</p>
     *
     * @param <T> the character sequence type
     * @param chars  the character sequence to check, validated not null by this method
     * @param index  the index to check
     * @return the validated character sequence (never {@code null} for method chaining)
     * @throws NullPointerException if the character sequence is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #ensureValidIndex(CharSequence, int, String, Object...)
     * @since 0.0.1
     */
    public static <T extends CharSequence> T ensureValidIndex(final T chars, final int index) {
        return ensureValidIndex(chars, index, DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE, index);
    }

    /**
     * Validates that the index is within the bounds of the argument
     * collection; otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.validIndex(myCollection, 2, "The collection index is invalid: ");</pre>
     *
     * <p>If the collection is {@code null}, then the message of the
     * exception is &quot;The validated object is null&quot;.</p>
     *
     * @param <T> the collection type
     * @param collection  the collection to check, validated not null by this method
     * @param index  the index to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated collection (never {@code null} for chaining)
     * @throws NullPointerException if the collection is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #ensureValidIndex(Collection, int)
     * @since 0.0.1
     */
    @FormatMethod
    public static <T extends Collection<?>> T ensureValidIndex(final T collection,
                                                               final int index,
                                                               @FormatString final String message,
                                                               final Object... values) {
        Objects.requireNonNull(collection, "collection");
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException(getMessage(message, values));
        }
        return collection;
    }

    /**
     * Validates that the index is within the bounds of the argument
     * character sequence; otherwise throwing an exception with the
     * specified message.
     *
     * <pre>Validate.validIndex(myStr, 2, "The string index is invalid: ");</pre>
     *
     * <p>If the character sequence is {@code null}, then the message
     * of the exception is &quot;The validated object is null&quot;.</p>
     *
     * @param <T> the character sequence type
     * @param chars  the character sequence to check, validated not null by this method
     * @param index  the index to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} for method chaining)
     * @throws NullPointerException if the character sequence is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #ensureValidIndex(CharSequence, int)
     * @since 0.0.1
     */
    @FormatMethod
    public static <T extends CharSequence> T ensureValidIndex(final T chars,
                                                              final int index,
                                                              @FormatString final String message,
                                                              final Object... values) {
        Objects.requireNonNull(chars, "chars");
        if (index < 0 || index >= chars.length()) {
            throw new IndexOutOfBoundsException(getMessage(message, values));
        }
        return chars;
    }

    /**
     * Validates that the index is within the bounds of the argument
     * array; otherwise throwing an exception.
     *
     * <pre>Validate.validIndex(myArray, 2);</pre>
     *
     * <p>If the array is {@code null}, then the message of the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * <p>If the index is invalid, then the message of the exception is
     * &quot;The validated array index is invalid: &quot; followed by the
     * index.</p>
     *
     * @param <T> the array type
     * @param array  the array to check, validated not null by this method
     * @param index  the index to check
     * @return the validated array (never {@code null} for method chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #ensureValidIndex(Object[], int, String, Object...)
     * @since 0.0.1
     */
    public static <T> T[] ensureValidIndex(final T[] array, final int index) {
        return ensureValidIndex(array, index, DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE, index);
    }

    /**
     * Validates that the index is within the bounds of the argument
     * array; otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.validIndex(myArray, 2, "The array index is invalid: ");</pre>
     *
     * <p>If the array is {@code null}, then the message of the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * @param <T> the array type
     * @param array  the array to check, validated not null by this method
     * @param index  the index to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated array (never {@code null} for method chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #ensureValidIndex(Object[], int)
     * @since 0.0.1
     */
    @FormatMethod
    public static <T> T[] ensureValidIndex(final T[] array,
                                           final int index,
                                           @FormatString final String message,
                                           final Object... values) {
        Objects.requireNonNull(array, "array");
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(getMessage(message, values));
        }
        return array;
    }

    /**
     * Validate that the stateful condition is {@code true}; otherwise
     * throwing an exception. This method is useful when validating according
     * to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.
     *
     * <pre>
     * Validate.validState(field &gt; 0);
     * Validate.validState(this.isOk());</pre>
     *
     * <p>The message of the exception is &quot;The validated state is
     * false&quot;.</p>
     *
     * @param expression  the boolean expression to check
     * @throws IllegalStateException if expression is {@code false}
     * @see #ensureValidState(boolean, String, Object...)
     * @since 0.0.1
     */
    public static void ensureValidState(final boolean expression) {
        if (!expression) {
            throw new IllegalStateException(DEFAULT_VALID_STATE_EX_MESSAGE);
        }
    }

    /**
     * Validate that the stateful condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.
     *
     * <pre>Validate.validState(this.isOk(), "The state is not OK: %s", myObject);</pre>
     *
     * @param expression  the boolean expression to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalStateException if expression is {@code false}
     * @see #ensureValidState(boolean)
     * @since 0.0.1
     */
    @FormatMethod
    public static void ensureValidState(final boolean expression,
                                        @FormatString final String message,
                                        final Object... values) {
        if (!expression) {
            throw new IllegalStateException(getMessage(message, values));
        }
    }

    /** Prevents instantiation of this utility class. */
    private Validate() { throwUnsupportedExForUtilityCls(); }
}
