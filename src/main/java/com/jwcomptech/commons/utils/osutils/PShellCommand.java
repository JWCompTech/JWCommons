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

import com.jwcomptech.commons.utils.osutils.windows.pshell.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.jwcomptech.commons.utils.StringUtils.isBlank;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.LF;

@SuppressWarnings({"NestedAssignment", "unused", "ClassWithTooManyMethods"})
public class PShellCommand extends ExecCommand {
    @Contract("_ -> new")
    public static @NotNull PShellCommand newPowerShell(final String command) {
        return new PShellCommand(command);
    }

    public static PShellCommand runNewPowerShell(final String command) throws IOException, InterruptedException {
        return new PShellCommand(command).run();
    }

    public static PShellCommand newPowerShellScript(final String installScript) {
        return new PShellCommand("cd")
                .enableNoProfile()
                .enableBypassExecutionPolicy()
                .enableFailOnError()
                .setScriptPath(installScript)
                .enableDownloadScript();
    }

    public static PShellCommand runNewPowerShellScript(final String installScript) throws IOException, InterruptedException {
        return new PShellCommand("cd")
                .enableNoProfile()
                .enableBypassExecutionPolicy()
                .enableFailOnError()
                .setScriptPath(installScript)
                .enableDownloadScript()
                .run();
    }

    private boolean noProfile;
    private boolean bypassExecutionPolicy;
    private String setText = "";
    private String scriptPath = "";
    private boolean downloadScript;
    private boolean failOnError;

    public PShellCommand(final String command) {
        super(command);
    }

    public PShellCommand enableFailOnError() {
        failOnError = true;
        return this;
    }

    public PShellCommand enableNoProfile() {
        noProfile = true;
        return this;
    }

    public PShellCommand disableNoProfile() {
        noProfile = false;
        return this;
    }

    public PShellCommand enableBypassExecutionPolicy() {
        bypassExecutionPolicy = true;
        return this;
    }

    public PShellCommand disableBypassExecutionPolicy() {
        bypassExecutionPolicy = false;
        return this;
    }

    public PShellCommand andSet(final String text) {
        setText = " && SET \"" + text + '"';
        return this;
    }

    public PShellCommand setScriptPath(final String scriptPath) {
        this.scriptPath = scriptPath;
        return this;
    }

    public PShellCommand enableDownloadScript() {
        downloadScript = true;
        return this;
    }

    @Override
    public PShellCommand setArgs(final String args) {
        super.setArgs(args);
        return this;
    }

    @Override
    public PShellCommand setWorkingDirectory(final String workingDirectory) {
        super.setWorkingDirectory(workingDirectory);
        return this;
    }

    @Override
    public PShellCommand setResultHandler(final Consumer<String> resultHandler) {
        super.setResultHandler(resultHandler);
        return this;
    }

    @Override
    public PShellCommand setErrorHandler(final Consumer<String> errorHandler) {
        super.setErrorHandler(errorHandler);
        return this;
    }

    @Override
    public PShellCommand enableDefaultPrintHandlers() {
        super.enableDefaultPrintHandlers();
        return this;
    }

    @Override
    public PShellCommand disableDefaultPrintHandlers() {
        super.disableDefaultPrintHandlers();
        return this;
    }

    @SuppressWarnings("OverlyComplexMethod")
    @Override
    protected void handleError(final Process process) throws IOException {
        try(final BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream(), UTF_8))) {
            String line;
            while((line = br.readLine()) != null) {
                errors.add(line);
                if(line.contains("FullyQualifiedErrorId") && failOnError) {
                    final String errorMessage = LF + errors.stream()
                            .map(s -> s + LF)
                            .collect(Collectors.joining());
                    if(errorMessage.contains("Attempted to divide by zero.")) {
                        throw new PSDivideByZeroException(errorMessage);
                    }
                    if(errorMessage.contains("CommandNotFoundException")) {
                        throw new PSCommandNotFoundException(errorMessage);
                    }
                    if(errorMessage.contains("ExpectedExpression")) {
                        throw new PSExpectedExpressionException(errorMessage);
                    }
                    if(errorMessage.contains("ExpectedValueExpression")) {
                        throw new PSExpectedValueExpressionException(errorMessage);
                    }
                    if(errorMessage.contains("MissingProperty")) {
                        throw new PSMissingPropertyNameException(errorMessage);
                    }
                    if(errorMessage.contains("UnexpectedToken")) {
                        throw new PSUnexpectedTokenException(errorMessage);
                    }
                    if(errorMessage.contains("ParserError")) {
                        throw new PowerShellParserErrorException(errorMessage);
                    }
                    if(errorMessage.contains("FileNotFoundException")) {
                        throw new FileNotFoundException(errorMessage);
                    }
                    if(errorMessage.contains("IOException")) {
                        throw new IOException(new PowerShellException(errorMessage));
                    }
                    throw new PowerShellException(errorMessage);
                }
                super.getErrorHandler().accept(line);
            }
        }
    }

    @SuppressWarnings("HardcodedFileSeparator")
    @Override
    public PShellCommand run() throws IOException, InterruptedException {
        final StringBuilder modifiedCommand = new StringBuilder();
        if(noProfile) modifiedCommand.append(" -NoProfile");
        modifiedCommand.append(" -InputFormat None");
        if(bypassExecutionPolicy) modifiedCommand.append(" -ExecutionPolicy Bypass");
        modifiedCommand.append(" -Command \"");
        if (isBlank(scriptPath)) modifiedCommand.append(command).append('"').append(setText);
        else {
            modifiedCommand.append(downloadScript
                    ? "iex ((New-Object System.Net.WebClient).DownloadString('" + scriptPath + "'))"
                    : "iex \"" + scriptPath + '"');
            modifiedCommand.append('"').append(setText);
        }
        super.command = "%SystemRoot%\\System32\\WindowsPowerShell\\v1.0\\powershell.exe "
                + modifiedCommand;
        super.run();
        return this;
    }

    @Override
    public PShellCommand runElevated() throws IOException, InterruptedException {
        final StringBuilder modifiedCommand = new StringBuilder();
        if(noProfile) modifiedCommand.append(" -NoProfile");
        if(bypassExecutionPolicy) modifiedCommand.append(" -ExecutionPolicy Bypass");
        modifiedCommand.append(" -Command \"& {Start-Process PowerShell -ArgumentList " +
                "'-NoProfile -ExecutionPolicy Bypass -Command \"\"");
        if (isBlank(scriptPath)) {
            if(isKeepWindowOpen()) {
                modifiedCommand.append(command).append("; pause\"\"' -Verb RunAs}\"").append(setText);
            } else {
                modifiedCommand.append(command).append("\"\"' -Verb RunAs}\"").append(setText);
            }
        }
        else {
            modifiedCommand.append(downloadScript
                    ? "iex ((New-Object System.Net.WebClient).DownloadString(''" + scriptPath + "''))"
                    : "iex ''" + scriptPath + "''");
            if(isKeepWindowOpen()) {
                modifiedCommand.append("; pause\"\"' -Verb RunAs}\"").append(setText);
            } else {
                modifiedCommand.append("\"\"' -Verb RunAs}\"").append(setText);
            }
        }
        super.command = "PowerShell " + modifiedCommand;
        super.run();
        return this;
    }
}
