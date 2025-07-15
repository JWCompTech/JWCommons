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

import com.jwcomptech.commons.functions.Function1;
import com.jwcomptech.commons.tuples.ImmutablePair;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.jwcomptech.commons.consts.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;

/**
 * Contains methods and objects for dealing with date and time.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
public final class DateTimeUtils {
    @Contract("_ -> new")
    public static @NotNull LocalDateTime fromInstant(final Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static @NotNull String formatted(final LocalDateTime datetime) {
        return formatted("MM/dd/yyyy h:mma z", datetime);
    }

    public static @NotNull String formatted(final String pattern, final LocalDateTime datetime) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        return formatter.format(ZonedDateTime.of(datetime, ZoneId.systemDefault()));
    }

    public static @NotNull String formatted(final Instant instant) {
        return formatted("MM/dd/yyyy h:mma z", instant);
    }

    public static @NotNull String formatted(final String pattern, final Instant instant) {
        checkArgumentNotNullOrEmpty(pattern, cannotBeNullOrEmpty("pattern"));
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        final LocalDateTime datetime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return formatter.format(ZonedDateTime.of(datetime, ZoneId.systemDefault()));
    }

    /**
     * Blocks the current thread for the specified duration.
     * @param timeUnit the unit of time to wait
     * @param time the time to wait
     */
    public static void waitTime(final @NotNull TimeUnit timeUnit, final long time) {
        try {
            timeUnit.sleep(time);
        } catch (final InterruptedException ignored) { }
    }

    /**
     * Tracks the time a Runnable takes to run and returns the result in milliseconds.
     * @param runnable the Runnable to benchmark
     * @return the Duration the Runnable took to run
     */
    public static Duration benchmarkRunnable(final @NotNull Runnable runnable) {
        final Instant startTime = Instant.now();
        runnable.run();
        final Instant endTime = Instant.now();

        return Duration.between(startTime, endTime);
    }

    /**
     * Tracks the time a Consumer takes to run and returns the result in milliseconds.
     * @param <T> the input type of the Consumer
     * @param consumer the Runnable to benchmark
     * @param value the value to pass to the Consumer
     * @return the Duration the Consumer took to run
     */
    public static <T> Duration benchmarkConsumer(final @NotNull Consumer<T> consumer, final T value) {
        final Instant startTime = Instant.now();
        consumer.accept(value);
        final Instant endTime = Instant.now();

        return Duration.between(startTime, endTime);
    }

    /**
     * Tracks the time a Function takes to run and returns the result in milliseconds.
     * @param <T> the input type of the Function
     * @param <R> the return type of the Function
     * @param function the Function to benchmark
     * @param value the value to pass to the Function
     * @return the Duration the Function took to run
     */
    public static <T, R> @NotNull ImmutablePair<R, Duration> benchmarkFunction(final @NotNull Function1<T, R> function, final T value) {
        final Instant startTime = Instant.now();
        final R result = function.apply(value);
        final Instant endTime = Instant.now();

        return new ImmutablePair<>(result, Duration.between(startTime, endTime));

    }

    /**
     * Tracks the time a Supplier takes to run and returns the result of the Supplier
     * and the evaluation time in milliseconds.
     * @param <R> the return type of the Supplier
     * @param supplier the Supplier to benchmark
     * @return the result of the Supplier and the Duration the Supplier took to run
     */
    public static <R> @NotNull ImmutablePair<R, Duration> benchmarkSupplier(final @NotNull Supplier<R> supplier) {
        final Instant startTime = Instant.now();
        final R result = supplier.get();
        final Instant endTime = Instant.now();

        return new ImmutablePair<>(result, Duration.between(startTime, endTime));

    }

    /**
     * Contains methods to convert milliseconds, seconds, minutes and hours into a readable format.
     */
    public static final class TimeConverter {
        /** The number of milliseconds in a second. */
        public static final int Second = 1000;
        /** The number of seconds in a minute. */
        public static final int Minute = 60;
        /** The number of seconds in an hour. */
        public static final int Hour = 3600;
        /** The number of seconds in a day. */
        public static final int Day = 86400;
        /** The number of seconds in a year. */
        public static final int Year = 31536000;
        /** One second less than the number of seconds in a decade. */
        public static final int SecondAwayFromADecade = 315359999;

        @Data
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        @SuppressWarnings("InnerClassTooDeeplyNested")
        private static final class TimeObj {
            private long years;
            private int days; //Will always be less than 365
            private int hours; //Will always be less than 24
            private int minutes; //Will always be less than 60
            private int seconds; //Will always be less than 60
            private int millis; //Will always be less than 1000
        }

        /**
         * Converts milliseconds into a readable format - years:days:hours:minutes:seconds:milliseconds.
         * @param milliseconds the amount of milliseconds to convert
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative
         */
        public static @NotNull String millisecondsToString(final long milliseconds) throws NegativeNumberException {
            return millisecondsToString(milliseconds, false);
        }

        /**
         * Converts milliseconds into a readable format - years:days:hours:minutes:seconds:milliseconds.
         * @param milliseconds the amount of milliseconds to convert
         * @param allowNegative if true and the number is negative a negative sign will be returned with the string
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative and allowNegative is false
         */
        public static @NotNull String millisecondsToString(final long milliseconds, final boolean allowNegative)
                throws NegativeNumberException {
            if(milliseconds >= 0) return structureTime(milliseconds);
            if(allowNegative) return '-' + structureTime(Math.abs(milliseconds));
            else throw new NegativeNumberException("Milliseconds cannot be negative!");
        }

        /**
         * Converts seconds into a readable format - years:days:hours:minutes:seconds.
         * @param seconds the amount of seconds to convert
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative
         */
        public static @NotNull String secondsToString(final long seconds) throws NegativeNumberException {
            return secondsToString(seconds, false);
        }

        /**
         * Converts seconds into a readable format - years:days:hours:minutes:seconds.
         * @param seconds the amount of seconds to convert
         * @param allowNegative if true and the number is negative a negative sign will be returned with the string
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative and allowNegative is false
         */
        public static @NotNull String secondsToString(final long seconds, final boolean allowNegative)
                throws NegativeNumberException {
            if(seconds >= 0) return structureTime(seconds * Second);
            if(allowNegative) return '-' + structureTime(Math.abs(seconds * Second));
            else throw new NegativeNumberException("Seconds cannot be negative!");
        }

        /**
         * Converts minutes into a readable format - years:days:hours:minutes:seconds.
         * @param minutes the amount of minutes to convert
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative
         */
        public static @NotNull String minutesToString(final long minutes) throws NegativeNumberException {
            return minutesToString(minutes, false);
        }

        /**
         * Converts minutes into a readable format - years:days:hours:minutes:seconds.
         * @param minutes the amount of minutes to convert
         * @param allowNegative if true and the number is negative a negative sign will be returned with the string
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative and allowNegative is false
         */
        public static @NotNull String minutesToString(final long minutes, final boolean allowNegative)
                throws NegativeNumberException {
            if(minutes >= 0) return structureTime(minutes * Minute * Second);
            if(allowNegative) return '-' + structureTime(Math.abs(minutes * Minute * Second));
            else throw new NegativeNumberException("Minutes cannot be negative!");
        }

        /**
         * Converts hours into a readable format - years:days:hours:minutes:seconds.
         * @param hours the amount of hours to convert
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative
         */
        public static @NotNull String hoursToString(final long hours) throws NegativeNumberException {
            return hoursToString(hours, false);
        }

        /**
         * Converts hours into a readable format - years:days:hours:minutes:seconds.
         * @param hours the amount of hours to convert
         * @param allowNegative if true and the number is negative a negative sign will be returned with the string
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative and allowNegative is false
         */
        public static @NotNull String hoursToString(final long hours, final boolean allowNegative)
                throws NegativeNumberException {
            if(hours >= 0) return structureTime(hours * Hour * Second);
            if(allowNegative) return '-' + structureTime(Math.abs(hours * Hour * Second));
            else throw new NegativeNumberException("Hours cannot be negative!");
        }

        @SuppressWarnings("NumericCastThatLosesPrecision")
        private static @NotNull String structureTime(final long milliseconds) {
            final var obj = new TimeObj();
            if(milliseconds >= 1000) {
                obj.setSeconds((int)(milliseconds / 1000));
                obj.setMillis((int)(milliseconds % 1000));

                if(obj.getSeconds() >= 60) {
                    obj.setMinutes(obj.getSeconds() / 60);
                    obj.setSeconds(obj.getSeconds() % 60);

                    if(obj.getMinutes() >= 60) {
                        obj.setHours(obj.getMinutes() / 60);
                        obj.setMinutes(obj.getMinutes() % 60);

                        if(obj.getHours() >= 24) {
                            obj.setDays(obj.getHours() / 24);
                            obj.setHours(obj.getHours() % 24);

                            if(obj.getDays() >= 365) {
                                obj.setYears(obj.getDays() / 365);
                                obj.setDays(obj.getDays() % 365);
                            }
                        }
                    }
                }
            }

            else { obj.setMillis((int)milliseconds); }

            return obj.getYears() + ":"
                    + obj.getDays() + ':'
                    + String.format("%02d", obj.getHours()) + ':'
                    + String.format("%02d", obj.getMinutes()) + ':'
                    + String.format("%02d", obj.getSeconds()) + ':'
                    + String.format("%02d", obj.getMillis());
        }

        /** This exception is thrown if a negative number is supplied to the methods in this class. */
        @SuppressWarnings("InnerClassTooDeeplyNested")
        public static class NegativeNumberException extends Exception {
            @Serial
            private static final long serialVersionUID = -9211932478286069658L;

            //Parameterless Constructor
            public NegativeNumberException() { }

            //Constructor that accepts a message
            public NegativeNumberException(final String message) { super(message); }
        }

        /** Prevents instantiation of this utility class. */
        private TimeConverter() { throwUnsupportedExForUtilityCls(); }

        //10:364:23:59:59:00
        //59
        //3540
        //82800
        //31449600
        //315360000
        //346895999

        /*try {
            System.out.println(secondsToString(-90, true));
            System.out.println(secondsToString(1, true));
            System.out.println(secondsToString(0));
            System.out.println(secondsToString(Minute + Hour + Day + Year + 1));
            System.out.println(secondsToString(SecondAwayFromADecade));
            System.out.println(secondsToString(Integer.MAX_VALUE));
            System.out.println(secondsToString(Long.MAX_VALUE));
        } catch(NegativeNumberException e) {
            e.printStackTrace();
        }*/
    }

    /** Prevents instantiation of this utility class. */
    private DateTimeUtils() { throwUnsupportedExForUtilityCls(); }
}
