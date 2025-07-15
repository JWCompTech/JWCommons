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

import com.jwcomptech.commons.enums.BaseEnum;
import lombok.Getter;
import lombok.ToString;

/**
 * Common RegEx patterns for use with pattern matching.
 *
 * @since 1.0.0-alpha
 */
@Getter
@ToString
@SuppressWarnings("HardcodedFileSeparator")
public enum RegExPatterns implements BaseEnum<String> {
    /**
     * A RegEx pattern that represents a valid IP address.
     */
    IPADDRESS("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-4])\\.){3}"
            + "([0-9]|[1-9][0-9]|1[0-9‌​]{2}|2[0-4][0-9]|25[0-4])$"),
    /**
     * A RegEx pattern that represents a valid website URL.
     */
    URL("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"),

    /**
     * A RegEx pattern that represents all special characters.
     */
    SPECIAL_CHARS("[^a-zA-Z0-9]+"),

    /**
     * A RegEx pattern that represents all alphanumeric characters.
     */
    ALPHANUMERIC("[a-zA-Z0-9]+"),

    /**
     * A RegEx pattern that represents all uppercase and lowercase letters.
     */
    ALPHA("[a-zA-Z]+"),

    /**
     * A RegEx pattern that represents all single numbers 1-9 without decimal.
     */
    NUMERIC("[0-9]+"),

    /**
     * A RegEx pattern that represents any length of whitespace characters.
     */
    WHITESPACE("\\s+"),;

    private final String regex;

    RegExPatterns(final String regex) {
        this.regex = regex;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    public String getValue() {
        return regex;
    }
}
