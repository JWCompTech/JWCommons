package com.jwcomptech.commons.resources.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
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
