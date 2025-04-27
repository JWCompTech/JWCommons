package com.jwcomptech.shared.info;

import com.jwcomptech.shared.info.os.WindowsOSEx;
import com.jwcomptech.shared.utils.SingletonUtils;
import com.jwcomptech.shared.values.IntegerValue;
import com.jwcomptech.shared.values.StringValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns Information about the current OS and Hardware on the current system.
 * @since 0.0.1
 */
//TODO: Change all objects to use Builder architecture
@SuppressWarnings("unused")
public final class ComputerInfo {
    private OSInfo.OSObject OSI;
    private HWInfo.HWObject HWI;

    public static synchronized ComputerInfo getInstance() {
        return SingletonUtils.getInstance(ComputerInfo.class, () -> {
            try {
                return new ComputerInfo();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private ComputerInfo() throws IOException, InterruptedException {
        HWI = reinitializeHW();
        OSI = reinitializeOS();
    }

    /* Reprocesses the OS information and returns a new OSObject. */
    public OSInfo.OSObject reinitializeOS() throws IOException, InterruptedException {
        final var activationStatus = WindowsOSEx.Activation.getStatusString();
        final var architecture = OS.architectureString;
        final var name = OS.name;
        final var nameExpanded = OS.nameExpanded;
        final var servicePack = WindowsOSEx.ServicePack.getString();
        final var servicePackNumber = WindowsOSEx.ServicePack.getNumber();
        final var version = WindowsOSEx.Version.get();

        final var iiobj = new WindowsOSEx.InstallInfoObject(
                activationStatus,
                architecture,
                nameExpanded,
                name,
                servicePack,
                IntegerValue.of(servicePackNumber),
                version);

        final var computerName = WindowsOSEx.Name.ComputerNameActive();
        final var computerNamePending = WindowsOSEx.Name.ComputerNamePending();
        final var domainName = WindowsOSEx.Users.getCurrentDomainName();
        final var loggedInUserName = WindowsOSEx.Users.getLoggedInUserName();
        final var registeredOrganization = WindowsOSEx.Users.getRegisteredOrganization();
        final var registeredOwner = WindowsOSEx.Users.getRegisteredOwner();

        OSI = new OSInfo.OSObject(
                computerName,
                computerNamePending,
                iiobj,
                registeredOrganization,
                registeredOwner,
                loggedInUserName,
                domainName
        );

        return OSI;
    }

    /* Reprocesses the Hardware information and returns a new HWObject. */
    public HWInfo.HWObject reinitializeHW() throws IOException {
        final var BIOSName = StringValue.of(HWInfo.BIOS.getVendor().get() + ' ' + HWInfo.BIOS.getVersion().get());
        final var BIOSReleaseDate = HWInfo.BIOS.getReleaseDate();
        final var BIOSVendor = HWInfo.BIOS.getVendor();
        final var BIOSVersion = HWInfo.BIOS.getVersion();
        final var biosobj = new HWInfo.BIOSObject(
                BIOSName,
                BIOSReleaseDate,
                BIOSVendor,
                BIOSVersion
        );

        final var connectionStatus = HWInfo.Network.isConnectedToInternet();
        final var internalIPAddress = HWInfo.Network.getInternalIPAddress();
        final var externalIPAddress = HWInfo.Network.getExternalIPAddress();
        final var nwobj = new HWInfo.NetworkObject(
                internalIPAddress,
                externalIPAddress,
                connectionStatus
        );

        final var processorName = HWInfo.Processor.Name();
        final var processorCores = HWInfo.Processor.Cores();
        final var pobj = new HWInfo.ProcessorObject(
                processorName,
                processorCores
        );

        final var totalInstalledRAM = HWInfo.RAM.getTotalRam();
        final var robj = new HWInfo.RAMObject(
                totalInstalledRAM
        );

        final var driveType = StringValue.of("Fixed");
        final var totalFree = HWInfo.Storage.getSystemDriveFreeSpace();
        final var totalSize = HWInfo.Storage.getSystemDriveSize();
        final var systemDrive = new HWInfo.DriveObject(
                driveType,
                totalSize,
                totalFree
        );

        final List<HWInfo.DriveObject> drives = new ArrayList<>();
        drives.add(systemDrive);

        final var sobj = new HWInfo.StorageObject(
                systemDrive,
                drives
        );

        final var systemOEM = HWInfo.OEM.Name();
        final var productName = HWInfo.OEM.ProductName();

        HWI = new HWInfo.HWObject(
                systemOEM,
                productName,
                biosobj,
                nwobj,
                pobj,
                robj,
                sobj
        );

        return HWI;
    }

    public OSInfo.OSObject getOSI() {
        return OSI;
    }

    public HWInfo.HWObject getHWI() {
        return HWI;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (!(o instanceof ComputerInfo computerInfo)) return false;

        return new EqualsBuilder()
                .append(OSI, computerInfo.OSI)
                .append(HWI, computerInfo.HWI)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(OSI)
                .append(HWI)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("OSI", OSI)
                .append("HWI", HWI)
                .toString();
    }
}