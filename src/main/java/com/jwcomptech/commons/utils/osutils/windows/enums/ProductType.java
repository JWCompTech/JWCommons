package com.jwcomptech.commons.utils.osutils.windows.enums;

/*-
 * #%L
 * JWCT Commons
 * %%
 * Copyright (C) 2025 JWCompTech
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.jwcomptech.commons.enums.BaseEnum;
import com.jwcomptech.commons.values.IntegerValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;

/**
 * A list of Product Types according to
 * <a href="http://msdn.microsoft.com/en-us/library/ms724833(VS.85).aspx">Microsoft Documentation</a>.
 *
 * @since 0.0.1
 */
@AllArgsConstructor
@Getter
@ToString
public enum ProductType implements BaseEnum<Integer> {
    /** Unknown OS. */
    Unknown(0),
    /** Workstation. */
    NTWorkstation(1),
    /** Domain Controller. */
    NTDomainController(2),
    /** Server. */
    NTServer(3);

    //Must be Integer not int or compilation fails
    private final Integer value;

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
}
