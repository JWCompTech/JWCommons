package com.jwcomptech.commons.logging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MDCManager {
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
    public static @Nullable MDC.MDCCloseable put(final String key, final String value) {
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
    public static @Nullable String get(final String key) {
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
    public static void remove(final String key) {
        if (!isNullOrBlank(key)) {
            MDC.remove(key);
        }
    }

    /**
     * Clears the entire MDC context.
     */
    public static void clear() {
        MDC.clear();
    }

    /**
     * Returns a copy of the current thread's context map, with keys and values of
     * type String. Returned value may be null.
     *
     * @return A copy of the current thread's context map. May be null.
     */
    public static Map<String, String> getCopyOfContextMap() {
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
    public static void setContextMap(final Map<String, String> contextMap) {
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
    public static MDCAdapter getMDCAdapter() {
        return MDC.getMDCAdapter();
    }

    /**
     * Returns a copy of the deque(stack) referenced by 'key'. May be null.
     *
     * @param key identifies the  stack
     * @return copy of stack referenced by 'key'. May be null.
     */
    public static Deque<String> getCopyOfDequeByKey(final @Nullable String key) {
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
    public static void with(final String key,
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
    public static <T> T with(final String key,
                                final String value,
                                final @NotNull Supplier<T> task) {
        try (MDC.MDCCloseable ignored = put(key, value)) {
            return task.get();
        }
    }

    private static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank(); // Java 11+
    }
}
