package com.jwcomptech.commons.properties;

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

import com.jwcomptech.commons.logging.JWLogger;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Loads and provides access to external property files based on the active Spring profile(s).
 * <p>
 * This class supports both:
 * <ul>
 *   <li>A merged, profile-aware view of all loaded properties</li>
 *   <li>A map of each individual properties file and its parsed values</li>
 * </ul>
 * The merged view mimics Spring Boot behavior, where later-loaded files override earlier ones.
 * You may also query the origin filename for each key via {@link #getSourceFile(String)}.
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class ExternalPropertyLoader extends PropertyLoader {

    @Getter
    private static final JWLogger logger = JWLogger.of(ExternalPropertyLoader.class);

    static {
        load(true, logger);
    }

    /**
     * Returns the merged view of all external properties loaded from the file system.
     * <p>
     * Keys from later-loaded files override earlier ones. This map represents the
     * final resolved external configuration.
     *
     * @return the merged {@link Properties} object for external sources
     */
    public static Properties getMergedProperties() {
        return externalMergedProperties;
    }

    /**
     * Returns a map of individual external property files and their associated {@link Properties} objects.
     * <p>
     * This allows inspection of raw properties grouped by filename as loaded from disk.
     *
     * @return a map of filenames to their loaded {@link Properties}
     */
    public static Map<String, Properties> getPropertiesMap() {
        return externalPropertiesMap;
    }

    /**
     * Returns a mapping of property keys to the external file where each key was last defined.
     * <p>
     * This is useful for tracing the source of a particular configuration value
     * when diagnosing overrides or external environment behavior.
     *
     * @return a map of config keys to the external file they were loaded from
     */
    public static Map<String, String> getKeyOriginMap() {
        return externalKeyOriginMap;
    }

    /**
     * Reloads internal properties by clearing and reloading from the classpath.
     */
    public static synchronized void reload() {
        externalPropertiesMap.clear();
        externalMergedProperties.clear();
        externalKeyOriginMap.clear();
        load(true, logger);
    }

    /**
     * Checks if the specified key exists in the external merged properties.
     *
     * @param key the key to lookup
     * @return true if key exists, false otherwise
     */
    public static boolean containsKey(final String key) {
        return externalMergedProperties.containsKey(key);
    }

    /**
     * Gets the value for a key from the external merged properties.
     *
     * @param key the property key
     * @return the string value or {@code null} if not found
     */
    public static @Nullable String get(final String key) {
        return externalMergedProperties.getProperty(key);
    }

    /**
     * Gets the integer value for the specified key, or a default value if parsing fails
     * or the key does not exist.
     *
     * @param key          the property key to look up
     * @param defaultValue the default value to return if not found or invalid
     * @return the parsed integer value, or {@code defaultValue}
     */
    public static int getInt(final String key, final int defaultValue) {
        return parseOrDefault(key, externalMergedProperties, Integer::parseInt, defaultValue);
    }

    /**
     * Gets the double value for the specified key, or a default value if parsing fails
     * or the key does not exist.
     *
     * @param key          the property key to look up
     * @param defaultValue the default value to return if not found or invalid
     * @return the parsed double value, or {@code defaultValue}
     */
    public static double getDouble(final String key, final double defaultValue) {
        return parseOrDefault(key, externalMergedProperties, Double::parseDouble, defaultValue);
    }

    /**
     * Gets the boolean value for the specified key, or a default value if parsing fails
     * or the key does not exist.
     *
     * @param key          the property key to look up
     * @param defaultValue the default value to return if not found or invalid
     * @return the parsed boolean value, or {@code defaultValue}
     */
    public static boolean getBoolean(final String key, final boolean defaultValue) {
        return parseOrDefault(key, externalMergedProperties, Boolean::parseBoolean, defaultValue);
    }

    /**
     * Gets the origin file where the given key was defined.
     *
     * @param key the config key
     * @return the filename it was loaded from, or {@code null} if not found
     */
    public static @Nullable String getSourceFile(final String key) {
        return externalKeyOriginMap.get(key);
    }

    /**
     * Gets all keys known to the loader.
     *
     * @return a set of all loaded keys
     */
    public static Set<String> getAllKeys() {
        return externalMergedProperties.stringPropertyNames();
    }

    /**
     * Returns a flattened map of all external merged properties.
     *
     * @return a {@code Map<String, String>} of the final resolved values
     */
    public static Map<String, String> asMap() {
        return externalMergedProperties.stringPropertyNames().stream()
                .collect(Collectors.toMap(
                        key -> key,
                        externalMergedProperties::getProperty,
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }
}
