package com.jwcomptech.commons.utils.osutils.windows;

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

import com.jwcomptech.commons.values.StringValue;
import com.sun.jna.platform.win32.WinReg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;
import static com.sun.jna.platform.win32.Advapi32Util.*;

/**
 * Returns information from the Windows registry.
 *
 * @since 0.0.1
 */
public final class Registry {
    /**
     * Gets string value from a registry key.
     * @param root Root key to use to access key
     * @param key Key path to access
     * @param value Value name to access
     * @return Key value as string
     */
    public static StringValue getStringValue(final HKEY root, final String key, final String value) {
        if(valueExists(root, key, value)) {
            return StringValue.of(registryGetStringValue(root.getKeyObj(), key, value));
        }
        return StringValue.EMPTY;
    }

    public static boolean keyExists(final @NotNull HKEY root, final String key) {
        return registryKeyExists(root.getKeyObj(), key);
    }

    public static boolean valueExists(final @NotNull HKEY root, final String key, final String value) {
        return registryValueExists(root.getKeyObj(), key, value);
    }

    @Contract("_, _ -> new")
    public static @NotNull List<String> getKeys(final @NotNull HKEY root, final String keyPath) {
        return new LinkedList<>(Arrays.asList(registryGetKeys(root.keyObj, keyPath)));
    }

    /** A list of the different parent keys in the Windows Registry that are used in the {@link Registry} class. */
    @AllArgsConstructor
    @Getter
    @ToString
    public enum HKEY {
        CLASSES_ROOT(WinReg.HKEY_CLASSES_ROOT),
        CURRENT_USER(WinReg.HKEY_CURRENT_USER),
        LOCAL_MACHINE(WinReg.HKEY_LOCAL_MACHINE),
        USERS(WinReg.HKEY_USERS),
        PERFORMANCE_DATA(WinReg.HKEY_PERFORMANCE_DATA),
        CURRENT_CONFIG(WinReg.HKEY_CURRENT_CONFIG);

        private final WinReg.HKEY keyObj;
    }

    /** Prevents instantiation of this utility class. */
    private Registry() { throwUnsupportedExForUtilityCls(); }
}
