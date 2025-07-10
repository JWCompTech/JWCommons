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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A fluent wrapper around a single Logback {@link ch.qos.logback.classic.Logger} instance
 * for more readable and expressive logger configuration.
 *
 * <p>Use this class to dynamically control a logger’s level, attach/detach appenders,
 * and apply specific encoder-based output patterns.
 *
 * <p>Supports adding predefined {@link Appenders}, building new console appenders,
 * and enabling extended logging output with or without MDC.
 *
 * <p>Example usage:
 * <pre>{@code
 * JWLogger.of(MyClass.class)
 *     .useBasicConsole(Level.INFO)
 *     .addAppender(Appenders.BasicFileAppender);
 * }</pre>
 *
 * <p>This class is intended for advanced use-cases where logger configuration
 * needs to be programmatic or dynamic (e.g., in plugin systems or CLI tools).
 *
 * @see LoggingManager
 * @see Appenders
 * @see Encoders
 * @see MDCManager
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JWLogger {
    Logger logger;

    /**
     * Creates a new instance of LoggerConfig with the specified logger object.
     *
     * @param logger the logger object to set
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull JWLogger of(final Logger logger) {
        return new JWLogger(logger);
    }

    /**
     * Creates a new instance of LoggerConfig with the specified logger name.
     *
     * @param className the name of the logger object to set
     * @apiNote calls {@code LoggingManager.getContext().getLogger(Class)} to retrieve
     * the logger instance and then stores it.
     * <p>
     * See: {@link LoggingManager#getContext()} for details.
     */
    @Contract("_ -> new")
    public static @NotNull JWLogger of(final String className) {
        return new JWLogger(LoggingManager.getContext().getLogger(className));
    }

    /**
     * Creates a new instance of LoggerConfig with the specified logger name.
     *
     * @param clazz the class of the logger object to set
     * @apiNote calls {@code LoggingManager.getContext().getLogger(Class)} to retrieve
     * the logger instance and then stores it.
     * <p>
     * See: {@link LoggingManager#getContext()} for details.
     */
    @Contract("_ -> new")
    public static @NotNull JWLogger of(final Class<?> clazz) {
        return new JWLogger(LoggingManager.getContext().getLogger(clazz));
    }

    /**
     * Creates a new instance of LoggerConfig with the specified logger name.
     *
     * @param packageObj the package of the logger object to set
     * @apiNote calls {@code LoggingManager.getContext().getLogger(Class)} to retrieve
     * the logger instance and then stores it.
     * <p>
     * See: {@link LoggingManager#getContext()} for details.
     */
    @Contract("_ -> new")
    public static @NotNull JWLogger of(final @NotNull Package packageObj) {
        return new JWLogger(LoggingManager.getContext().getLogger(packageObj.getName()));
    }

    /**
     * Returns the name of the logger.
     *
     * @return the name of the logger
     */
    public String getName() {
        return logger.getName();
    }

    /**
     * Configures the log level of the logger.
     *
     * @param logLevel the log level to set
     * @return this instance
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLogger setLogLevel(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        return this;
    }

    /**
     * Returns the log level of the logger.
     *
     * @return the log level of the logger
     */
    public Level getLogLevel() {
        return logger.getLevel();
    }

    /**
     * Resets the logger level and appenders to inherit from parent.
     *
     * @return this instance
     */
    public JWLogger reset() {
        logger.setLevel(null); // null means inherit
        logger.setAdditive(true);
        logger.detachAndStopAllAppenders();
        return this;
    }

    /**
     * Configures the logger to use the specified log level and adds
     * the {@link Appenders#LimitedConsoleAppender}.
     *
     * @param logLevel the log level to set
     * @return this instance
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLogger useLimitedConsole(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        attachIfAbsent(Appenders.LimitedConsoleAppender.getAppender());
        return this;
    }

    /**
     * Configures the logger to use the specified log level and adds
     * the {@link Appenders#BasicConsoleAppender}.
     *
     * @param logLevel the log level to set
     * @return this instance
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLogger useBasicConsole(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        attachIfAbsent(Appenders.BasicConsoleAppender.getAppender());
        return this;
    }

    /**
     * Configures the logger to use the specified log level and adds
     * the {@link Appenders#ExtendedConsoleAppender}.
     *
     * @param logLevel the log level to set
     * @return this instance
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLogger useExtendedConsole(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        attachIfAbsent(Appenders.ExtendedConsoleAppender.getAppender());
        return this;
    }

    /**
     * Disables the logger by setting the log level to {@link Level#OFF}.
     *
     * @return this instance
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLogger disable() {
        logger.setAdditive(false);
        logger.setLevel(Level.OFF);
        return this;
    }

    /**
     * Adds the specified {@link Appender} to the logger.
     *
     * @param appender the appender to add
     * @return this instance
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLogger addAppender(final @NotNull Appenders appender) {
        logger.setAdditive(false);
        attachIfAbsent(appender.getAppender());
        return this;
    }

    /**
     * Adds the specified {@link Appender} to the logger.
     *
     * @param newAppender the appender to add
     * @return this instance
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLogger addAppender(final Appender<ILoggingEvent> newAppender) {
        logger.setAdditive(false);
        attachIfAbsent(newAppender);
        return this;
    }

    private void attachIfAbsent(final Appender<ILoggingEvent> appender) {
        if (!hasAppender(appender)) {
            logger.addAppender(appender);
        }
    }

    /**
     * Adds a new {@link ConsoleAppender} with the encoder from {@link Encoders#BasicEncoder}
     * and sets the name to "console".
     *
     * @return this instance
     * @apiNote The start method is automatically called at the end of this method.
     */
    public JWLogger addNewConsoleAppender() {
        return addNewConsoleAppender("console", Encoders.BasicEncoder);
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified encoder and sets the name to "console".
     *
     * @param encoder the encoder to set
     * @return this instance
     * @apiNote The start method is automatically called at the end of this method.
     */
    public JWLogger addNewConsoleAppender(final @NotNull Encoders encoder) {
        return addNewConsoleAppender(encoder.getEncoder());
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified encoder (e.g. PatternLayoutEncoder) and sets the name to "console".
     *
     * @param encoder the encoder to set
     * @return this instance
     * @apiNote The start method is automatically called at the end of this method.
     */
    public JWLogger addNewConsoleAppender(final Encoder<ILoggingEvent> encoder) {
        return addNewConsoleAppender("console", encoder);
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified name and encoder.
     *
     * @param name the name to set
     * @param encoder the encoder to set
     * @return this instance
     * @apiNote if the name value is null or empty the default is "console" and
     * if encoder is null then the encoder from
     * {@link Encoders#BasicEncoder} is used instead.
     * Also, the start method is automatically run at the end of this method.
     */
    public JWLogger addNewConsoleAppender(final String name, final @NotNull Encoders encoder) {
        return addNewConsoleAppender(name, encoder.getEncoder());
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified name and encoder.
     *
     * @param name the name to set
     * @param encoder the encoder to set
     * @return this instance
     * @apiNote if the name value is null or empty the default is "console" and
     * if encoder is null then the encoder from
     * {@link Encoders#BasicEncoder} is used instead.
     * Also, the start method is automatically run at the end of this method.
     */
    public JWLogger addNewConsoleAppender(final String name, final Encoder<ILoggingEvent> encoder) {
        return addAppender(LoggingManager.createNewConsoleAppender(name, encoder));
    }

    /**
     * Removes the specified appender.
     *
     * @param name the name of the appender to remove
     * @return true if no errors occurred
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean removeAppender(final String name) {
        return logger.detachAppender(name);
    }

    /**
     * Removes the specified appender.
     *
     * @param appender the appender to remove
     * @return true if no errors occurred
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean removeAppender(final Appender<ILoggingEvent> appender) {
        return logger.detachAppender(appender);
    }

    /**
     * Checks if the logger has the specified appender.
     *
     * @param name the name of the appender to lookup
     * @return true if exists
     */
    public boolean hasAppender(final String name) {
        return logger.getAppender(name) != null;
    }

    /**
     * Checks if the logger has the specified appender.
     *
     * @param appender the appender to lookup
     * @return true if exists
     */
    public boolean hasAppender(final Appender<ILoggingEvent> appender) {
        return logger.isAttached(appender);
    }

    /**
     * Returns the specified appender.
     *
     * @param name the name of the appender to lookup
     * @return the appender instance
     */
    public Appender<ILoggingEvent> getAppender(final String name) {
        return logger.getAppender(name);
    }
}
