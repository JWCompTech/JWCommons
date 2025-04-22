package com.jwcomptech.shared.values;

import java.io.Serializable;

/**
 * Provides immutable access to a value.
 *
 * @param <T> the type to set and get
 * @since 0.0.1
 */
public interface ImmutableValue<T> extends Comparable<T>, Serializable {
    /**
     * Returns the value.
     * @return the stored value
     */
    T get();
}
