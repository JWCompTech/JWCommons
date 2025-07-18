package com.jwcomptech.commons.download;

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

import com.jwcomptech.commons.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Represents the current status of the HTTPDownloader.
 * @since 1.0.0-alpha
 */
@RequiredArgsConstructor
@Getter
@ToString
public enum DLStatus implements BaseEnum<String> {
    DOWNLOADING("Downloading"),
    PAUSED("Paused"),
    COMPLETE("Complete"),
    CANCELLED("Cancelled"),
    ERROR("Error"),
    IDLE("Idle");

    private final String value;
}
