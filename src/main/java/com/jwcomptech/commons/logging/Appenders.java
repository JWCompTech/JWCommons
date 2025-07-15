package com.jwcomptech.commons.logging;

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

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * Defines commonly used {@link ch.qos.logback.core.Appender} templates
 * for consistent logging configuration across the application.
 *
 * <p>Each enum constant wraps a pre-configured {@link ch.qos.logback.core.ConsoleAppender} or
 * file-based appender instance using one of the encoders defined in {@link Encoders}.
 *
 * <p>Use {@code getAppender()} to retrieve the singleton instance or
 * {@code newAppenderInstance()} to create a fresh appender with the same configuration.
 *
 * <p>Intended for quick plug-and-play with {@link JWLogger} or {@link LoggingManager}.
 *
 * @see Encoders
 * @see JWLogger
 * @see LoggingManager
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Getter
@RequiredArgsConstructor
@ToString
public enum Appenders {
    /** A console appender that uses the {@link Encoders#LimitedEncoder} encoder. */
    LimitedConsoleAppender(LoggingManager.createNewConsoleAppender(Encoders.LimitedEncoder)),
    /** A console appender that uses the {@link Encoders#BasicEncoder} encoder. */
    BasicConsoleAppender(LoggingManager.createNewConsoleAppender(Encoders.BasicEncoder)),
    /** A console appender that uses the {@link Encoders#ExtendedEncoder} encoder. */
    ExtendedConsoleAppender(LoggingManager.createNewConsoleAppender(Encoders.ExtendedEncoder)),
    /**
     * A file appender that uses a {@link RollingFileAppender},
     * the {@link Encoders#LimitedEncoder} encoder, and sets
     * the log file to use the filename "limited.log"
     */
    LimitedFileAppender(LoggingManager.getRollingFileAppenderBuilder()
                    .setEncoder(Encoders.LimitedEncoder.getEncoder())
                    .setFileName("limited.log")
                    .build()
    ),
    /**
     * A file appender that uses a {@link RollingFileAppender},
     * the {@link Encoders#CompactFileEncoder} encoder, and sets
     * the log file to use the filename "compact.log"
     */
    CompactFileAppender(LoggingManager.getRollingFileAppenderBuilder()
            .setEncoder(Encoders.CompactFileEncoder.getEncoder())
            .setFileName("compact.log")
            .build()
    ),
    /**
     * A file appender that uses a {@link RollingFileAppender},
     * the {@link Encoders#BasicEncoder} encoder, and sets
     * the log file to use the filename "basic.log"
     */
    BasicFileAppender(LoggingManager.getRollingFileAppenderBuilder()
                    .setEncoder(Encoders.BasicEncoder.getEncoder())
                    .setFileName("basic.log")
                    .build()
    ),
    /**
     * A file appender that uses a {@link RollingFileAppender},
     * the {@link Encoders#ExtendedEncoder} encoder, and sets
     * the log file to use the filename "extended.log"
     */
    ExtendedFileAppender(LoggingManager.getRollingFileAppenderBuilder()
                    .setEncoder(Encoders.ExtendedEncoder.getEncoder())
                    .setFileName("extended.log")
                    .build()
    ),
    ;

    private final Appender<ILoggingEvent> appender;

    /**
     * Returns a fresh appender instance with the same configuration as the enum constant.
     *
     * @return a new instance of this appender
     * @throws IllegalStateException if the enum constant is unhandled
     */
    public @NotNull Appender<ILoggingEvent> newAppenderInstance() {
        return switch (this) {
            case LimitedConsoleAppender ->
                    LoggingManager.createNewConsoleAppender(Encoders.LimitedEncoder);
            case BasicConsoleAppender ->
                    LoggingManager.createNewConsoleAppender(Encoders.BasicEncoder);
            case ExtendedConsoleAppender ->
                    LoggingManager.createNewConsoleAppender(Encoders.ExtendedEncoder);
            case LimitedFileAppender -> LoggingManager.getRollingFileAppenderBuilder()
                        .setEncoder(Encoders.LimitedEncoder.getEncoder())
                        .setFileName("limited.log")
                        .build();
            case CompactFileAppender -> LoggingManager.getRollingFileAppenderBuilder()
                        .setEncoder(Encoders.CompactFileEncoder.getEncoder())
                        .setFileName("compact.log")
                        .build();
            case BasicFileAppender -> LoggingManager.getRollingFileAppenderBuilder()
                        .setEncoder(Encoders.BasicEncoder.getEncoder())
                        .setFileName("basic.log")
                        .build();
            case ExtendedFileAppender -> LoggingManager.getRollingFileAppenderBuilder()
                        .setEncoder(Encoders.ExtendedEncoder.getEncoder())
                        .setFileName("extended.log")
                        .build();
            //noinspection UnnecessaryDefault
            default -> throw new IllegalStateException("Unexpected value: " + this);
        };
    }
}
