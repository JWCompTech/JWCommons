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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * A fluent utility class for configuring and managing {@link Appender} instances
 * on a Logback {@link ch.qos.logback.classic.Logger} wrapped by {@link JWLogger}.
 *
 * <p>This class provides expressive, chainable methods to add, remove, inspect,
 * and configure log appenders—such as {@link ConsoleAppender}s—without needing
 * to directly interact with Logback’s lower-level APIs.
 *
 * <p>It is typically accessed via {@link JWLoggerConfigMethods#appenders()}
 * and is especially useful in dynamic environments like plugin systems,
 * CLI tools, or multi-module applications that require programmatic control
 * over logging outputs.
 *
 * <p>Features include:
 * <ul>
 *   <li>Attaching appenders with or without custom encoders</li>
 *   <li>Creating new {@link ConsoleAppender}s using presets from {@link Encoders}</li>
 *   <li>Safely attaching only if absent, and removing or checking appenders by name or reference</li>
 *   <li>Delegating back to the config stage via {@link #back()} for fluent transitions</li>
 * </ul>
 *
 * <p><b>Example usage:</b>
 * <pre>{@code
 * JWLogger.of(MyClass.class)
 *     .config()
 *     .appenders()
 *     .addAppender(Appenders.ExtendedConsoleAppender)
 *     .addNewConsoleAppender("specialConsole", Encoders.ExtendedEncoder)
 *     .back()
 *     .level()
 *     .setLevel(Level.DEBUG);
 * }</pre>
 *
 * @see JWLogger
 * @see JWLoggerConfigMethods
 * @see Appenders
 * @see Encoders
 * @see ch.qos.logback.core.Appender
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Data
@NoArgsConstructor(access = AccessLevel.NONE)
public class JWLoggerAppenderMethods {

    @Getter(AccessLevel.NONE)
    private final JWLoggerConfigMethods methods;

    @Getter(AccessLevel.NONE)
    private final JWLogger jwLogger;

    private final Logger logger;

    @Contract(pure = true)
    JWLoggerAppenderMethods(final @NotNull JWLoggerConfigMethods methods) {
        this.methods = methods;
        this.jwLogger = methods.getJwLogger();
        this.logger = jwLogger.getLogger();
    }

    /**
     * Adds the specified {@link Appender} to the logger.
     *
     * @param appender the appender to add
     * @return this instance for fluent chaining
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLoggerAppenderMethods addAppender(final @NotNull Appenders appender) {
        logger.setAdditive(false);
        attachIfAbsent(appender.getAppender());
        return this;
    }

    /**
     * Adds the specified {@link Appender} to the logger.
     *
     * @param newAppender the appender to add
     * @return this instance for fluent chaining
     * @apiNote Additive is automatically disabled in this method
     */
    public JWLoggerAppenderMethods addAppender(final Appender<ILoggingEvent> newAppender) {
        logger.setAdditive(false);
        attachIfAbsent(newAppender);
        return this;
    }

    void attachIfAbsent(final Appender<ILoggingEvent> appender) {
        if (!hasAppender(appender)) {
            logger.addAppender(appender);
        }
    }

    /**
     * Adds a new {@link ConsoleAppender} with the encoder from {@link Encoders#BasicEncoder}
     * and sets the name to "console".
     *
     * @return this instance for fluent chaining
     * @apiNote The start method is automatically called at the end of this method.
     */
    public JWLoggerAppenderMethods addNewConsoleAppender() {
        return addNewConsoleAppender("console", Encoders.BasicEncoder);
    }

    /**
     * Adds a new {@link ConsoleAppender} with the encoder from {@link Encoders#BasicEncoder}
     * and sets the name to the specified name.
     *
     * @param name the name to set
     * @return this instance for fluent chaining
     * @apiNote The start method is automatically called at the end of this method.
     */
    public JWLoggerAppenderMethods addNewConsoleAppender(final String name) {
        return addNewConsoleAppender(name, Encoders.BasicEncoder);
    }


    /**
     * Adds a new {@link ConsoleAppender} with the specified encoder and sets the name to "console".
     *
     * @param encoder the encoder to set
     * @return this instance for fluent chaining
     * @apiNote The start method is automatically called at the end of this method.
     */
    public JWLoggerAppenderMethods addNewConsoleAppender(final @NotNull Encoders encoder) {
        return addNewConsoleAppender(encoder.getEncoder());
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified encoder (e.g. PatternLayoutEncoder) and sets the name to "console".
     *
     * @param encoder the encoder to set
     * @return this instance for fluent chaining
     * @apiNote The start method is automatically called at the end of this method.
     */
    public JWLoggerAppenderMethods addNewConsoleAppender(final Encoder<ILoggingEvent> encoder) {
        return addNewConsoleAppender("console", encoder);
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified name and encoder.
     *
     * @param name the name to set
     * @param encoder the encoder to set
     * @return this instance for fluent chaining
     * @apiNote if the name value is null or empty the default is "console" and
     * if encoder is null then the encoder from
     * {@link Encoders#BasicEncoder} is used instead.
     * Also, the start method is automatically run at the end of this method.
     */
    public JWLoggerAppenderMethods addNewConsoleAppender(final String name, final @NotNull Encoders encoder) {
        return addNewConsoleAppender(name, encoder.getEncoder());
    }

    /**
     * Adds a new {@link ConsoleAppender} with the specified name and encoder.
     *
     * @param name the name to set
     * @param encoder the encoder to set
     * @return this instance for fluent chaining
     * @apiNote if the name value is null or empty the default is "console" and
     * if encoder is null then the encoder from
     * {@link Encoders#BasicEncoder} is used instead.
     * Also, the start method is automatically run at the end of this method.
     */
    public JWLoggerAppenderMethods addNewConsoleAppender(final String name, final Encoder<ILoggingEvent> encoder) {
        return addAppender(LoggingManager.createNewConsoleAppender(name, encoder));
    }

    /**
     * Removes the appender with the specified name from the logger.
     *
     * @param name the name of the appender to remove
     * @return {@code true} if the appender was successfully removed; {@code false} otherwise
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean removeAppender(final String name) {
        return logger.detachAppender(name);
    }

    /**
     * Removes the specified appender instance from the logger.
     *
     * @param appender the appender instance to remove
     * @return {@code true} if the appender was successfully removed; {@code false} otherwise
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean removeAppender(final Appender<ILoggingEvent> appender) {
        return logger.detachAppender(appender);
    }

    /**
     * Checks if an appender with the given name exists on the logger.
     *
     * @param name the name of the appender to check
     * @return {@code true} if the appender exists; {@code false} otherwise
     */
    public boolean hasAppender(final String name) {
        return logger.getAppender(name) != null;
    }

    /**
     * Checks if the specified appender instance is attached to the logger.
     *
     * @param appender the appender instance to check
     * @return {@code true} if the appender is attached; {@code false} otherwise
     */
    public boolean hasAppender(final Appender<ILoggingEvent> appender) {
        return logger.isAttached(appender);
    }

    /**
     * Retrieves the appender with the given name from the logger.
     *
     * @param name the name of the appender to retrieve
     * @return the appender instance, or {@code null} if not found
     */
    public Appender<ILoggingEvent> getAppender(final String name) {
        return logger.getAppender(name);
    }

    /**
     * Checks if the specified appender instance is currently attached.
     *
     * @param appender the appender to check
     * @return {@code true} if the appender is attached; {@code false} otherwise
     */
    public boolean isAttached(final Appender<ILoggingEvent> appender) {
        return logger.isAttached(appender);
    }

    /**
     * Detaches the specified appender from the logger.
     *
     * @param appender the appender to detach
     * @return {@code true} if the appender was detached; {@code false} otherwise
     */
    public boolean detachAppender(final Appender<ILoggingEvent> appender) {
        return logger.detachAppender(appender);
    }

    /**
     * Detaches the appender with the given name from the logger.
     *
     * @param name the name of the appender to detach
     * @return {@code true} if the appender was detached; {@code false} otherwise
     */
    public boolean detachAppender(final String name) {
        return logger.detachAppender(name);
    }

    /**
     * Returns an iterator over all appenders currently attached to the logger.
     *
     * @return an {@link Iterator} for all attached appenders
     */
    public Iterator<Appender<ILoggingEvent>> iteratorForAppenders() {
        return logger.iteratorForAppenders();
    }

    /**
     * Invokes all attached appenders with the provided logging event.
     *
     * @param event the logging event to process
     * @return this instance for fluent chaining
     */
    public JWLoggerAppenderMethods callAppenders(final ILoggingEvent event) {
        logger.callAppenders(event);
        return this;
    }

    /**
     * Detaches all appenders from the logger and calls {@code stop()} on each.
     * Useful for resetting the logger or cleaning up resources.
     *
     * @return this instance for fluent chaining
     */
    public JWLoggerAppenderMethods detachAndStopAllAppenders() {
        logger.detachAndStopAllAppenders();
        return this;
    }

    /**
     * Returns the parent {@link JWLoggerConfigMethods} instance to continue fluent chaining.
     *
     * @return the parent JWLoggerConfigMethods instance
     */
    public JWLoggerConfigMethods back() {
        return methods;
    }
}
