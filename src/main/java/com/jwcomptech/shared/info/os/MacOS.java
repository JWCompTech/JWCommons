package com.jwcomptech.shared.info.os;

import com.jwcomptech.shared.info.AbstractOperatingSystem;
import com.jwcomptech.shared.info.enums.OSList;
import com.jwcomptech.shared.utils.osutils.ExecCommand;
import com.jwcomptech.shared.utils.Parse;
import com.jwcomptech.shared.utils.SingletonUtils;
import com.jwcomptech.shared.values.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class MacOS extends AbstractOperatingSystem {
    private final Logger logger = LoggerFactory.getLogger(MacOS.class);

    private MacOS() { }

    public static synchronized MacOS getInstance() {
        return SingletonUtils.getInstance(MacOS.class, MacOS::new);
    }

    @Override
    public StringValue getName() {
        return StringValue.of("Mac OSX");
    }

    @Override
    public OSList getNameEnum() {
        return OSList.MacOSX;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    @Override
    public StringValue getNameExpanded() {
        return OS_NAME;
    }

    @Override
    public StringValue getVersion() {
        return StringValue.of(System.getProperty("os.version"));
    }

    @Override
    public StringValue getManufacturer() {
        return StringValue.of("Apple");
    }

    @Override
    public boolean isServer() {
        return Boolean.FALSE;
    }

    @Override
    public boolean is64BitOS() {
        try {
            return 64 == Parse.parseIntOrDefault(ExecCommand
                    .runNewCmd("getconf LONG_BIT").getFirstResult(), 32, logger);
        } catch (final IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
