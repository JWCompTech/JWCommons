package com.jwcomptech.commons.info.os;

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

import com.jwcomptech.commons.info.ComputerInfo;
import com.jwcomptech.commons.info.OperatingSystem;
import com.jwcomptech.commons.info.enums.OSList;
import com.jwcomptech.commons.utils.osutils.ExecCommand;
import com.jwcomptech.commons.utils.osutils.windows.Registry;
import com.jwcomptech.commons.utils.osutils.windows.enums.*;
import com.jwcomptech.commons.values.IntegerValue;
import com.jwcomptech.commons.values.StringValue;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.info.OSInfo.*;
import static com.jwcomptech.commons.validators.CheckIf.*;

/**
 * Returns extended information about the current Windows installation.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public final class WindowsOSEx {
    public static final OperatingSystem OS = WindowsOS.getInstance();

    /** Returns information about the Windows activation status. */
    @SuppressWarnings("unused")
    public static final class Activation {
        /**
         * Identifies if OS is activated.
         * @return true if activated, false if not activated
         * @throws IOException if error occurs
         */
        public static boolean isActivated() throws IOException { return Status.Licensed == getStatusAsEnum(); }

        /**
         * Identifies If Windows is Activated, uses the Software Licensing Manager Script,
         * this is the quicker method.
         * @return "Licensed" If Genuinely Activated as enum
         * @throws IOException if error occurs
         */
        public static Status getStatusAsEnum() throws IOException {
            return switch (getStatusFromSLMGR().get()) {
                case "Unlicensed" -> Status.Unlicensed;
                case "Licensed" -> Status.Licensed;
                case "Out-Of-Box Grace" -> Status.OutOfBoxGrace;
                case "Out-Of-Tolerance Grace" -> Status.OutOfToleranceGrace;
                case "Non Genuine Grace" -> Status.NonGenuineGrace;
                case "Notification" -> Status.Notification;
                case "Extended Grace" -> Status.ExtendedGrace;
                default -> Status.Unknown;
            };
        }

        /**
         * Identifies If Windows is Activated, uses the Software Licensing Manager Script,
         * this is the quicker method.
         * @return "Licensed" If Genuinely Activated
         * @throws IOException if an error occurs
         */
        public static StringValue getStatusString() throws IOException { return getStatusFromSLMGR(); }

        /**
         * Identifies If Windows is Activated, uses WMI.
         * @return Licensed If Genuinely Activated
         * @throws IOException if error occurs
         * @throws InterruptedException if command is interrupted
         */
        public static @NotNull StringValue getStatusFromWMI() throws IOException, InterruptedException {
            final var ComputerName = "localhost";

            final var LicenseStatus = WMI.getWMIValue("SELECT * "
                    + "FROM SoftwareLicensingProduct "
                    + "Where PartialProductKey <> null "
                    + "AND ApplicationId='55c92734-d682-4d71-983e-d6ec3f16059f' "
                    + "AND LicenseisAddon=False", "LicenseStatus");

            String value = switch (LicenseStatus.toInteger().get()) {
                case 0 -> "Unlicensed";
                case 1 -> "Licensed";
                case 2 -> "Out-Of-Box Grace";
                case 3 -> "Out-Of-Tolerance Grace";
                case 4 -> "Non Genuine Grace";
                case 5 -> "Notification";
                case 6 -> "Extended Grace";
                default -> "Unknown License Status";
            };

            return StringValue.of(value);
        }

        /**
         * Identifies If Windows is Activated, uses the Software Licensing Manager Script,
         * this is the quicker method.
         * @return Licensed If Genuinely Activated
         * @throws IOException if an error occurs
         */
        //TODO: fixme
        public static @NotNull StringValue getStatusFromSLMGR() throws IOException {
            final ExecCommand cmd;
            try {
                cmd = ExecCommand.runNewVBS("C:\\Windows\\System32\\slmgr.vbs", "/dli");
                for(final String line : cmd.getResult()) {
                    if(line.contains("License Status: ")) {
                        return StringValue.of(line.replace("License Status: ", ""));
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return StringValue.of("Unknown");
//            while(true) {
//                final var p = Runtime.getRuntime().exec(
//                        new String[]{ "cscript C:\\Windows\\System32\\slmgr.vbs /dli" });
//                try(final var stdOut = new BufferedReader(new InputStreamReader(p.getInputStream(), UTF_8))) {
//                    String s;
//                    while((s = stdOut.readLine()) != null) {
//                        //System.out.println(s);
//                        if(s.contains("License Status: ")) {
//                            return StringValue.of(s.replace("License Status: ", ""));
//                        }
//                    }
//                }
//            }
        }

        /** A list of Activation statuses that are the result of the methods in the {@link WindowsOSEx.Activation} class. */
        @SuppressWarnings("InnerClassTooDeeplyNested")
        public enum Status {
            Unlicensed,
            Licensed,
            OutOfBoxGrace,
            OutOfToleranceGrace,
            NonGenuineGrace,
            Notification,
            ExtendedGrace,
            Unknown
        }

        /** Prevents instantiation of this utility class. */
        private Activation() { }
    }

    /** Returns the product type of the operating system running on this Computer. */
    @SuppressWarnings("unused")
    public static final class Edition {
        /**
         * Identifies if OS is a Windows Server OS.
         * @return true if OS is a Windows Server OS
         */
        public static boolean isWindowsServer() {
            return ProductType.NTWorkstation != Edition.getProductTypeEnum();
        }

        /**
         * Identifies if OS is a Windows Domain Controller.
         * @return true if OS is a Windows Server OS
         */
        public static boolean isWindowsDomainController() {
            return ProductType.NTDomainController == Edition.getProductTypeEnum();
        }

        /**
         * Returns the product type of the OS as an integer.
         * @return integer equivalent of the operating system product type
         */
        public static IntegerValue getProductType() {
            final WinNT.OSVERSIONINFOEX osVersionInfo = new WinNT.OSVERSIONINFOEX();
            return IntegerValue.of(getVersionInfoFailed(osVersionInfo)
                    ? ProductEdition.Undefined.getValue()
                    : osVersionInfo.wProductType);
        }

        /**
         * Returns the product type of the OS as an enum.
         * @return enum equivalent of the operating system product type
         */
        public static ProductType getProductTypeEnum() {
            return ProductType.parse(getProductType());
        }

        /**
         * Returns the product type of the OS as a string.
         * @return string containing the operating system product type
         * @throws IOException if error occurs
         * @throws InterruptedException if command is interrupted
         */
        public static StringValue getString() throws IOException, InterruptedException {
            if(Version.getMajor().isEqualTo(5)) return getVersion5();
            if(Version.getMajor().isEqualTo(6)
                    || Version.getMajor().isEqualTo(10)) return getVersion6AndUp();
            return StringValue.EMPTY;
        }

        /**
         * Returns the product type from Windows 2000 to XP and Server 2000 to 2003.
         * @return the version string
         */
        @SuppressWarnings({"MethodWithMultipleReturnPoints", "OverlyComplexMethod", "OverlyLongMethod"})
        private static StringValue getVersion5() {
            final WinNT.OSVERSIONINFOEX osVersionInfo = new WinNT.OSVERSIONINFOEX();
            if(getVersionInfoFailed(osVersionInfo)) return StringValue.EMPTY;
            final List<VERSuite> verSuite = VERSuite.parse(osVersionInfo.wSuiteMask);

            if(getSystemMetrics(OtherConsts.SMMediaCenter)) return StringValue.of(" Media Center");
            if(getSystemMetrics(OtherConsts.SMTabletPC)) return StringValue.of(" Tablet PC");
            if(isWindowsServer()) {
                if(Version.getMinor().isEqualTo(0)) {
                    // Windows 2000 Datacenter Server
                    if(verSuite.contains(VERSuite.Datacenter)) return StringValue.of(" Datacenter Server");
                    // Windows 2000 Advanced Server
                    if(verSuite.contains(VERSuite.Enterprise)) return StringValue.of(" Advanced Server");
                    // Windows 2000 Server
                    return StringValue.of(" Server");
                }
                if(Version.getMinor().isEqualTo(2)) {
                    // Windows Server 2003 Datacenter Edition
                    if(verSuite.contains(VERSuite.Datacenter))
                        return StringValue.of(" Datacenter Edition");
                    // Windows Server 2003 Enterprise Edition
                    if(verSuite.contains(VERSuite.Enterprise))
                        return StringValue.of(" Enterprise Edition");
                    // Windows Server 2003 Storage Edition
                    if(verSuite.contains(VERSuite.StorageServer))
                        return StringValue.of(" Storage Edition");
                    // Windows Server 2003 Compute Cluster Edition
                    if(verSuite.contains(VERSuite.ComputeServer))
                        return StringValue.of(" Compute Cluster Edition");
                    // Windows Server 2003 Web Edition
                    if(verSuite.contains(VERSuite.Blade))
                        return StringValue.of(" Web Edition");
                    // Windows Server 2003 Standard Edition
                    return StringValue.of(" Standard Edition");
                }
            } else {
                //Windows XP Embedded
                if(verSuite.contains(VERSuite.EmbeddedNT)) return StringValue.of(" Embedded");
                // Windows XP / Windows 2000 Professional
                return StringValue.of((verSuite.contains(VERSuite.Personal)) ? " Home" : " Professional");
            }
            return StringValue.EMPTY;
        }

        /**
         * Returns the product type from Windows Vista to 10 and Server 2008 to 2016.
         * @return the version string
         */
        private static @NotNull StringValue getVersion6AndUp() {
            final IntByReference strProductType = new IntByReference();
            var ignored = Kernel32.INSTANCE.GetProductInfo(Version.getMajor().get(), Version.getMinor().get(),
                    0, 0, strProductType);
            return ProductEdition.parse(strProductType.getValue()).getFullName();
        }

        /** Prevents instantiation of this utility class. */
        private Edition() { }
    }

    /** Returns the different names provided by the operating system. */
    public static final class Name {
        /**
         * Returns the current computer name.
         * @return String value of current computer name
         */
        public static StringValue ComputerNameActive() {
            final var key = "System\\ControlSet001\\Control\\ComputerName\\ActiveComputerName";
            final var value = "ComputerName";
            return Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
        }

        /**
         * Returns the pending computer name that it will update to on reboot.
         * @return String value of the pending computer name
         */
        public static StringValue ComputerNamePending() {
            final var key = "System\\ControlSet001\\Control\\ComputerName\\ComputerName";
            final var value = "ComputerName";
            final var text = Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
            return text.trim().isEmpty() ? StringValue.of("N/A") : text;
        }

        /** Prevents instantiation of this utility class. */
        private Name() { }
    }

    /** Returns the service pack information of the operating system running on this Computer. */
    public static final class ServicePack {
        /**
         * Returns the service pack information of the operating system running on this Computer.
         * @return A String containing the operating system service pack information
         * @throws IOException if error occurs
         * @throws InterruptedException if command is interrupted
         */
        public static @NotNull StringValue getString() throws IOException, InterruptedException {
            final String sp = getNumber().toString();
            return StringValue.of(isWin8OrLater() ? "" : sp.trim().isEmpty() ? "Service Pack 0" : "Service Pack " + sp);
        }

        /**
         * Returns the service pack information of the operating system running on this Computer.
         * @return An int containing the operating system service pack number
         */
        public static @NotNull IntegerValue getNumber() {
            final WinNT.OSVERSIONINFOEX osVersionInfo = new WinNT.OSVERSIONINFOEX();
            return IntegerValue.of(getVersionInfoFailed(osVersionInfo)
                    ? -1
                    : osVersionInfo.wServicePackMajor.intValue());
        }

        /** Prevents instantiation of this utility class. */
        private ServicePack() { }
    }

    /** Returns info about the currently logged-in user account. */
    @SuppressWarnings("unused")
    public static final class Users {
        /**
         * Returns the current Registered Organization.
         * @return Registered Organization as string
         */
        public static StringValue getRegisteredOrganization() {
            final var key = "Software\\Microsoft\\Windows NT\\CurrentVersion";
            final var value = "RegisteredOrganization";
            return Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
        }

        /**
         * Returns the current Registered Owner.
         * @return Registered Owner as string
         */
        public static StringValue getRegisteredOwner() {
            final var key = "Software\\Microsoft\\Windows NT\\CurrentVersion";
            final var value = "RegisteredOwner";
            return Registry.getStringValue(Registry.HKEY.LOCAL_MACHINE, key, value);
        }

        /**
         * Returns the username of the person who is currently logged on to the Windows operating system.
         * @return Logged-in username as string
         * @throws IllegalStateException if cannot the logged-in username retrieved
         */
        @SuppressWarnings("StringSplitter")
        public static StringValue getLoggedInUserName() {
            final char[] userNameBuf = new char[10000];
            final IntByReference size = new IntByReference(userNameBuf.length);
            final boolean result = Secur32.INSTANCE.GetUserNameEx(
                    Secur32.EXTENDED_NAME_FORMAT.NameSamCompatible, userNameBuf, size);

            if(!result)
                throw new IllegalStateException("Cannot retrieve name of the currently logged-in user");

            return StringValue.of(new String(userNameBuf, 0, size.getValue()).split("\\\\")[1]);
        }

        /**
         * Returns the network domain name associated with the current user.
         * @return Current domain name as string
         * @throws IllegalStateException if the joined domain cannot be retrieved
         */
        @SuppressWarnings("StringSplitter")
        public static StringValue getCurrentDomainName() {
            final var userNameBuf = new char[10000];
            final var size = new IntByReference(userNameBuf.length);
            final var result = Secur32.INSTANCE.GetUserNameEx(
                    Secur32.EXTENDED_NAME_FORMAT.NameSamCompatible, userNameBuf, size);

            if(!result)
                throw new IllegalStateException("Cannot retrieve name of the currently joined domain");

            final var username = new String(userNameBuf, 0, size.getValue()).split("\\\\");
            return StringValue.of(username[0]);
        }

        /**
         * Returns the current host name for the system.
         * @return Current domain name as string
         * @throws UnknownHostException if error occurs
         */
        public static StringValue getCurrentMachineName() throws UnknownHostException {
            final var localMachineIP = InetAddress.getLocalHost();
            return StringValue.of(localMachineIP.getHostName());
        }

        /**
         * Identifies if the current user is an account administrator.
         * @return true if current user is an account administrator
         */
        public static boolean isCurrentUserAdmin() {
            final Advapi32Util.Account[] groups = Advapi32Util.getCurrentUserGroups();
            for(final Advapi32Util.Account group : groups) {
                final WinNT.PSIDByReference sid = new WinNT.PSIDByReference();
                Advapi32.INSTANCE.ConvertStringSidToSid(group.sidString, sid);
                if(Advapi32.INSTANCE.IsWellKnownSid(sid.getValue(),
                        WinNT.WELL_KNOWN_SID_TYPE.WinBuiltinAdministratorsSid)) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }

        /** Prevents instantiation of this utility class. */
        private Users() { }
    }

    /** Returns the full version of the operating system running on this Computer. */
    @SuppressWarnings("unused")
    public static final class Version {
        private static final com.jwcomptech.commons.info.Version versionObj;

        static {
            try {
                versionObj = getVersionInfo();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Returns the Version object representing the version of the operating system running on this Computer.
         * @return Version object
         */
        public static com.jwcomptech.commons.info.Version get() {
            return versionObj;
        }

        /**
         * Returns the full version of the operating system running on this Computer.
         * @return Full version as string
         */
        public static StringValue getMain() {
            return versionObj.main();
        }

        /**
         * Returns the major version of the operating system running on this Computer.
         * @return Major version as int
         */
        public static IntegerValue getMajor() {
            return versionObj.major(); }

        /**
         * Returns the minor version of the operating system running on this Computer.
         * @return Minor version as int
         */
        public static IntegerValue getMinor() {
            return versionObj.minor(); }

        /**
         * Returns the build version of the operating system running on this Computer.
         * @return Build version as int
         */
        public static IntegerValue getBuild() {
            return versionObj.build(); }

        /**
         * Returns the revision version of the operating system running on this Computer.
         * @return Build Revision as int
         */
        public static IntegerValue getRevision() {
            return versionObj.revision(); }

        /**
         * Returns a numeric value representing OS version.
         * @return OSMajorVersion times 10 plus OSMinorVersion
         */
        public static IntegerValue getNumber() {
            return versionObj.number();
        }

        @Contract(" -> new")
        private static com.jwcomptech.commons.info.@NotNull Version getVersionInfo() throws IOException, InterruptedException {
            final StringValue VersionString = WMI.getWMIValue("SELECT * FROM "
                    + WMIClasses.OS.OperatingSystem,"Version");

            StringValue temp;
            final StringValue Major = VersionString.substring(0, VersionString.indexOf(".").get());
            temp = VersionString.substring(Major.length().get() + 1);
            final StringValue Minor = temp.substring(0, VersionString.indexOf(".").get() - 1);
            temp = VersionString.substring(Major.length().get() + 1 + Minor.length().get() + 1);
            final StringValue Build;
            if(temp.contains(".")) {
                Build = temp.substring(0, VersionString.indexOf(".").get() - 1);
                temp = VersionString.substring(
                        Major.length().get() + 1 + Minor.length().get() + 1 + Build.length().get() + 1);
            } else {
                Build = temp;
                temp = StringValue.of("0");
            }
            final StringValue Revision = temp;

            final IntegerValue Number = IntegerValue.of(
                    Integer.parseInt(Major.get()) * 10 + Integer.parseInt(Minor.get()));

            return new com.jwcomptech.commons.info.Version(
                    VersionString,
                    Major.toInteger(),
                    Minor.toInteger(),
                    Build.toInteger(),
                    Revision.toInteger(),
                    Number
            );
        }

        /** Prevents instantiation of this utility class. */
        private Version() { }
    }

    /** Returns information from WMI. */
    public static final class WMI {
        private static final String CRLF = System.lineSeparator();

        /**
         * Generate a VBScript string capable of querying the desired WMI information.
         * @param wmiQueryStr                the query string to be passed to the WMI sub-system. <br>i.e. "Select *
         *                                   FROM Win32_ComputerSystem"
         * @param wmiCommaSeparatedFieldName a comma separated list of the WMI fields to be collected from the query
         *                                   results. <br>i.e. "Model"
         * @return the vbscript string.
         */
        //TODO: Change to use multiline string instead of StringBuilder
        @SuppressWarnings("StringSplitter")
        private static @NotNull StringValue getVBScript(final String wmiQueryStr,
                                                        final @NotNull String wmiCommaSeparatedFieldName) {
            final StringBuilder vbs = new StringBuilder();
            vbs.append("Dim oWMI : Set oWMI = GetObject(\"winmgmts:\")").append(CRLF);
            vbs.append(String.format("Dim classComponent : Set classComponent = oWMI.ExecQuery(\"%s\")",
                    wmiQueryStr)).append(CRLF);
            vbs.append("Dim obj, strData").append(CRLF);
            vbs.append("For Each obj in classComponent").append(CRLF);
            final String[] wmiFieldNameArray = wmiCommaSeparatedFieldName.split(",");
            for(final String fieldName : wmiFieldNameArray) {
                vbs.append(String.format("  strData = strData & obj.%s & VBCrLf", fieldName)).append(CRLF);
            }
            vbs.append("Next").append(CRLF);
            vbs.append("wscript.echo strData").append(CRLF);
            return StringValue.of(vbs.toString());
        }

        /**
         * Get an environment variable from Windows.
         * @param variableName The name of the environment variable to get
         * @return The value of the environment variable
         * @throws IOException if error occurs
         * @throws InterruptedException if command is interrupted
         * @throws IllegalArgumentException if Environment Variable doesn't exist
         */
        @SuppressWarnings("HardcodedFileSeparator")
        public static @NotNull StringValue getEnvironmentVar(final String variableName) throws IOException, InterruptedException {
            final String varName = '%' + variableName + '%';
            String variableValue = ExecCommand.runNewCmd("cmd", "/C echo " + varName)
                    .getResult().getFirst();
            //execute(new String[] {"cmd.exe", "/C", "echo " + varName});
            variableValue = variableValue.replace("\"", "");
            checkArgument(!variableValue.equals(varName),
                    String.format("Environment variable '%s' does not exist!", variableName));
            return StringValue.of(variableValue);
        }

        /**
         * Get the given WMI value from the WMI subsystem on the local computer.
         * @param wmiQueryStr                the query string as syntactically defined by the WMI reference
         * @param wmiCommaSeparatedFieldName the field object that you want to get out of the query results
         * @return the value
         * @throws IOException if error occurs
         * @throws InterruptedException if command is interrupted
         */
        public static @NotNull StringValue getWMIValue(final String wmiQueryStr,
                                                       final String wmiCommaSeparatedFieldName)
                throws IOException, InterruptedException {
            var cmd = ExecCommand.runNewVBS(getVBScript(wmiQueryStr, wmiCommaSeparatedFieldName));
            String output = cmd.getResult().toString();
            return StringValue.of(output.trim());
        }

        /**
         * Instantiate a WMI Query in the default namespace
         *
         * @param <T>
         *                     The enum type
         * @param wmiClassName The WMI Class to use. May include a WHERE clause
         *                     with filtering conditions.
         * @param propertyEnum An Enum that contains the properties to query
         * @return a new WMI Query object
         */
        @Contract("_, _ -> new")
        public static <T extends Enum<T>> WbemcliUtil.@NotNull WmiQuery<T> newWmiQuery(final String wmiClassName,
                                                                                       final Class<T> propertyEnum) {
            checkArgumentNotNullOrEmpty(wmiClassName, cannotBeNullOrEmpty("wmiClassName"));
            checkArgumentNotNull(propertyEnum, cannotBeNull("propertyEnum"));
            return new WbemcliUtil.WmiQuery<>(wmiClassName, propertyEnum);
        }

        /** Prevents instantiation of this utility class. */
        private WMI() { }
    }

    /**
     * Identifies if OS is activated.
     * @return true if activated, false if not activated
     * @throws IOException if error occurs
     */
    public static boolean isActivated() throws IOException { return Activation.isActivated(); }

    /**
     * Identifies if OS is a Windows Domain Controller.
     * @return true if OS is a Windows Server OS
     */
    public static boolean isWindowsDomainController() {
        return ProductType.NTDomainController == ProductType.parse(Edition.getProductType());
    }

    /**
     * Identifies if computer has joined a domain.
     * @return true if computer has joined a domain
     * @throws UnknownHostException if error occurs
     */
    public static boolean isDomainJoined() throws UnknownHostException {
        return !Users.getCurrentMachineName().equals(Users.getCurrentDomainName());
    }

    /**
     * Identifies if the current user is an account administrator.
     * @return true if current user is an account administrator
     */
    public static boolean isCurrentUserAdmin() {
        var isAdmin = false;
        final var groups = Advapi32Util.getCurrentUserGroups();
        for(final var group : groups) {
            final var sid = new WinNT.PSIDByReference();
            Advapi32.INSTANCE.ConvertStringSidToSid(group.sidString, sid);
            if(Advapi32.INSTANCE.IsWellKnownSid(sid.getValue(),
                    WinNT.WELL_KNOWN_SID_TYPE.WinBuiltinAdministratorsSid)) {
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }

    /**
     * Identifies if OS is a Windows Server OS.
     * @return true if OS is a Windows Server OS
     */
    public static boolean isWindowsServer() { return Edition.isWindowsServer(); }

    /**
     * Identifies if OS is XP or later.
     * @return true if XP or later, false if 2000 or previous
     */
    public static boolean isWinXPOrLater() {
        return Version.getNumber().isGreaterThanOrEqualTo(51); }

    /**
     * Identifies if OS is XP x64 or later.
     * @return true if XP x64 or later, false if XP or previous
     */
    public static boolean isWinXP64OrLater() {
        return Version.getNumber().isGreaterThanOrEqualTo(52); }

    /**
     * Identifies if OS is Vista or later.
     * @return true if Vista or later, false if XP or previous
     */
    public static boolean isWinVistaOrLater() {
        return Version.getNumber().isGreaterThanOrEqualTo(60); }

    /**
     * Identifies if OS is Windows 7 or later.
     * @return true if Windows 7 or later, false if Vista or previous
     */
    public static boolean isWin7OrLater() {
        return Version.getNumber().isGreaterThanOrEqualTo(61); }

    /**
     * Identifies if OS is Windows 8 or later.
     * @return true if Windows 8 or later, false if Windows 7 or previous
     * @throws IOException if error occurs
     * @throws InterruptedException if command is interrupted
     */
    public static boolean isWin8OrLater() throws IOException, InterruptedException {
        return Version.getNumber().isGreaterThanOrEqualTo(62); }

    /**
     * Identifies if OS is Windows 8.1 or later.
     * @return true if Windows 8.1 or later, false if Windows 8 or previous
     */
    public static boolean isWin81OrLater() {
        return Version.getNumber().isGreaterThanOrEqualTo(63); }

    /**
     * Identifies if OS is Windows 10 or later.
     * @return true if Windows 10 or later, false if Windows 8.1 or previous
     */
    public static boolean isWin10OrLater() {
        return Version.getNumber().isGreaterThanOrEqualTo(100); }

    /**
     * Identifies if OS is Windows 11 or later.
     * @return true if Windows 11 or later, false if Windows 8.1 or previous
     * @throws IOException if error occurs
     * @throws InterruptedException if command is interrupted
     */
    public static boolean isWin11OrLater() throws IOException, InterruptedException {
        return Version.getNumber().isGreaterThanOrEqualTo(100)
                && Version.getBuild().isGreaterThanOrEqualTo(22000); }

    /**
     * Identifies if OS is the specified OS or later.
     * @param os the OS type to check
     * @return true if the specified OS or later
     * @throws IOException if error occurs
     * @throws InterruptedException if command is interrupted
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static boolean isOrLater(final @NotNull OSList os) throws IOException, InterruptedException {
        return switch (os) {
            case MacOSX -> isMac();
            case Linux -> isLinux();
            case Solaris -> isSolaris();
            case FreeBSD -> isFreeBSD();
            case Android -> isAndroid();
            case Windows2000AndPrevious -> Version.getNumber().isLessThan(51);
            case WindowsXP -> isWinXPOrLater();
            case WindowsXP64 -> isWinXP64OrLater();
            case WindowsVista -> isWinVistaOrLater();
            case Windows7 -> isWin7OrLater();
            case Windows8 -> isWin8OrLater();
            case Windows81 -> isWin81OrLater();
            case Windows10 -> isWin10OrLater();
            case Windows11 -> isWin11OrLater();
            case WindowsServer2003 -> isWinXP64OrLater() && isWindowsServer()
                    && !getSystemMetrics(OtherConsts.SMServerR2);
            case WindowsServer2003R2 -> isWinXP64OrLater() && isWindowsServer()
                    && getSystemMetrics(OtherConsts.SMServerR2);
            case WindowsServer2008 -> isWinVistaOrLater() && isWindowsServer();
            case WindowsServer2008R2 -> isWin7OrLater() && isWindowsServer();
            case WindowsServer2012 -> isWin8OrLater() && isWindowsServer();
            case WindowsServer2012R2 -> isWin81OrLater() && isWindowsServer();
            case WindowsServer2016 -> isWin10OrLater() && isWindowsServer();
            case WindowsServer2019 -> isWin10OrLater() && isWindowsServer()
                    && Version.getBuild().isGreaterThanOrEqualTo(17763);
            case WindowsServer2022 -> isWin10OrLater() && isWindowsServer()
                    && Version.getBuild().isGreaterThanOrEqualTo(20348);
            case WindowsServer2025 -> isWin10OrLater() && isWindowsServer()
                    && Version.getBuild().isGreaterThanOrEqualTo(26100);
            case Unknown -> Boolean.FALSE;
        };
    }

    /**
     * Checks if a system metrics value is true.
     * @param index Value to check for
     * @return False if value is false
     */
    public static boolean getSystemMetrics(final int index) {
        return Kernel32.INSTANCE.GetSystemMetrics(index); }

    /**
     * Generates a new instance of the VersionInfo object.
     * @param osVersionInfo Empty VersionInfo object to fill
     * @return True if an error occurs
     */
    public static boolean getVersionInfoFailed(final WinNT.OSVERSIONINFOEX osVersionInfo) {
        return !Kernel32.INSTANCE.GetVersionEx(osVersionInfo); }

    /**
     * Generates a new instance of the ProductInfo object.
     * @param major version
     * @param minor version
     * @return Product Info as an int
     */
    public static int getProductInfo(final int major, final int minor) {
        final var strProductType = new IntByReference();
        Kernel32.INSTANCE.GetProductInfo(major, minor, 0, 0, strProductType);
        return strProductType.getValue();
    }

    /** Interface object to hold all the Kernel32 Instances. */
    @SuppressWarnings("InterfaceNeverImplemented")
    private interface Kernel32 extends com.sun.jna.platform.win32.Kernel32 {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class,
                W32APIOptions.DEFAULT_OPTIONS);

        boolean GetProductInfo(
                int dwOSMajorVersion,
                int dwOSMinorVersion,
                int dwSpMajorVersion,
                int dwSpMinorVersion,
                IntByReference pdwReturnedProductType);

        boolean GetSystemMetrics(int nIndex);
    }

    /**
     * An Install Info Object for use with the {@link ComputerInfo} class.
     */
        //TODO: Make available for all OSes
        public record InstallInfoObject(StringValue activationStatus,
                                        StringValue architecture,
                                        StringValue nameExpanded,
                                        StringValue name,
                                        StringValue servicePack,
                                        IntegerValue servicePackNumber,
                                        com.jwcomptech.commons.info.Version version) { }

    /** Prevents instantiation of this utility class. */
    private WindowsOSEx() { }
}
