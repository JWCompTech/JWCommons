package com.jwcomptech.shared.utils.osutils.windows.enums;

import com.jwcomptech.shared.enums.BaseEnum;
import com.jwcomptech.shared.values.IntegerValue;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;

/**
 * A list of Product Types according to
 * <a href="http://msdn.microsoft.com/en-us/library/ms724833(VS.85).aspx">Microsoft Documentation</a>.
 */
public enum ProductType implements BaseEnum<Integer> {
    /** Unknown OS. */
    Unknown(0),
    /** Workstation. */
    NTWorkstation(1),
    /** Domain Controller. */
    NTDomainController(2),
    /** Server. */
    NTServer(3);

    private final int value;

    ProductType(final int value) {
        this.value = value;
    }

    public static ProductType parse(final int value) {
        return Arrays.stream(ProductType.values())
                .filter(type -> type.value == value)
                .findFirst()
                .orElse(null);
    }

    public static ProductType parse(final IntegerValue value) {
        return Arrays.stream(ProductType.values())
                .filter(type -> type.value == value.intValue())
                .findFirst()
                .orElse(null);
    }

    @Override
    public Integer getValue() { return value; }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .toString();
    }
}
