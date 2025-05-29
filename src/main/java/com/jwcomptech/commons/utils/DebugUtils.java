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

import java.security.KeyPair;

@SuppressWarnings({"UseOfSystemOutOrSystemErr", "unused"})
public final class DebugUtils {
    public static void print(final String str) { System.out.println(str); }

    public static void print(final Boolean str) { System.out.println(str); }

    public static void print(final Integer num) { System.out.println(num); }

    public static void print(final Long num) { System.out.println(num); }

    public static void print(final @NotNull KeyPair keyPair) {
        System.out.println("Private Key: " + keyPair.getPrivate());
        System.out.println("Public Key: " + keyPair.getPublic());
    }

    /** Prevents instantiation of this utility class. */
    private DebugUtils() { }
}
