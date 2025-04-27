package com.jwcomptech.shared;

/**
 * A list of settings that are used throughout the application.
 */
public final class Settings {
    /**
     * The application version.
     */
    public static final String APP_VERSION = "0.0.1";
    /**
     * The company name string for use in all URLs and paths.
     */
    public static final String COMPANY_NAME = "JWCompTech";
    /**
     * The default installation path for windows.
     */
    public static final String WINDOWS_DEFAULT_INSTALL_PATH = "C:\\Program Files\\" + COMPANY_NAME;
    /**
     * The current Java LTS version number.
     */
    public static final int CURRENT_LTS = 21;

    /** Prevents instantiation of this utility class. */
    private Settings() { }
}
