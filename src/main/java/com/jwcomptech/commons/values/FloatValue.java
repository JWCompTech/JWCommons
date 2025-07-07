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
 * Provides mutable access to an {@link Float}.
 *
 * @since 0.0.1
 */
@SuppressWarnings("ClassWithTooManyMethods")
public final class FloatValue extends NumberValue<Float, FloatValue> {
    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    @Serial
    private static final long serialVersionUID = -6475675786857950255L;

    private FloatValue() {
        super(0.0F);
        super.setListenersTarget(this);
    }

    private FloatValue(final int defaultValue) {
        super((float) defaultValue);
        super.setListenersTarget(this);
    }

    private FloatValue(final float defaultValue) {
        super(defaultValue);
        super.setListenersTarget(this);
    }

    private FloatValue(final @NotNull Number defaultValue) {
        super(defaultValue.floatValue());
        checkArgumentNotNull(defaultValue, cannotBeNull("defaultValue"));
        super.setListenersTarget(this);
    }

    private FloatValue(final String defaultValue) {
        super(Float.parseFloat(defaultValue));
        checkArgumentNotNullOrEmpty(defaultValue, cannotBeNullOrEmpty("defaultValue"));
        super.setListenersTarget(this);
    }

    /**
     * Creates a new FloatValue instance with the default value of 0.0.
     * @return a new FloatValue instance with the default value of 0.0
     */
    @Contract(" -> new")
    public static @NotNull FloatValue of() {
        return new FloatValue();
    }

    /**
     * Creates a new FloatValue instance with the specified default int value.
     * @param defaultValue the value to set
     * @return a new FloatValue instance with the specified default int value
     */
    @Contract("_ -> new")
    public static @NotNull FloatValue of(final int defaultValue) {
        return new FloatValue(defaultValue);
    }

    /**
     * Creates a new FloatValue instance with the specified default float value.
     * @param defaultValue the value to set
     * @return a new FloatValue instance with the specified default float value
     */
    @Contract("_ -> new")
    public static @NotNull FloatValue of(final float defaultValue) {
        return new FloatValue(defaultValue);
    }

    /**
     * Creates a new FloatValue instance with the specified number value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null
     * @return a new FloatValue instance with the specified number value
     */
    @Contract("_ -> new")
    public static @NotNull FloatValue of(final Number defaultValue) {
        return new FloatValue(defaultValue);
    }

    /**
     * Creates a new FloatValue instance with the specified default string value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null or empty
     * @return a new FloatValue instance with the specified default string value
     */
    @Contract("_ -> new")
    public static @NotNull FloatValue of(final String defaultValue) {
        return new FloatValue(defaultValue);
    }

    /**
     * Increments the value.
     * @return this instance
     * @throws ArithmeticException if the result overflows a float
     */
    @Override
    public FloatValue increment() {
        final Float last = value;
        value = value + 1.0F;
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Increments this instance's value by 1.0; this method returns the value associated with the instance
     * immediately prior to the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was incremented
     * @throws ArithmeticException if the result overflows a float
     */
    @Override
    public Float incrementAndGet() {
        final Float last = value;
        value = value + 1.0F;
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Increments this instance's value by 1.0; this method returns the value associated with the instance
     * immediately after the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is incremented
     * @throws ArithmeticException if the result overflows a float
     */
    @Override
    public Float getAndIncrement() {
        final Float last = value;
        value = value + 1.0F;
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Decrements the value.
     * @return this instance
     * @throws ArithmeticException if the result overflows a float
     */
    @Override
    public FloatValue decrement() {
        final Float last = value;
        value = value - 1.0F;
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Decrements this instance's value by 1.0; this method returns the value associated with the instance
     * immediately prior to the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was decremented
     * @throws ArithmeticException if the result overflows a float
     */
    @Override
    public Float decrementAndGet() {
        final Float last = value;
        value = value - 1.0F;
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Decrements this instance's value by 1.0; this method returns the value associated with the instance
     * immediately after the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is decremented
     * @throws ArithmeticException if the result overflows a float
     */
    @Override
    public Float getAndDecrement() {
        final Float last = value;
        value = value - 1.0F;
        return last;
    }

    /**
     * Adds a value to the value of this instance.
     *
     * @param operand the value to add, not null
     * @throws IllegalArgumentException if the object is null
     * @throws ArithmeticException if the result overflows a float
     * @return this instance
     */
    @Contract("_ -> this")
    @Override
    public FloatValue add(final @NotNull Number operand) {
        final Float last = value;
        value = value + operand.floatValue();
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a float
     * @return the value associated with this instance after adding the operand
     */
    @Override
    public Float addAndGet(final @NotNull Number operand) {
        final Float last = value;
        value = value + operand.floatValue();
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a float
     * @return the value associated with this instance immediately before adding the operand
     */
    @Override
    public Float getAndAdd(final @NotNull Number operand) {
        final Float last = value;
        value = value + operand.floatValue();
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Subtracts a value from the value of this instance.
     *
     * @param operand  the value to subtract, not null
     * @throws IllegalArgumentException if the object is null
     * @throws ArithmeticException if the result overflows a float
     * @return this instance
     */
    @Contract("_ -> this")
    @Override
    public FloatValue subtract(final @NotNull Number operand) {
        final Float last = value;
        value = value - operand.floatValue();
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a float
     * @return the value associated with this instance after subtracting the operand
     */
    @Override
    public Float subtractAndGet(final @NotNull Number operand) {
        final Float last = value;
        value = value - operand.floatValue();
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows a float
     * @return the value associated with this instance immediately before subtracting the operand
     */
    @Override
    public Float getAndSubtract(final @NotNull Number operand) {
        final Float last = value;
        value = value - operand.floatValue();
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
    @Contract("_ -> this")
    @Override
    public FloatValue multiply(final @NotNull Number operand) {
        final Float last = value;
        value = value * operand.floatValue();
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
    public Float multiplyAndGet(final @NotNull Number operand) {
        final Float last = value;
        value = value * operand.floatValue();
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
    public Float getAndMultiply(final @NotNull Number operand) {
        final Float last = value;
        value = value * operand.floatValue();
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
    @Contract("_ -> this")
    @Override
    public FloatValue divide(final @NotNull Number operand) {
        final Float last = value;
        value = value / operand.floatValue();
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
    public Float divideAndGet(final @NotNull Number operand) {
        final Float last = value;
        value = value / operand.floatValue();
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
    public Float getAndDivide(final @NotNull Number operand) {
        final Float last = value;
        value = value / operand.floatValue();
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
        return value == 0.0F;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqualTo(final @NotNull Number number) {
        return value.equals(number.floatValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNotEqualTo(final @NotNull Number number) {
        return !value.equals(number.floatValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThanOrEqualTo(final @NotNull Number number) {
        return value <= number.floatValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThanOrEqualTo(final @NotNull Number number) {
        return value >= number.floatValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThan(final @NotNull Number number) {
        return value < number.floatValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThan(final @NotNull Number number) {
        return value > number.floatValue();
    }

    @Override
    public int compareTo(final @NotNull FloatValue other) {
        //noinspection AccessingNonPublicFieldOfAnotherObject
        return Float.compare(value, other.value);
    }

    @Override
    public int compareTo(@NotNull final Float other) {
        return Float.compare(value, other);
    }

    @Override
    public int compareTo(@NotNull final Value<Float, FloatValue> other) {
        return this.compareTo(other.get());
    }

    /**
     * Returns the value.
     * @return the stored value
     */
    @Override
    public Float get() {
        return value;
    }

    /**
     * Sets the value.
     * @param value the value to store
     * @return this instance
     * @throws IllegalArgumentException if specified value is null
     */
    @Override
    public FloatValue set(final Float value) {
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
    public FloatValue set(final Number value) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        this.value = value.floatValue();
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof FloatValue && value.equals(((FloatValue) obj).floatValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
