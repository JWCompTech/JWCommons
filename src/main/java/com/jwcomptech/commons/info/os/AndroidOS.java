package com.jwcomptech.commons.info.os;

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

import com.jwcomptech.commons.info.AbstractOperatingSystem;
import com.jwcomptech.commons.info.enums.OSList;
import com.jwcomptech.commons.SingletonManager;
import com.jwcomptech.commons.values.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AndroidOS extends AbstractOperatingSystem {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(AndroidOS.class);

    private AndroidOS() { }

    public static synchronized AndroidOS getInstance() {
        return SingletonManager.getInstance(AndroidOS.class, AndroidOS::new);
    }

    @Override
    public StringValue getName() {
        return StringValue.of("Android");
    }

    @Override
    public OSList getNameEnum() {
        return OSList.Android;
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
        return StringValue.of("Google");
    }

    @Override
    public boolean isServer() {
        return Boolean.FALSE;
    }

    @Override
    public boolean is64BitOS() {
        return false;
    }
}
