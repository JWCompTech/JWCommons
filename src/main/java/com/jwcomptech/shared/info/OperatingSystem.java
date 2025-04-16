package com.jwcomptech.shared.info;

import com.jwcomptech.shared.info.enums.OSList;
import com.jwcomptech.shared.values.IntegerValue;
import com.jwcomptech.shared.values.StringValue;

public interface OperatingSystem {
    StringValue getName();
    OSList getNameEnum();
    StringValue getNameExpanded();
    StringValue getVersion() throws Exception;
    StringValue getManufacturer();
    boolean isServer();
    boolean is32BitOS();
    boolean is64BitOS();
    StringValue getBitString();
    IntegerValue getBitNumber();
}
