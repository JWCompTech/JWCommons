package com.jwcomptech.commons.consts;

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

import com.jwcomptech.commons.utils.Parse;
import com.jwcomptech.commons.utils.StringUtils;

import java.util.*;

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

/**
 * Constant values for use across entire library.
 */
public final class Consts {
    /**
     * A list of byte data size labels for use in {@link Parse#convertBytesToString(Number)}.
     */
    public static final List<String> BYTE_UNITS = List.of(
            "bytes", "KB", "MB", "GB", "TB", "PB", "EB");

    /**
     * The number of bytes in each data unit for use in {@link Parse#convertBytesToString(Number)}.
     */
    public static final double BYTES_PER_UNIT = 1024.0;

    /**
     * A list of strings that can represent a true boolean value for use in
     * {@link StringUtils#toBoolean(String)}. The values are:
     * "true", "t", "yes", "y", "1", "succeeded", "succeed", "enabled", "on"
     */
    @SuppressWarnings("Java9CollectionFactory")
    public static final Set<String> TRUE_VALUES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("true", "t", "yes", "y", "1",
            "succeeded", "succeed", "enabled", "on")));

    /**
     * A list of strings that can represent a false boolean value for use in
     * {@link StringUtils#toBoolean(String)}. The values are:
     * "false", "f", "no", "n", "0", "-1", "failed", "fail", "disabled", "off"
     */
    @SuppressWarnings("Java9CollectionFactory")
    public static final Set<String> FALSE_VALUES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("false", "f", "no", "n", "0",
                    "-1", "failed", "fail", "disabled", "off")));

    /** Prevents instantiation of this utility class.  */
    private Consts() { throwUnsupportedExForUtilityCls(); }
}
