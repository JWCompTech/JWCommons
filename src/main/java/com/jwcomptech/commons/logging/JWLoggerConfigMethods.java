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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.spi.LoggingEventBuilder;

/**
 * A fluent configuration utility for customizing core behaviors of a {@link JWLogger} instance.
 *
 * <p>This class provides an expressive DSL for setting log levels, attaching standard appenders,
 * toggling additive behavior, and resetting logger configurations. It wraps a Logback
 * {@link ch.qos.logback.classic.Logger} and exposes higher-level logging setup options
 * through clearly named methods.
 *
 * <p>Typically accessed via {@link JWLogger#config()}, this class enables quick and readable
 * logger configuration suitable for plugin environments, CLI tools, or dynamically scoped logging needs.
 *
 * <p>Available customization features include:
 * <ul>
 *   <li>Preset console appender setups (limited, basic, extended)</li>
 *   <li>Log level manipulation via {@link #level()}</li>
 *   <li>Appender management via {@link #appenders()}</li>
 *   <li>Reset and disable methods for reverting to or clearing default behavior</li>
 * </ul>
 *
 * <p><b>Example usage:</b>
 * <pre>{@code
 * JWLogger.of(MyClass.class)
 *     .config()
 *     .useExtendedConsole(Level.DEBUG)
 *     .level()
 *         .setLevel(Level.INFO)
 *         .back()
 *     .appenders()
 *         .addNewConsoleAppender("dev-console")
 *         .back()
 *     .back()
 *     .info("Logger ready!");
 * }</pre>
 *
 * @see JWLogger
 * @see JWLoggerAppenderMethods
 * @see JWLoggerLevelMethods
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Data
@NoArgsConstructor(access = AccessLevel.NONE)
public class JWLoggerConfigMethods {

    @Getter(AccessLevel.PACKAGE)
    private final JWLogger jwLogger;
    private final Logger logger;

    @Contract(pure = true)
    JWLoggerConfigMethods(final @NotNull JWLogger logger) {
        this.jwLogger = logger;
        this.logger = logger.getLogger();
    }

    /**
     * Configures the logger to use the specified log level and attaches
     * the {@link Appenders#LimitedConsoleAppender}, optimized for minimal console output.
     *
     * @param logLevel the log level to set
     * @return this instance for fluent chaining
     * @apiNote This method disables additive mode automatically.
     */
    public JWLoggerConfigMethods useLimitedConsole(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        appenders().attachIfAbsent(Appenders.LimitedConsoleAppender.getAppender());
        return this;
    }

    /**
     * Configures the logger to use the specified log level and attaches
     * the {@link Appenders#BasicConsoleAppender}, offering balanced output formatting.
     *
     * @param logLevel the log level to set
     * @return this instance for fluent chaining
     * @apiNote This method disables additive mode automatically.
     */
    public JWLoggerConfigMethods useBasicConsole(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        appenders().attachIfAbsent(Appenders.BasicConsoleAppender.getAppender());
        return this;
    }

    /**
     * Configures the logger to use the specified log level and attaches
     * the {@link Appenders#ExtendedConsoleAppender}, which includes detailed output formatting.
     *
     * @param logLevel the log level to set
     * @return this instance for fluent chaining
     * @apiNote This method disables additive mode automatically.
     */
    public JWLoggerConfigMethods useExtendedConsole(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        appenders().attachIfAbsent(Appenders.ExtendedConsoleAppender.getAppender());
        return this;
    }

    /**
     * Disables all logger output by setting the level to {@link Level#OFF}.
     *
     * @return this instance for fluent chaining
     * @apiNote This method disables additive mode automatically.
     */
    public JWLoggerConfigMethods disable() {
        logger.setAdditive(false);
        logger.setLevel(Level.OFF);
        return this;
    }

    /**
     * Resets the logger by restoring additive mode, clearing the explicit log level,
     * and detaching all custom appenders.
     *
     * <p>This allows the logger to fall back to its parent’s configuration.
     *
     * @return this instance for fluent chaining
     */
    public JWLoggerConfigMethods reset() {
        logger.setLevel(null); // null means inherit
        logger.setAdditive(true);
        logger.detachAndStopAllAppenders();
        return this;
    }

    /**
     * Sets the logger's additive mode, which determines whether log events
     * should propagate to parent loggers.
     *
     * @param additive {@code true} to enable propagation; {@code false} to isolate the logger
     * @return this instance for fluent chaining
     */
    public JWLoggerConfigMethods setAdditive(final boolean additive) {
        logger.setAdditive(additive);
        return this;
    }

    /**
     * Returns whether the logger is currently in additive mode.
     *
     * @return {@code true} if the logger inherits from parent loggers; otherwise {@code false}
     */
    public boolean isAdditive() {
        return logger.isAdditive();
    }


    /**
     * Creates a new {@link LoggingEventBuilder} instance for constructing
     * structured log events at the specified level.
     *
     * @param level the log level for the event
     * @return a fresh {@link LoggingEventBuilder} bound to this logger
     */
    public LoggingEventBuilder makeLoggingEventBuilder(final org.slf4j.event.Level level) {
        return logger.makeLoggingEventBuilder(level);
    }

    /**
     * Returns a fluent utility for managing logger appenders such as
     * console or file outputs.
     *
     * @return a new {@link JWLoggerAppenderMethods} instance
     */
    public JWLoggerAppenderMethods appenders() {
        return new JWLoggerAppenderMethods(this);
    }

    /**
     * Returns a fluent utility for manipulating the logger's level
     * and checking whether levels are enabled.
     *
     * @return a new {@link JWLoggerLevelMethods} instance
     */
    public JWLoggerLevelMethods level() {
        return new JWLoggerLevelMethods(this);
    }

    /**
     * Provides access to the MDC manager for setting and retrieving diagnostic context entries.
     *
     * @return the MDC manager instance
     */
    public MDCManager mdc() {
        return new MDCManager(this);
    }


    /**
     * Returns the parent {@link JWLogger} instance to continue
     * fluent chaining or to perform actual logging.
     *
     * @return the parent JWLogger
     */
    public JWLogger back() {
        return jwLogger;
    }
}
