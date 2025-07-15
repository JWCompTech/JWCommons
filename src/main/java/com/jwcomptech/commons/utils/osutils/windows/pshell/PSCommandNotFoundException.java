package com.jwcomptech.commons.utils.osutils.windows.pshell;

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

import java.io.Serial;

@SuppressWarnings("unused")
public class PSCommandNotFoundException extends PowerShellParserErrorException {
    @Serial
    private static final long serialVersionUID = 6593243817776902869L;

    public PSCommandNotFoundException() { }

    public PSCommandNotFoundException(final String message) {
        super(message);
    }

    public PSCommandNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PSCommandNotFoundException(final Throwable cause) {
        super(cause);
    }

    protected PSCommandNotFoundException(final String message,
                                         final Throwable cause,
                                         final boolean enableSuppression,
                                         final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
