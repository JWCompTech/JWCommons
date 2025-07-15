package com.jwcomptech.commons.info;

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

import com.jwcomptech.commons.utils.RegExPatterns;
import com.jwcomptech.commons.utils.osutils.windows.Registry;
import com.jwcomptech.commons.values.StringValue;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShlObj;
import com.sun.jna.platform.win32.WinDef;
import com.sun.management.OperatingSystemMXBean;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.util.List;

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;
import static com.jwcomptech.commons.utils.Parse.convertBytesToString;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Returns information about the system hardware.
 * @since 1.0.0-alpha
 */
@SuppressWarnings("HardcodedFileSeparator")
public final class HWInfo {
    /** Returns information about the system BIOS. */
    public static final class BIOS {
        /**
         * Returns the system BIOS release date stored in the registry.
         * @return BIOS date as string
         */
        public static StringValue getReleaseDate() {
            if(OS.isWindows) {
                final var key = "HARDWARE\\DESCRIPTION\\System\\BIOS";
                final var value = "BIOSReleaseDate";
                return Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
            }
            return StringValue.of("Unknown");
        }

        /**
         * Returns the system BIOS version stored in the registry.
         * @return BIOS version as string
         */
        public static StringValue getVersion() {
            if(OS.isWindows) {
                final var key = "HARDWARE\\DESCRIPTION\\System\\BIOS";
                final var value = "BIOSVersion";
                return Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
            }
            return StringValue.of("Unknown");
        }

        /**
         * Returns the system BIOS vendor name stored in the registry.
         * @return BIOS vendor name as string
         */
        public static StringValue getVendor() {
            if(OS.isWindows) {
                final var key = "HARDWARE\\DESCRIPTION\\System\\BIOS";
                final var value = "BIOSVendor";
                return Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
            }
            return StringValue.of("Unknown");
        }

        /** Prevents instantiation of this utility class. */
        private BIOS() { throwUnsupportedExForUtilityCls(); }
    }

    /** Returns information about the current network. */
    public static final class Network {
        /**
         * Returns the Internal IP Address.
         * @return Internal IP Address as string
         */
        public static @NotNull StringValue getInternalIPAddress() {
            try {
                final var ip = (InetAddress.getLocalHost().getHostAddress()).trim();
                return StringValue.of("127.0.0.1".equals(ip) ? "N/A" : ip);
            } catch(final UnknownHostException ex) { return StringValue.of(ex.getMessage()); }
        }

        /**
         * Returns the External IP Address by connecting to "<a href="http://api.ipify.org">http://api.ipify.org</a>".
         * @return External IP address as string
         */
        public static @NotNull StringValue getExternalIPAddress() {
            final URL url;
            try { url = URI.create("https://api.ipify.org").toURL(); }
            catch(final MalformedURLException e) { return StringValue.of(e.getMessage()); }
            try(final var in = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8))) {
                return StringValue.of((in.readLine()).trim());
            } catch(final IOException ex) {
                return StringValue.of(ex.toString().contains(
                        "java.net.UnknownHostException:") ? "N/A" : ex.getMessage());
            }
        }

        /**
         * Returns status of internet connection.
         * @return Internet connection status as boolean
         * */
        public static boolean isConnectedToInternet() { return !getExternalIPAddress().get().equals("N/A"); }

        /** Prevents instantiation of this utility class. */
        private Network() { throwUnsupportedExForUtilityCls(); }
    }

    /** Returns information about the system manufacturer. */
    public static final class OEM {
        /**
         * Returns the system manufacturer name that is stored in the registry.
         * @return OEM name as string
         * */
        public static StringValue Name() {
            var key = "HARDWARE\\DESCRIPTION\\System\\BIOS";
            var value = "SystemManufacturer";
            final var text = Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
            if(text.trim().isEmpty()) {
                key = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\OEMInFormation";
                value = "Manufacturer";
                return Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
            }
            return text;
        }

        /**
         * Returns the system product name that is stored in the registry.
         * @return Product name as string
         * */
        public static StringValue ProductName() {
            var key = "HARDWARE\\DESCRIPTION\\System\\BIOS";
            var value = "SystemProductName";
            final var text = Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
            if(text.trim().isEmpty()) {
                key = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\OEMInFormation";
                value = "Model";
                return Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
            }
            return text;
        }

        /** Prevents instantiation of this utility class. */
        private OEM() { throwUnsupportedExForUtilityCls(); }
    }

    /** Returns information about the system processor. */
    public static final class Processor {
        /**
         * Returns the system processor name that is stored in the registry.
         * @return Processor name as string
         * */
        public static StringValue Name() {
            final var key = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0";
            final var value = "ProcessorNameString";
            return Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
        }

        /**
         * Returns the number of cores available on the system processor.
         * @return Number of cores as int
         * @throws IOException if error occurs
         * */
        @SuppressWarnings("OverlyComplexMethod")
        public static int Cores() throws IOException {
            String[] command = { "" };

            if(OS.isMac) command = new String[]{ "/bin/sh", "-c", "sysctl", "-n", "machdep.cpu.core_count" };
            else if(OS.isLinux) command = new String[]{ "lscpu" };
            else if(OS.isWindows) command = new String[]{ "cmd", "/C", "WMIC", "CPU", "Get", "/Format:List" };

            final Process process;
            var numberOfCores = 0;
            var sockets = 0;

            process = Runtime.getRuntime().exec(command);

            assert process != null;
            try(final var reader = new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8))) {
                String line;

                while((line = reader.readLine()) != null) {
                    if(OS.isMac) {
                        numberOfCores = line.trim().isEmpty() ? 0 : Integer.parseInt(line);
                    } else if(OS.isLinux) {
                        if(line.contains("Core(s) per socket:")) {
                            numberOfCores = lineSplit(line);
                        }
                        if(line.contains("Socket(s):")) {
                            sockets = lineSplit(line);
                        }
                    } else if(OS.isWindows && line.contains("NumberOfCores")) {
                        numberOfCores = Integer.parseInt(line.split("=")[1]);
                    }
                }
            }

            if(OS.isLinux) return numberOfCores * sockets;
            return numberOfCores;
        }

        private static int lineSplit(final @NotNull String line) {
            return Integer.parseInt(line.split(RegExPatterns.WHITESPACE.getRegex())[
                    line.split(RegExPatterns.WHITESPACE.getRegex()).length - 1]);
        }

        /** Prevents instantiation of this utility class. */
        private Processor() { throwUnsupportedExForUtilityCls(); }
    }

    /** Returns information about the system RAM. */
    public static final class RAM {
        /**
         * Returns the total ram installed on the system.
         * @return Total Ram as string
         * */
        public static @NotNull StringValue getTotalRam() {
            final var memorySize = ((OperatingSystemMXBean)
                    ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize();
            return StringValue.of(convertBytesToString((double) memorySize));
        }

        /** Prevents instantiation of this utility class. */
        private RAM() { throwUnsupportedExForUtilityCls(); }
    }

    /** Returns information about the system storage. */
    @SuppressWarnings("unused")
    public static final class Storage {
        /**
         * Returns the file path to the root of the drive Windows is installed on.
         * @return System drive file path as string
         * */
        public static @NotNull StringValue getSystemDrivePath() {
            final var pszPath = new char[WinDef.MAX_PATH];
            Shell32.INSTANCE.SHGetFolderPath(
                    null, ShlObj.CSIDL_WINDOWS, null, ShlObj.SHGFP_TYPE_CURRENT, pszPath);
            return StringValue.of(Native.toString(pszPath).replace("WINDOWS", ""));
        }

        /**
         * Returns the file path to the Windows directory.
         * @return Windows directory file path as string
         * */
        public static @NotNull StringValue getWindowsPath() {
            final var pszPath = new char[WinDef.MAX_PATH];
            Shell32.INSTANCE.SHGetFolderPath(
                    null, ShlObj.CSIDL_WINDOWS, null, ShlObj.SHGFP_TYPE_CURRENT, pszPath);
            return StringValue.of(Native.toString(pszPath));
        }

        /**
         * Returns the drive size of the drive Windows is installed on.
         * @return System drive size as string
         * */
        public static @NotNull StringValue getSystemDriveSize() {
            return getDriveSize(getSystemDrivePath().replace(":/", "").charAt(0));
        }

        /**
         * Returns the drive size of the specified drive by drive letter, returns "N/A" if drive doesn't exist.
         * @param driveLetter Drive letter of drive to get the size of
         * @return Drive size of the specified drive letter
         * */
        public static @NotNull StringValue getDriveSize(final char driveLetter) {
            final var aDrive = new File(driveLetter + ":");
            //noinspection HardcodedFileSeparator
            return StringValue.of(aDrive.exists() ? convertBytesToString((double) aDrive.getTotalSpace()) : "N/A");
        }

        /**
         * Returns the free space of drive of the drive Windows is installed on.
         * @return System drive free space as string
         * */
        public static @NotNull StringValue getSystemDriveFreeSpace() {
            return getDriveFreeSpace(getSystemDrivePath().replace(":/", "").charAt(0));
        }

        /**
         * Returns the free space of the specified drive by drive letter, returns "N/A" if drive doesn't exist.
         * @param driveLetter Drive letter of drive to get the free space of
         * @return Drive free space of the specified drive letter
         * */
        public static @NotNull StringValue getDriveFreeSpace(final char driveLetter) {
            final var aDrive = new File(driveLetter + ":");
            //noinspection HardcodedFileSeparator
            return StringValue.of(aDrive.exists() ? convertBytesToString((double) aDrive.getUsableSpace()) : "N/A");
        }

        /** Prevents instantiation of this utility class. */
        private Storage() { throwUnsupportedExForUtilityCls(); }
    }

    /**
     * A Hardware Object for use with the {@link ComputerInfo} class.
     * @param systemOEM the system OEM vendor name
     * @param productName the system product name
     * @param BIOS the BIOS object
     * @param network the network object
     * @param processor the processor object
     * @param RAM the RAM object
     * @param storage the storage object
     */
    public record HWObject(StringValue systemOEM,
                           StringValue productName,
                           BIOSObject BIOS,
                           NetworkObject network,
                           ProcessorObject processor,
                           RAMObject RAM,
                           StorageObject storage) {
    }

    /**
     * A BIOS Object for use with the {@link ComputerInfo} class.
     * @param name the bios name
     * @param releaseDate the bios release date
     * @param vendor the bios vendor
     * @param version the bios firmware version
     */
        public record BIOSObject(StringValue name,
                                 StringValue releaseDate,
                                 StringValue vendor,
                                 StringValue version) {
    }

    /**
     * A Drive Object for use with the {@link ComputerInfo} class.
     *
     * @param driveType the drive type
     * @param totalSize the total max space on the drive
     * @param totalFree the total unused space on the drive
     */
    //TODO: Finalize this class with more info including name, format, and label.
    public record DriveObject(StringValue driveType,
                              StringValue totalSize,
                              StringValue totalFree) { }

    /**
     * A Network Object for use with the {@link ComputerInfo} class.
     * @param internalIPAddress the internal IP address
     * @param externalIPAddress the external IP address
     * @param connectionStatus the connection status
     */
    public record NetworkObject(StringValue internalIPAddress,
                                StringValue externalIPAddress,
                                Boolean connectionStatus) { }

    /**
     * A Processor Object for use with the {@link ComputerInfo} class.
     * @param name the name of the processor
     * @param cores the number of cores
     */
    public record ProcessorObject(StringValue name, int cores) { }

    /**
     * A RAM Object for use with the {@link ComputerInfo} class.
     * @param totalInstalled the total installed ram
     */
    public record RAMObject(StringValue totalInstalled) { }

    /**
     * A Storage Object for use with the {@link ComputerInfo} class.
     * @param systemDrive the main system drive (drive C:/ in Windows)
     * @param installedDrives a list of all installed drives, including the system drive
     */
    public record StorageObject(DriveObject systemDrive, List<DriveObject> installedDrives) { }

    /** Prevents instantiation of this utility class. */
    private HWInfo() { throwUnsupportedExForUtilityCls(); }
}
