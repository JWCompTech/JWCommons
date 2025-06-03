package com.jwcomptech.commons;

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

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

/**
 * A list of settings that are used throughout the application.
 *
 * @since 0.0.1
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
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String WINDOWS_DEFAULT_INSTALL_PATH = "C:\\Program Files\\" + COMPANY_NAME;
    /**
     * The current Java LTS version number.
     */
    public static final int CURRENT_LTS = 21;

    /** Prevents instantiation of this utility class. */
    private Settings() { throwUnsupportedExForUtilityCls(); }
}
