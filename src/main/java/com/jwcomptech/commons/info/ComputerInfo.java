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

import com.jwcomptech.commons.info.os.WindowsOSEx;
import com.jwcomptech.commons.SingletonManager;
import com.jwcomptech.commons.values.IntegerValue;
import com.jwcomptech.commons.values.StringValue;
import lombok.Data;
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
@Data
//TODO: Change all objects to use Builder architecture
@SuppressWarnings("unused")
public final class ComputerInfo {
    private OSInfo.OSObject OSI;
    private HWInfo.HWObject HWI;

    public static synchronized ComputerInfo getInstance() {
        return SingletonManager.getInstance(ComputerInfo.class, () -> {
            try {
                return new ComputerInfo();
            } catch (final IOException | InterruptedException e) {
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
}
