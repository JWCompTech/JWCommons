package com.jwcomptech.shared.utils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Web utilities for completing tasks dealing with websites.
 * @since 0.0.1
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
                while(-1 != (data = buf.read())) {
                    sb.append((char) data);
                }
            }
            return sb.toString();
        }

        /** Prevents instantiation of this utility class. */
        private HTML() { }
    }

    /** Prevents instantiation of this utility class. */
    private WebUtils() { }
}
