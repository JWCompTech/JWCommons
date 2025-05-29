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

import com.google.errorprone.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.lang.constant.DynamicConstantDesc;
import java.util.Optional;

import static com.jwcomptech.commons.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNull;

/**
 * Provides immutable access to a {@link Boolean}.
 *
 * @since 0.0.1
 * @apiNote DO NOT use everywhere or else things will get unnecessarily complicated.
 */
@SuppressWarnings({"ClassWithTooManyMethods", "unused"})
@Immutable
public final class BooleanValue implements ImmutableValue<Boolean> {
    private final Boolean value;
    /**
     * Required for serialization support.
     *
     * @see Serializable
     */
    @Serial
    private static final long serialVersionUID = -6608126361014685145L;

    private BooleanValue() {
        value = Boolean.FALSE;
    }

    private BooleanValue(final boolean defaultValue) {
        value = defaultValue;
    }

    private BooleanValue(final Boolean defaultValue) {
        checkArgumentNotNull(defaultValue, cannotBeNullOrEmpty("defaultValue"));
        value = defaultValue;
    }

    /** Creates a new BooleanValue instance with the default value of false. */
    @Contract(value = " -> new", pure = true)
    public static @NotNull BooleanValue of() {
        return new BooleanValue();
    }

    /**
     * Creates a new BooleanValue instance with the specified default value.
     * @param defaultValue the value to set
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BooleanValue of(final boolean defaultValue) {
        return new BooleanValue(defaultValue);
    }

    /**
     * Creates a new BooleanValue instance with the specified default value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default is null
     */
    @Contract("_ -> new")
    public static @NotNull BooleanValue of(final Boolean defaultValue) {
        return new BooleanValue(defaultValue);
    }

    /**
     * A static instance of a BooleanValue with the value equal to true.
     */
    public static final BooleanValue TRUE = new BooleanValue(Boolean.TRUE);

    /**
     * A static instance of a BooleanValue with the value equal to false.
     */
    public static final BooleanValue FALSE = new BooleanValue(Boolean.FALSE);

    /**
     * Returns the value of this BooleanValue as a boolean.
     *
     * @return the boolean value represented by this object.
     */
    public boolean booleanValue() {
        return value;
    }

    /**
     * Checks if the value equals true.
     * @return if the value equals true
     */
    public boolean isTrue() { return value; }

    /**
     * Checks if the value equals false.
     * @return if the value equals false
     */
    public boolean isFalse() { return !value; }

    /**
     * Runs the specified runnable if the value is true.
     * @param runnable the runnable to run
     * @return this instance
     * @throws IllegalArgumentException if runnable is null
     */
    public BooleanValue ifTrue(final Runnable runnable) {
        checkArgumentNotNull(runnable, cannotBeNullOrEmpty("runnable"));
        if(value) runnable.run();
        return this;
    }

    /**
     * Runs the specified runnable if the value is false.
     * @param runnable the runnable to run
     * @return this instance
     * @throws IllegalArgumentException if runnable is null
     */
    public BooleanValue ifFalse(final Runnable runnable) {
        checkArgumentNotNull(runnable, cannotBeNullOrEmpty("runnable"));
        if(!value) runnable.run();
        return this;
    }

    /**
     * Sets the value to the opposite of the current value.
     * @return this instance
     */
    @Contract(value = " -> new", pure = true)
    public @NotNull BooleanValue flip() {
        return BooleanValue.of(!value);
    }

    /**
     * Performs an AND operation on this value and the specified value.
     * @param value the value to combine with this value
     * @return the result of the operation
     */
    @Contract("_ -> new")
    public @NotNull BooleanValue and(final BooleanValue value) {
        return BooleanValue.of(this.value && value.get());
    }

    /**
     * Performs an AND operation on this value and the specified value.
     * @param value the value to combine with this value
     * @return the result of the operation
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull BooleanValue and(final boolean value) {
        return BooleanValue.of(this.value && value);
    }

    /**
     * Performs an OR operation on this value and the specified value.
     * @param value the value to combine with this value
     * @return the result of the operation
     */
    @Contract("_ -> new")
    public @NotNull BooleanValue or(final BooleanValue value) {
        return BooleanValue.of(this.value || value.get());
    }

    /**
     * Performs an OR operation on this value and the specified value.
     * @param value the value to combine with this value
     * @return the result of the operation
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull BooleanValue or(final boolean value) {
        return BooleanValue.of(this.value || value);
    }

    /**
     * Gets this value as an instance of Boolean.
     *
     * @return a Boolean instance containing the value from this value, never null
     */
    @Contract(pure = true)
    public @NotNull Boolean toBoolean() {
        return booleanValue();
    }

    /**
     * Returns the value.
     *
     * @return the stored value
     */
    public Boolean get() {
        return value;
    }

    /**
     * Compares this BooleanValue's value to another in ascending order.
     *
     * @param other  the other boolean to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     *  where false is less than true
     */
    @Override
    public int compareTo(final @NotNull Boolean other) {
        return value.compareTo(other);
    }

    /**
     * Compares this BooleanValue to another in ascending order.
     *
     * @param other  the other BooleanValue to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     *  where false is less than true
     */
    public int compareTo(final @NotNull BooleanValue other) {
        //noinspection AccessingNonPublicFieldOfAnotherObject
        return value.compareTo(other.value);
    }

    /**
     * Returns an {@link Optional} containing the nominal descriptor for this
     * instance.
     *
     * @return an {@link Optional} describing the {@linkplain Boolean} value
     */
    @Override
    public Optional<DynamicConstantDesc<Boolean>> describeConstable() {
        return value.describeConstable();
    }

    /**
     * Compares this object to the specified object. The result is {@code true} if and only if the argument is
     * not {@code null} and is an BooleanValue object that contains the same
     * {@code boolean} value as this object.
     *
     * @param obj  the object to compare with, null returns false
     * @return {@code true} if the objects are the same; {@code false} otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof BooleanValue && value == ((BooleanValue) obj).value;
    }

    /**
     * Returns a suitable hash code for this immutable.
     *
     * @return the hash code returned by {@code Boolean.TRUE} or {@code Boolean.FALSE}
     */
    @Override
    public int hashCode() {
        return (value ? Boolean.TRUE : Boolean.FALSE).hashCode();
    }


    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return value.toString();
    }
}
