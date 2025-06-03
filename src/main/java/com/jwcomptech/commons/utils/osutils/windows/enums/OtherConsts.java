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

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

public final class OtherConsts {
    //sysMetrics ( http://msdn.microsoft.com/en-us/library/ms724385(VS.85).aspx )
    public static final int SMTabletPC = 86;
    public static final int SMMediaCenter = 87;
    //public static final int SMStarter = 88;
    public static final int SMServerR2 = 89;

    /** Prevents instantiation of this utility class. */
    private OtherConsts() { throwUnsupportedExForUtilityCls(); }
}
