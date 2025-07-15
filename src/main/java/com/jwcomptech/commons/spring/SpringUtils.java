package com.jwcomptech.commons.spring;

/*-
 * #%L
 * JWCommons
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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class for use with Spring framework.
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpringUtils {

    /**
     * Returns the comma-separated raw value of the active Spring profiles
     * from system properties or environment variables.
     *
     * @return active profiles as raw string, or {@code null} if not set
     */
    public static String getActiveSpringProfileRaw() {
        return System.getProperty("spring.profiles.active",
                System.getenv("SPRING_PROFILES_ACTIVE"));
    }

    /**
     * Gets all active Spring profiles as a list. Supports comma-separated profiles like "dev,feature-x".
     *
     * @return list of active profile names, or empty list if none are set
     */
    public static List<String> getActiveSpringProfiles() {
        final String raw = getActiveSpringProfileRaw();
        if (raw == null || raw.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Checks if a given profile is currently active (case-sensitive).
     *
     * @param profile the profile to check
     * @return true if the profile is active
     */
    public static boolean isProfileActive(final String profile) {
        return getActiveSpringProfiles().contains(profile);
    }

    /**
     * Checks if any of the given profiles are active.
     *
     * @param profiles one or more profile names to check
     * @return true if any of the specified profiles are active
     */
    public static boolean anyProfileActive(final String... profiles) {
        final List<String> active = getActiveSpringProfiles();
        return Arrays.stream(profiles).anyMatch(active::contains);
    }
}


