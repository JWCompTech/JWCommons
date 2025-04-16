package com.jwcomptech.shared.info;

import com.jwcomptech.shared.info.enums.OSList;
import com.jwcomptech.shared.info.enums.OSType;
import com.jwcomptech.shared.info.os.*;
import com.jwcomptech.shared.values.IntegerValue;
import com.jwcomptech.shared.values.StringValue;
import com.sun.jna.Platform;
import org.apache.commons.lang3.SystemUtils;

/** Returns information about the operating system. */
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
        if (SystemUtils.IS_OS_WINDOWS) return OSType.Windows;
        else if (SystemUtils.IS_OS_MAC) return OSType.MacOS;
        else if (SystemUtils.IS_OS_LINUX) return OSType.Linux;
        else if (Platform.isAndroid()) return OSType.Android;
        else if (SystemUtils.IS_OS_FREE_BSD) return OSType.FreeBSD;
        else if (SystemUtils.IS_OS_SOLARIS) return OSType.Solaris;
        else return OSType.Other;
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

    /** An Operating System Object for use with the {@link ComputerInfo} class. */
    public static final class OSObject {
        private final StringValue ComputerName;
        private final StringValue ComputerNamePending;
        private final WindowsOSEx.InstallInfoObject InstallInfo;
        private final StringValue RegisteredOrganization;
        private final StringValue RegisteredOwner;
        private final StringValue LoggedInUserName;
        private final StringValue DomainName;

        public OSObject(StringValue computerName,
                        StringValue computerNamePending,
                        WindowsOSEx.InstallInfoObject installInfo,
                        StringValue registeredOrganization,
                        StringValue registeredOwner,
                        StringValue loggedInUserName,
                        StringValue domainName) {
            ComputerName = computerName;
            ComputerNamePending = computerNamePending;
            InstallInfo = installInfo;
            RegisteredOrganization = registeredOrganization;
            RegisteredOwner = registeredOwner;
            LoggedInUserName = loggedInUserName;
            DomainName = domainName;
        }

        public StringValue ComputerName() { return ComputerName; }

        public StringValue ComputerNamePending() { return ComputerNamePending; }

        public WindowsOSEx.InstallInfoObject InstallInfo() { return InstallInfo; }

        public StringValue RegisteredOrganization() { return RegisteredOrganization; }

        public StringValue RegisteredOwner() { return RegisteredOwner; }

        public StringValue LoggedInUserName() { return LoggedInUserName; }

        public StringValue DomainName() { return DomainName; }
    }
}
