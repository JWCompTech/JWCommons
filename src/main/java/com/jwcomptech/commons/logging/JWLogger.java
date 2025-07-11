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
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Marker;
import org.slf4j.event.LoggingEvent;
import org.slf4j.spi.LoggingEventBuilder;

import java.util.Iterator;

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
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.NONE)
public class JWLogger {
    private final Logger logger;

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

    public LoggerContext getLoggerContext() {
        return logger.getLoggerContext();
    }

    /**
     * Configures the log level of the logger.
     *
     * @param logLevel the log level to set
     * @return this instance
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLogger setLevel(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        return this;
    }

    /**
     * Returns the log level of the logger.
     *
     * @return the log level of the logger
     */
    public Level getLevel() {
        return logger.getLevel();
    }

    /**
     * Returns the effective log level of the logger.
     *
     * @return the effective log level of the logger
     */
    public Level getEffectiveLevel() {
        return logger.getEffectiveLevel();
    }

    //region Logging Methods

    /**
     * Logs an error-level message.
     *
     * @param msg the message to log
     */
    public void error(final String msg) {
        logger.error(msg);
    }

    /**
     * Logs an error-level message using the specified SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     */
    public void error(final Marker marker, final String msg) {
        logger.error(marker, msg);
    }

    /**
     * Logs an error-level message along with a {@link Throwable} cause.
     *
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void error(final String msg, final Throwable t) {
        logger.error(msg, t);
    }

    /**
     * Logs an error-level message with a throwable and an SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void error(final Marker marker, final String msg, final Throwable t) {
        logger.error(marker, msg, t);
    }

    /**
     * Logs an error-level formatted message with variable arguments.
     *
     * @param format the message format string
     * @param argArray the arguments to substitute into the format
     */
    public void error(final String format, final Object... argArray) {
        logger.error(format, argArray);
    }

    /**
     * Logs an error-level formatted message with one argument.
     *
     * @param format the format string
     * @param arg the argument to insert
     */
    public void error(final String format, final Object arg) {
        logger.error(format, arg);
    }

    /**
     * Logs an error-level formatted message with two arguments.
     *
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void error(final String format, final Object arg1, final Object arg2) {
        logger.error(format, arg1, arg2);
    }

    /**
     * Logs an error-level formatted message with a marker and multiple arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param argArray the arguments to insert
     */
    public void error(final Marker marker, final String format, final Object... argArray) {
        logger.error(marker, format, argArray);
    }

    /**
     * Logs an error-level formatted message with a marker and one argument.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg the argument to insert
     */
    public void error(final Marker marker, final String format, final Object arg) {
        logger.error(marker, format, arg);
    }

    /**
     * Logs an error-level formatted message with a marker and two arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
        logger.error(marker, format, arg1, arg2);
    }


    /**
     * Logs a warn-level message.
     *
     * @param msg the message to log
     */
    public void warn(final String msg) {
        logger.warn(msg);
    }

    /**
     * Logs a warn-level message using the specified SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     */
    public void warn(final Marker marker, final String msg) {
        logger.warn(marker, msg);
    }

    /**
     * Logs a warn-level message along with a {@link Throwable} cause.
     *
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void warn(final String msg, final Throwable t) {
        logger.warn(msg, t);
    }

    /**
     * Logs a warn-level message with a throwable and an SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void warn(final Marker marker, final String msg, final Throwable t) {
        logger.warn(marker, msg, t);
    }

    /**
     * Logs a warn-level formatted message with variable arguments.
     *
     * @param format the message format string
     * @param argArray the arguments to substitute into the format
     */
    public void warn(final String format, final Object... argArray) {
        logger.warn(format, argArray);
    }

    /**
     * Logs a warn-level formatted message with one argument.
     *
     * @param format the format string
     * @param arg the argument to insert
     */
    public void warn(final String format, final Object arg) {
        logger.warn(format, arg);
    }

    /**
     * Logs a warn-level formatted message with two arguments.
     *
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void warn(final String format, final Object arg1, final Object arg2) {
        logger.warn(format, arg1, arg2);
    }

    /**
     * Logs a warn-level formatted message with a marker and multiple arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param argArray the arguments to insert
     */
    public void warn(final Marker marker, final String format, final Object... argArray) {
        logger.warn(marker, format, argArray);
    }

    /**
     * Logs a warn-level formatted message with a marker and one argument.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg the argument to insert
     */
    public void warn(final Marker marker, final String format, final Object arg) {
        logger.warn(marker, format, arg);
    }

    /**
     * Logs a warn-level formatted message with a marker and two arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
        logger.warn(marker, format, arg1, arg2);
    }


    /**
     * Logs an info-level message.
     *
     * @param msg the message to log
     */
    public void info(final String msg) {
        logger.info(msg);
    }

    /**
     * Logs an info-level message using the specified SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     */
    public void info(final Marker marker, final String msg) {
        logger.info(marker, msg);
    }

    /**
     * Logs an info-level message along with a {@link Throwable} cause.
     *
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void info(final String msg, final Throwable t) {
        logger.info(msg, t);
    }

    /**
     * Logs an info-level message with a throwable and an SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void info(final Marker marker, final String msg, final Throwable t) {
        logger.info(marker, msg, t);
    }

    /**
     * Logs an info-level formatted message with variable arguments.
     *
     * @param format the message format string
     * @param argArray the arguments to substitute into the format
     */
    public void info(final String format, final Object... argArray) {
        logger.info(format, argArray);
    }

    /**
     * Logs an info-level formatted message with one argument.
     *
     * @param format the format string
     * @param arg the argument to insert
     */
    public void info(final String format, final Object arg) {
        logger.info(format, arg);
    }

    /**
     * Logs an info-level formatted message with two arguments.
     *
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void info(final String format, final Object arg1, final Object arg2) {
        logger.info(format, arg1, arg2);
    }

    /**
     * Logs an info-level formatted message with a marker and multiple arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param argArray the arguments to insert
     */
    public void info(final Marker marker, final String format, final Object... argArray) {
        logger.info(marker, format, argArray);
    }

    /**
     * Logs an info-level formatted message with a marker and one argument.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg the argument to insert
     */
    public void info(final Marker marker, final String format, final Object arg) {
        logger.info(marker, format, arg);
    }

    /**
     * Logs an info-level formatted message with a marker and two arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
        logger.info(marker, format, arg1, arg2);
    }


    /**
     * Logs a debug-level message.
     *
     * @param msg the message to log
     */
    public void debug(final String msg) {
        logger.debug(msg);
    }

    /**
     * Logs a debug-level message using the specified SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     */
    public void debug(final Marker marker, final String msg) {
        logger.debug(marker, msg);
    }

    /**
     * Logs a debug-level message along with a {@link Throwable} cause.
     *
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void debug(final String msg, final Throwable t) {
        logger.debug(msg, t);
    }

    /**
     * Logs a debug-level message with a throwable and an SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void debug(final Marker marker, final String msg, final Throwable t) {
        logger.debug(marker, msg, t);
    }

    /**
     * Logs a debug-level formatted message with variable arguments.
     *
     * @param format the message format string
     * @param argArray the arguments to substitute into the format
     */
    public void debug(final String format, final Object... argArray) {
        logger.debug(format, argArray);
    }

    /**
     * Logs a debug-level formatted message with one argument.
     *
     * @param format the format string
     * @param arg the argument to insert
     */
    public void debug(final String format, final Object arg) {
        logger.debug(format, arg);
    }

    /**
     * Logs a debug-level formatted message with two arguments.
     *
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void debug(final String format, final Object arg1, final Object arg2) {
        logger.debug(format, arg1, arg2);
    }

    /**
     * Logs a debug-level formatted message with a marker and multiple arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param argArray the arguments to insert
     */
    public void debug(final Marker marker, final String format, final Object... argArray) {
        logger.debug(marker, format, argArray);
    }

    /**
     * Logs a debug-level formatted message with a marker and one argument.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg the argument to insert
     */
    public void debug(final Marker marker, final String format, final Object arg) {
        logger.debug(marker, format, arg);
    }

    /**
     * Logs a debug-level formatted message with a marker and two arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
        logger.debug(marker, format, arg1, arg2);
    }


    /**
     * Logs a trace-level message.
     *
     * @param msg the message to log
     */
    public void trace(final String msg) {
        logger.trace(msg);
    }

    /**
     * Logs a trace-level message using the specified SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     */
    public void trace(final Marker marker, final String msg) {
        logger.trace(marker, msg);
    }

    /**
     * Logs a trace-level message along with a {@link Throwable} cause.
     *
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void trace(final String msg, final Throwable t) {
        logger.trace(msg, t);
    }

    /**
     * Logs a trace-level message with a throwable and an SLF4J marker.
     *
     * @param marker the marker associated with the log event
     * @param msg the message to log
     * @param t the exception to include in the log
     */
    public void trace(final Marker marker, final String msg, final Throwable t) {
        logger.trace(marker, msg, t);
    }

    /**
     * Logs a trace-level formatted message with variable arguments.
     *
     * @param format the message format string
     * @param argArray the arguments to substitute into the format
     */
    public void trace(final String format, final Object... argArray) {
        logger.trace(format, argArray);
    }

    /**
     * Logs a trace-level formatted message with one argument.
     *
     * @param format the format string
     * @param arg the argument to insert
     */
    public void trace(final String format, final Object arg) {
        logger.trace(format, arg);
    }

    /**
     * Logs a trace-level formatted message with two arguments.
     *
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void trace(final String format, final Object arg1, final Object arg2) {
        logger.trace(format, arg1, arg2);
    }

    /**
     * Logs a trace-level formatted message with a marker and multiple arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param argArray the arguments to insert
     */
    public void trace(final Marker marker, final String format, final Object... argArray) {
        logger.trace(marker, format, argArray);
    }

    /**
     * Logs a trace-level formatted message with a marker and one argument.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg the argument to insert
     */
    public void trace(final Marker marker, final String format, final Object arg) {
        logger.trace(marker, format, arg);
    }

    /**
     * Logs a trace-level formatted message with a marker and two arguments.
     *
     * @param marker the marker associated with the log event
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
        logger.trace(marker, format, arg1, arg2);
    }


    /**
     * Logs a fully constructed {@link LoggingEvent}.
     *
     * @param slf4jEvent the SLF4J logging event to dispatch
     */
    public void log(final LoggingEvent slf4jEvent) {
        logger.log(slf4jEvent);
    }

    /**
     * Logs a custom event at a specific log level, bypassing SLF4J formatting.
     *
     * @param marker the SLF4J marker associated with the log event
     * @param fqcn the fully qualified class name of the calling logger
     * @param levelInt the numeric level (e.g., {@code Level.INFO.toInt()})
     * @param message the log message
     * @param argArray optional format arguments
     * @param t optional throwable to include
     */
    public void log(final Marker marker, final String fqcn, final int levelInt, final String message, final Object[] argArray, final Throwable t) {
        logger.log(marker, fqcn, levelInt, message, argArray, t);
    }

    //endregion Logging Methods

    public JWLoggerConfigMethods config() {
        return new JWLoggerConfigMethods(this);
    }

    @Override
    public String toString() {
        return "JWLogger[" + getName() + "]";
    }
}
