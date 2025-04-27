package com.jwcomptech.shared.info;

import com.jwcomptech.shared.values.IntegerValue;
import com.jwcomptech.shared.values.StringValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static com.jwcomptech.shared.utils.StringUtils.toTitleCase;

public abstract class AbstractOperatingSystem implements OperatingSystem {
    protected final StringValue OS_NAME = StringValue.of(toTitleCase(
            System.getProperty("os.name", "generic").toLowerCase(Locale.getDefault())));

    protected AbstractOperatingSystem() { }

    @Override
    public final boolean is32BitOS() {
        return !is64BitOS();
    }

    @Contract(" -> new")
    @Override
    public final @NotNull StringValue getBitString() {
        return StringValue.of(is64BitOS() ? "64 bit" : "32 bit");
    }

    @Contract(" -> new")
    @Override
    public final @NotNull IntegerValue getBitNumber() {
        return IntegerValue.of(is64BitOS() ? 64 : 32);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        AbstractOperatingSystem that = (AbstractOperatingSystem) o;

        return new EqualsBuilder()
                .append(OS_NAME, that.OS_NAME)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(OS_NAME)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("OS_NAME", OS_NAME)
                .toString();
    }
}
