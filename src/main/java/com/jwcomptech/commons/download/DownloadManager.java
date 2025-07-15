package com.jwcomptech.commons.download;

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

import com.google.errorprone.annotations.DoNotCall;
import com.jwcomptech.commons.annotations.Beta;
import com.jwcomptech.commons.values.StringValue;

@SuppressWarnings("unused")
@Beta
//TODO Finish implementation
public final class DownloadManager {
    private DownloadManager() {
    }

    @DoNotCall
    public static void download(final String url) {

    }

    @DoNotCall
    public static StringValue getString(final String url) {
        return StringValue.of("");
    }

    @DoNotCall
    public static StringValue getJSON(final String url) {
        return StringValue.of("");
    }
}
