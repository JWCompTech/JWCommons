package com.jwcomptech.commons.resources.enums;

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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@ToString
public enum ResourceExtension {
    // --- Audio Files ---
    FLAC(".flac", "audio/x-flac"),
    MP3(".mp3", "audio/mpeg"),
    M4A(".m4a", "audio/mp4"),
    OGG(".ogg", "audio/ogg"),
    WAV(".wav", "audio/wav"),
    WMA(".wma", "audio/x-ms-wma"),

    // --- Compressed Archives ---
    GZ(".gz", "application/gzip"),
    JAR(".jar", "application/java-archive"),
    RAR(".rar", "application/vnd.rar"),
    SEVEN_Z(".7z", "application/x-7z-compressed"),
    TAR(".tar", "application/x-tar"),
    TAR_GZ(".tar.gz", "application/gzip"),
    ZIP(".zip", "application/zip"),

    // --- Config Files ---
    CFG(".cfg", "text/plain"),
    CONFIG(".config", "text/plain"),
    INI(".ini", "text/plain"),
    PROPERTIES(".properties", "text/x-java-properties"),

    // --- Data Files ---
    DAT(".dat", "application/octet-stream"),
    DB(".db", "application/octet-stream"),
    SQLITE(".sqlite", "application/x-sqlite3"),

    // --- Font Files ---
    EOT(".eot", "application/vnd.ms-fontobject"), //Embedded OpenType fonts
    OTF(".otf", "font/otf"), //OpenType font files
    TTF(".ttf", "font/ttf"), //TrueType font files

    // --- Image Files ---
    BMP(".bmp", "image/bmp"),
    GIF(".gif", "image/gif"),
    ICO(".ico", "image/x-icon"),
    JPEG(".jpeg", "image/jpeg"),
    JPG(".jpg", "image/jpeg"),
    PNG(".png", "image/png"),
    SVG(".svg", "image/svg+xml"),
    WEBP(".webp", "image/webp"),

    // --- Object/Structured Files ---
    FXML(".fxml", "application/xml"),
    JSON(".json", "application/json"),
    POM(".pom", "application/xml"),
    XML(".xml", "application/xml"),
    YAML(".yaml", "application/x-yaml"),
    YML(".yml", "application/x-yaml"),

    // --- Script Files
    BAT(".bat", "application/x-bat"),
    BASH(".bash", "application/x-shellscript"),
    JS(".js", "application/javascript"),
    PS1(".ps1", "application/powershell"),
    PY(".py", "text/x-python"),
    SCRIPT(".script", "text/x-script"),
    SH(".sh", "application/x-sh"),
    SQL(".sql", "application/x-sql"),
    VBS(".vbs", "text/vbscript"),

    // --- Text Files ---
    CSV(".csv", "text/csv"),
    LOG(".log", "text/plain"),
    MAN(".man", "application/x-troff-man"),
    MD(".md", "text/markdown"),
    README(".readme", "text/markdown"),
    TXT(".txt", "text/plain"),

    // --- Video Files ---
    AVI(".avi", "video/x-msvideo"),
    MP4(".mp4", "video/mp4"),
    MOV(".mov", "video/quicktime"),
    MPEG(".mpeg", "video/mpeg"),
    MPG(".mpg", "video/mpeg"),
    TS(".ts", "video/mp2t"),
    WEBM(".webm", "video/webm"),
    WMV(".wmv", "video/x-ms-wmv"),

    // --- Web Files ---
    CSS(".css", "text/css"),
    HTM(".htm", "text/html"),
    HTML(".html", "text/html"),
    PHP(".php", "application/x-httpd-php");

    private final String extension;
    private final String mimeType;

    private static final Map<String, ResourceExtension> extensionLookup = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(
                ext -> extensionLookup.put(ext.extension.toLowerCase(), ext));
    }

    public static ResourceExtension fromExtension(final String ext) {
        if (ext == null) return null;
        String ext_ = ext.trim().toLowerCase();
        if (!ext.startsWith(".")) ext_ = "." + ext_;
        return extensionLookup.get(ext_);
    }
}
