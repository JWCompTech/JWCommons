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

import com.jwcomptech.commons.info.AbstractOperatingSystem;
import com.jwcomptech.commons.info.OSInfo;
import com.jwcomptech.commons.info.enums.OSList;
import com.jwcomptech.commons.utils.osutils.windows.WmiUtil;
import com.jwcomptech.commons.utils.osutils.windows.enums.OtherConsts;
import com.jwcomptech.commons.utils.osutils.windows.enums.WMIClasses;
import com.jwcomptech.commons.SingletonManager;
import com.jwcomptech.commons.values.StringValue;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.jwcomptech.commons.info.os.WindowsOSEx.*;

public final class WindowsOS extends AbstractOperatingSystem {
    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(WindowsOS.class);

    private WindowsOS() { }

    public static synchronized WindowsOS getInstance() {
        return SingletonManager.getInstance(WindowsOS.class, WindowsOS::new);
    }

    private enum ArchProperty { ADDRESS_WIDTH }

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public @NotNull StringValue getName() {
        String value = switch (getNameEnum()) {
            case WindowsXP -> "Windows XP";
            case WindowsXP64 -> "Windows XP x64";
            case WindowsVista -> "Windows Vista";
            case Windows7 -> "Windows 7";
            case Windows8 -> "Windows 8";
            case Windows81 -> "Windows 8.1";
            case Windows10 -> "Windows 10";
            case Windows11 -> "Windows 11";
            case WindowsServer2003 -> "Windows 2003";
            case WindowsServer2003R2 -> "Windows 2003 R2";
            case WindowsServer2008 -> "Windows 2008";
            case WindowsServer2008R2 -> "Windows 2008 R2";
            case WindowsServer2012 -> "Windows 2012";
            case WindowsServer2012R2 -> "Windows 2012 R2";
            case WindowsServer2016 -> "Windows 2016";
            case WindowsServer2019 -> "Windows 2019";
            case WindowsServer2022 -> "Windows 2022";
            case WindowsServer2025 -> "Windows 2025";
            default -> "Unknown";
        };

        return StringValue.of(value);
    }

    @SuppressWarnings("OverlyComplexMethod")
    @Override
    public OSList getNameEnum() {
        try {
            return switch (WindowsOSEx.Version.getNumber().get()) {
                case 51 -> OSList.WindowsXP;
                case 52 -> isServer() ? (getSystemMetrics(OtherConsts.SMServerR2)
                        ? OSList.WindowsServer2003R2 : OSList.WindowsServer2003) : OSList.WindowsXP64;
                case 60 -> isServer() ? OSList.WindowsServer2008 : OSList.WindowsVista;
                case 61 -> isServer() ? OSList.WindowsServer2008R2 : OSList.Windows7;
                case 62 -> isServer() ? OSList.WindowsServer2012 : OSList.Windows8;
                case 63 -> isServer() ? OSList.WindowsServer2012R2 : OSList.Windows81;
                case 100 -> {
                    if (isServer()) yield OSList.WindowsServer2016;
                    else if(isWin11OrLater()) yield OSList.Windows11;
                    else yield OSList.Windows10;
                }
                default -> OSList.Unknown;
            };
        } catch (final IOException | InterruptedException e) {
            return OSList.Unknown;
        }
    }

    /**
     * Returns a full version String, ex.: "Windows XP SP2 (32 Bit)".
     * @return String representing a fully displayable version
     */
    @Override
    public @NotNull StringValue getNameExpanded() {
        try {
            //noinspection StringConcatenationMissingWhitespace
            final String SPString = isWin8OrLater() ? "- " + WindowsOSEx.Version.getBuild()
                    : " SP" + WindowsOSEx.ServicePack.getString().replace("Service Pack ", "");

            return StringValue.of(getName().get() + ' ' + WindowsOSEx.Edition.getString().get() + ' '
                    + SPString + " (" + OSInfo.getBitNumber().get() + " Bit)");
        } catch (final IOException | InterruptedException e) {
            return StringValue.of("Unknown");
        }
    }

    @Override
    public StringValue getVersion() throws IOException, InterruptedException {
        return WindowsOSEx.Version.getMain();
    }

    @Override
    public StringValue getManufacturer() {
        return StringValue.of("Microsoft");
    }

    @Override
    public boolean isServer() {
        return WindowsOSEx.Edition.isWindowsServer();
    }

    @Override
    public boolean is64BitOS() {
        if (null != System.getenv("ProgramFiles(x86)")) return Boolean.TRUE;
        final WbemcliUtil.WmiQuery<ArchProperty> query =
                WindowsOSEx.WMI.newWmiQuery(WMIClasses.Hardware.Processor.getValue(), ArchProperty.class);
        final WbemcliUtil.WmiResult<ArchProperty> result = WmiUtil.queryWMI(query);
        if (0 < result.getResultCount()) {
            return 64 == WmiUtil.getUint16(result, ArchProperty.ADDRESS_WIDTH, 0);
        }

        return Boolean.FALSE;
    }
}
