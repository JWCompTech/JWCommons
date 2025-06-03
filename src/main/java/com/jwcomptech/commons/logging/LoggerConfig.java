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
import com.jwcomptech.commons.Literals;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;
import static com.jwcomptech.commons.utils.StringUtils.isBlank;

/**
 * Contains methods to manage a Logback logger instance.
 *
 * @since 0.0.1
 */

@SuppressWarnings({"unused", "UnusedReturnValue"})
@Value
public class LoggerConfig {
    Logger logger;

    /**
     * Creates a new instance of LoggerConfig with the specified logger object.
     * @param logger the logger object to set
     */
    public LoggerConfig(final Logger logger) {
        this.logger = logger;
        disable();
    }

    /**
     * Creates a new instance of LoggerConfig with the specified logger name.
     * @param className the name of the logger object to set
     */
    public LoggerConfig(final String className) {
        logger = LoggingManager.getContext().getLogger(className);
        disable();
    }

    /**
     * Enables the logger to use the specified log level.
     * @apiNote Additive is automatically disabled in this method
     * @param logLevel the log level to set
     * @return this instance
     */
    public LoggerConfig enable(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        return this;
    }

    /**
     * Enables the logger to use the specified log level and adds
     * the {@link Appenders#LimitedConsoleAppender}.
     * @apiNote Additive is automatically disabled in this method
     * @param logLevel the log level to set
     * @return this instance
     */
    public LoggerConfig enableLimitedConsole(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        if(!logger.isAttached(Appenders.LimitedConsoleAppender.getAppender())) {
            logger.addAppender(Appenders.LimitedConsoleAppender.getAppender());
        }
        return this;
    }

    /**
     * Enables the logger to use the specified log level and adds
     * the {@link Appenders#BasicConsoleAppender}.
     * @apiNote Additive is automatically disabled in this method
     * @param logLevel the log level to set
     * @return this instance
     */
    public LoggerConfig enableBasicConsole(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        if(!logger.isAttached(Appenders.BasicConsoleAppender.getAppender())) {
            logger.addAppender(Appenders.BasicConsoleAppender.getAppender());
        }
        return this;
    }

    /**
     * Enables the logger to use the specified log level and adds
     * the {@link Appenders#ExtendedConsoleAppender}.
     * @apiNote Additive is automatically disabled in this method
     * @param logLevel the log level to set
     * @return this instance
     */
    public LoggerConfig enableExtendedConsole(final Level logLevel) {
        logger.setAdditive(false);
        logger.setLevel(logLevel);
        if(!logger.isAttached(Appenders.ExtendedConsoleAppender.getAppender())) {
            logger.addAppender(Appenders.ExtendedConsoleAppender.getAppender());
        }
        return this;
    }

    /**
     * Disables the logger by setting the log level to {@link Level#OFF}.
     * @apiNote Additive is automatically disabled in this method
     * @return this instance
     */
    public LoggerConfig disable() {
        logger.setAdditive(false);
        logger.setLevel(Level.OFF);
        return this;
    }

    /**
     * Adds the specified {@link Appender} to the logger.
     * @apiNote Additive is automatically disabled in this method
     * @param appender the appender to add
     * @return this instance
     */
    public LoggerConfig addAppender(final @NotNull Appenders appender) {
        logger.setAdditive(false);
        if(!logger.isAttached(appender.getAppender())) {
            logger.addAppender(appender.getAppender());
        }
        return this;
    }

    /**
     * Adds the specified {@link Appender} to the logger.
     * @apiNote Additive is automatically disabled in this method
     * @param newAppender the appender to add
     * @return this instance
     */
    public LoggerConfig addAppender(final Appender<ILoggingEvent> newAppender) {
        logger.setAdditive(false);
        if(!logger.isAttached(newAppender)) {
            logger.addAppender(newAppender);
        }
        return this;
    }

    /**
     * Adds a new {@link ConsoleAppender} with the encoder from {@link Encoders#BasicEncoder}
     * and sets the name to "console".
     * @apiNote The start method is automatically called at the end of this method.
     * @return this instance
     */
    public LoggerConfig addNewConsoleAppender() {
        return addNewConsoleAppender("console", Encoders.BasicEncoder);
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified encoder and sets the name to "console".
     * @apiNote The start method is automatically run at the end of this method.
     * @param encoder the encoder to set
     * @return this instance
     */
    public LoggerConfig addNewConsoleAppender(final @NotNull Encoders encoder) {
        return addNewConsoleAppender(encoder.getEncoder());
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified encoder (e.g. PatternLayoutEncoder) and sets the name to "console".
     * @apiNote The start method is automatically run at the end of this method.
     * @param encoder the encoder to set
     * @return this instance
     */
    public LoggerConfig addNewConsoleAppender(final Encoder<ILoggingEvent> encoder) {
        return addNewConsoleAppender("console", encoder);
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified name and encoder.
     * @apiNote if the name value is null or empty the default is "console" and
     * if encoder is null then the encoder from
     * {@link Encoders#BasicEncoder} is used instead.
     * Also, the start method is automatically run at the end of this method.
     * @param name the name to set
     * @param encoder the encoder to set
     * @return this instance
     */
    public LoggerConfig addNewConsoleAppender(final String name, final @NotNull Encoders encoder) {
        return addNewConsoleAppender(name, encoder.getEncoder());
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified name and encoder.
     * @apiNote if the name value is null or empty the default is "console" and
     * if encoder is null then the encoder from
     * {@link Encoders#BasicEncoder} is used instead.
     * Also, the start method is automatically run at the end of this method.
     * @param name the name to set
     * @param encoder the encoder to set
     * @return this instance
     */
    public LoggerConfig addNewConsoleAppender(final String name, final Encoder<ILoggingEvent> encoder) {
        checkArgumentNotNullOrEmpty(name, Literals.cannotBeNullOrEmpty("name"));
        final ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<>();
        logConsoleAppender.setContext(LoggingManager.getContext());
        if (isBlank(name)) {
            logConsoleAppender.setName("console");
        } else {
            logConsoleAppender.setName(name);
        }
        if (encoder == null) {
            logConsoleAppender.setEncoder(Encoders.BasicEncoder.getEncoder());
        } else {
            logConsoleAppender.setEncoder(encoder);
        }
        logConsoleAppender.start();
        return addAppender(logConsoleAppender);
    }

    /**
     * Returns the name of the logger.
     * @return the name of the logger
     */
    public String getName() {
        return logger.getName();
    }

    /**
     * Removes the specified appender.
     * @param name the name of the appender to remove
     * @return true if no errors occurred
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean removeAppender(final String name) {
        return logger.detachAppender(name);
    }

    /**
     * Removes the specified appender.
     * @param appender the appender to remove
     * @return true if no errors occurred
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean removeAppender(final Appender<ILoggingEvent> appender) {
        return logger.detachAppender(appender);
    }

    /**
     * Checks if the logger has the specified appender.
     * @param name the name of the appender to lookup
     * @return true if exists
     */
    public boolean hasAppender(final String name) {
        return logger.getAppender(name) != null;
    }

    /**
     * Checks if the logger has the specified appender.
     * @param appender the appender to lookup
     * @return true if exists
     */
    public boolean hasAppender(final Appender<ILoggingEvent> appender) {
        return logger.isAttached(appender);
    }

    /**
     * Returns the specified appender.
     * @param name the name of the appender to lookup
     * @return the appender instance
     */
    public Appender<ILoggingEvent> getAppender(final String name) {
        return logger.getAppender(name);
    }
}
