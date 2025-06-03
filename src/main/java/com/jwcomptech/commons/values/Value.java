package com.jwcomptech.commons.values;

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

import java.io.Serializable;

/**
 * Provides mutable access to a value.
 *
 * @param <T> the type to set and get
 * @param <V> the value of the object that implements
 *            this class to allow for method chaining
 * @since 0.0.1
 */
public interface Value<T, V> extends Comparable<Value<T, V>>, Serializable {
    /**
     * Returns the value.
     * @return the stored value
     */
    T get();
    /**
     * Sets the value.
     * @param value the value to store
     * @return this instance
     */
    V set(T value);
}
