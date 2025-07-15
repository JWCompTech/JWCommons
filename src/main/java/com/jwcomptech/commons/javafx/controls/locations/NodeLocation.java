package com.jwcomptech.commons.javafx.controls.locations;

/*-
 * #%L
 * JWCommons
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

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.*;

@SuppressWarnings("unused")
@Data
public abstract class NodeLocation<T> {
    @Getter(AccessLevel.PRIVATE)
    private final Map<String, T> locationValues = new LinkedHashMap<>(4);

    public int size() {
        return locationValues.size();
    }

    public boolean isEmpty() {
        return locationValues.isEmpty();
    }

    public boolean containsKey(final String key) {
        return locationValues.containsKey(key);
    }

    public boolean containsValue(final T value) {
        return locationValues.containsValue(value);
    }

    public T get(final String key) {
        return locationValues.get(key);
    }

    protected T put(final String key, final T value) {
        return locationValues.put(key, value);
    }

    public Set<String> keySet() {
        return Collections.unmodifiableSet(locationValues.keySet());
    }

    public Collection<T> values() {
        return Collections.unmodifiableCollection(locationValues.values());
    }

    public T getOrDefault(final String key, final T defaultValue) {
        return locationValues.getOrDefault(key, defaultValue);
    }
}
