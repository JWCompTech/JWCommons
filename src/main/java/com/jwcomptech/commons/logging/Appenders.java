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

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@AllArgsConstructor
@ToString
public enum Appenders {
    /** A console extender that uses the {@link Encoders#LimitedEncoder}. */
    LimitedConsoleAppender(LoggingManager.createNewConsoleAppender(Encoders.LimitedEncoder)),
    /** A console extender that uses the {@link Encoders#BasicEncoder}. */
    BasicConsoleAppender(LoggingManager.createNewConsoleAppender(Encoders.BasicEncoder)),
    /** A console extender that uses the {@link Encoders#ExtendedEncoder}. */
    ExtendedConsoleAppender(LoggingManager.createNewConsoleAppender(Encoders.ExtendedEncoder));

    private final ConsoleAppender<ILoggingEvent> appender;
}
