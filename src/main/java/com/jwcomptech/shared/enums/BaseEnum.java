package com.jwcomptech.shared.enums;

/**
 * Is used as a base interface for all enums that store a value.
 * @param <T> the value of the enum
 */
@FunctionalInterface
public interface BaseEnum<T> {
    T getValue();
}
