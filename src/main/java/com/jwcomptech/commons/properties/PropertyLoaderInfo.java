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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Diagnostic container that provides metadata and statistics about
 * loaded configuration properties from both internal (classpath) and
 * external (filesystem) sources.
 *
 * <p>This class is useful for inspecting:
 * <ul>
 *   <li>Which property files were loaded</li>
 *   <li>How many keys were loaded from each source</li>
 *   <li>The origin file for each individual key</li>
 *   <li>Which keys were overridden across sources</li>
 * </ul>
 *
 * <p>Used primarily by {@link PropertyLoader#getInfo()} to allow visibility
 * into the configuration resolution process at runtime.
 *
 * <p>The concept of "shared" keys refers to the final merged view that combines
 * both internal and external values, where external properties override internal ones.
 *
 * @see PropertyLoader
 * @see InternalPropertyLoader
 * @see ExternalPropertyLoader
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyLoaderInfo {

    /**
     * Total keys loaded from internal sources.
     */
    private int internalKeyCount;

    /**
     * Total keys loaded from external sources.
     */
    private int externalKeyCount;

    /**
     * Total keys in the final shared view.
     */
    private int sharedKeyCount;

    /**
     * All filenames loaded from internal sources.
     */
    private @NotNull List<String> internalFiles = new ArrayList<>();

    /**
     * All filenames loaded from external sources.
     */
    private @NotNull List<String> externalFiles = new ArrayList<>();

    /**
     * Keys and their origin source filename from internal config.
     */
    private @NotNull Map<String, String> internalKeyOrigins = new LinkedHashMap<>();

    /**
     * Keys and their origin source filename from external config.
     */
    private @NotNull Map<String, String> externalKeyOrigins = new LinkedHashMap<>();

    /**
     * Keys that were present in both internal and external configs (overridden).
     */
    private @NotNull Set<String> overriddenKeys = new LinkedHashSet<>();
}

