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

import com.jwcomptech.commons.values.StringValue;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Contains methods for dealing with files.
 *
 * @since 1.0.0-alpha
 */
public final class FileUtils {
    /**
     * Write the specified string to the specified file.
     * @param filename the file to write the string to
     * @param data     a string to be written to the file
     * @throws IOException if error occurs
     */
    public static void writeStringToFile(final String filename, final @NotNull StringValue data) throws IOException {
        try(final Writer output = Files.newBufferedWriter(Paths.get(filename), UTF_8)) {
            output.write(data.get());
            output.flush();
        }
    }

    /** Prevents instantiation of this utility class. */
    private FileUtils() { throwUnsupportedExForUtilityCls(); }
}
