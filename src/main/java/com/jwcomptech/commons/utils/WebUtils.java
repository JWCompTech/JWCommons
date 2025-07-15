package com.jwcomptech.commons.utils;

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

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

/**
 * Web utilities for completing tasks dealing with websites.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
public final class WebUtils {
    /** HTML utilities for handling html source code. */
    public static final class HTML {
        /**
         * Gets HTML source code from specified URL.
         * @param url URL to download from
         * @return HTML source code as string
         * @throws IOException if error occurs
         * @throws MalformedURLException if url is invalid
         */
        public static @NotNull String getHTML(final String url) throws IOException {
            final URL newurl = URI.create(url).toURL();
            final StringBuilder sb;

            try(final var buf = new BufferedInputStream(newurl.openStream())) {
                sb = new StringBuilder();
                int data;
                while((data = buf.read()) != -1) {
                    sb.append((char) data);
                }
            }
            return sb.toString();
        }

        /** Prevents instantiation of this utility class. */
        private HTML() { throwUnsupportedExForUtilityCls(); }
    }

    /** Prevents instantiation of this utility class. */
    private WebUtils() { throwUnsupportedExForUtilityCls(); }
}
