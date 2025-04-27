package com.jwcomptech.shared.info.os;

import com.jwcomptech.shared.info.AbstractOperatingSystem;
import com.jwcomptech.shared.info.enums.OSList;
import com.jwcomptech.shared.utils.osutils.ExecCommand;
import com.jwcomptech.shared.utils.SingletonUtils;
import com.jwcomptech.shared.values.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class FreeBSDOS extends AbstractOperatingSystem {
    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(FreeBSDOS.class);

    private FreeBSDOS() { }

    public static synchronized FreeBSDOS getInstance() {
        return SingletonUtils.getInstance(FreeBSDOS.class, FreeBSDOS::new);
    }

    @Override
    public StringValue getName() {
        return StringValue.of("FreeBSD");
    }

    @Override
    public OSList getNameEnum() {
        return OSList.FreeBSD;
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

    @SuppressWarnings("HardcodedFileSeparator")
    @Override
    public StringValue getManufacturer() {
        return StringValue.of("Unix/BSD");
    }

    @Override
    public boolean isServer() {
        return Boolean.FALSE;
    }

    @Override
    public boolean is64BitOS() {
        try {
            return ExecCommand.runNewCmd("uname -m").getFirstResult().contains("64");
        } catch (final IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
