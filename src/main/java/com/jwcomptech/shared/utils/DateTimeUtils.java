package com.jwcomptech.shared.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.jwcomptech.shared.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNullOrEmpty;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        return formatter.format(ZonedDateTime.of(datetime, ZoneId.systemDefault()));
    }

    public static @NotNull String formatted(final Instant instant) {
        return formatted("MM/dd/yyyy h:mma z", instant);
    }

    public static @NotNull String formatted(final String pattern, final Instant instant) {
        checkArgumentNotNullOrEmpty(pattern, cannotBeNullOrEmpty("pattern"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        LocalDateTime datetime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return formatter.format(ZonedDateTime.of(datetime, ZoneId.systemDefault()));
    }

    /** Prevents instantiation of this utility class. */
    private DateTimeUtils() { }
}
