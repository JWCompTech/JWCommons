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

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;

import java.util.Deque;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Delegates methods from the SLF4J MDC class in a safe way
 * checking to make sure keys are not null or empty.
 *
 * <p>
 * If the underlying logging system offers MDC functionality, then SLF4J's MDC
 * will delegate to the underlying system's MDC. Note that at
 * this time, only two logging systems, namely log4j and logback, offer MDC
 * functionality. For java.util.logging which does not support MDC,
 * {@link BasicMDCAdapter} will be used. For other systems, i.e. slf4j-simple
 * and slf4j-nop, {@link NOPMDCAdapter} will be used.
 *
 * <p>
 * Thus, as a SLF4J user, you can take advantage of MDC in the presence of log4j,
 * logback, or java.util.logging, but without forcing these systems as
 * dependencies upon your users.
 *
 * <p>
 * For more information on MDC please see the <a
 * href="http://logback.qos.ch/manual/mdc.html">chapter on MDC</a> in the
 * logback manual.
 *
 * @see MDC
 *
 * @since 1.0.0-alpha
 */
@Data
public final class MDCManager {

    private final JWLoggerConfigMethods methods;

    public MDCManager() {
        this.methods = null;
    }

    public MDCManager(final JWLoggerConfigMethods methods) {
        this.methods = methods;
    }

    /**
     * Puts a key-value pair into MDC.
     *
     * <p>
     * This method delegates all work to the MDC of the underlying logging system.
     * <p>
     * This method return a {@link AutoCloseable} object that can remove {@code key} when
     * {@code close} is called.
     *
     * <p>
     * Usage example:
     * {@code
     *   try(MDC.MDCCloseable closeable = MDCManager.put(key, value)) {
     *     ....
     *   }
     * }
     *
     * @return a closable object for use in try-with-resources,
     * will return null if key is null
     */
    public @Nullable MDC.MDCCloseable put(final String key, final String value) {
        if (!isNullOrBlank(key) && value != null) {
            return MDC.putCloseable(key, value);
        }

        return null;
    }

    /**
     * Get the diagnostic context identified by the {@code key} parameter.
     *
     * <p>
     * This method delegates all work to the MDC of the underlying logging system.
     *
     * @param key the key to retrieve the value of
     * @return the string value identified by the {@code key} parameter,
     * will return null if {@code key} is null
     */
    public @Nullable String get(final String key) {
        return isNullOrBlank(key) ? null : MDC.get(key);
    }

    /**
     * Remove the diagnostic context identified by the {@code key} parameter
     * using the underlying system's MDC implementation.
     *
     * @param key the key to remove
     * @apiNote This method does nothing if {@code key} is null
     * or if there is no previous value associated with {@code key}.
     */
    public void remove(final String key) {
        if (!isNullOrBlank(key)) {
            MDC.remove(key);
        }
    }

    /**
     * Clears the entire MDC context.
     */
    public void clear() {
        MDC.clear();
    }

    /**
     * Returns a copy of the current thread's context map, with keys and values of
     * type String. Returned value may be null.
     *
     * @return A copy of the current thread's context map. May be null.
     */
    public Map<String, String> getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }

    /**
     * Set the current thread's context map by first clearing any existing map and
     * then copying the map passed as parameter. The context map passed as
     * parameter must only contain keys and values of type String.
     * <p>
     * Null valued argument is allowed (since SLF4J version 2.0.0).
     *
     * @param contextMap
     *          must contain only keys and values of type String
     */
    public void setContextMap(final Map<String, String> contextMap) {
        MDC.setContextMap(contextMap);
    }

    /**
     * Returns the MDCAdapter instance currently in use.
     *
     * @return the MDCAdapter instance currently in use.
     * @apiNote if the MDCAdapter instance is null, then this method sets it to use
     * the adapter returned by the SLF4JProvider. However, in the vast majority of cases
     * the MDCAdapter will be set earlier (during initialization) by {@link LoggerFactory}.
     */
    public MDCAdapter getMDCAdapter() {
        return MDC.getMDCAdapter();
    }

    /**
     * Returns a copy of the deque(stack) referenced by 'key'. May be null.
     *
     * @param key identifies the  stack
     * @return copy of stack referenced by 'key'. May be null.
     */
    public Deque<String> getCopyOfDequeByKey(final @Nullable String key) {
        return MDC.getMDCAdapter().getCopyOfDequeByKey(key);
    }

    /**
     * Executes a task with the specified MDC key/value set.
     * The key will be removed after the task completes (even on error).
     *
     * @param key the MDC key
     * @param value the MDC value
     * @param task the task to run
     */
    public void with(final String key,
                               final String value,
                               final @NotNull Runnable task) {
        try (MDC.MDCCloseable ignored = put(key, value)) {
            task.run();
        }
    }

    /**
     * Executes a task with the specified MDC key/value set.
     * The key will be removed after the task completes (even on error).
     *
     * @param key the MDC key
     * @param value the MDC value
     * @param task the task to run
     * @return the value from the supplier task
     */
    public <T> T with(final String key,
                                final String value,
                                final @NotNull Supplier<T> task) {
        try (MDC.MDCCloseable ignored = put(key, value)) {
            return task.get();
        }
    }

    private static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank(); // Java 11+
    }

    /**
     * Returns to the parent {@link JWLoggerConfigMethods} instance if available.
     *
     * @return the parent JWLoggerConfigMethods instance, or null if unavailable
     */
    public JWLoggerConfigMethods back() {
        return methods;
    }
}
