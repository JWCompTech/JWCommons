package com.jwcomptech.shared.values;

import com.google.errorprone.annotations.Immutable;

import java.io.Serializable;
import java.lang.constant.Constable;

/**
 * Provides immutable access to a value.
 *
 * @param <T> the type to set and get
 *
 * @since 0.0.1
 */
@Immutable
public interface ImmutableValue<T> extends Comparable<T>, Serializable, Constable {
    /**
     * Returns the value.
     * @return the stored value
     */
    T get();
}
