package com.jwcomptech.commons.utils.osutils;

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

import com.jwcomptech.commons.info.OSInfo;
import com.jwcomptech.commons.values.StringValue;
import com.sun.jna.platform.win32.Shell32;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.jwcomptech.commons.Literals.NEW_LINE;
import static com.jwcomptech.commons.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.info.os.WindowsOSEx.WMI.getEnvironmentVar;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNullOrEmpty;
import static com.jwcomptech.commons.utils.FileUtils.writeStringToFile;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A class for executing on the command line and returning the result of
 * execution.
 *
 * @since 0.0.1
 */

@SuppressWarnings("ClassWithTooManyMethods")
public class ExecCommand {
    @Contract("_ -> new")
    public static @NotNull ExecCommand newCmd(final String command) {
        return new ExecCommand(command);
    }

    public static ExecCommand runNewCmd(final String command) throws IOException, InterruptedException {
        return new ExecCommand(command).run();
    }

    public static ExecCommand runNewCmd(final String command, final String args) throws IOException, InterruptedException {
        return new ExecCommand(command).setArgs(args).run();
    }

    public static ExecCommand runNewVBS(final StringValue scriptText) throws IOException, InterruptedException {
        return runNewVBS(scriptText.get());
    }

    public static ExecCommand runNewVBS(final String scriptText) throws IOException, InterruptedException {
        return runNewVBS(scriptText, StringValue.EMPTY.get());
    }

    public static ExecCommand runNewVBS(final String scriptText, final String args) throws IOException, InterruptedException {
        final String tmpFileName = getEnvironmentVar(
                "TEMP").trim() + File.separator + "java.vbs";
        writeStringToFile(tmpFileName, StringValue.of(scriptText));
        final ExecCommand cmd = ExecCommand.runNewCmd(
                "cmd", "/C cscript.exe " + tmpFileName + " " + args);
        Files.delete(Paths.get(tmpFileName));
        return cmd;
    }

    @SuppressWarnings("OverlyComplexAnonymousInnerClass")
    protected final List<String> result = new ArrayList<>() {
        @Override
        public String toString() {
            //NOTE: Cannot be OS.isWindows or things will break
            if (OSInfo.isWindows()) {
                return result.stream()
                        .filter(line -> !line.contains("Windows Script Host Version")
                                && !line.contains("Microsoft Corporation. All rights reserved.")
                                && !line.trim().isEmpty())
                        .map(line -> line + System.lineSeparator()).collect(Collectors.joining());
            }
            return result.stream().map(line -> line + System.lineSeparator()).collect(Collectors.joining());
        }
    };

    protected String command;
    private String args = "";
    private int exitCode;
    protected final List<String> errors = new ArrayList<>();
    private Consumer<String> resultHandler = s -> {};
    private Consumer<String> errorHandler = s -> {};
    private boolean keepWindowOpen;
    private String workingDirectory;

    public ExecCommand(final String command) {
        checkArgumentNotNullOrEmpty(command, cannotBeNullOrEmpty("command"));
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }

    public ExecCommand setArgs(final String args) {
        if(args != null) this.args = args;
        return this;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ExecCommand setWorkingDirectory(final String workingDirectory) {
        this.workingDirectory = workingDirectory;
        return this;
    }

    /**
     * If true, pauses cmd window and forces it to stay open after command is completed. <p>
     * If false and "elevate" is true, cmd window will close after command is completed. <p>
     * This parameter is ignored if "hideWindow" is true, this prevents cmd window from staying
     * open when hidden and unnecessarily using RAM.
     * @return true if the window is to be kept open
     */
    public boolean isKeepWindowOpen() {
        return keepWindowOpen;
    }

    public ExecCommand setKeepWindowOpen(final boolean keepWindowOpen) {
        this.keepWindowOpen = keepWindowOpen;
        return this;
    }

    /**
     * Returns the text result of the command.
     * @return the text result of the command
     */
    public List<String> getResult() {
        while(result.getFirst().isEmpty()) {
            result.removeFirst();
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns the first line of the text result of the command.
     * @return the first line of the text result of the command
     */
    public String getFirstResult() { return getResultAt(1); }

    /**
     * Returns the specified line of the text result of the command. Line numbers begin with 1.
     * @return the specified line of the text result of the command
     */
    public String getResultAt(final int lineNumber) {
        if (1 <= lineNumber && lineNumber < getResult().size()) {
            return getResult().get(lineNumber - 1);
        }
        return "";
    }

    /**
     * Returns the text errors of the command.
     * @return the text errors of the command
     */
    public List<String> getErrors() { return Collections.unmodifiableList(errors); }

    /**
     * Returns the exit code, returns 0 if no error occurred.
     * @return the exit code, returns 0 if no error occurred
     */
    public int getExitCode() {
        return exitCode;
    }

    public Consumer<String> getResultHandler() {
        return resultHandler;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ExecCommand setResultHandler(final Consumer<String> resultHandler) {
        this.resultHandler = resultHandler == null ? (s -> {}) : resultHandler;
        return this;
    }

    public Consumer<String> getErrorHandler() {
        return errorHandler;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ExecCommand setErrorHandler(final Consumer<String> errorHandler) {
        this.errorHandler = errorHandler == null ? (s -> {}) : errorHandler;
        return this;
    }

    public void print() { result.forEach(System.out::println); }

    @SuppressWarnings("UnusedReturnValue")
    public ExecCommand enableDefaultPrintHandlers() {
        setResultHandler(System.out::println);
        setErrorHandler(System.out::println);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ExecCommand disableDefaultPrintHandlers() {
        setResultHandler(null);
        setErrorHandler(null);
        return this;
    }

    /**
     * Executes a command on the native command line and saves the result. This is
     * a convenience method to call {@link Runtime#exec(String)} and
     * capture the resulting output in a list of Strings. On Windows, built-in
     * commands not associated with an executable program may require
     * {@code cmd.exe /c} to be prepended to the command.
     * @return this instance
     * @throws IOException if error occurs
     * @throws InterruptedException if command is interrupted
     */
    public ExecCommand run() throws IOException, InterruptedException {
        checkArgumentNotNullOrEmpty(command, cannotBeNullOrEmpty("command"));
        return run(command, args == null ? "" : args, false, true, false, workingDirectory);
    }

    /**
     * Executes an elevated command on the native command line and does not save the result. On Windows,
     * this creates a new batch file that contains the specified command then runs the file with
     * the Shell32.ShellExecute native library passing the "runas" parameter. On all other operating systems
     * sudo is prepended to the beginning of the command.
     * @return this instance
     * @throws IOException if error occurs
     * @throws InterruptedException if command is interrupted
     */
    //TODO: Fix Runtime.getRuntime().exec() deprecation
    public ExecCommand runElevated() throws IOException, InterruptedException {
        checkArgumentNotNullOrEmpty(command, cannotBeNullOrEmpty("command"));
        return run(command, args == null ? "" : args, true, false, keepWindowOpen, workingDirectory);
    }

    @SuppressWarnings({"HardcodedFileSeparator", "CallToRuntimeExec"})
    private ExecCommand run(final String command, final String args, final boolean elevate,
                                 final boolean hideWindow, final boolean keepWindowOpen, final String workingDirectory)
            throws IOException, InterruptedException {

        result.clear();
        errors.clear();

        if((elevate || !hideWindow) && OSInfo.isWindows()) {
            final String filename = "temp.bat";

            final Path path = Paths.get(filename);
            try(final Writer writer = Files.newBufferedWriter(path, UTF_8)) {
                writer.write("@Echo off" + NEW_LINE);
                writer.write('"' + command + "\" " + args + NEW_LINE);
                if(keepWindowOpen && !hideWindow) { writer.write("pause"); }
            }

            final int windowStatus = hideWindow ? 0 : 1;
            final String operation = elevate ? "runas" : "open";

            Shell32.INSTANCE.ShellExecute(null, operation, filename, null, workingDirectory, windowStatus);

            Thread.sleep(2000);

            Files.delete(path);
        } else {
            final Process process;
            if(OSInfo.isWindows()) {
                final String cmdString = String.format("cmd /C \"%s %s\"", command, args);
                process = Runtime.getRuntime().exec(cmdString);
            } else if(elevate) {
                final String sudoString = String.format("sudo %s %s", command, args);
                process = Runtime.getRuntime().exec(sudoString);
            } else {
                process = Runtime.getRuntime().exec(command);
            }

            assert !Objects.isNull(process);
            handleResult(process);

            handleError(process);

            process.waitFor();

            exitCode = process.exitValue();
        }
        return this;
    }

    protected void handleResult(final Process process) throws IOException {
        try(final BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8))) {
            String line;
            while((line = br.readLine()) != null) {
                result.add(line);
                resultHandler.accept(line);
            }
        }
    }

    protected void handleError(final Process process) throws IOException {
        try(final BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream(), UTF_8))) {
            String line;
            while((line = br.readLine()) != null) {
                errors.add(line);
                errorHandler.accept(line);
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (!(o instanceof ExecCommand output)) return false;

        return new EqualsBuilder()
                .append(exitCode, output.exitCode)
                .append(result, output.result)
                .append(errors, output.errors)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(result)
                .append(errors)
                .append(exitCode)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("result", result)
                .append("errors", errors)
                .append("exitCode", exitCode)
                .toString();
    }
}
