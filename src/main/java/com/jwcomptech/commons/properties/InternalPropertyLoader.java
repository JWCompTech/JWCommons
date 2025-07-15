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
 * Loads and provides access to internal classpath-based application properties
 * using Spring-style profile resolution.
 * <p>
 * Loads files like:
 * <ul>
 *   <li>{@code application.properties}</li>
 *   <li>{@code application-dev.properties}</li>
 *   <li>{@code application-prod.properties}</li>
 * </ul>
 * from the classpath (usually inside the JAR).
 * <p>
 * Later-loaded files override earlier ones. This class is static and self-initializing.
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class InternalPropertyLoader extends PropertyLoader {

    @Getter
    private static final JWLogger logger = JWLogger.of(InternalPropertyLoader.class);

    static {
        load(false, logger);
    }

    /**
     * Returns the merged view of all internal properties loaded from classpath resources.
     * <p>
     * Keys in later-loaded files override earlier ones.
     * This map represents the final resolved configuration used by the internal loader.
     *
     * @return the merged {@link Properties} object for internal sources
     */
    public static Properties getMergedProperties() {
        return internalMergedProperties;
    }

    /**
     * Returns a map of individual internal property files and their associated {@link Properties} objects.
     * <p>
     * This allows inspection of raw properties grouped by filename as they were loaded from the classpath.
     *
     * @return a map of filenames to their loaded {@link Properties}
     */
    public static Map<String, Properties> getPropertiesMap() {
        return internalPropertiesMap;
    }

    /**
     * Returns a mapping of property keys to the internal file where each key was last defined.
     * <p>
     * This is useful for tracing the source of a particular configuration value when debugging overrides.
     *
     * @return a map of config keys to the classpath resource they originated from
     */
    public static Map<String, String> getKeyOriginMap() {
        return internalKeyOriginMap;
    }

    /**
     * Reloads the properties by resetting the loader and reloading the files.
     */
    public static synchronized void reload() {
        internalPropertiesMap.clear();
        internalMergedProperties.clear();
        internalKeyOriginMap.clear();
        load(false, logger);
    }

    /**
     * Checks if the specified key exists in the internal merged properties.
     *
     * @param key the key to lookup
     * @return true if key exists, false otherwise
     */
    public static boolean containsKey(final String key) {
        return internalMergedProperties.containsKey(key);
    }

    /**
     * Gets the value for a key from the internal merged properties.
     *
     * @param key the property key
     * @return the string value or {@code null} if not found
     */
    public static @Nullable String get(final String key) {
        return internalMergedProperties.getProperty(key);
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
        return parseOrDefault(key, internalMergedProperties, Integer::parseInt, defaultValue);
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
        return parseOrDefault(key, internalMergedProperties, Double::parseDouble, defaultValue);
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
        return parseOrDefault(key, internalMergedProperties, Boolean::parseBoolean, defaultValue);
    }

    /**
     * Gets the origin file where the given key was defined.
     *
     * @param key the config key
     * @return the filename it was loaded from, or {@code null} if not found
     */
    public static @Nullable String getSourceFile(final String key) {
        return internalKeyOriginMap.get(key);
    }

    /**
     * Gets all keys known to the loader.
     *
     * @return a set of all loaded keys
     */
    public static Set<String> getAllKeys() {
        return internalMergedProperties.stringPropertyNames();
    }

    /**
     * Returns a flattened map of all internal merged properties.
     *
     * @return a {@code Map<String, String>} of the final resolved values
     */
    public static Map<String, String> asMap() {
        return internalMergedProperties.stringPropertyNames().stream()
                .collect(Collectors.toMap(
                        key -> key,
                        internalMergedProperties::getProperty,
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }
}

