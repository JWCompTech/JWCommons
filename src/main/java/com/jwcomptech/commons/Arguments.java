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

import com.jwcomptech.commons.annotations.Beta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

import static com.jwcomptech.commons.Settings.APP_VERSION;
import static com.jwcomptech.commons.Settings.CURRENT_LTS;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

/**
 * Initializes and holds all provided command line arguments to be passed to the rest of the application.
 *
 * @since 0.0.1
 */
@SuppressWarnings({"unused", "ClassWithTooManyFields", "OverlyComplexClass"})
@Command(name = "jupdate", version = APP_VERSION,
        description = "A basic Java updater for AdoptOpenJDK.",
        optionListHeading = "%nOptions:%n")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Beta
//TODO: Finish implementation
public class Arguments implements Callable<Integer> {
    private static final Logger logger = LoggerFactory.getLogger(Arguments.class);

    /**
     * The command line object instance.
     */
    private CommandLine cmd;

    /**
     * Is true if the -version argument was passed.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-version", "--version"}, versionHelp = true, description = "display version info")
    private boolean versionInfoRequested = false;

    /**
     * Is true if the -? or --help argument was passed.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-?", "--help"}, usageHelp = true, description = "display this help message")
    private boolean usageHelpRequested = false;

    /**
     * The specified version.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-jv", "--javaVersion"}, arity = "0..1",
            defaultValue = "11", fallbackValue = "11",
            description = "the version of java to lookup (default: ${DEFAULT-VALUE}),\n"
                    + "if specified without parameter: ${FALLBACK-VALUE}")
    private int version = CURRENT_LTS;

    /**
     * Is true if debug logging should be enabled.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-debug", "--debug"}, description = "enables debug logging")
    private boolean debug = false;

    /**
     * Is true if trace logging should be enabled.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-trace", "--trace"}, description = "enables trace logging")
    private boolean trace = false;

    /**
     * Is true if the JDK release should be used.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-jdk", "--jdk"}, description = "enables usage of jdk (default)")
    private boolean jdk = false;

    /**
     * Is true if the JRE release should be used.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-jre", "--jre"}, description = "enables usage of jre")
    private boolean jre = false;

    /**
     * Is true if the Hotspot release should be used.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-h", "--hotspot"}, description = "enables usage of hotspot jvm type (default)")
    private boolean hotspot = false;

    /**
     * Is true if the OpenJ9 release should be used.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-j9", "--openj9"}, description = "enables usage of openj9 jvm type")
    private boolean openJ9 = false;

    /**
     * The specified asset name.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-a", "--asset"}, description = "sets the asset name to install")
    private String asset = "";

    /**
     * Is true if a refresh of the exclusions file is needed.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-r", "--refresh"}, description = "overwrites the exclusions file")
    private boolean refresh = false;

    /**
     * Is true if asset info should be shown after lookup.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-info", "--showassetinfo"}, description = "shows info about the os appropriate asset")
    private boolean showAssetInfo = false;

    /**
     * Is true if logging should be disabled and a boolean result returned.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-b", "--boolean"}, description = "enables minimal output")
    private boolean showBoolean = false;

    /**
     * Is true if pre-release assets should be used.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-pre", "--prerelease"}, description = "enables use of prerelease assets")
    private boolean prerelease = false;

    /**
     * Is true if the release should be downloaded and installed.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-i", "--install"}, description = "downloads and installs the installer")
    private boolean install = false;

    /**
     * Is true if the release should be downloaded.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-d", "--download"}, description = "downloads the installer")
    private boolean download = false;

    /**
     * The specified download path for the installer.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-dp", "--downloadpath"}, description = "sets the path to download to")
    private String downloadPath = "";

    /**
     * The specified GitHub API ID.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-apiid", "--apiid"}, description = "sets the id for api usage\n"
            + "(is ignored if both id and secret are not specified)")
    private String apiID = "";

    /**
     * The specified GitHub API Secret.
     */
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    @Option(names = {"-apisecret", "--apisecret"}, description = "sets the secret for api usage\n"
            + "(is ignored if both id and secret are not specified)")
    private String apiSecret = "";

    /**
     * Runs the application
     * @return exit code
     * @throws IOException if any IO error occurs
     */
    @Override
    public Integer call() throws IOException {
        //return new JUpdateApp(this).call();
        return 0;
    }
}
