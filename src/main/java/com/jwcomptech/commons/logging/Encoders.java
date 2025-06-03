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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@AllArgsConstructor
@ToString
public enum Encoders {
    /**
     * A {@link PatternLayoutEncoder} with the pattern "%msg%n".
     * @apiNote This encoder is automatically started after initialization.
     */
    LimitedEncoder(LoggingManager.createNewLogEncoder("%msg%n")),
    /**
     * A {@link PatternLayoutEncoder} with the pattern
     * "%-12d{YYYY-MM-dd HH:mm:ss} %level %logger{0} - %msg%n".
     * @apiNote This encoder is automatically started after initialization.
     */
    BasicEncoder(LoggingManager.createNewLogEncoder("%-12d{YYYY-MM-dd HH:mm:ss} %level %logger{0} - %msg%n")),
    /**
     * A {@link PatternLayoutEncoder} with the pattern
     * "%-12d{YYYY-MM-dd HH:mm:ss.SSS} [%thread] %level %logger{100} - %msg%n".
     * @apiNote This encoder is automatically started after initialization.
     */
    ExtendedEncoder(LoggingManager.createNewLogEncoder(
            "%-12d{YYYY-MM-dd HH:mm:ss.SSS} [%thread] %level %logger{100} - %msg%n"))
    ;

    private final PatternLayoutEncoder encoder;
}
