package com.jwcomptech.shared.info;

import com.jwcomptech.shared.values.IntegerValue;
import com.jwcomptech.shared.values.StringValue;
import java.util.Locale;

import static com.jwcomptech.shared.utils.StringUtils.toTitleCase;

public abstract class AbstractOperatingSystem implements OperatingSystem {
    protected final StringValue OS_NAME = StringValue.of(toTitleCase(
            System.getProperty("os.name", "generic").toLowerCase(Locale.getDefault())));

    @Override
    public final boolean is32BitOS() {
        return !is64BitOS();
    }

    @Override
    public final StringValue getBitString() {
        return StringValue.of(is64BitOS() ? "64 bit" : "32 bit");
    }

    @Override
    public final IntegerValue getBitNumber() {
        return IntegerValue.of(is64BitOS() ? 64 : 32);
    }
}
