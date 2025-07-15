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
import org.slf4j.Marker;
import org.slf4j.helpers.CheckReturnValue;
import org.slf4j.spi.LoggingEventBuilder;

/**
 * A fluent utility class that provides methods for configuring and querying
 * the logging level of a {@link ch.qos.logback.classic.Logger} instance
 * wrapped by {@link JWLogger}.
 *
 * <p>This class is typically accessed via {@link JWLoggerConfigMethods#level()}
 * and is intended to be used in fluent chaining. It exposes operations to:
 * <ul>
 *   <li>Set the logger’s log level using {@link #setLevel(Level)}</li>
 *   <li>Query the current and effective levels with {@link #getLevel()} and {@link #getEffectiveLevel()}</li>
 *   <li>Check if logging is enabled for specific levels or SLF4J {@link org.slf4j.Marker}s</li>
 *   <li>Build structured logging events fluently via SLF4J's {@link org.slf4j.spi.LoggingEventBuilder}</li>
 * </ul>
 *
 * <p>Using this class can help you make logging logic more dynamic and explicit
 * without needing to access the raw Logback API directly.
 *
 * <p><b>Example usage:</b>
 * <pre>{@code
 * JWLogger.of(MyClass.class)
 *     .config()
 *     .level()
 *     .setLevel(Level.DEBUG)
 *     .back()
 *     .appenders()
 *     .addAppender(Appenders.ExtendedConsoleAppender);
 * }</pre>
 *
 * <p>This class is part of the modular JWLogger fluent API
 * to improve developer ergonomics and maintain clear separation
 * of concerns for logger configuration.
 *
 * @see JWLogger
 * @see JWLoggerConfigMethods
 * @see JWLoggerAppenderMethods
 * @see ch.qos.logback.classic.Level
 * @see org.slf4j.Marker
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Data
@NoArgsConstructor(access = AccessLevel.NONE)
public class JWLoggerLevelMethods {

    @Getter(AccessLevel.NONE)
    private final JWLoggerConfigMethods methods;

    @Getter(AccessLevel.NONE)
    private final JWLogger jwLogger;

    private final Logger logger;

    @Contract(pure = true)
    JWLoggerLevelMethods(final @NotNull JWLoggerConfigMethods methods) {
        this.methods = methods;
        this.jwLogger = methods.getJwLogger();
        this.logger = jwLogger.getLogger();
    }

    /**
     * Sets the logger’s direct log level, overriding any inherited level from its parent.
     * <p>
     * This method also disables additivity, meaning log messages will not propagate
     * to parent loggers. This is often desired when applying fine-grained control over
     * individual loggers (e.g., plugin loggers or CLI tools).
     *
     * <p><strong>Note:</strong> If you want to inherit the log level from the parent,
     * use {@code getLogger().setLevel(null)} or {@link JWLoggerConfigMethods#reset()} instead.
     *
     * @param logLevel the explicit log level to apply (e.g., {@code Level.INFO}, {@code Level.DEBUG})
     * @return this instance for fluent chaining
     *
     * @apiNote Sets {@code logger.setAdditive(false)} automatically to prevent log duplication.
     */
    public JWLoggerLevelMethods setLevel(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        return this;
    }

    /**
     * Retrieves the log level explicitly assigned to this logger.
     * <p>
     * If this returns {@code null}, the logger inherits its level from the nearest
     * ancestor with a non-null level.
     *
     * @return the directly configured log level, or {@code null} if inherited
     */
    public Level getLevel() {
        return logger.getLevel();
    }

    /**
     * Retrieves the logger’s effective log level after resolving inheritance.
     * <p>
     * If no level is explicitly set on this logger, this method walks up the logger
     * hierarchy to determine the nearest non-null log level.
     *
     * @return the resolved log level currently in effect (never {@code null})
     */
    public Level getEffectiveLevel() {
        return logger.getEffectiveLevel();
    }

    //region Is Enabled Methods

    /**
     * Checks whether the logger instance is enabled for the specified Logback {@link Level}.
     *
     * @param level the logback log level to check
     * @return {@code true} if enabled, {@code false} otherwise
     */
    public boolean isEnabledFor(final Level level) {
        return logger.isEnabledFor(level);
    }

    /**
     * Checks whether the logger instance is enabled for the specified Logback {@link Level}
     * and SLF4J {@link Marker}.
     *
     * @param marker the marker to check against
     * @param level  the logback log level to check
     * @return {@code true} if enabled with the given marker, {@code false} otherwise
     */
    public boolean isEnabledFor(final Marker marker, final Level level) {
        return logger.isEnabledFor(marker, level);
    }

    /**
     * Checks whether the logger instance is enabled for the specified SLF4J {@link org.slf4j.event.Level}.
     *
     * @param level the SLF4J log level to check
     * @return {@code true} if enabled, {@code false} otherwise
     */
    public boolean isEnabledForLevel(final org.slf4j.event.Level level) {
        return logger.isEnabledForLevel(level);
    }

    /**
     * Checks whether the logger is enabled for the {@link Level#ERROR} level.
     *
     * @return {@code true} if error logging is enabled
     */
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    /**
     * Checks whether the logger is enabled for the {@link Level#WARN} level.
     *
     * @return {@code true} if warn logging is enabled
     */
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    /**
     * Checks whether the logger is enabled for the {@link Level#INFO} level.
     *
     * @return {@code true} if info logging is enabled
     */
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    /**
     * Checks whether the logger is enabled for the {@link Level#DEBUG} level.
     *
     * @return {@code true} if debug logging is enabled
     */
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    /**
     * Checks whether the logger is enabled for the {@link Level#TRACE} level.
     *
     * @return {@code true} if trace logging is enabled
     */
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    /**
     * Checks whether the logger is enabled for {@link Level#ERROR} with the given SLF4J {@link Marker}.
     *
     * @param marker the marker to check against
     * @return {@code true} if error logging is enabled with the marker
     */
    public boolean isErrorEnabled(final Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    /**
     * Checks whether the logger is enabled for {@link Level#WARN} with the given SLF4J {@link Marker}.
     *
     * @param marker the marker to check against
     * @return {@code true} if warn logging is enabled with the marker
     */
    public boolean isWarnEnabled(final Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    /**
     * Checks whether the logger is enabled for {@link Level#INFO} with the given SLF4J {@link Marker}.
     *
     * @param marker the marker to check against
     * @return {@code true} if info logging is enabled with the marker
     */
    public boolean isInfoEnabled(final Marker marker) {
        return logger.isInfoEnabled(marker);
    }


    /**
     * Checks whether the logger is enabled for {@link Level#DEBUG} with the given SLF4J {@link Marker}.
     *
     * @param marker the marker to check against
     * @return {@code true} if debug logging is enabled with the marker
     */
    public boolean isDebugEnabled(final Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    /**
     * Checks whether the logger is enabled for {@link Level#TRACE} with the given SLF4J {@link Marker}.
     *
     * @param marker the marker to check against
     * @return {@code true} if trace logging is enabled with the marker
     */
    public boolean isTraceEnabled(final Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    //endregion Is Enabled Methods

    //region At Level Event Builder Methods

    /**
     * Returns an event builder at the specified level for fluent message construction.
     *
     * <pre>{@code
     * logger.level().atLevel(Level.INFO).addMarker(marker).log("Hello");
     * }</pre>
     */
    @CheckReturnValue
    public LoggingEventBuilder atLevel(final org.slf4j.event.Level level) {
        return logger.atLevel(level);
    }

    /**
     * Returns an event builder at the ERROR level for fluent message construction.
     *
     * <pre>{@code
     * logger.level().atError().addMarker(marker).log("Hello");
     * }</pre>
     */
    @CheckReturnValue
    public LoggingEventBuilder atError() {
        return logger.atError();
    }

    /**
     * Returns an event builder at the WARN level for fluent message construction.
     *
     * <pre>{@code
     * logger.level().atWarn().addMarker(marker).log("Hello");
     * }</pre>
     */
    @CheckReturnValue
    public LoggingEventBuilder atWarn() {
        return logger.atWarn();
    }

    /**
     * Returns an event builder at the INFO level for fluent message construction.
     *
     * <pre>{@code
     * logger.level().atInfo().addMarker(marker).log("Hello");
     * }</pre>
     */
    @CheckReturnValue
    public LoggingEventBuilder atInfo() {
        return logger.atInfo();
    }

    /**
     * Returns an event builder at the DEBUG level for fluent message construction.
     *
     * <pre>{@code
     * logger.level().atDebug().addMarker(marker).log("Hello");
     * }</pre>
     */
    @CheckReturnValue
    public LoggingEventBuilder atDebug() {
        return logger.atDebug();
    }

    /**
     * Returns an event builder at the TRACE level for fluent message construction.
     *
     * <pre>{@code
     * logger.level().atTrace().addMarker(marker).log("Hello");
     * }</pre>
     */
    @CheckReturnValue
    public LoggingEventBuilder atTrace() {
        return logger.atTrace();
    }

    //endregion At Level Event Builder Methods

    /**
     * Returns the parent {@link JWLoggerConfigMethods} instance to continue fluent chaining.
     *
     * @return the parent JWLoggerConfigMethods instance
     */
    public JWLoggerConfigMethods back() {
        return methods;
    }
}
