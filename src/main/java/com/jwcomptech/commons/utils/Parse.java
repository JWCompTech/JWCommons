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

import com.jwcomptech.commons.exceptions.ParseException;
import com.jwcomptech.commons.functions.Function1;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static com.jwcomptech.commons.consts.Consts.*;
import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

/**
 * Contains methods for parsing data into various types with fallbacks and logging.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Getter
@Setter
public final class Parse {
    /**
     * Sets the global logger to be used in parsing methods.
     */
    @Getter
    @Setter
    private static Logger logger;
    /**
     * Sets the global log level used for logging parse failures.
     */
    @Getter
    @Setter
    private static Level logLevel = Level.WARN;
    /**
     * Sets the global log level used for logging parse failures.
     */
    private static ZoneId defaultZoneId = ZoneId.systemDefault();
    private static final ThreadLocal<DecimalFormat> BYTE_FORMAT =
            ThreadLocal.withInitial(() -> {
                final DecimalFormat format = new DecimalFormat("#.##");
                format.setRoundingMode(RoundingMode.DOWN);
                return format;
            });

    /**
     * Logs a formatted message and exception using the provided logger and level.
     *
     * @param logger  the logger to use
     * @param level   the log level
     * @param message the log message
     * @param t       the throwable
     * @param args    optional format arguments
     */
    public static void log(final Logger logger, final Level level, final String message,
                           final Throwable t, final Object... args) {
        if (logger == null || level == null) return;
        switch (level) {
            case ERROR -> logger.error(message, args, t);
            case WARN -> logger.warn(message, args, t);
            case INFO -> logger.info(message, args, t);
            case DEBUG -> logger.debug(message, args, t);
            case TRACE -> logger.trace(message, args, t);
        }
    }

    /**
     * Logs a formatted message and exception using the global logger and log level.
     *
     * @param message the log message
     * @param t       the throwable
     * @param args    optional format arguments
     */
    public static void log(final String message, final Throwable t, final Object... args) {
        if (logger != null && logLevel != null) {
            log(logger, logLevel, message, t, args);
        }
    }

    /**
     * Parses a string using a provided parser function, returning a fallback value on failure.
     *
     * @param logger  the logger to use
     * @param level   the log level
     * @param value    the string to parse
     * @param fallback the fallback value
     * @param parser   the parsing function
     * @param <T>      the target type
     * @return the parsed value or fallback
     */
    public static <T> T parseWithFallback(final Logger logger, final Level level,
                                          final String value, final T fallback,
                                          final Function1<String, T> parser) {
        try {
            return parser.apply(value);
        } catch (final RuntimeException e) {
            log(logger, logLevel, ParseException.buildMessage(value, e), e);
            return fallback;
        }
    }

    /**
     * Parses a string using a provided parser function, returning a fallback value on failure.
     *
     * @param value    the string to parse
     * @param fallback the fallback value
     * @param parser   the parsing function
     * @param <T>      the target type
     * @return the parsed value or fallback
     */
    public static <T> T parseWithFallback(final String value,
                                          final T fallback,
                                          final Function1<String, T> parser) {
        try {
            return parser.apply(value);
        } catch (final RuntimeException e) {
            log(logger, logLevel, ParseException.buildMessage(value, e), e);
            return fallback;
        }
    }

    //region Byte Conversions

    /**
     * Converts a {@link Number} representing a byte count to a human-readable string (e.g., "2.00 MB").
     *
     * @param input the input number of bytes
     * @return the human-readable string
     */
    public static @NotNull String convertBytesToString(final Number input) {
        if (input == null) return "0 Bytes";
        double value = input.doubleValue();
        int unitIndex = 0;

        while (value >= BYTES_PER_UNIT && unitIndex < BYTE_UNITS.size() - 1) {
            value /= BYTES_PER_UNIT;
            unitIndex++;
        }

        return BYTE_FORMAT.get().format(value) + " " + BYTE_UNITS.get(unitIndex);
    }

    //endregion Byte Conversions

    //region Int Parsers

    /**
     * Parses an integer from the given string.
     *
     * @param s the string to parse
     * @return parsed integer
     * @throws ParseException if parsing fails
     */
    public static int parseInt(final String s) {
        try {
            return Integer.parseInt(s);
        } catch (final NumberFormatException e) {
            throw new ParseException(Integer.class, s, e);
        }
    }

    /**
     * Parses an integer from the given string, logging failures.
     *
     * @param s      the string to parse
     * @param logger the logger to use
     * @return parsed integer
     */
    public static Optional<Integer> parseInt(final String s, final Logger logger) {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (final NumberFormatException e) {
            final var message = ParseException.buildMessage(Integer.class, s, e);
            log(logger, Level.WARN, message, e);
        }

        return Optional.empty();
    }

    /**
     * Parses an integer or returns the default on failure.
     *
     * @param s        the string to parse
     * @param fallback fallback value
     * @return parsed integer or fallback
     */
    public static int parseIntOrDefault(final String s, final int fallback) {
        return parseIntOrDefault(s, fallback, logger);
    }

    /**
     * Parses an integer or returns the default on failure with logging.
     *
     * @param s        the string to parse
     * @param fallback fallback value
     * @param logger   the logger to use
     * @return parsed integer or fallback
     */
    public static int parseIntOrDefault(final String s, final int fallback, final Logger logger) {
        try {
            return Integer.parseInt(s);
        } catch (final NumberFormatException e) {
            final var message = ParseException.buildMessage(Integer.class, s, e)
                    + " - Returning fallback value...";
            log(logger, Level.WARN, message, e);
        }

        return fallback;
    }

    //endregion Int Parsers

    //region Long Parsers

    /**
     * Parses a long from the given string.
     *
     * @param s the string to parse
     * @return parsed long
     * @throws ParseException if parsing fails
     */
    public static long parseLong(final String s) {
        try {
            return Long.parseLong(s);
        } catch (final NumberFormatException e) {
            throw new ParseException(Long.class, s, e);
        }
    }

    /**
     * Parses a long from the given string, logging failures.
     *
     * @param s      the string to parse
     * @param logger the logger to use
     * @return parsed long
     */
    public static Optional<Long> parseLong(final String s, final Logger logger) {
        try {
            return Optional.of(Long.parseLong(s));
        } catch (final NumberFormatException e) {
            final var message = ParseException.buildMessage(Long.class, s, e);
            log(logger, Level.WARN, message, e);
        }

        return Optional.empty();
    }

    /**
     * Parses a long or returns the default on failure.
     *
     * @param s        the string to parse
     * @param fallback fallback value
     * @return parsed long or fallback
     */
    public static long parseLongOrDefault(final String s, final long fallback) {
        return parseLongOrDefault(s, fallback, logger);
    }

    /**
     * Parses a long or returns the default on failure with logging.
     *
     * @param s        the string to parse
     * @param fallback fallback value
     * @param logger   the logger to use
     * @return parsed long or fallback
     */
    public static long parseLongOrDefault(final String s, final long fallback, final Logger logger) {
        try {
            return Long.parseLong(s);
        } catch (final NumberFormatException e) {
            final var message = ParseException.buildMessage(Long.class, s, e)
                    + " - Returning fallback value...";
            log(logger, Level.WARN, message, e);
        }

        return fallback;
    }

    //endregion Long Parsers

    //region Float Parsers

    /**
     * Parses a float from the given string.
     *
     * @param s the string to parse
     * @return parsed float
     * @throws ParseException if parsing fails
     */
    public static float parseFloat(final String s) {
        try {
            return Float.parseFloat(s);
        } catch (final NumberFormatException e) {
            throw new ParseException(Float.class, s, e);
        }
    }

    /**
     * Parses a float from the given string, logging failures.
     *
     * @param s      the string to parse
     * @param logger the logger to use
     * @return parsed float
     */
    public static Optional<Float> parseFloat(final String s, final Logger logger) {
        try {
            return Optional.of(Float.parseFloat(s));
        } catch (final NumberFormatException e) {
            final var message = ParseException.buildMessage(Float.class, s, e);
            log(logger, Level.WARN, message, e);
        }

        return Optional.empty();
    }

    /**
     * Parses a float or returns the default on failure.
     *
     * @param s        the string to parse
     * @param fallback fallback value
     * @return parsed float or fallback
     */
    public static float parseFloatOrDefault(final String s, final long fallback) {
        return parseFloatOrDefault(s, fallback, logger);
    }

    /**
     * Parses a float or returns the default on failure with logging.
     *
     * @param s        the string to parse
     * @param fallback fallback value
     * @param logger   the logger to use
     * @return parsed float or fallback
     */
    public static float parseFloatOrDefault(final String s, final long fallback, final Logger logger) {
        try {
            return Float.parseFloat(s);
        } catch (final NumberFormatException e) {
            final var message = ParseException.buildMessage(Float.class, s, e)
                    + " - Returning fallback value...";
            log(logger, Level.WARN, message, e);
        }

        return fallback;
    }

    //endregion Float Parsers

    //region Double Parsers

    /**
     * Parses a double from the given string.
     *
     * @param s the string to parse
     * @return parsed double
     * @throws ParseException if parsing fails
     */
    public static double parseDouble(final String s) {
        try {
            return Double.parseDouble(s);
        } catch (final NumberFormatException e) {
            throw new ParseException(Double.class, s, e);
        }
    }

    /**
     * Parses a double from the given string, logging failures.
     *
     * @param s      the string to parse
     * @param logger the logger to use
     * @return parsed double
     */
    public static Optional<Double> parseDouble(final String s, final Logger logger) {
        try {
            return Optional.of(Double.parseDouble(s));
        } catch (final NumberFormatException e) {
            final var message = ParseException.buildMessage(Double.class, s, e);
            log(logger, Level.WARN, message, e);
        }

        return Optional.empty();
    }

    /**
     * Parses a double or returns the default on failure.
     *
     * @param s        the string to parse
     * @param fallback fallback value
     * @return parsed double or fallback
     */
    public static double parseDoubleOrDefault(final String s, final long fallback) {
        return parseDoubleOrDefault(s, fallback, logger);
    }

    /**
     * Parses a double or returns the default on failure with logging.
     *
     * @param s        the string to parse
     * @param fallback fallback value
     * @param logger   the logger to use
     * @return parsed double or fallback
     */
    public static double parseDoubleOrDefault(final String s, final long fallback, final Logger logger) {
        try {
            return Double.parseDouble(s);
        } catch (final NumberFormatException e) {
            final var message = ParseException.buildMessage(Double.class, s, e)
                    + " - Returning fallback value...";
            log(logger, Level.WARN, message, e);
        }

        return fallback;
    }

    //endregion Double Parsers

    //region Boolean Parsers

    /**
     * Parses a {@link Boolean} from the provided string.
     * Uses case-insensitive true/false value sets.
     *
     * @param s the string to parse
     * @return the parsed boolean value
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public static boolean parseBoolean(final @NotNull String s) {
        final String value = s.trim().toLowerCase();
        if (TRUE_VALUES.contains(value)) return true;
        if (FALSE_VALUES.contains(value)) return false;
        throw new ParseException(Boolean.class, s, "");
    }

    /**
     * Parses a {@link Boolean} from the provided string using a logger on error.
     *
     * @param s      the string to parse
     * @param logger the logger to use for errors
     * @return the parsed boolean value
     */
    public static Optional<Boolean> parseBoolean(final @NotNull String s, final Logger logger) {
        final String value = s.trim().toLowerCase();
        if (TRUE_VALUES.contains(value)) return Optional.of(true);
        if (FALSE_VALUES.contains(value)) return Optional.of(false);
        final var message = ParseException.buildMessage(Boolean.class, s, "");
        log(logger, Level.WARN, message, null);

        return Optional.empty();
    }

    /**
     * Parses a {@link Boolean} from the provided string or returns a default value.
     *
     * @param s             the string to parse
     * @param defaultValue  the default value to return on failure
     * @return the parsed boolean value or the default
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public static boolean parseBooleanOrDefault(final @NotNull String s, final boolean defaultValue) {
        final String value = s.trim().toLowerCase();
        if (TRUE_VALUES.contains(value)) return true;
        if (FALSE_VALUES.contains(value)) return false;
        return defaultValue;
    }

    /**
     * Parses a {@link Boolean} from the provided string using a logger or returns a default value.
     *
     * @param s             the string to parse
     * @param defaultValue  the default value to return on failure
     * @param logger        the logger to use for errors
     * @return the parsed boolean value or the default
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public static boolean parseBooleanOrDefault(final @NotNull String s, final boolean defaultValue, final Logger logger) {
        final String value = s.trim().toLowerCase();
        if (TRUE_VALUES.contains(value)) return true;
        if (FALSE_VALUES.contains(value)) return false;

        final var message = ParseException.buildMessage(Boolean.class, s, "")
                + " - Returning fallback value...";
        log(logger, Level.WARN, message, null);
        return defaultValue;
    }

    //endregion Boolean Parsers

    //region Enum Parsers

    /**
     * Parses an enum constant from a string.
     *
     * @param enumClass the enum class
     * @param s         the string to parse
     * @param <T>       the enum type
     * @return the parsed enum constant
     * @throws ParseException if parsing fails
     */
    public static <T extends Enum<T>> @NotNull T parseEnum(final Class<T> enumClass,
                                                           final @NotNull String s) {
        try {
            return Enum.valueOf(enumClass, s.trim());
        } catch (final IllegalArgumentException e) {
            throw new ParseException(enumClass, s, e);
        }
    }

    /**
     * Parses an enum constant from a string using a logger on error.
     *
     * @param enumClass the enum class
     * @param s         the string to parse
     * @param logger    the logger to use for errors
     * @param <T>       the enum type
     * @return the parsed enum constant
     */
    public static <T extends Enum<T>> @NotNull Optional<T> parseEnum(@NotNull final Class<T> enumClass,
                                                           @NotNull final String s,
                                                           final Logger logger) {
        try {
            return Optional.of(Enum.valueOf(enumClass, s.trim()));
        } catch (final IllegalArgumentException e) {
            final var message = ParseException.buildMessage(Integer.class, s, e);
            log(logger, Level.WARN, message, e);
        }

        return Optional.empty();
    }

    /**
     * Parses an enum constant from a string or returns a default value.
     *
     * @param enumClass     the enum class
     * @param s             the string to parse
     * @param defaultValue  the default value to return on failure
     * @param <T>           the enum type
     * @return the parsed enum constant or the default
     */
    public static <T extends Enum<T>> T parseEnumOrDefault(final Class<T> enumClass,
                                                           final String s,
                                                           final T defaultValue) {
        return parseWithFallback(s, defaultValue, v -> Enum.valueOf(enumClass, v.trim()));
    }

    /**
     * Parses an enum constant from a string using a logger or returns a default value.
     *
     * @param enumClass     the enum class
     * @param s             the string to parse
     * @param defaultValue  the default value to return on failure
     * @param logger        the logger to use for errors
     * @param <T>           the enum type
     * @return the parsed enum constant or the default
     */
    public static <T extends Enum<T>> T parseEnumOrDefault(final Class<T> enumClass, final String s,
                                                           final T defaultValue, final Logger logger) {
        return parseWithFallback(logger, logLevel, s, defaultValue, v -> Enum.valueOf(enumClass, v.trim()));
    }

    //endregion Parsers

    //region Other Parsers

    /**
     * Parses an epoch millisecond value into a {@link ZonedDateTime}.
     *
     * @param milliseconds the epoch millisecond timestamp
     * @return the parsed ZonedDateTime
     * @throws ParseException if parsing fails
     */
    @Contract("_ -> new")
    public static @NotNull ZonedDateTime parseEpochMillisToZonedDateTime(final long milliseconds) {
        try {
            return Instant.ofEpochMilli(milliseconds).atZone(defaultZoneId);
        } catch (final DateTimeException e) {
            final var message = ParseException.buildMessage(Long.class, String.valueOf(milliseconds), e);
            throw new ParseException(ZonedDateTime.class, message, e);
        }
    }

    /**
     * Parses an epoch millisecond value into a {@link ZonedDateTime} and logs on error.
     *
     * @param milliseconds the epoch millisecond timestamp
     * @param logger       the logger to use
     * @return the parsed ZonedDateTime or null on failure
     */
    public static @NotNull Optional<ZonedDateTime> parseEpochMillisToZonedDateTime(final long milliseconds,
                                                                                   final Logger logger) {
        try {
            return Optional.of(Instant.ofEpochMilli(milliseconds).atZone(defaultZoneId));
        } catch (final DateTimeException e) {
            log(logger, logLevel, "Failed to parse epoch millis to ZonedDateTime: {}", e, milliseconds);
        }

        return Optional.empty();
    }

    /**
     * Parses an epoch millisecond value into a {@link ZonedDateTime}, returning a default on failure.
     *
     * @param milliseconds the epoch millisecond timestamp
     * @param defaultValue the default ZonedDateTime
     * @return the parsed ZonedDateTime or the default
     */
    public static ZonedDateTime parseEpochMillisToZonedDateTimeOrDefault(final long milliseconds,
                                                                         final ZonedDateTime defaultValue) {
        try {
            return Instant.ofEpochMilli(milliseconds).atZone(defaultZoneId);
        } catch (final DateTimeException e) {
            return defaultValue;
        }
    }

    /**
     * Parses an epoch millisecond value into a {@link ZonedDateTime}, returning a default on failure and logging.
     *
     * @param milliseconds the epoch millisecond timestamp
     * @param defaultValue the default ZonedDateTime
     * @param logger       the logger to use
     * @return the parsed ZonedDateTime or the default
     */
    public static ZonedDateTime parseEpochMillisToZonedDateTimeOrDefault(final long milliseconds, final ZonedDateTime defaultValue, final Logger logger) {
        try {
            return Instant.ofEpochMilli(milliseconds).atZone(defaultZoneId);
        } catch (final DateTimeException e) {
            log(logger, logLevel, "Failed to parse epoch millis to ZonedDateTime: {}", e, milliseconds);
        }

        return defaultValue;
    }


    //endregion Other Parsers

    /** Prevents instantiation of this utility class. */
    private Parse() { throwUnsupportedExForUtilityCls(); }
}
