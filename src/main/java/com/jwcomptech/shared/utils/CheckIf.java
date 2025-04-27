package com.jwcomptech.shared.utils;

import com.jwcomptech.shared.functions.Function1;

import java.util.Locale;
import java.util.function.Consumer;

import static com.jwcomptech.shared.Literals.LOCALE_CANNOT_BE_NULL;
import static com.jwcomptech.shared.utils.StringUtils.strip;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("unused")
public final class CheckIf {
    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     * @param expression a boolean expression
     * @throws IllegalArgumentException if {@code expression} is false
     */
    @SuppressWarnings("BooleanParameter")
    public static void checkArgument(final boolean expression)
            throws IllegalArgumentException {
        if (!expression) throw new IllegalArgumentException("Invalid argument specified!");
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     * @param expression a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code expression} is false
     */
    @SuppressWarnings("BooleanParameter")
    public static void checkArgument(final boolean expression, final Object errorMessage)
            throws IllegalArgumentException {
        if (!expression) throw new IllegalArgumentException(String.valueOf(errorMessage));
    }

    /**
     * Ensures that {@code reference} is non-null,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * @param <T> the type of the reference
     * @param reference the object to verify
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @return {@code reference}, guaranteed to be non-null, for convenience
     * @throws IllegalArgumentException if {@code reference} is {@code null}
     */
    public static <T> T checkArgumentNotNull(final T reference, final Object errorMessage)
            throws IllegalArgumentException {
        checkArgument(null != reference, errorMessage);
        return reference;
    }

    /**
     * Ensures that {@code reference} is non-null,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * If no exception is thrown, {@code reference} is then passed to the specified supplier.
     * @param <T> the type of the reference
     * @param reference the object to verify
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @param runnable the consumer to run, supplying {@code reference}, guaranteed to be non-null, for convenience
     * @throws IllegalArgumentException if {@code reference} is {@code null}
     */
    public static <T> void checkArgumentNotNullAndRun(final T reference,
                                                      final Object errorMessage,
                                                      final Consumer<T> runnable)
            throws IllegalArgumentException {
        checkArgument(null != reference, errorMessage);
        runnable.accept(reference);
    }

    /**
     * Ensures that {@code reference} is non-null,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * If no exception is thrown, {@code reference} is then passed to the specified supplier.
     * @param <T> the type of the reference
     * @param reference the object to verify
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @param function the supplier to run, supplying {@code reference}, guaranteed to be non-null, for convenience
     * @return the result of the specified function
     * @throws IllegalArgumentException if {@code reference} is {@code null}
     */
    public static <T, R> R checkArgumentNotNullAndTransform(final T reference,
                                                      final Object errorMessage,
                                                      final Function1<T, R> function)
            throws IllegalArgumentException {
        checkArgument(null != reference, errorMessage);
        return function.apply(reference);
    }

    /**
     * Ensures that {@code reference} is non-null and non-empty,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * @apiNote This method calls {@link org.apache.commons.lang3.StringUtils#isNotBlank(CharSequence)} on the specified reference.
     * @param reference the object to verify
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @return {@code reference}, guaranteed to be non-null and non-empty, for convenience
     * @throws IllegalArgumentException if {@code reference} is {@code null} or empty
     */
    public static String checkArgumentNotNullOrEmpty(final String reference, final Object errorMessage)
            throws IllegalArgumentException {
        checkArgument(isNotBlank(reference), errorMessage);
        return reference;
    }

    /**
     * Ensures that {@code reference} is non-null and non-empty,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * If no exception is thrown, {@code reference} is then passed to the specified supplier.
     * @apiNote This method calls {@link org.apache.commons.lang3.StringUtils#isNotBlank(CharSequence)} on the specified reference.
     * @param reference the object to verify
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *          string using {@link String#valueOf(Object)}
     * @param runnable the consumer to run, supplying {@code reference}, guaranteed to be non-null, for convenience
     * @throws IllegalArgumentException if {@code reference} is {@code null} or empty
     */
    public static void checkArgumentNotNullOrEmptyAndRun(final String reference,
                                                           final Object errorMessage,
                                                           final Consumer<String> runnable)
            throws IllegalArgumentException {
        checkArgument(isNotBlank(reference), errorMessage);
        runnable.accept(reference);
    }

    /**
     * Ensures that {@code reference} is non-null and non-empty,
     * throwing an {@code IllegalArgumentException} with a custom message otherwise.
     * If no exception is thrown, {@code reference} is then passed to the specified supplier.
     *
     * @param reference    the object to verify
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @param function     the supplier to run, supplying {@code reference}, guaranteed to be non-null, for convenience
     * @return the result of the specified function
     * @throws IllegalArgumentException if {@code reference} is {@code null} or empty
     * @apiNote This method calls {@link org.apache.commons.lang3.StringUtils#isNotBlank(CharSequence)} on the specified reference.
     */
    public static <R> R checkArgumentNotNullOrEmptyAndTransform(final String reference,
                                                            final Object errorMessage,
                                                            final Function1<String, R> function)
            throws IllegalArgumentException {
        checkArgument(isNotBlank(reference), errorMessage);
        return function.apply(reference);
    }

    /**
     * Checks if a string can be converted to a Boolean.
     * @param input string to check
     * @return true if string matches a boolean, false if string does not match or is null
     */
    public static boolean isBoolean(final String input) {
        return isBoolean(input, Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     * Checks if a string can be converted to a Boolean.
     * @apiNote The following strings are considered true boolean values:
     *          "true", "t", "yes", "y", "1", "succeeded", "succeed", "enabled".
     *          The following strings are considered false boolean values:
     *          "false", "f", "no", "n", "0", "-1", "failed", "fail", "disabled".
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
    private CheckIf() { }
}
