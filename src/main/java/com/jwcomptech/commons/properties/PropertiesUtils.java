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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.jwcomptech.commons.spring.SpringUtils.getActiveSpringProfiles;

/**
 * Utility class for loading and merging application configuration properties
 * from classpath resources, including both standard `.properties` and YAML-based files.
 *
 * <p>Supports optional Spring-style profile resolution, allowing property overrides
 * from files like {@code application-dev.yml} or {@code config/myapp-test.properties}.
 *
 * <p>Primarily intended to support early configuration injection (e.g. into
 * Spring Boot via {@code setDefaultProperties}) or manual loading for plugins
 * or modular systems that require their own config bootstrap phase.
 */
@SuppressWarnings("unused")
public class PropertiesUtils {

    private static final JWLogger logger = JWLogger.of(PropertiesUtils.class);

    /**
     * Loads properties from the standard Spring Boot file names:
     * {@code application.properties}, {@code application.yml}, and {@code application.yaml},
     * in that order of precedence.
     *
     * <p>This method merges all keys from available files into a single map,
     * with later formats (e.g. YAML) overriding earlier ones on key collision.
     * It uses the given class's class loader to resolve resources.
     *
     * @param clazz the class whose class loader will be used to locate the config files
     * @return a map of merged key-value pairs from all detected application config files
     * @throws RuntimeException if a file exists but cannot be read
     * @apiNote This is a lightweight utility for loading configuration files without the full
     * {@link PropertyLoader} lifecycle or tracking features.
     * <p>
     * Useful for testing, plugin bootstrapping, or standalone modules.
     * For more advanced usage, prefer {@link InternalPropertyLoader} or {@link ExternalPropertyLoader}.
     */
    public static @NotNull Map<String, Object> loadApplicationProperties(final Class<?> clazz) {
        return loadApplicationProperties(clazz, "application", null);
    }

    /**
     * Loads properties from a custom base path (excluding extension),
     * such as {@code config/my-plugin}, checking for both `.properties` and `.yml`/`.yaml` files.
     *
     * <p>This method does not attempt to load profile-specific files.
     *
     * @param clazz    the class whose class loader will be used to locate the config files
     * @param basePath the base path for config files, excluding file extensions
     * @return a merged map of key-value pairs from all matching config files
     * @apiNote This is a lightweight utility for loading configuration files without the full
     * {@link PropertyLoader} lifecycle or tracking features.
     * <p>
     * Useful for testing, plugin bootstrapping, or standalone modules.
     * For more advanced usage, prefer {@link InternalPropertyLoader} or {@link ExternalPropertyLoader}.
     */
    public static @NotNull Map<String, Object> loadApplicationProperties(final Class<?> clazz, final String basePath) {
        return loadApplicationProperties(clazz, basePath, null);
    }

    /**
     * Loads and merges configuration files from the specified base path and profile,
     * checking for the following formats in order:
     * <ul>
     *   <li>{@code basePath.properties}</li>
     *   <li>{@code basePath-<profile>.properties}</li>
     *   <li>{@code basePath.yml}</li>
     *   <li>{@code basePath-<profile>.yml}</li>
     *   <li>{@code basePath.yaml}</li>
     *   <li>{@code basePath-<profile>.yaml}</li>
     * </ul>
     *
     * <p>Files loaded later override keys from earlier files in case of duplication.
     *
     * @param clazz    the class whose class loader will be used to locate the config files
     * @param basePath the base path (excluding extension) of the config files
     * @param profile  optional Spring profile (e.g. "dev", "test", etc.)
     * @return a map of resolved and merged configuration values
     * @apiNote This is a lightweight utility for loading configuration files without the full
     * {@link PropertyLoader} lifecycle or tracking features.
     * <p>
     * Useful for testing, plugin bootstrapping, or standalone modules.
     * For more advanced usage, prefer {@link InternalPropertyLoader} or {@link ExternalPropertyLoader}.
     */
    public static @NotNull Map<String, Object> loadApplicationProperties(@NotNull final Class<?> clazz,
                                                                         final String basePath,
                                                                         final String profile) {
        final Map<String, Object> result = new HashMap<>();

        // extensions to check
        final String[] exts = {".properties", ".yml", ".yaml"};

        for (String ext : exts) {
            mergeProperties(result, clazz, basePath + ext);
            if (profile != null && !profile.isBlank()) {
                mergeProperties(result, clazz, basePath + "-" + profile + ext);
            }
        }

        if (result.isEmpty()) {
            logger.warn("No configuration found for basePath: {}", basePath);
        }

        return result;
    }

    /**
     * Attempts to load a single property file from the classpath and merge its contents
     * into the given map. Supports `.properties`, `.yml`, and `.yaml` formats.
     *
     * <p>If the file does not exist, the method silently returns. If the file exists
     * but cannot be parsed, a {@link RuntimeException} is thrown.
     *
     * @param target the map to merge new properties into
     * @param clazz  the class to use for resource loading
     * @param path   the full resource path (including extension)
     */
    private static void mergeProperties(final Map<String, Object> target,
                                        @NotNull final Class<?> clazz,
                                        final String path) {
        final ClassLoader classLoader = clazz.getClassLoader();
        final Resource resource = new ClassPathResource(path, classLoader);

        if (!resource.exists()) return;

        try {
            if (path.endsWith(".properties")) {
                try (InputStream in = resource.getInputStream()) {
                    final Properties props = new Properties();
                    props.load(in);
                    for (String name : props.stringPropertyNames()) {
                        target.put(name, props.getProperty(name));
                    }
                }
            } else if (path.endsWith(".yml") || path.endsWith(".yaml")) {
                final YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
                yamlFactory.setResources(resource);
                final Properties yamlProps = yamlFactory.getObject();
                if (yamlProps != null) {
                    for (String name : yamlProps.stringPropertyNames()) {
                        target.put(name, yamlProps.getProperty(name));
                    }
                }
            }
            logger.debug("Loaded config file: {}", path);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load config file: " + path, ex);
        }
    }

    /**
     * Builds an ordered list of property file names corresponding to the active Spring profiles,
     * including both `.properties` and `.yml/.yaml` formats.
     * <p>
     * Always includes base files:
     * <ul>
     *   <li>{@code application.properties}</li>
     *   <li>{@code application.yml}</li>
     *   <li>{@code application.yaml}</li>
     * </ul>
     * Then includes matching files for each active profile:
     * <pre>
     * For profile "dev":
     *   - application-dev.properties
     *   - application-dev.yml
     *   - application-dev.yaml
     * </pre>
     *
     * @return an ordered list of config filenames to load (highest priority last)
     */
    public static @NotNull List<String> getEffectivePropertyFileList() {
        List<String> files = new ArrayList<>();

        // Base config files
        files.add("application.properties");
        files.add("application.yml");
        files.add("application.yaml");

        // Profile-specific files
        for (String profile : getActiveSpringProfiles()) {
            files.add("application-" + profile + ".properties");
            files.add("application-" + profile + ".yml");
            files.add("application-" + profile + ".yaml");
        }

        return files;
    }
}
