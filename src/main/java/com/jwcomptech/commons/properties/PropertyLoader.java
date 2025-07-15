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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jwcomptech.commons.properties.PropertiesUtils.getEffectivePropertyFileList;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode()
@ToString()
public abstract class PropertyLoader {
    // Internal
    @Getter(AccessLevel.PROTECTED)
    protected static final Properties internalMergedProperties = new Properties();
    @Getter(AccessLevel.PROTECTED)
    protected static final Map<String, Properties> internalPropertiesMap = new LinkedHashMap<>();
    @Getter(AccessLevel.PROTECTED)
    protected static final Map<String, String> internalKeyOriginMap = new LinkedHashMap<>();

    // External
    @Getter(AccessLevel.PROTECTED)
    protected static final Properties externalMergedProperties = new Properties();
    @Getter(AccessLevel.PROTECTED)
    protected static final Map<String, Properties> externalPropertiesMap = new LinkedHashMap<>();
    @Getter(AccessLevel.PROTECTED)
    protected static final Map<String, String> externalKeyOriginMap = new LinkedHashMap<>();

    // Shared
    @Getter protected static final Properties sharedMergedProperties = new Properties();

    /**
     * Loads all property files for internal or external sources.
     *
     * @param external whether to load from file system or classpath
     * @param logger   the logger for reporting
     */
    protected static void load(final boolean external, final JWLogger logger) {
        final List<String> filenames = getEffectivePropertyFileList();

        final Properties targetMerged = external ? externalMergedProperties : internalMergedProperties;
        final Map<String, Properties> targetMap = external ? externalPropertiesMap : internalPropertiesMap;
        final Map<String, String> targetKeyOrigin = external ? externalKeyOriginMap : internalKeyOriginMap;

        for (final String filename : filenames) {
            final Properties props = new Properties();
            File file = null;

            if(external) {
                file = new File(filename);
                if (!file.exists()) continue;
            } else {
                if(PropertyLoader.class.getClassLoader().getResource(filename) == null) continue;
            }

            try {
                if (filename.endsWith(".yml") || filename.endsWith(".yaml")) {
                    final YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
                    Resource resource = external ? new FileSystemResource(file) : new ClassPathResource(filename);

                    yamlFactory.setResources(resource);
                    final Properties yamlProps = yamlFactory.getObject();
                    if (yamlProps != null) props.putAll(yamlProps);
                } else {
                    try (InputStream input = external
                            ? new FileInputStream(file)
                            : PropertyLoader.class.getClassLoader().getResourceAsStream(filename)) {
                        if (input != null) {
                            props.load(input);
                        } else {
                            continue;
                        }
                    }
                }

                logger.debug(String.format("Loading properties from file: %s", filename));
                targetMap.put(filename, props);

                for (final String key : props.stringPropertyNames()) {
                    final String newValue = props.getProperty(key);
                    final String oldValue = targetMerged.getProperty(key);
                    logger.debug("New: {}, Old: {}", newValue, oldValue);
                    if(newValue == null) continue;
                    if (oldValue != null && !Objects.equals(newValue, oldValue)) {
                        logger.debug("Overriding key '{}' from '{}' to '{}' via {}", key, oldValue, newValue, filename);
                    }
                    targetMerged.setProperty(key, newValue);
                    targetKeyOrigin.put(key, filename);
                }

                logger.debug("Loaded {} config: {}", external ? "external" : "internal", filename);

            } catch (IOException e) {
                logger.error("Failed to load " + (external ? "external" : "internal") + " config file "
                        + filename + ": " + e.getMessage());
            }
        }

        rebuildSharedProperties();
    }

    /**
     * Attempts to parse a string property value into a desired type using the provided parser function.
     * Returns a fallback default value if the key is not found or the parse fails.
     *
     * @param key the property key to look up
     * @param props the properties set to query
     * @param parser the function to convert the string into the desired type
     * @param defaultValue the value to return if parsing fails
     * @param <T> the return type
     * @return the parsed value or {@code defaultValue} if not found or invalid
     */
    public static <T> T parseOrDefault(final String key,
                                          final Properties props,
                                          final Function<String, T> parser,
                                          final T defaultValue) {
        try {
            return parser.apply(Objects.requireNonNull(props.getProperty(key)));
        } catch (Exception e) {
            return defaultValue;
        }
    }


    /**
     * Clears and rebuilds the shared merged property set from both internal and external sources.
     * Values from external properties override those from internal ones.
     * This is automatically called after every load, but may be called manually to recompute state.
     */
    public static void rebuildSharedProperties() {
        sharedMergedProperties.clear();
        sharedMergedProperties.putAll(internalMergedProperties);
        sharedMergedProperties.putAll(externalMergedProperties);
    }

    /**
     * Clears all loaded properties from internal, external, and shared maps.
     * Does not reload any files. Useful for full resets or test isolation.
     */
    public static void clear() {
        internalMergedProperties.clear();
        internalPropertiesMap.clear();
        internalKeyOriginMap.clear();

        externalMergedProperties.clear();
        externalPropertiesMap.clear();
        externalKeyOriginMap.clear();

        sharedMergedProperties.clear();
    }

    /**
     * Returns a list of all profile-aware filenames used during load.
     * Useful for debugging which files were attempted or loaded.
     *
     * @return list of profile-specific filenames
     */
    public static @NotNull List<String> getProfiledSources() {
        return getEffectivePropertyFileList();
    }

    /**
     * Checks if a given property file was successfully loaded into internal or external maps.
     *
     * @param filename the file to check
     * @return true if it was loaded
     */
    public static boolean isLoaded(final String filename) {
        return internalPropertiesMap.containsKey(filename) || externalPropertiesMap.containsKey(filename);
    }

    /**
     * Checks if a given property key was loaded from any source.
     *
     * @param key the property key
     * @return true if it exists in either internal or external merged properties
     */
    public static boolean hasSource(final String key) {
        return internalMergedProperties.containsKey(key) || externalMergedProperties.containsKey(key);
    }

    /**
     * Checks if the specified key exists in the shared merged properties.
     *
     * @param key the key to lookup
     * @return true if key exists, false otherwise
     */
    public static boolean containsKeyShared(final String key) {
        return sharedMergedProperties.containsKey(key);
    }

    /**
     * Gets the value for a key from the shared merged properties.
     *
     * @param key the property key
     * @return the string value or {@code null} if not found
     */
    public static @Nullable String getShared(final String key) {
        return sharedMergedProperties.getProperty(key);
    }

    /**
     * Gets the integer value for the specified shared key, or a default value if parsing fails
     * or the key does not exist.
     *
     * @param key          the shared property key to look up
     * @param defaultValue the default value to return if not found or invalid
     * @return the parsed integer value, or {@code defaultValue}
     */
    public static int getIntShared(final String key, final int defaultValue) {
        return parseOrDefault(key, sharedMergedProperties, Integer::parseInt, defaultValue);
    }

    /**
     * Gets the double value for the specified shared key, or a default value if parsing fails
     * or the key does not exist.
     *
     * @param key          the shared property key to look up
     * @param defaultValue the default value to return if not found or invalid
     * @return the parsed double value, or {@code defaultValue}
     */
    public static double getDoubleShared(final String key, final double defaultValue) {
        return parseOrDefault(key, sharedMergedProperties, Double::parseDouble, defaultValue);
    }

    /**
     * Gets the boolean value for the specified shared key, or a default value if parsing fails
     * or the key does not exist.
     *
     * @param key          the shared property key to look up
     * @param defaultValue the default value to return if not found or invalid
     * @return the parsed boolean value, or {@code defaultValue}
     */
    public static boolean getBooleanShared(final String key, final boolean defaultValue) {
        return parseOrDefault(key, sharedMergedProperties, Boolean::parseBoolean, defaultValue);
    }

    /**
     * Gets all shared keys known to the loader.
     *
     * @return a set of all loaded shared keys
     */
    public static Set<String> getAllSharedKeys() {
        return sharedMergedProperties.stringPropertyNames();
    }

    /**
     * Returns a flattened map of all shared merged properties.
     *
     * @return a {@code Map<String, String>} of the final resolved values
     */
    public static Map<String, String> asSharedMap() {
        return sharedMergedProperties.stringPropertyNames().stream()
                .collect(Collectors.toMap(
                        key -> key,
                        sharedMergedProperties::getProperty,
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }

    /**
     * Returns a full report of the loaded property files and key origins.
     *
     * @return PropertyLoaderInfo containing diagnostic data
     */
    public static @NotNull PropertyLoaderInfo getInfo() {
        final PropertyLoaderInfo info = new PropertyLoaderInfo();

        info.setInternalKeyCount(internalMergedProperties.size());
        info.setExternalKeyCount(externalMergedProperties.size());
        info.setSharedKeyCount(sharedMergedProperties.size());

        info.setInternalFiles(new ArrayList<>(internalPropertiesMap.keySet()));
        info.setExternalFiles(new ArrayList<>(externalPropertiesMap.keySet()));

        info.setInternalKeyOrigins(new LinkedHashMap<>(internalKeyOriginMap));
        info.setExternalKeyOrigins(new LinkedHashMap<>(externalKeyOriginMap));

        final Set<String> internalKeys = internalMergedProperties.stringPropertyNames();
        final Set<String> externalKeys = externalMergedProperties.stringPropertyNames();

        final Set<String> overridden = new LinkedHashSet<>(internalKeys);
        overridden.retainAll(externalKeys); // Intersection

        info.setOverriddenKeys(overridden);

        return info;
    }
}
