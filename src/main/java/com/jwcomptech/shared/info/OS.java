package com.jwcomptech.shared.info;

import com.jwcomptech.shared.info.enums.OSList;
import com.jwcomptech.shared.info.enums.OSType;
import com.jwcomptech.shared.values.IntegerValue;
import com.jwcomptech.shared.values.StringValue;

import static com.jwcomptech.shared.info.OSInfo.*;

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
    private OS() { }
}
