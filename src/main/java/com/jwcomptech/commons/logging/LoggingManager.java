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
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.jwcomptech.commons.interfaces.Buildable;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;
import java.util.function.Supplier;

import static com.jwcomptech.commons.utils.StringUtils.isBlank;

/**
 * Central utility class for managing application-wide logging configuration.
 *
 * <p>This class provides methods to configure and control Logback loggers dynamically
 * at runtime. It includes support for:
 * <ul>
 *   <li>Setting log levels by name, class, or package</li>
 *   <li>Attaching or detaching appenders</li>
 *   <li>Creating new appenders and encoders</li>
 *   <li>Enabling/disabling specific loggers</li>
 *   <li>Creating {@link JWLogger} wrappers for fluent configuration</li>
 *   <li>Optionally including MDC key/value pairs in logs</li>
 * </ul>
 *
 * <p>This class delegates to the Logback {@link ch.qos.logback.classic.LoggerContext}
 * and works seamlessly with SLF4J’s API.
 *
 * <p>Common usage examples include:
 * <pre>{@code
 * LoggingManager.setLoggerLevel("my.package", Level.DEBUG);
 * LoggingManager.enableSpecificClassLogging(MyClass.class, Level.INFO);
 * }</pre>
 *
 * <p>For fluent, chainable logger setup, see {@link JWLogger}.
 *
 * @apiNote If this class is used, a logback configuration file is not
 * required to be added to your project and if included that configuration
 * will be overwritten by the methods in this class. Also, all methods in
 * this class disable the logger additive setting to allow for more control
 * when using the loggers.
 * @see JWLogger
 * @see Appenders
 * @see Encoders
 * @see MDCManager
 *
 * @since 1.0.0-alpha
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggingManager {

    /** The Logback logger context */
    @Getter
    private static final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    public static final JWLogger ROOT = JWLogger.of(Logger.ROOT_LOGGER_NAME);

    /** A basic log file filename with name "logfile.log" */
    public static final String DEFAULT_LOG_FILE_NAME = "logfile.log";
    /** A basic log file filename with name "logfile-%d{yyyy-MM-dd_HH}.log" */
    public static final String DEFAULT_LOG_FILE_NAME_DATED = "logfile-%d{yyyy-MM-dd_HH}.log";

    /** The setting to use if you want an unlimited file history when building a TimeBasedRollingPolicy. */
    public static final int MAX_HISTORY_UNLIMITED = 0;

    /** The setting to use if you want an unlimited file cap when building a TimeBasedRollingPolicy. */
    public static final FileSize FILE_SIZE_UNLIMITED = new FileSize(0L);

    /**
     * Sets the log level for the specified logger name.
     *
     * @param name the logger name
     * @param logLevel the log level to set
     */
    public static void setLoggerLevel(final String name,
                                       final Level logLevel) {
        final Logger logger = context.getLogger(name);
        logger.setAdditive(false);
        logger.setLevel(logLevel);
    }

    /**
     * Gets the current log level for the specified logger name.
     *
     * @param name the logger name
     * @return the current log level, or null if not explicitly set
     */
    public static Level getLoggerLevel(final String name) {
        return context.getLogger(name).getLevel();
    }

    /**
     * Checks if the logger with the specified name is enabled.
     *
     * @param name the name of the logger to check
     * @return true if the logger with the specified name is enabled
     */
    public static boolean isLoggerEnabled(final String name) {
        final Level level = getLoggerLevel(name);
        return level != null && !level.equals(Level.OFF);
    }

    /**
     * Resets the logger to inherit settings from its parent.
     *
     * @param name the logger name
     */
    public static void resetLogger(final String name) {
        final Logger logger = context.getLogger(name);
        logger.setLevel(null); // null means inherit
        logger.setAdditive(true);
        logger.detachAndStopAllAppenders();
    }

    /**
     * Returns a list of all currently registered loggers.
     *
     * @return a list of all currently registered loggers
     */
    public static List<Logger> listAllLoggers() {
        return context.getLoggerList();
    }

    /**
     * Executes a task with the specified MDC key/value set.
     * The key will be removed after the task completes (even on error).
     *
     * @param key the MDC key
     * @param value the MDC value
     * @param task the task to run
     */
    public static void withMDC(final String key,
                               final String value,
                               final @NotNull Runnable task) {
        try (MDC.MDCCloseable ignored = MDCManager.put(key, value)) {
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
     */
    public static <T> T withMDC(final String key,
                                final String value,
                                final @NotNull Supplier<T> task) {
        try (MDC.MDCCloseable ignored = MDCManager.put(key, value)) {
            return task.get();
        }
    }

    /**
     * Enables the logger for the specified class to use the specified log level.
     *
     * @param className the logger name
     * @param logLevel the log level to set
     * @apiNote the name can be any name (class name, package name, custom name,
     * etc.) because Logback loggers don't specifically tie a class to
     * a logger, just it's name.
     */
    public static void enableSpecificClassLogging(final String className, final Level logLevel) {
        enableLoggerByName(className, logLevel);
    }

    /**
     * Enables the logger for the specified package to use the specified log level.
     *
     * @param packageName the logger name
     * @param logLevel the log level to set
     * @apiNote the name can be any name (class name, package name, custom name,
     * etc.) because Logback loggers don't specifically tie a class to
     * a logger, just it's name.
     */
    public static void enableSpecificPackageLogging(final String packageName,
                                                    final Level logLevel) {
        enableLoggerByName(packageName, logLevel);
    }

    /**
     * Enables the logger for the specified class to use the specified log level.
     *
     * @param clazz the class to use for the logger
     * @param logLevel the log level to set
     * @apiNote the name can be any name (class name, package name, custom name,
     * etc.) because Logback loggers don't specifically tie a class to
     * a logger, just it's name.
     */
    public static void enableSpecificClassLogging(final @NotNull Class<?> clazz, final Level logLevel) {
        enableLoggerByName(clazz.getName(), logLevel);
    }

    /**
     * Enables the logger for the specified package to use the specified log level.
     *
     * @param packageObj the package to use for the logger
     * @param logLevel the log level to set
     * @apiNote the name can be any name (class name, package name, custom name,
     * etc.) because Logback loggers don't specifically tie a class to
     * a logger, just it's name.
     */
    public static void enableSpecificPackageLogging(final @NotNull Package packageObj,
                                                    final Level logLevel) {
        enableLoggerByName(packageObj.getName(), logLevel);
    }

    /**
     * Enables the logger for the specified name to use the specified log level.
     *
     * @param name the logger name
     * @apiNote the name can be any name (class name, package name, custom name,
     * etc.) because Logback loggers don't specifically tie a class to
     * a logger, just it's name.
     */
    public static void enableLoggerByName(final String name,
                                          final Level logLevel) {
        setLoggerLevel(name, logLevel);
    }

    /**
     * Disables the logger for the specified name by setting the log level
     * to {@link Level#OFF}.
     *
     * @param name the logger name
     */
    public static void disableLoggerByName(final String name) {
        setLoggerLevel(name, Level.OFF);
    }

    /**
     * Adds the specified {@link Appender} to the logger for the specified class.
     *
     * @param className the logger name
     * @param newAppender the appender to add
     */
    public static void addSpecificClassLoggingAppender(final String className,
                                                       final Appender<ILoggingEvent> newAppender) {
        final Logger logger = context.getLogger(className);
        logger.setAdditive(false);
        logger.addAppender(newAppender);
    }

    /**
     * Creates a new logger with the specified log level and calls
     * {@link #createNewConsoleAppender()} to set a console appender.
     *
     * @param level the log level to set
     */
    public static void initDefaultConsoleLogger(final Level level) {
        final Logger root = context.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setAdditive(false);
        root.setLevel(level);
        root.addAppender(createNewConsoleAppender());
    }

    /**
     * Creates a new {@link PatternLayoutEncoder} with the specified pattern.
     *
     * @param pattern the String pattern
     * @return a new PatternLayoutEncoder instance
     * @apiNote The start method is automatically called at the end of this method.
     */
    public static @NotNull PatternLayoutEncoder createNewLogEncoder(final String pattern) {
        final PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(context);
        logEncoder.setPattern(pattern);
        logEncoder.start();
        return logEncoder;
    }

    /**
     * Creates a new {@link ConsoleAppender} with the
     * {@link Encoders#BasicEncoder} and sets the name to "console".
     *
     * @return a new ConsoleAppender instance
     * @apiNote The start method is automatically called at the end of this method.
     */
    public static @NotNull ConsoleAppender<ILoggingEvent> createNewConsoleAppender() {
        return createNewConsoleAppender("console", Encoders.BasicEncoder);
    }

    /**
     * Creates a new {@link ConsoleAppender} with the specified encoder and sets
     * the name to "console".
     *
     * @param encoder the encoder to set
     * @return a new ConsoleAppender instance
     * @apiNote The start method is automatically run at the end of this method.
     */
    public static @NotNull ConsoleAppender<ILoggingEvent> createNewConsoleAppender(final @NotNull Encoders encoder) {
        return createNewConsoleAppender(encoder.getEncoder());
    }

    /**
     * Creates a new {@link ConsoleAppender} with the specified encoder
     * (e.g. PatternLayoutEncoder) and sets the name to "console".
     *
     * @param encoder the encoder to set
     * @return a new ConsoleAppender instance
     * @apiNote The start method is automatically run at the end of this method.
     */
    public static @NotNull ConsoleAppender<ILoggingEvent> createNewConsoleAppender(final Encoder<ILoggingEvent> encoder) {
        return createNewConsoleAppender("console", encoder);
    }

    /**
     * Creates a new {@link ConsoleAppender} with the specified name and encoder.
     *
     * @param name the name to set
     * @param encoder the encoder to set
     * @return a new ConsoleAppender instance
     * @apiNote if the name value is null or empty the default is "console" and
     * if encoder is null then the {@link Encoders#BasicEncoder} is used instead.
     * Also, the start method is automatically run at the end of this method.
     */
    public static @NotNull ConsoleAppender<ILoggingEvent> createNewConsoleAppender(final String name,
                                                                                   final @NotNull Encoders encoder) {
        return createNewConsoleAppender(name, encoder.getEncoder());
    }

    /**
     * Creates a new {@link ConsoleAppender} with the specified name and encoder.
     *
     * @param name the name to set
     * @param encoder the encoder to set
     * @return a new ConsoleAppender instance
     * @apiNote if the name value is null or empty the default is "console" and
     * if encoder is null then the {@link Encoders#BasicEncoder} is used instead.
     * Also, the start method is automatically run at the end of this method.
     */
    public static @NotNull ConsoleAppender<ILoggingEvent> createNewConsoleAppender(final String name,
                                                                                   final Encoder<ILoggingEvent> encoder) {
        final ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<>();
        logConsoleAppender.setContext(context);
        logConsoleAppender.setName((name == null || isBlank(name)) ? "console" : name);
        logConsoleAppender.setEncoder(safeEncoder(encoder));
        logConsoleAppender.start();
        return logConsoleAppender;
    }

    private static @NotNull Encoder<ILoggingEvent> safeEncoder(@Nullable Encoder<ILoggingEvent> encoder) {
        return encoder == null ? Encoders.BasicEncoder.getEncoder() : encoder;
    }

    /**
     * Creates a new RollingFileAppender with the default values.
     *
     * @return a new RollingFileAppender instance
     */
    public static @NotNull RollingFileAppender<ILoggingEvent> createDefaultRollingFileAppender() {
        return getRollingFileAppenderBuilder().build();
    }

    /**
     * Gets a new RollingFileAppenderBuilder instance.
     *
     * @return a new RollingFileAppenderBuilder instance
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull RollingFileAppenderBuilder getRollingFileAppenderBuilder() {
        return new RollingFileAppenderBuilder();
    }

    /**
     * This class contains methods to build a RollingFileAppender.
     */
    @Data
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class RollingFileAppenderBuilder implements Buildable<RollingFileAppender<ILoggingEvent>> {
        private String name = "logFile";
        private TimeBasedRollingPolicy<ILoggingEvent> logFilePolicy = createDefaultTimeBasedRollingPolicy();
        private PatternLayoutEncoder encoder = Encoders.BasicEncoder.getEncoder();
        private String fileName = DEFAULT_LOG_FILE_NAME_DATED;

        /**
         * Sets the name of the appender.
         *
         * @param name the name to set
         * @return this instance
         */
        public RollingFileAppenderBuilder setName(final String name) {
            if(name != null && !isBlank(name)) this.name = name;
            return this;
        }

        /**
         * Sets the TimeBasedRollingPolicy.
         *
         * @param logFilePolicy the policy to set
         * @return this instance
         * @apiNote This must be set or the build method will
         * throw an IllegalArgumentException.
         */
        public RollingFileAppenderBuilder setLogFilePolicy(final TimeBasedRollingPolicy<ILoggingEvent> logFilePolicy) {
            if(logFilePolicy != null) this.logFilePolicy = logFilePolicy;
            return this;
        }

        /**
         * Sets the PatternLayoutEncoder.
         *
         * @param encoder the encoder to set
         * @return this instance
         */
        public RollingFileAppenderBuilder setEncoder(final PatternLayoutEncoder encoder) {
            if(encoder != null) this.encoder = encoder;
            return this;
        }

        /**
         * Sets the filename.
         *
         * @param fileName the filename to set
         * @return this instance
         */
        public RollingFileAppenderBuilder setFileName(final String fileName) {
            if(fileName != null && !isBlank(fileName)) this.fileName = fileName;
            return this;
        }

        /**
         * Builds a new RollingFileAppender instance.
         *
         * @return a new RollingFileAppender instance
         * @apiNote If the name is not set the default is "logFile" and
         * if the encoder is not set the {@link Encoders#BasicEncoder} is used instead.
         * The start method on both the TimeBasedRollingPolicy
         * and the new RollingFileAppender are called automatically.
         */
        @Override
        public @NotNull RollingFileAppender<ILoggingEvent> build() {
            final RollingFileAppender<ILoggingEvent> logFileAppender = new RollingFileAppender<>();
            logFileAppender.setContext(context);
            logFileAppender.setAppend(true);
            logFileAppender.setName(name);
            logFileAppender.setEncoder(encoder);
            logFileAppender.setFile(fileName);
            logFilePolicy.setParent(logFileAppender);
            logFileAppender.setRollingPolicy(logFilePolicy);
            logFileAppender.start();
            return logFileAppender;
        }
    }

    public static @NotNull TimeBasedRollingPolicy<ILoggingEvent> createDefaultTimeBasedRollingPolicy() {
        return new TimeBasedRollingPolicyBuilder().build();
    }

    /**
     * Gets a new TimeBasedRollingPolicyBuilder instance.
     *
     * @return a new TimeBasedRollingPolicyBuilder instance
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull TimeBasedRollingPolicyBuilder getTimeBasedRollingPolicyBuilder() {
        return new TimeBasedRollingPolicyBuilder();
    }

    /**
     * This class contains methods to build a TimeBasedRollingPolicy.
     */
    @Data
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class TimeBasedRollingPolicyBuilder implements Buildable<TimeBasedRollingPolicy<ILoggingEvent>> {
        private String fileNamePattern = DEFAULT_LOG_FILE_NAME_DATED;
        private FileSize totalSizeCap = FILE_SIZE_UNLIMITED;
        private int maxFileHistory = MAX_HISTORY_UNLIMITED;
        private boolean cleanHistoryOnStart = false;
        private TimeBasedFileNamingAndTriggeringPolicy<ILoggingEvent> timeBasedFileNamingAndTriggeringPolicy =
                new DefaultTimeBasedFileNamingAndTriggeringPolicy<>();
        private FileAppender<ILoggingEvent> parent = null;

        /**
         * Sets the fileName pattern.
         *
         * @param fileNamePattern the pattern to set
         * @return this instance
         */
        public TimeBasedRollingPolicyBuilder setFileNamePattern(final String fileNamePattern) {
            if(fileNamePattern != null && !isBlank(fileNamePattern)) {
                this.fileNamePattern = fileNamePattern;
            }
            return this;
        }

        /**
         * Sets the total size cap parsed from a string.
         *
         * @param totalSizeCap the total size cap to set
         * @return this instance
         */
        public TimeBasedRollingPolicyBuilder setTotalSizeCap(final String totalSizeCap) {
            if(totalSizeCap != null && !isBlank(totalSizeCap)) {
                this.totalSizeCap = FileSize.valueOf(totalSizeCap);
            }
            return this;
        }

        /**
         * Sets the total size cap parsed from a {@link FileSize} object.
         *
         * @param totalSizeCap the total size cap to set
         * @return this instance
         */
        public TimeBasedRollingPolicyBuilder setTotalSizeCap(final FileSize totalSizeCap) {
            if(totalSizeCap != null) this.totalSizeCap = totalSizeCap;
            return this;
        }

        /**
         * Sets the maximum number of archive files to keep.
         *
         * @param maxFileHistory the value to set
         * @return this instance
         * @apiNote if this value is less than 0 then this setting is ignored.
         */
        public TimeBasedRollingPolicyBuilder setMaxFileHistory(final int maxFileHistory) {
            this.maxFileHistory = maxFileHistory;
            return this;
        }

        /**
         * Sets if archive removal should be attempted on application start up.
         *
         * @param cleanHistoryOnStart the value to set
         * @return this instance
         */
        public TimeBasedRollingPolicyBuilder setCleanHistoryOnStart(final boolean cleanHistoryOnStart) {
            this.cleanHistoryOnStart = cleanHistoryOnStart;
            return this;
        }

        /**
         * Sets the TimeBasedFileNamingAndTriggeringPolicy.
         *
         * @param timeBasedTriggering the policy to set
         * @return this instance
         */
        public TimeBasedRollingPolicyBuilder setTimeBasedFileNamingAndTriggeringPolicy(
                final TimeBasedFileNamingAndTriggeringPolicy<ILoggingEvent> timeBasedTriggering) {
            this.timeBasedFileNamingAndTriggeringPolicy = timeBasedTriggering;
            return this;
        }

        /**
         * Sets the parent FileAppender.
         *
         * @param appender the parent appender to set
         * @return this instance
         * @apiNote This method is not necessary if passing this object to the
         * {@link RollingFileAppenderBuilder#setLogFilePolicy(TimeBasedRollingPolicy)}
         * method as this value is set automatically during the build method.
         */
        public TimeBasedRollingPolicyBuilder setParent(final FileAppender<ILoggingEvent> appender) {
            this.parent = appender;
            return this;
        }

        /**
         * Builds a new TimeBasedRollingPolicy instance.
         *
         * @return a new TimeBasedRollingPolicy instance
         * @apiNote The start method is not called automatically to allow adding the
         * policy to a RollingFileAppender first.
         */
        @Override
        public @NotNull TimeBasedRollingPolicy<ILoggingEvent> build() {
            final TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
            rollingPolicy.setContext(context);
            rollingPolicy.setFileNamePattern(fileNamePattern);
            rollingPolicy.setTotalSizeCap(totalSizeCap);
            rollingPolicy.setMaxHistory(maxFileHistory);
            rollingPolicy.setCleanHistoryOnStart(cleanHistoryOnStart);
            rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(timeBasedFileNamingAndTriggeringPolicy);
            if(parent != null) rollingPolicy.setParent(parent);
            return rollingPolicy;
        }
    }
}
