package com.jwcomptech.commons.download;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.jwcomptech.commons.validators.Condition;
import com.jwcomptech.commons.base.Validated;
import com.jwcomptech.commons.values.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.download.DLStatus.*;
import static com.jwcomptech.commons.utils.Parse.convertBytesToString;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.Preconditions.isBoolean;

/**
 * Uses the {@link java.net.HttpURLConnection} class to download files.
 * @since 0.0.1
 */
@SuppressWarnings({"unused", "ClassWithTooManyFields", "ClassWithTooManyMethods", "ClassWithTooManyConstructors", "OverlyComplexClass"})
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HTTPDownloader extends Validated implements AutoCloseable, Runnable {
    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 1024;

    private final URL url;
    private MutableStringValue downloadDir;
    private final IntegerValue totalDownloadSize;
    private final IntegerValue totalBytesDownloaded;
    private final EnumValue<DLStatus> status;
    private StringValue errorMessage;
    private final MutableStringValue filename;
    private final MutableStringValue filepath;
    private RandomAccessFile file;
    private InputStream stream;
    private HttpURLConnection connection;

    @Serial
    private static final long serialVersionUID = 1356368919575260587L;

    private enum Type {
        FileDownload,
        JSONDownload,
        TextDownload,
        BooleanDownload
    }

    public HTTPDownloader(final String url) throws MalformedURLException {
        this(StringValue.EMPTY, URI.create(url).toURL());
    }

    public HTTPDownloader(final URL url) {
        this(StringValue.EMPTY, url);
    }

    public HTTPDownloader(final String downloadDir, final String url) throws MalformedURLException {
        this(downloadDir, URI.create(url).toURL());
    }

    public HTTPDownloader(final @NotNull StringValue downloadDir, final String url) throws MalformedURLException {
        this(downloadDir.get(), URI.create(url).toURL());
    }

    public HTTPDownloader(final @NotNull StringValue downloadDir, final URL url) {
        this(downloadDir.get(), url);
    }

    // Constructor for Download.
    @SuppressWarnings("HardcodedFileSeparator")
    public HTTPDownloader(final String downloadDir, final URL url) {
        checkArgumentNotNull(downloadDir, cannotBeNull("path"));
        checkArgumentNotNull(url, cannotBeNull("url"));
        this.downloadDir = MutableStringValue.of(downloadDir);

        while (this.downloadDir.endsWith("/")) {
            this.downloadDir = this.downloadDir.substring(0, this.downloadDir.length().subtractAndGet(1));
        }

        if(!this.downloadDir.isEmpty()) this.downloadDir = this.downloadDir.addToEnd('/');

        this.url = url;
        this.status = new EnumValue<>(DLStatus.IDLE);
        this.totalDownloadSize = IntegerValue.of(-1);
        this.totalBytesDownloaded = IntegerValue.of(0);
        this.filename = MutableStringValue.EMPTY();
        this.filepath = MutableStringValue.EMPTY();
        this.errorMessage = StringValue.EMPTY;
    }

    /**
     * Returns the download's URL.
     * @return the download's URL
     */
    public StringValue getUrl() {
        return StringValue.of(url.toString());
    }

    /**
     * Returns the download's progress.
     * @return the download's progress
     */
    public FloatValue getProgress() {
        float progress = totalBytesDownloaded
                        .divide(totalDownloadSize)
                        .multiply(100).floatValue();

        progress = Float.isNaN(progress) ? 0 : progress;

        return FloatValue.of(convertBytesToString(progress));
    }

    /**
     * Return the download's status.
     * @return the download's status
     */
    public DLStatus getStatus() {
        return status.get();
    }

    /**
     * Returns the download's filename.
     * @return the download's filename
     */
    public StringValue getFilename() {
        return filename.toImmutable();
    }

    /**
     * Returns the path of the folder to download to.
     * @return the path of the folder to download to
     */
    public StringValue getDownloadDir() {
        return downloadDir.toImmutable();
    }

    /**
     * Returns the full path to download the file to.
     * @return the full path to download the file to
     */
    public StringValue getFilepath() {
        return filepath.toImmutable();
    }

    /**
     * Pauses the file download.
     * @return this instance
     */
    public HTTPDownloader pause() {
        status.set(PAUSED);
        return this;
    }

    /**
     * Resumes the file download.
     * @return this instance
     */
    public HTTPDownloader resume() {
        status.set(DOWNLOADING);
        return download();
    }

    /**
     * Cancels the file download.
     * @return this instance
     */
    public HTTPDownloader cancel() {
        status.set(CANCELLED);
        return this;
    }

    /**
     * Marks the file download as having an error.
     */
    private void error(final String errorMessage) {
        status.set(ERROR);
        this.errorMessage = StringValue.of(errorMessage);
    }

    /**
     * Closes the file and download stream.
     */
    @Override
    public void close() {
        // Close the file.
        if (file != null) {
            try {
                file.close();
            } catch (final IOException ignored) {}
        }

        // Close the connection to server.
        if (stream != null) {
            try {
                stream.close();
            } catch (final IOException ignored) {}
        }
    }

    /**
     * Starts or resumes the file download.
     * @return this instance
     */
    public HTTPDownloader download() {
        preStart();

        //The run method is executed in a new thread
        new Thread(this).start();

        return this;
    }

    /**
     * Connects to the remote server and requests info about the file.
     * @throws IOException if any connection errors occur
     */
    private void connect() throws IOException {
        // Open connection to URL.
        connection = (HttpURLConnection) url.openConnection();

        // Specify what portion of file to download.
        connection.setRequestProperty("Range", "bytes=%s-".formatted(totalBytesDownloaded));

        // Connect to server.
        connection.connect();
    }

    /**
     * This method is run in another thread to do the task of downloading the file.
     */
    @Override
    public void run() {
        final Path filePath;

        try {
            connect();

            // Make sure response code is in the 200 range.
            processResponseCode(connection.getResponseCode(), Type.FileDownload);

            // Check for valid content length.
            final int contentLength = connection.getContentLength();
            if (contentLength < 1) error("Invalid Content Length!");

            //Set the size for this download if it hasn't been already set.
            if (totalDownloadSize.isEqualTo(-1)) {
                totalDownloadSize.set(contentLength);
            }

            filename.set(parseFilename(url));
            filepath.set(downloadDir.get() + filename);

            // Open file and seek to the end of it.
            filePath = Paths.get(filepath.get());

            file = new RandomAccessFile(filePath.toFile(), "rw");
            file.seek(totalBytesDownloaded.get());

            stream = connection.getInputStream();
            while (status.equals(DOWNLOADING)) {
                // Size buffer according to how much of the file is left to download.
                final byte[] buffer = totalDownloadSize.subtractAndGet(totalBytesDownloaded) > MAX_BUFFER_SIZE
                        ? new byte[MAX_BUFFER_SIZE]
                        : new byte[totalDownloadSize.subtractAndGet(totalBytesDownloaded)];

                // Read from server into buffer.
                final int read = stream.read(buffer);
                if (read == -1) break;

                // Write buffer to file.
                file.write(buffer, 0, read);
                totalBytesDownloaded.add(read);
            }

            verifyComplete();
        } catch (final Exception e) {
            error(e.getMessage());
        }
    }

    /**
     * Attempts to return the url location result as a string.
     * @return the result as a string
     * @throws IOException if any errors occur
     */
    public Optional<String> processTextAsString() throws IOException {
        return process(this::getHTTPResponseAsString, Type.TextDownload);
    }

    /**
     * Attempts to return the url location result as a boolean.
     * @return the result as a boolean
     * @throws IOException if any errors occur
     */
    public Optional<Boolean> processTextAsBoolean() throws IOException {
        return process(() -> isBoolean(getHTTPResponseAsString()), Type.BooleanDownload);
    }

    /**
     * Attempts to return the url location result as a json array.
     * @return the result as a json array
     * @throws IOException if any errors occur
     */
    public Optional<JsonArray> processJSONAsArray() throws IOException {
        return process(this::getHTTPResponseAsJSONArray, Type.JSONDownload);
    }

    private <T> Optional<T> process(final Supplier<T> task, final Type downloadType) throws IOException {
        Optional<T> result = Optional.empty();

        if(!preStart()) return Optional.empty();

        connect();

        if(processResponseCode(connection.getResponseCode(), downloadType)) {
            result = Optional.ofNullable(task.get());
        }

        verifyComplete();

        return result;
    }

    //TODO: might not work
    private String getHTTPResponseAsString() {
        try {
            //This line might not be supported
            stream = connection.getInputStream();
            String encoding = connection.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            return IOUtils.toString(stream, encoding);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    //TODO: might not work
    @SneakyThrows
    private JsonArray getHTTPResponseAsJSONArray() {
        //This line might not be supported
        try(final var inputStream = connection.getInputStream();
            final InputStreamReader isr = new InputStreamReader(stream)) {
            this.stream = inputStream;
            return JsonParser.parseReader(isr).getAsJsonArray();
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        } finally {
            stream.close();
        }
    }

    //TODO: might not work
    private @NotNull String getHTTPErrorMessage() {
        //This line might not be supported
        try (final InputStreamReader isr = new InputStreamReader(connection.getErrorStream())) {
            return JsonParser.parseReader(isr).getAsJsonObject()
                    .get("message").getAsString().replace("\"", "");
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod", "BooleanMethodNameMustStartWithQuestion"})
    private boolean processResponseCode(final int code, final Type type) {
        final Condition github = Condition.of(() -> type == Type.JSONDownload
                && url.getHost().contains("github.com")).evaluate();

        switch (code) {
            case 200, 201, 202, 203, 204, 205, 206, 207, 208 -> {
                return true;
            }
            case 400 -> error("Bad Request!");
            case 401 ->
                    error(github.hasEvaluatedTrue() ? "GitHub API Bad Credentials!" : "Unauthorized Or Bad Credentials!");
            case 403 -> {
                if (github.hasEvaluatedTrue()) {
                    error("GitHub API Rate Limit Reached!");
                    throw new IllegalStateException("GitHub API Rate Limit Reached!");
                }
                error("Forbidden!");
            }
            case 404 -> {
                if (github.hasEvaluatedTrue()) {
                    final String message = getHTTPErrorMessage();

                    error(message.contains("Not Found") ? "GitHub Page Not Found!" : "GitHub API: " + message);
                } else error("Not Found!");
            }
            case 500 -> error("Internal Server Error!");
            case 501 -> error("Not Implemented!");
            case 502 -> error("Bad Gateway!");
            case 503 -> error("Service Unavailable!");
            case 504 -> error("Gateway Timeout!");
            default -> {
                final String message = getHTTPErrorMessage();

                error(type == Type.JSONDownload ? "Unknown API Error! Error Message: " + message : message);
            }
        }

        return false;
    }


    /**
     * Returns the filename portion of URL.
     * @param url the url to parse
     * @return the filename portion of URL
     */
    private static @NotNull String parseFilename(@NotNull final URL url) {
        final String fileName = url.getFile();
        //noinspection HardcodedFileSeparator
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    private boolean preStart() {
        if(status.equals(DOWNLOADING)) return false;

        totalDownloadSize.set(-1);
        totalBytesDownloaded.set(0);
        status.set(DOWNLOADING);

        return true;
    }

    private void verifyComplete() {
        // Change status to complete if this point was reached because downloading has finished.
        if (status.equals(DOWNLOADING)) {
            status.set(COMPLETE);
        }
    }
}
