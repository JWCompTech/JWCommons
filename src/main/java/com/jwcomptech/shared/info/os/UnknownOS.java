package com.jwcomptech.shared.info.os;

import com.jwcomptech.shared.info.AbstractOperatingSystem;
import com.jwcomptech.shared.info.enums.OSList;
import com.jwcomptech.shared.utils.SingletonUtils;
import com.jwcomptech.shared.values.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnknownOS extends AbstractOperatingSystem {
    private final Logger logger = LoggerFactory.getLogger(UnknownOS.class);

    private UnknownOS() { }

    public static synchronized UnknownOS getInstance() {
        return SingletonUtils.getInstance(UnknownOS.class, UnknownOS::new);
    }

    @Override
    public StringValue getName() {
        return OS_NAME;
    }

    @Override
    public OSList getNameEnum() {
        return OSList.Unknown;
    }

    @Override
    public StringValue getNameExpanded() {
        return OS_NAME;
    }

    @Override
    public StringValue getVersion() {
        return StringValue.of("Unknown");
    }

    @Override
    public StringValue getManufacturer() {
        return StringValue.of("Unknown");
    }

    @Override
    public boolean isServer() {
        return Boolean.FALSE;
    }

    @Override
    public boolean is64BitOS() {
        return System.getProperty("os.arch").contains("64");
    }
}
