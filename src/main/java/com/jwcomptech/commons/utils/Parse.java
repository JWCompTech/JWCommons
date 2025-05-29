package com.jwcomptech.commons.utils;

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

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Contains methods for parsing data.
 *
 * @since 0.0.1
 */
public final class Parse {
    /**
     * Returns the conversion from bytes to the correct version (1024 bytes = 1 KB) as a string.
     * @param input number to convert to a readable string
     * @return the specified number converted to a readable string
     */
    public static @NotNull String convertBytesToString(final @NotNull Number input) {
        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        final double factor = 1024.0d;
        final String suffix;
        double newNum = input.doubleValue();
        if(factor <= newNum) {
            newNum /= factor;
            if(factor <= newNum) {
                newNum /= factor;
                if(factor <= newNum) {
                    newNum /= factor;
                    if(factor <= newNum) {
                        newNum /= factor;
                        suffix = " TB";
                    } else suffix = " GB";
                } else suffix = " MB";
            } else suffix = " KB";
        } else suffix = " Bytes";
        return df.format(newNum) + suffix;
    }

    /**
     * Attempts to parse a string to an int. If it fails, returns the default
     *
     * @param s
     *            The string to parse
     * @param defaultInt
     *            The value to return if parsing fails
     * @return The parsed int, or the default if parsing failed
     */
    public static int parseIntOrDefault(final String s, final int defaultInt, final Logger logger) {
        try {
            return Integer.parseInt(s);
        } catch (final NumberFormatException e) {
            logger.error(s + " didn't parse. Returning default. " + defaultInt, s, e);
            return defaultInt;
        }
    }

    /**
     * Attempts to parse a string to a long. If it fails, returns the default
     *
     * @param s
     *            The string to parse
     * @param defaultLong
     *            The value to return if parsing fails
     * @return The parsed long, or the default if parsing failed
     */
    public static long parseLongOrDefault(final String s, final long defaultLong, final Logger logger) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            logger.error(s + " didn't parse. Returning default. " + defaultLong, s, e);
            return defaultLong;
        }
    }

    /** Prevents instantiation of this utility class. */
    private Parse() { }
}
