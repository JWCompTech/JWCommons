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

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class ParseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5235666396679960545L;

    public ParseException() { }

    public ParseException(final String message) {
        super(message);
    }

    public ParseException(@NotNull final Class<?> type,
                          final String message) {
        super(buildMessage(type, message));
    }

    public ParseException(@NotNull final Class<?> type,
                          final String attemptedValue,
                          final String message) {
        super(buildMessage(type, attemptedValue, message));
    }

    public ParseException(final String attemptedValue,
                          final Throwable cause) {
        super(buildMessage(attemptedValue, cause), cause);
    }

    public ParseException(@NotNull final Class<?> type,
                          final String attemptedValue,
                          @NotNull final Throwable cause) {
        super(buildMessage(type, attemptedValue, cause), cause);
    }

    public ParseException(@NotNull final Class<?> type,
                          @NotNull final Throwable cause) {
        super(buildMessage(type, cause), cause);
    }

    public static @NotNull String buildMessage(final @NotNull Class<?> type,
                                               final String attemptedValue,
                                               final @NotNull Throwable cause) {
        return "Failed to parse %s [%s] with exception: \"%s\""
                .formatted(type.getName(), attemptedValue, cause.getMessage());
    }

    public static @NotNull String buildMessage(final @NotNull Class<?> type,
                                               final @NotNull Throwable cause) {
        return "Failed to parse %s with exception: \"%s\""
                .formatted(type.getName(), cause.getMessage());
    }

    public static @NotNull String buildMessage(final String attemptedValue,
                                               final @NotNull Throwable cause) {
        return "Failed to parse [%s] with exception: \"%s\""
                .formatted(attemptedValue, cause.getMessage());
    }

    public static @NotNull String buildMessage(final @NotNull Class<?> type,
                                               final String attemptedValue,
                                               final String message) {
        return "Failed to parse %s [%s]: %s".formatted(type, attemptedValue, message);
    }

    public static @NotNull String buildMessage(final @NotNull Class<?> type,
                                               final String message) {
        return "Failed to parse %s: %s".formatted(type, message);
    }
}
