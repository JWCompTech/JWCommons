package com.jwcomptech.commons.resources.enums;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

/**
 * The types of resources supported by the ResourceManager.
 *
 * @since 0.0.1
 */
@SuppressWarnings("HardcodedFileSeparator")
@AllArgsConstructor
@Getter
@ToString
public enum ResourceDir {
    /**
     * Audio files stored in the "audio" directory.
     */
    AUDIO("/audio"),
    /**
     * Compressed files stored in the "compressed" directory.
     */
    COMPRESSED("/compressed"),
    /**
     * Config files stored in the "config" directory.
     */
    CONFIG("/config"),
    /**
     * CSS files stored in the "css" directory.
     */
    CSS("/css"),
    /**
     * Data files stored in the "data" directory.
     */
    DATA("/data"),
    /**
     * Font files stored in the "font" directory.
     */
    FONT("/font"),
    /**
     * FXML files stored in the "fxml" directory.
     */
    FXML("/fxml"),
    /**
     * Image files stored in the "img" directory.
     */
    IMAGE("/image"),
    /**
     * JSON files stored in the "json" directory.
     */
    JSON("/json"),
    /**
     * Media files stored in the "media" directory.
     */
    MEDIA("/media"),
    /**
     * POM files stored in the "pom" directory.
     */
    POM("/pom"),
    /**
     * Properties files stored in the "props" directory.
     */
    PROPERTIES("/props"),
    /**
     * Script files stored in the "script" directory.
     */
    SCRIPT("/script"),
    /**
     * Text files stored in the "txt" directory.
     */
    TEXT("/txt"),
    /**
     * Video files stored in the "video" directory.
     */
    VIDEO("/video"),
    /**
     * Web files stored in the "web" directory.
     */
    WEB("/web"),
    /**
     * XML files stored in the "xml" directory.
     */
    XML("/xml"),
    /**
     * YAML files stored in the "yaml" directory.
     */
    YAML("/yaml"),
    ;

    private final String path;

    public static @NotNull Optional<ResourceDir> parse(final String path) {
        return Arrays.stream(values())
                .filter(dir -> dir.path.equalsIgnoreCase(path))
                .findFirst();
    }
}
