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

import com.jwcomptech.commons.info.enums.OSList;
import com.jwcomptech.commons.info.enums.OSType;
import com.jwcomptech.commons.info.os.*;
import com.jwcomptech.commons.values.IntegerValue;
import com.jwcomptech.commons.values.StringValue;
import com.sun.jna.Platform;
import org.apache.commons.lang3.SystemUtils;

/**
 * Returns information about the operating system.
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public class OSInfo {
    public static final OperatingSystem OS;

    static {
        switch (getOSType()) {
            case Windows:
                OS = WindowsOS.getInstance();
                break;
            case MacOS:
                OS = MacOS.getInstance();
                break;
            case Linux:
                OS = LinuxOS.getInstance();
                break;
            case Solaris:
                OS = SolarisOS.getInstance();
                break;
            case FreeBSD:
                OS = FreeBSDOS.getInstance();
                break;
            case Android:
                OS = AndroidOS.getInstance();
                break;
            default:
                OS = UnknownOS.getInstance();
                break;
        }
    }

    /**
     * Returns the name of the operating system running on this computer.
     *
     * @return String value containing the operating system name
     */
    public static StringValue getName() {
        return OS.getName();
    }

    /**
     * Returns the name of the operating system running on this computer.
     *
     * @return Enum value containing the operating system name
     */
    public static OSList getNameEnum() {
        return OS.getNameEnum();
    }

    /**
     * Returns a full version String, ex.: "Windows XP SP2 (32 Bit)".
     *
     * @return String representing a fully displayable version
     */
    public static StringValue getNameExpanded() {
        return OS.getNameExpanded();
    }

    /**
     * Retrieves operating system version.
     *
     * @return operating system version
     */
    public static StringValue getVersion() {
        try {
            return OS.getVersion();
        } catch (final Exception e) {
            return StringValue.of("Unknown");
        }
    }

    /**
     * Retrieves the manufacturer name of the OS.
     *
     * @return the manufacturer name of the OS
     */
    public static StringValue getManufacturer() {
        return OS.getManufacturer();
    }

    /**
     * Identifies if OS is a Server OS.
     *
     * @return true if OS is a Server OS
     */
    public static boolean isServer() {
        return isWindows() && OS.isServer();
    }

    /**
     * Identifies if OS is a 32-Bit OS.
     *
     * @return True if OS is a 32-Bit OS
     */
    public static boolean is32BitOS() {
        return !is64BitOS();
    }

    /**
     * Identifies if OS is a 64-Bit OS.
     *
     * @return True if OS is a 64-Bit OS
     */
    public static boolean is64BitOS() {
        return is64BitJVM() && OS.is64BitOS();
    }

    private static boolean is64BitJVM() {
        return System.getProperty("os.arch").contains("64");
    }

    /**
     * Determines if the current application is 32 or 64-bit.
     *
     * @return if computer is 32 bit or 64 bit as string
     */
    public static StringValue getBitString() {
        return OS.getBitString();
    }

    /**
     * Determines if the current application is 32 or 64-bit.
     *
     * @return if computer is 32 bit or 64 bit as int
     */
    public static IntegerValue getBitNumber() {
        return OS.getBitNumber();
    }

    /**
     * The OS type as an enum.
     *
     * @return The OS type as an enum
     */
    public static OSType getOSType() {
        final OSType result;

        if (SystemUtils.IS_OS_WINDOWS) result = OSType.Windows;
        else if (SystemUtils.IS_OS_MAC) result = OSType.MacOS;
        else if (SystemUtils.IS_OS_LINUX) result = OSType.Linux;
        else if (Platform.isAndroid()) result = OSType.Android;
        else if (SystemUtils.IS_OS_FREE_BSD) result = OSType.FreeBSD;
        else if (SystemUtils.IS_OS_SOLARIS) result = OSType.Solaris;
        else result = OSType.Other;

        return result;
    }

    /**
     * Identifies if OS is the specified OS.
     *
     * @param type the OS type to check
     * @return True if OS is the specified OS
     */
    public static boolean isOS(final OSType type) {
        return getOSType() == type;
    }

    /**
     * Identifies if OS is Windows.
     *
     * @return True if OS is Windows
     */
    public static boolean isWindows() {
        return isOS(OSType.Windows);
    }

    /**
     * Identifies if OS is MacOSX.
     *
     * @return True if OS is MacOSX
     */
    public static boolean isMac() {
        return isOS(OSType.MacOS);
    }

    /**
     * Identifies if OS is a distro of Linux.
     *
     * @return True if OS is Linux
     */
    public static boolean isLinux() {
        return isOS(OSType.Linux);
    }

    /**
     * Identifies if OS is Solaris.
     *
     * @return True if OS is Solaris
     */
    public static boolean isSolaris() {
        return isOS(OSType.Solaris);
    }

    /**
     * Identifies if OS is FreeBSD.
     *
     * @return True if OS is FreeBSD
     */
    public static boolean isFreeBSD() {
        return isOS(OSType.FreeBSD);
    }

    /**
     * Identifies if OS is Android.
     *
     * @return True if OS is Android
     */
    public static boolean isAndroid() {
        return isOS(OSType.Android);
    }

    /**
     * Prevents instantiation of this utility class.
     */
    protected OSInfo() { }

    /**
     * An Operating System Object for use with the {@link ComputerInfo} class.
     */
        public record OSObject(StringValue computerName,
                               StringValue computerNamePending,
                               WindowsOSEx.InstallInfoObject installInfo,
                               StringValue registeredOrganization,
                               StringValue registeredOwner,
                               StringValue loggedInUserName,
                               StringValue domainName) { }
}
