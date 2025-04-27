package com.jwcomptech.shared.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Contains methods to do misc tasks.
 * @since 0.0.1
 */
public final class Misc {
    /**
     * Contains methods to convert seconds into a readable format.
     * @since 1.3.0
     */
    public static final class SecondsConverter {
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

        @SuppressWarnings("InnerClassTooDeeplyNested")
        private static final class TimeObj {
            private long years;
            private int days;
            private int hours;
            private int minutes;
            private int seconds;

            private TimeObj() {
            }

            public long getYears() {
                return years;
            }

            public void setYears(long years) {
                this.years = years;
            }

            public int getDays() {
                return days;
            }

            public void setDays(int days) {
                this.days = days;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;

                if (o == null || getClass() != o.getClass()) return false;

                TimeObj timeObj = (TimeObj) o;

                return new EqualsBuilder()
                        .append(years, timeObj.years)
                        .append(days, timeObj.days)
                        .append(hours, timeObj.hours)
                        .append(minutes, timeObj.minutes)
                        .append(seconds, timeObj.seconds)
                        .isEquals();
            }

            @Override
            public int hashCode() {
                return new HashCodeBuilder(17, 37)
                        .append(years)
                        .append(days)
                        .append(hours)
                        .append(minutes)
                        .append(seconds)
                        .toHashCode();
            }

            @Override
            public String toString() {
                return new ToStringBuilder(this)
                        .append("years", years)
                        .append("days", days)
                        .append("hours", hours)
                        .append("minutes", minutes)
                        .append("seconds", seconds)
                        .toString();
            }
        }

        /**
         * Converts seconds into a readable format - years:days:hours:minutes:seconds.
         * @param seconds the amount of seconds to convert
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative
         */
        public static @NotNull String toString(final long seconds) throws NegativeNumberException {
            return toString(seconds, false);
        }

        /**
         * Converts seconds into a readable format - years:days:hours:minutes:seconds.
         * @param seconds the amount of seconds to convert
         * @param allowNegative if true and the number is negative a negative sign will be returned with the string
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative and allowNegative is false
         */
        public static @NotNull String toString(final long seconds, final boolean allowNegative) throws NegativeNumberException {
            if(seconds >= 0) return structureTime(seconds);
            if(allowNegative) return '-' + structureTime(Math.abs(seconds));
            else throw new NegativeNumberException("Seconds cannot be negative!");
        }

        private static @NotNull String structureTime(final long seconds) {
            final var obj = new TimeObj();
            if(60 <= seconds) {
                obj.setMinutes((int)(seconds / 60));
                obj.setSeconds((int)(seconds % 60));

                if(60 <= obj.getMinutes()) {
                    obj.setHours(obj.getMinutes() / 60);
                    obj.setMinutes(obj.getMinutes() % 60);

                    if(24 <= obj.getHours()) {
                        obj.setDays(obj.getHours() / 24);
                        obj.setHours(obj.getHours() % 24);

                        if(365 <= obj.getDays()) {
                            obj.setYears(obj.getDays() / 365);
                            obj.setDays(obj.getDays() % 365);
                        }
                    }
                }
            }
            else { obj.setSeconds((int)seconds); }

            return obj.getYears() + ":"
                    + obj.getDays() + ':'
                    + String.format("%02d", obj.getHours()) + ':'
                    + String.format("%02d", obj.getMinutes()) + ':'
                    + String.format("%02d", obj.getSeconds());
        }

        /** This exception is thrown if a negative number is supplied to the methods in this class. */
        @SuppressWarnings("InnerClassTooDeeplyNested")
        public static class NegativeNumberException extends Exception {
            //Parameterless Constructor
            public NegativeNumberException() { }

            //Constructor that accepts a message
            public NegativeNumberException(final String message) { super(message); }
        }

        /** Prevents instantiation of this utility class. */
        private SecondsConverter() { }

        //10:364:23:59:59
        //59
        //3540
        //82800
        //31449600
        //315360000
        //346895999

        /*try {
            System.out.println(toString(-90, true));
            System.out.println(toString(1, true));
            System.out.println(toString(0));
            System.out.println(toString(Minute + Hour + Day + Year + 1));
            System.out.println(toString(SecondAwayFromADecade));
            System.out.println(toString(Integer.MAX_VALUE));
            System.out.println(toString(Long.MAX_VALUE));
        } catch(NegativeNumberException e) {
            e.printStackTrace();
        }*/
    }

    /** Prevents instantiation of this utility class. */
    private Misc() { }
}
