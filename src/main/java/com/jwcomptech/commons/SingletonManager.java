package com.jwcomptech.commons;

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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Stores all global singleton object instances.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public final class SingletonManager {
    private static final Map<Class<?>, Object> instances = new HashMap<>();

    /**
     * Returns the specified instance if it exists globally, if not creates it.
     * @param singletonClass the class of the type to create as a singleton
     * @param creator the code used to create a new instance
     * @param <T> the type of the singleton class
     * @return the specified instance if it exists globally, if not creates it
     */
    public synchronized static <T> @Nullable T getInstance(final Class<T> singletonClass, final Supplier<T> creator) {
        if(!instances.containsKey(singletonClass)) {
            if(creator == null) return null;
            instances.put(singletonClass, creator.get());
        }
        try {
            return singletonClass.cast(instances.get(singletonClass));
        } catch (ClassCastException e) {
            throw new IllegalStateException("Singleton class type mismatch");
        }
    }

    /**
     * Returns an immutable map of the existing instances.
     * @return an immutable map of the existing instances
     */
    @Contract(pure = true)
    public static @NotNull @UnmodifiableView Map<Class<?>, Object> getInstances() {
        return Collections.unmodifiableMap(instances);
    }

    /**
     * Checks if the specified class already has an existing global singleton.
     * @param singletonClass the class to lookup
     * @return true if a singleton exists matching the specified class
     */
    public synchronized static boolean instanceExists(final Class<?> singletonClass) {
        return instances.containsKey(singletonClass);
    }

    /** Prevents instantiation of this utility class. */
    public SingletonManager() { }
}
