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
import com.jwcomptech.commons.values.IntegerValue;
import com.jwcomptech.commons.values.StringValue;

import static com.jwcomptech.commons.info.OSInfo.*;
import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

public final class OS {
    public static final OSType osType;
    public static final StringValue manufacturer;
    public static final StringValue version;
    public static final IntegerValue architecture;
    public static final StringValue architectureString;
    public static final StringValue name;
    public static final OSList nameEnum;
    public static final StringValue nameExpanded;
    public static final boolean isWindows;
    public static final boolean isMac;
    public static final boolean isLinux;
    public static final boolean isSolaris;
    public static final boolean isFreeBSD;
    public static final boolean isAndroid;
    public static final boolean isServer;

    static {
        osType = OSInfo.getOSType();
        isWindows = isWindows();
        isMac = isMac();
        isLinux = isLinux();
        isSolaris = isSolaris();
        isFreeBSD = isFreeBSD();
        isAndroid = isAndroid();

        //NOTE: these must be after the "is" methods or things won't work
        manufacturer = getManufacturer();
        version = getVersion();
        architecture = getBitNumber();
        architectureString = getBitString();
        name = getName();
        nameEnum = getNameEnum();
        nameExpanded = getNameExpanded();
        isServer = isServer();
    }

    /** Prevents instantiation of this utility class. */
    private OS() { throwUnsupportedExForUtilityCls(); }
}
