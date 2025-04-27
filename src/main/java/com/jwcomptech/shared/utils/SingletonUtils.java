package com.jwcomptech.shared.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class SingletonUtils {
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
    @SuppressWarnings("unused")
    public synchronized static boolean instanceExists(final Class<?> singletonClass) {
        return instances.containsKey(singletonClass);
    }

    /** Prevents instantiation of this utility class. */
    public SingletonUtils() { }
}
