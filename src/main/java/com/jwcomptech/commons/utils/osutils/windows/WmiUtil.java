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

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Wbemcli;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.COM.WbemcliUtil.WmiQuery;
import com.sun.jna.platform.win32.COM.WbemcliUtil.WmiResult;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

/**
 * Helper class for WMI
 *
 * @author Daniel Widdis(widdis[at]gmail[dot]com)
 * @since 0.0.1
 */
@SuppressWarnings("ProhibitedExceptionThrown")
public final class WmiUtil {
    private static final Logger LOG = LoggerFactory.getLogger(WmiUtil.class);

    // Global timeout for WMI queries
    private static final int wmiTimeout = Wbemcli.WBEM_INFINITE;

    // Cache namespaces
    private static final Set<String> hasNamespaceCache = new HashSet<>();
    private static final Set<String> hasNotNamespaceCache = new HashSet<>();

    // Cache failed wmi classes
    private static final Collection<String> failedWmiClassNames = new HashSet<>();
    // Not a built-in namespace, failed connections are normal and don't need error logging
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String OHM_NAMESPACE = "ROOT\\OpenHardwareMonitor";

    private static final String CLASS_CAST_MSG = "%s is not a %s type. CIM Type is %d and VT type is %d";

    // Track initialization of COM and Security
    private static boolean comInitialized;
    private static boolean securityInitialized;

    static {
        // Initialize COM
        initCOM();

        // Set up hook to uninit on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(WmiUtil::unInitCOM));
    }

    /**
     * Query WMI for values, with no timeout.
     *
     * @param <T>
     *            The properties enum
     * @param query
     *            A WmiQuery object encapsulating the namespace, class, and
     *            properties
     * @return a WmiResult object containing the query results, wrapping an
     *         EnumMap
     */
    public static <T extends Enum<T>> WmiResult<T> queryWMI(final @NotNull WmiQuery<T> query) {

        WmiResult<T> result = WbemcliUtil.INSTANCE.new WmiResult<>(query.getPropertyEnum());
        if(failedWmiClassNames.contains(query.getWmiClassName())) return result;
        try {
            // Initialize COM if not already done. Needed if COM was previously
            // initialized externally but is no longer initialized.
            if(!comInitialized) initCOM();

            result = query.execute(wmiTimeout);
        } catch(final COMException e) {
            // Ignore any exceptions with OpenHardwareMonitor
            if(!OHM_NAMESPACE.equals(query.getNameSpace())) {
                switch (e.getHresult().intValue()) {
                    case Wbemcli.WBEM_E_INVALID_NAMESPACE ->
                            LOG.warn("COM exception: Invalid Namespace {}", query.getNameSpace());
                    case Wbemcli.WBEM_E_INVALID_CLASS ->
                            LOG.warn("COM exception: Invalid Class {}", query.getWmiClassName());
                    case Wbemcli.WBEM_E_INVALID_QUERY ->
                            LOG.warn("COM exception: Invalid Query: {}", queryToString(query));
                    default -> LOG.warn(
                            "COM exception querying {}, which might not be on your system." +
                                    " Will not attempt to query it again. Error was: {}:",
                            query.getWmiClassName(), e.getMessage());
                }
                failedWmiClassNames.add(query.getWmiClassName());
            }
        } catch(final TimeoutException e) {
            LOG.error("WMI query timed out after {} ms: {}", wmiTimeout, queryToString(query));
        }
        return result;
    }

    /**
     * Translate a WmiQuery to the actual query string
     *
     * @param <T>
     *            The properties enum
     * @param query
     *            The WmiQuery object
     * @return The string that is queried in WMI
     */
    public static <T extends Enum<T>> String queryToString(final @NotNull WmiQuery<T> query) {
        final T[] props = query.getPropertyEnum().getEnumConstants();
        return Arrays.stream(props).map(Enum::name).collect(Collectors.joining(",", "SELECT ", " FROM " + query.getWmiClassName()));
    }

    /**
     * Gets a Uint16 value from a WmiResult. Note that while the CIM type is
     * unsigned, the return type is signed and requires further processing by
     * the user if unsigned values are desired.
     *
     * @param <T>
     *            The enum type containing the property keys
     * @param result
     *            The WmiResult from which to fetch the value
     * @param property
     *            The property (column) to fetch
     * @param index
     *            The index (row) to fetch
     * @return The stored value if non-null, 0 otherwise
     */
    public static <T extends Enum<T>> int getUint16(final @NotNull WmiResult<T> result, final T property, final int index) {
        if(result.getCIMType(property) == Wbemcli.CIM_UINT16) return getInt(result, property, index);
        throw new ClassCastException(String.format(CLASS_CAST_MSG, property.name(), "UINT16",
                result.getCIMType(property), result.getVtType(property)));
    }

    private static <T extends Enum<T>> int getInt(final @NotNull WmiResult<T> result, final T property, final int index) {
        final Object obj = result.getValue(property, index);
        if(obj == null) return 0;
        if(result.getVtType(property) == Variant.VT_I4) return (int) obj;
        throw new ClassCastException(String.format(CLASS_CAST_MSG, property.name(), "32-bit integer",
                result.getCIMType(property), result.getVtType(property)));
    }

    /**
     * Initializes COM library and sets security to impersonate the local user
     */
    public static void initCOM() {
        HRESULT hres;
        // Step 1: --------------------------------------------------
        // Initialize COM. ------------------------------------------
        if(!comInitialized) {
            hres = Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_MULTITHREADED);
            switch (hres.intValue()) {
                // Successful local initialization
                case COMUtils.S_OK -> comInitialized = true;

                // COM was already initialized
                case COMUtils.S_FALSE, WinError.RPC_E_CHANGED_MODE -> {
                }
                // Any other results is an error
                default -> throw new COMException("Failed to initialize COM library.");
            }
        }
        // Step 2: --------------------------------------------------
        // Set general COM security levels --------------------------
        if(!securityInitialized) {
            hres = Ole32.INSTANCE.CoInitializeSecurity(null, -1, null,
                    null, Ole32.RPC_C_AUTHN_LEVEL_DEFAULT, Ole32.RPC_C_IMP_LEVEL_IMPERSONATE,
                    null, Ole32.EOAC_NONE, null);
            // If security already initialized we get RPC_E_TOO_LATE
            // This can be safely ignored
            if(COMUtils.FAILED(hres) && hres.intValue() != WinError.RPC_E_TOO_LATE) {
                Ole32.INSTANCE.CoUninitialize();
                throw new COMException("Failed to initialize security.");
            }
            securityInitialized = true;
        }
    }

    /**
     * UnInitializes COM library if it was initialized by the {@link #initCOM()}
     * method. Otherwise, does nothing.
     */
    public static void unInitCOM() {
        if(comInitialized) {
            Ole32.INSTANCE.CoUninitialize();
            comInitialized = false;
        }
    }

    /** Prevents instantiation of this utility class. */
    private WmiUtil() { throwUnsupportedExForUtilityCls(); }
}
