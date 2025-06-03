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
import com.sun.jna.platform.win32.WinDef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A list of WindowsVersion Suite Masks according to
 * <a href="http://msdn.microsoft.com/en-us/library/ms724833(VS.85).aspx">Microsoft Documentation</a>.
 *
 * @since 0.0.1
 */
@AllArgsConstructor
@Getter
@ToString
public enum VERSuite implements BaseEnum<Integer> {
    Unknown(0),
    //SmallBusiness(1),
    /** Enterprise. */
    Enterprise(2),

    //BackOffice(4),
    //Terminal(16),
    //SmallBusinessRestricted(32),
    /** EmbeddedNT. */
    EmbeddedNT(64),

    /** Datacenter. */
    Datacenter(128),

    //SingleUserTS(256),
    /** Personal. */
    Personal(512),

    /** Blade. */
    Blade(1024),
    /** StorageServer. */
    StorageServer(8192),
    /** ComputeServer. */
    ComputeServer(16384);
    //WHServer = (32768);

    //Must be Integer not int or compilation fails
    private final Integer value;

    public static List<VERSuite> parse(final WinDef.WORD value) {
        return Arrays.stream(values())
                .filter(vs -> (value.intValue() & vs.value) != 0)
                .collect(Collectors.toList());
    }

    public static VERSuite parse(final int value) {
        return Arrays.stream(VERSuite.values())
                .filter(type -> type.value == value)
                .findFirst()
                .orElse(Unknown);
    }
}
