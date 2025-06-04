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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.consts.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;

/**
 * Provides mutable access to an {@link Integer}.
 *
 * @since 0.0.1
 */
@SuppressWarnings("ClassWithTooManyMethods")
public final class LongValue extends NumberValue<Long, LongValue> {
    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    @Serial
    private static final long serialVersionUID = -6266537770334178L;

    private LongValue() {
        super(0L);
        super.setListenersTarget(this);
    }

    private LongValue(final int defaultValue) {
        super((long) defaultValue);
        super.setListenersTarget(this);
    }

    private LongValue(final long defaultValue) {
        super(defaultValue);
        super.setListenersTarget(this);
    }

    private LongValue(final @NotNull Number defaultValue) {
        super(defaultValue.longValue());
        checkArgumentNotNull(defaultValue, cannotBeNull("defaultValue"));
        super.setListenersTarget(this);
    }

    private LongValue(final String defaultValue) {
        super(Long.parseLong(defaultValue));
        checkArgumentNotNullOrEmpty(defaultValue, cannotBeNullOrEmpty("defaultValue"));
        super.setListenersTarget(this);
    }

    /** Creates a new LongValue instance with the default value of 0. */
    @Contract(" -> new")
    public static @NotNull LongValue of() {
        return new LongValue();
    }

    /**
     * Creates a new LongValue instance with the specified default int value.
     * @param defaultValue the value to set
     */
    @Contract("_ -> new")
    public static @NotNull LongValue of(final int defaultValue) {
        return new LongValue(defaultValue);
    }

    /**
     * Creates a new LongValue instance with the specified default long value.
     * @param defaultValue the value to set
     */
    @Contract("_ -> new")
    public static @NotNull LongValue of(final long defaultValue) {
        return new LongValue(defaultValue);
    }

    /**
     * Creates a new LongValue instance with the specified number value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null
     */
    @Contract("_ -> new")
    public static @NotNull LongValue of(final Number defaultValue) {
        return new LongValue(defaultValue);
    }

    /**
     * Creates a new LongValue instance with the specified default string value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null or empty
     */
    @Contract("_ -> new")
    public static @NotNull LongValue of(final String defaultValue) {
        return new LongValue(defaultValue);
    }

    /**
     * Increments the value.
     * @return this instance
     * @throws ArithmeticException if the result overflows a long
     */
    @Override
    public LongValue increment() {
        final Long last = value;
        value = Math.incrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Increments this instance's value by 1; this method returns the value associated with the instance
     * immediately prior to the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was incremented
     * @throws ArithmeticException if the result overflows a long
     */
    @Override
    public Long incrementAndGet() {
        final Long last = value;
        value = Math.incrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Increments this instance's value by 1; this method returns the value associated with the instance
     * immediately after the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is incremented
     * @throws ArithmeticException if the result overflows a long
     */
    @Override
    public Long getAndIncrement() {
        final Long last = value;
        value = Math.incrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Decrements the value.
     * @return this instance
     * @throws ArithmeticException if the result overflows a long
     */
    @Override
    public LongValue decrement() {
        final Long last = value;
        value = Math.decrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Decrements this instance's value by 1; this method returns the value associated with the instance
     * immediately prior to the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was decremented
     * @throws ArithmeticException if the result overflows a long
     */
    @Override
    public Long decrementAndGet() {
        final Long last = value;
        value = Math.decrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Decrements this instance's value by 1; this method returns the value associated with the instance
     * immediately after the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is decremented
     * @throws ArithmeticException if the result overflows a long
     */
    @Override
    public Long getAndDecrement() {
        final Long last = value;
        value = Math.decrementExact(value);
        return last;
    }

    /**
     * Adds a value to the value of this instance.
     *
     * @param operand the value to add, not null
     * @throws IllegalArgumentException if the object is null
     * @throws ArithmeticException if the result overflows a long
     * @return this instance
     */
    @Override
    public LongValue add(final Number operand) {
        final Long last = value;
        value = Math.addExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a long
     * @return the value associated with this instance after adding the operand
     */
    @Override
    public Long addAndGet(final @NotNull Number operand) {
        final Long last = value;
        value = Math.addExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a long
     * @return the value associated with this instance immediately before adding the operand
     */
    @Override
    public Long getAndAdd(final @NotNull Number operand) {
        final Long last = value;
        value = Math.addExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Subtracts a value from the value of this instance.
     *
     * @param operand  the value to subtract, not null
     * @throws IllegalArgumentException if the object is null
     * @throws ArithmeticException if the result overflows a long
     * @return this instance
     */
    @Override
    public LongValue subtract(final @NotNull Number operand) {
        final Long last = value;
        value = Math.subtractExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a long
     * @return the value associated with this instance after subtracting the operand
     */
    @Override
    public Long subtractAndGet(final @NotNull Number operand) {
        final Long last = value;
        value = Math.subtractExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a long
     * @return the value associated with this instance immediately before subtracting the operand
     */
    @Override
    public Long getAndSubtract(final @NotNull Number operand) {
        final Long last = value;
        value = Math.subtractExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Multiplies a value from the value of this instance.
     *
     * @param operand  the value to multiply, not null
     * @throws IllegalArgumentException if the object is null
     * @return this instance
     */
    @Override
    public LongValue multiply(final @NotNull Number operand) {
        final Long last = value;
        value = Math.multiplyExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Multiplies this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the multiplication operation. This method is not thread safe.
     *
     * @param operand the quantity to multiply, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @return the value associated with this instance after multiplying the operand
     */
    @Override
    public Long multiplyAndGet(final @NotNull Number operand) {
        final Long last = value;
        value = Math.multiplyExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Multiplies this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the multiplication operation. This method is not thread safe.
     *
     * @param operand the quantity to multiply, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @return the value associated with this instance immediately before multiplying the operand
     */
    @Override
    public Long getAndMultiply(final @NotNull Number operand) {
        final Long last = value;
        value = Math.multiplyExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Divides a value from the value of this instance.
     *
     * @param operand  the value to divide, not null
     * @throws IllegalArgumentException if the object is null
     * @return this instance
     */
    @Override
    public LongValue divide(final @NotNull Number operand) {
        final Long last = value;
        value = Math.divideExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Divides this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the division operation. This method is not thread safe.
     *
     * @param operand the quantity to divide, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @return the value associated with this instance after dividing the operand
     */
    @Override
    public Long divideAndGet(final @NotNull Number operand) {
        final Long last = value;
        value = Math.divideExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Divides this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the division operation. This method is not thread safe.
     *
     * @param operand the quantity to divide, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @return the value associated with this instance immediately before dividing the operand
     */
    @Override
    public Long getAndDivide(final @NotNull Number operand) {
        final Long last = value;
        value = Math.divideExact(value, operand.longValue());
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPositive() {
        return Long.signum(value) > 0L;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNegative() {
        return Long.signum(value) < 0L;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isZero() {
        return Long.signum(value) == 0L;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqualTo(final @NotNull Number number) {
        return value == number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNotEqualTo(final @NotNull Number number) {
        return value != number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThanOrEqualTo(final @NotNull Number number) {
        return value <= number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThanOrEqualTo(final @NotNull Number number) {
        return value >= number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThan(final @NotNull Number number) {
        return value < number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThan(final @NotNull Number number) {
        return value > number.longValue();
    }

    @Override
    public int compareTo(final @NotNull LongValue other) {
        //noinspection AccessingNonPublicFieldOfAnotherObject
        return Long.compare(value, other.value);
    }

    @Override
    public int compareTo(@NotNull final Long other) {
        return Long.compare(value, other);
    }

    @Override
    public int compareTo(@NotNull final Value<Long, LongValue> other) {
        return this.compareTo(other.get());
    }

    /**
     * Returns the value.
     * @return the stored value
     */
    @Override
    public Long get() {
        return value;
    }

    /**
     * Sets the value.
     * @param value the value to store
     * @return this instance
     * @throws IllegalArgumentException if specified value is null
     */
    @Override
    public LongValue set(final Long value) {
        checkArgumentNotNull(value, cannotBeNull("defaultValue"));
        this.value = value;
        return this;
    }

    /**
     * Sets the value.
     * @param value the value to store
     * @return this instance
     * @throws IllegalArgumentException if specified value is null
     */
    @Override
    public LongValue set(final Number value) {
        checkArgumentNotNull(value, cannotBeNull("defaultValue"));
        this.value = value.longValue();
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof LongValue && value == ((LongValue) obj).longValue();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
