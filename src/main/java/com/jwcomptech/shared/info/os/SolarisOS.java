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

public final class SolarisOS extends AbstractOperatingSystem {
    private final Logger logger = LoggerFactory.getLogger(SolarisOS.class);

    private SolarisOS() { }

    public static synchronized SolarisOS getInstance() {
        return SingletonUtils.getInstance(SolarisOS.class, SolarisOS::new);
    }

    @Override
    public StringValue getName() {
        return StringValue.of("Solaris");
    }

    @Override
    public OSList getNameEnum() {
        return OSList.Solaris;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
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
        return StringValue.of("Oracle");
    }

    @Override
    public boolean isServer() {
        return Boolean.FALSE;
    }

    @Override
    public boolean is64BitOS() {
        try {
            return 64 == Parse.parseIntOrDefault(ExecCommand
                    .runNewCmd("isainfo -b").getFirstResult(), 32, logger);
        } catch (final IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
