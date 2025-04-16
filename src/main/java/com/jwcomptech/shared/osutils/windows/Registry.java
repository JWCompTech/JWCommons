package com.jwcomptech.shared.osutils.windows;

import com.jwcomptech.shared.values.StringValue;
import com.sun.jna.platform.win32.WinReg;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.sun.jna.platform.win32.Advapi32Util.*;

/** Returns information from the Windows registry. */
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

    public static boolean keyExists(final HKEY root, final String key) {
        return registryKeyExists(root.getKeyObj(), key);
    }

    public static boolean valueExists(final HKEY root, final String key, final String value) {
        return registryValueExists(root.getKeyObj(), key, value);
    }

    public static List<String> getKeys(final HKEY root, final String keyPath) {
        return new LinkedList<>(Arrays.asList(registryGetKeys(root.keyObj, keyPath)));
    }

    /** A list of the different parent keys in the Windows Registry that are used in the {@link Registry} class. */
    public enum HKEY {
        CLASSES_ROOT(WinReg.HKEY_CLASSES_ROOT),
        CURRENT_USER(WinReg.HKEY_CURRENT_USER),
        LOCAL_MACHINE(WinReg.HKEY_LOCAL_MACHINE),
        USERS(WinReg.HKEY_USERS),
        PERFORMANCE_DATA(WinReg.HKEY_PERFORMANCE_DATA),
        CURRENT_CONFIG(WinReg.HKEY_CURRENT_CONFIG);

        final WinReg.HKEY keyObj;

        HKEY(final WinReg.HKEY keyObj) { this.keyObj = keyObj;}

        public WinReg.HKEY getKeyObj() { return keyObj;}
    }

    /** Prevents instantiation of this utility class. */
    private Registry() { }
}
