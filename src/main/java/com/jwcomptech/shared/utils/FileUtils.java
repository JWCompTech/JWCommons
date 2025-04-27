package com.jwcomptech.shared.utils;

import com.jwcomptech.shared.values.StringValue;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Contains methods for dealing with files.
 * @since 0.0.1
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
    private FileUtils() { }
}
