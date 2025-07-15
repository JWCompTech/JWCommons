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

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Defines reusable {@link ch.qos.logback.classic.encoder.PatternLayoutEncoder} configurations
 * for log formatting consistency.
 *
 * <p>Encoders control the format of log messages and can be used across multiple
 * {@link ch.qos.logback.core.Appender} instances.
 *
 * <p>This enum offers various formatting styles such as:
 * <ul>
 *   <li>{@link #LimitedEncoder} – minimal output, message only</li>
 *   <li>{@link #BasicEncoder} – includes timestamp, log level, logger name</li>
 *   <li>{@link #ExtendedEncoder} – includes timestamp with milliseconds, thread name, and full logger path</li>
 * </ul>
 *
 * <p>Each encoder is pre-started and ready to use in console or file appenders.
 *
 * @see Appenders
 * @see JWLogger
 * @see LoggingManager
 *
 * @since 1.0.0-alpha
 */
@Getter
@RequiredArgsConstructor
@ToString
public enum Encoders {
    /**
     * A {@link PatternLayoutEncoder} with the pattern "%msg%n".
     *
     * @apiNote This encoder is automatically started after initialization.
     */
    LimitedEncoder(LoggingManager.createNewLogEncoder("%msg%n")),
    /**
     * A {@link PatternLayoutEncoder} with the pattern
     * "%-12d{YYYY-MM-dd HH:mm:ss} %level %logger{0} %X - %msg%n".
     *
     * @apiNote This encoder is automatically started after initialization.
     */
    BasicEncoder(LoggingManager.createNewLogEncoder("%-12d{YYYY-MM-dd HH:mm:ss} %level %logger{0} %X - %msg%n")),
    /**
     * A {@link PatternLayoutEncoder} with the pattern
     * "%-12d{YYYY-MM-dd HH:mm:ss.SSS} [%thread] %level %logger{100} %X - %msg%n".
     *
     * @apiNote This encoder is automatically started after initialization.
     */
    ExtendedEncoder(LoggingManager.createNewLogEncoder(
            "%-12d{YYYY-MM-dd HH:mm:ss.SSS} [%thread] %level %logger{100} %X - %msg%n")),
    /**
     * A {@link PatternLayoutEncoder} with the pattern
     * "%level [%thread] %logger - %msg%n".
     *
     * @apiNote This encoder is automatically started after initialization.
     */
    CompactFileEncoder(LoggingManager.createNewLogEncoder(
            "%level [%thread] %logger - %msg%n")),
    /**
     * A {@link PatternLayoutEncoder} with the pattern
     * "%highlight(%-12d{HH:mm:ss.SSS}) [%thread] %highlight(%-5level) %cyan(%logger{36}) %X - %msg%n".
     *
     * @apiNote This encoder is automatically started after initialization.
     */
    ColoredEncoder(LoggingManager.createNewLogEncoder(
            "%highlight(%-12d{HH:mm:ss.SSS}) [%thread] %highlight(%-5level) %cyan(%logger{36}) %X - %msg%n")),

    /**
     * A {@link PatternLayoutEncoder} with the pattern "%ex%n".
     *
     * @apiNote This encoder is automatically started after initialization.
     */
    ExceptionOnlyEncoder(LoggingManager.createNewLogEncoder("%ex%n")),

    /**
     * A {@link PatternLayoutEncoder} with the pattern
     * "%date{ISO8601} [%thread] %-5level %logger{36} %X [%file:%line] %msg%n".
     *
     * @apiNote This encoder is automatically started after initialization.
     */
    DebugEncoder(LoggingManager.createNewLogEncoder(
            "%date{ISO8601} [%thread] %-5level %logger{36} %X [%file:%line] %msg%n")),

    SpringBootBasicEncoder(LoggingManager.createNewLogEncoder(
            "%d{yyyy-MM-dd HH:mm:ss} %5p ${PID:- } --- [%15.15t] %X %-40.40logger{39} : %m%n%ex"
    )),

    SpringBootExtendedEncoder(LoggingManager.createNewLogEncoder(
            "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p ${PID:- } --- [%15.15t] %X %-40.40logger{39} : %m%n%ex"
    )),

    SpringBootBasicColoredEncoder(LoggingManager.createNewSpringBootLogEncoder(
            "%clr(%d{yyyy-MM-dd HH:mm:ss}){faint} " +
                    "%clr([%15.15t]){faint} " +
                    "%clr(%X){faint} " +
                    "%clr(%-40.40logger{39}){cyan} %clr(:){faint} " +
                    "%clr(%5p) %clr(:){faint} %m%n%xwEx"
    )),

    //TODO: Add PID parsing
    SpringBootExtendedColoredEncoder(LoggingManager.createNewSpringBootLogEncoder(
            "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} " +
                    "%clr(${PID}){magenta} " +
                    "%clr([%15.15t]){faint} " +
                    "%clr(%X){faint} " +
                    "%clr(%-40.40logger{39}){cyan} %clr(:){faint} " +
                    "%clr(%5p) %clr(:){faint} %m%n%xwEx"
    )),
    ;

    private final LayoutWrappingEncoder<ILoggingEvent> encoder;
}
