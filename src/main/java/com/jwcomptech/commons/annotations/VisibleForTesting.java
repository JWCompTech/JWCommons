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

/**
 * Annotates a program element that exists, or is more widely visible than otherwise necessary, only
 * for use in test code.
 *
 * <p><b>Do not use this interface</b> for public or protected declarations: it is a fig leaf for
 * bad design, and it does not prevent anyone from using the declaration---and experience has shown
 * that they will. If the method breaks the encapsulation of its class, then its internal
 * representation will be hard to change. Instead, use <a
 * href="http://errorprone.info/bugpattern/RestrictedApi">RestrictedApiChecker</a>, which enforces
 * fine-grained visibility policies.
 *
 * @author Johannes Henkel
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
public @interface VisibleForTesting { }
