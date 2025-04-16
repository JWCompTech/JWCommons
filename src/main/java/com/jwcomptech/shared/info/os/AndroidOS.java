package com.jwcomptech.shared.info.os;

import com.jwcomptech.shared.info.AbstractOperatingSystem;
import com.jwcomptech.shared.info.enums.OSList;
import com.jwcomptech.shared.utils.SingletonUtils;
import com.jwcomptech.shared.values.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AndroidOS extends AbstractOperatingSystem {
    private final Logger logger = LoggerFactory.getLogger(AndroidOS.class);

    private AndroidOS() { }

    public static synchronized AndroidOS getInstance() {
        return SingletonUtils.getInstance(AndroidOS.class, AndroidOS::new);
    }

    @Override
    public StringValue getName() {
        return StringValue.of("Android");
    }

    @Override
    public OSList getNameEnum() {
        return OSList.Android;
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
        return StringValue.of("Google");
    }

    @Override
    public boolean isServer() {
        return Boolean.FALSE;
    }

    @Override
    public boolean is64BitOS() {
        return false;
    }
}
