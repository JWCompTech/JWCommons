package com.jwcomptech.commons.annotations;

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

import java.lang.annotation.*;

/**
 * Signifies that a public API (public class, method or field) is subject to incompatible changes,
 * or even removal, in a future release. An API bearing this annotation is exempt from any
 * compatibility guarantees made by its containing library. Note that the presence of this
 * annotation implies nothing about the quality or performance of the API in question, only the fact
 * that it is not "API-frozen."
 *
 * <p>It is generally safe for <i>applications</i> to depend on beta APIs, at the cost of some extra
 * work during upgrades. However, it is generally inadvisable for <i>libraries</i> (which get
 * included on users' CLASSPATHs, outside the library developers' control) to do so.
 *
 * @author Kevin Bourrillion
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.TYPE
})
@Documented
public @interface Beta { }
