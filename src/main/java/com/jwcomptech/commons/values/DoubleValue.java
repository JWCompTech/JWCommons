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
 * Provides mutable access to an {@link Double}.
 *
 * @since 0.0.1
 */
@SuppressWarnings("ClassWithTooManyMethods")
public final class DoubleValue extends NumberValue<Double, DoubleValue> {
    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    @Serial
    private static final long serialVersionUID = 5626868775336965253L;

    private DoubleValue() {
        super(0.0);
        super.setListenersTarget(this);
    }

    private DoubleValue(final int defaultValue) {
        super((double) defaultValue);
        super.setListenersTarget(this);
    }

    private DoubleValue(final double defaultValue) {
        super(defaultValue);
        super.setListenersTarget(this);
    }


    private DoubleValue(final @NotNull Number defaultValue) {
        super(defaultValue.doubleValue());
        checkArgumentNotNull(defaultValue, cannotBeNull("defaultValue"));
        super.setListenersTarget(this);
    }

    private DoubleValue(final String defaultValue) {
        super(Double.parseDouble(defaultValue));
        checkArgumentNotNullOrEmpty(defaultValue, cannotBeNullOrEmpty("defaultValue"));
        super.setListenersTarget(this);
    }

    /**
     * Creates a new DoubleValue instance with the default value of 0.0.
     * @return a new DoubleValue instance with the default value of 0.0
     * */
    @Contract(" -> new")
    public static @NotNull DoubleValue of() {
        return new DoubleValue();
    }

    /**
     * Creates a new DoubleValue instance with the specified default int value.
     * @param defaultValue the value to set
     * @return a new DoubleValue instance with the specified default int value
     */
    @Contract("_ -> new")
    public static @NotNull DoubleValue of(final int defaultValue) {
        return new DoubleValue(defaultValue);
    }

    /**
     * Creates a new DoubleValue instance with the specified default double value.
     * @param defaultValue the value to set
     * @return a new DoubleValue instance with the specified default double value
     */
    @Contract("_ -> new")
    public static @NotNull DoubleValue of(final double defaultValue) {
        return new DoubleValue(defaultValue);
    }

    /**
     * Creates a new DoubleValue instance with the specified number value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null
     * @return a new DoubleValue instance with the specified number value
     */
    @Contract("_ -> new")
    public static @NotNull DoubleValue of(final Number defaultValue) {
        return new DoubleValue(defaultValue);
    }

    /**
     * Creates a new DoubleValue instance with the specified default string value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null or empty
     * @return a new DoubleValue instance with the specified default string value
     */
    @Contract("_ -> new")
    public static @NotNull DoubleValue of(final String defaultValue) {
        return new DoubleValue(defaultValue);
    }

    /**
     * Increments the value.
     * @return this instance
     * @throws ArithmeticException if the result overflows a double
     */
    @Override
    public DoubleValue increment() {
        final Double last = value;
        value = value + 1.0;
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Increments this instance's value by 1.0; this method returns the value associated with the instance
     * immediately prior to the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was incremented
     * @throws ArithmeticException if the result overflows a double
     */
    @Override
    public Double incrementAndGet() {
        final Double last = value;
        value = value + 1.0;
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Increments this instance's value by 1.0; this method returns the value associated with the instance
     * immediately after the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is incremented
     * @throws ArithmeticException if the result overflows a double
     */
    @Override
    public Double getAndIncrement() {
        final Double last = value;
        value = value + 1.0;
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Decrements the value.
     * @return this instance
     * @throws ArithmeticException if the result overflows a double
     */
    @Override
    public DoubleValue decrement() {
        final Double last = value;
        value = value - 1.0;
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Decrements this instance's value by 1.0; this method returns the value associated with the instance
     * immediately prior to the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was decremented
     * @throws ArithmeticException if the result overflows a double
     */
    @Override
    public Double decrementAndGet() {
        final Double last = value;
        value = value - 1.0;
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Decrements this instance's value by 1.0; this method returns the value associated with the instance
     * immediately after the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is decremented
     * @throws ArithmeticException if the result overflows a double
     */
    @Override
    public Double getAndDecrement() {
        final Double last = value;
        value = value - 1.0;
        return last;
    }

    /**
     * Adds a value to the value of this instance.
     *
     * @param operand the value to add, not null
     * @throws IllegalArgumentException if the object is null
     * @throws ArithmeticException if the result overflows a double
     * @return this instance
     */
    @Override
    public DoubleValue add(final @NotNull Number operand) {
        final Double last = value;
        value = value + operand.doubleValue();
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a double
     * @return the value associated with this instance after adding the operand
     */
    @Override
    public Double addAndGet(final @NotNull Number operand) {
        final Double last = value;
        value = value + operand.doubleValue();
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a double
     * @return the value associated with this instance immediately before adding the operand
     */
    @Override
    public Double getAndAdd(final @NotNull Number operand) {
        final Double last = value;
        value = value + operand.intValue();
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Subtracts a value from the value of this instance.
     *
     * @param operand  the value to subtract, not null
     * @throws IllegalArgumentException if the object is null
     * @throws ArithmeticException if the result overflows a double
     * @return this instance
     */
    @Override
    public DoubleValue subtract(final @NotNull Number operand) {
        final Double last = value;
        value = value - operand.intValue();
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a double
     * @return the value associated with this instance after subtracting the operand
     */
    @Override
    public Double subtractAndGet(final @NotNull Number operand) {
        final Double last = value;
        value = value - operand.intValue();
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a double
     * @return the value associated with this instance immediately before subtracting the operand
     */
    @Override
    public Double getAndSubtract(final @NotNull Number operand) {
        final Double last = value;
        value = value - operand.intValue();
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
    public DoubleValue multiply(final @NotNull Number operand) {
        final Double last = value;
        value = value * operand.intValue();
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
    public Double multiplyAndGet(final @NotNull Number operand) {
        final Double last = value;
        value = value * operand.intValue();
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
    public Double getAndMultiply(final @NotNull Number operand) {
        final Double last = value;
        value = value * operand.intValue();
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
    public DoubleValue divide(final @NotNull Number operand) {
        final Double last = value;
        value = value / operand.intValue();
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
    public Double divideAndGet(final @NotNull Number operand) {
        final Double last = value;
        value = value / operand.intValue();
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
    public Double getAndDivide(final @NotNull Number operand) {
        final Double last = value;
        value = value / operand.intValue();
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPositive() {
        return Integer.signum(value.intValue()) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNegative() {
        return Integer.signum(value.intValue()) < 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isZero() {
        return value == 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqualTo(final @NotNull Number number) {
        return value.equals(number.doubleValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNotEqualTo(final @NotNull Number number) {
        return !value.equals(number.doubleValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThanOrEqualTo(final @NotNull Number number) {
        return value <= number.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThanOrEqualTo(final @NotNull Number number) {
        return value >= number.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThan(final @NotNull Number number) {
        return value < number.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThan(final @NotNull Number number) {
        return value > number.doubleValue();
    }

    @Override
    public int compareTo(final @NotNull DoubleValue other) {
        //noinspection AccessingNonPublicFieldOfAnotherObject
        return Double.compare(value, other.value);
    }

    @Override
    public int compareTo(@NotNull final Double other) {
        return Double.compare(value, other);
    }

    @Override
    public int compareTo(@NotNull final Value<Double, DoubleValue> other) {
        return this.compareTo(other.get());
    }

    /**
     * Returns the value.
     * @return the stored value
     */
    @Override
    public Double get() {
        return value;
    }

    /**
     * Sets the value.
     * @param value the value to store
     * @return this instance
     * @throws IllegalArgumentException if specified value is null
     */
    @Override
    public DoubleValue set(final Double value) {
        checkArgumentNotNull(value, cannotBeNull("value"));
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
    public DoubleValue set(final Number value) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        this.value = value.doubleValue();
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof DoubleValue && value.equals(((DoubleValue) obj).doubleValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
