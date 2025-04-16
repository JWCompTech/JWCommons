package com.jwcomptech.shared.values;

import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeSupport;
import java.io.Serial;

import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNullOrEmpty;

/**
 * Provides mutable access to an {@link Integer}.
 * @since 0.0.1
 */
public class IntegerValue extends NumberValue<Integer, IntegerValue> {
    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    @Serial
    private static final long serialVersionUID = -4508054126789004835L;

    private IntegerValue() {
        value = 0;
        listeners = new PropertyChangeSupport(this);
    }

    private IntegerValue(final int defaultValue) {
        value = defaultValue;
        listeners = new PropertyChangeSupport(this);
    }


    private IntegerValue(final Number defaultValue) {
        checkArgumentNotNull(defaultValue, "Default value cannot be null!");
        value = defaultValue.intValue();
        listeners = new PropertyChangeSupport(this);
    }

    private IntegerValue(final String defaultValue) {
        checkArgumentNotNullOrEmpty(defaultValue, "Default value cannot be null or empty!");
        value = Integer.parseInt(defaultValue);
        listeners = new PropertyChangeSupport(this);
    }

    /** Creates a new IntegerValue instance with the default value of 0. */
    public static IntegerValue of() {
        return new IntegerValue();
    }

    /**
     * Creates a new IntegerValue instance with the specified default int value.
     * @param defaultValue the value to set
     */
    public static IntegerValue of(final int defaultValue) {
        return new IntegerValue(defaultValue);
    }

    /**
     * Creates a new IntegerValue instance with the specified number value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null
     */
    public static IntegerValue of(final Number defaultValue) {
        return new IntegerValue(defaultValue);
    }

    /**
     * Creates a new IntegerValue instance with the specified default string value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null or empty
     */
    public static IntegerValue of(final String defaultValue) {
        return new IntegerValue(defaultValue);
    }

    /**
     * Increments the value.
     * @return this instance
     * @throws ArithmeticException if the result overflows an int
     */
    @Override
    public IntegerValue increment() {
        final Integer last = value;
        value = Math.incrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Increments this instance's value by 1; this method returns the value associated with the instance
     * immediately prior to the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was incremented
     * @throws ArithmeticException if the result overflows an int
     */
    @Override
    public Integer incrementAndGet() {
        final Integer last = value;
        value = Math.incrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Increments this instance's value by 1; this method returns the value associated with the instance
     * immediately after the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is incremented
     * @throws ArithmeticException if the result overflows an int
     */
    @Override
    public Integer getAndIncrement() {
        final Integer last = value;
        value = Math.incrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Decrements the value.
     * @return this instance
     * @throws ArithmeticException if the result overflows an int
     */
    @Override
    public IntegerValue decrement() {
        final Integer last = value;
        value = Math.decrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Decrements this instance's value by 1; this method returns the value associated with the instance
     * immediately prior to the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was decremented
     * @throws ArithmeticException if the result overflows an int
     */
    @Override
    public Integer decrementAndGet() {
        final Integer last = value;
        value = Math.decrementExact(value);
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Decrements this instance's value by 1; this method returns the value associated with the instance
     * immediately after the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is decremented
     * @throws ArithmeticException if the result overflows an int
     */
    @Override
    public Integer getAndDecrement() {
        final Integer last = value;
        value = Math.decrementExact(value);
        return last;
    }

    /**
     * Adds a value to the value of this instance.
     *
     * @param operand the value to add, not null
     * @throws IllegalArgumentException if the object is null
     * @throws ArithmeticException if the result overflows an int
     * @return this instance
     */
    @Override
    public IntegerValue add(final Number operand) {
        final Integer last = value;
        value = Math.addExact(value, operand.intValue());
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows an int
     * @return the value associated with this instance after adding the operand
     */
    @Override
    public Integer addAndGet(final Number operand) {
        final Integer last = value;
        value = Math.addExact(value, operand.intValue());
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows an int
     * @return the value associated with this instance immediately before adding the operand
     */
    @Override
    public Integer getAndAdd(final Number operand) {
        final Integer last = value;
        value = Math.addExact(value, operand.intValue());
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * Subtracts a value from the value of this instance.
     *
     * @param operand  the value to subtract, not null
     * @throws IllegalArgumentException if the object is null
     * @throws ArithmeticException if the result overflows an int
     * @return this instance
     */
    @Override
    public IntegerValue subtract(final Number operand) {
        final Integer last = value;
        value = Math.subtractExact(value, operand.intValue());
        listeners.firePropertyChange("value", last, value);
        return this;
    }

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows an int
     * @return the value associated with this instance after subtracting the operand
     */
    @Override
    public Integer subtractAndGet(final Number operand) {
        final Integer last = value;
        value = Math.subtractExact(value, operand.intValue());
        listeners.firePropertyChange("value", last, value);
        return value;
    }

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @throws ArithmeticException if the result overflows an int
     * @return the value associated with this instance immediately before subtracting the operand
     */
    @Override
    public Integer getAndSubtract(final Number operand) {
        final Integer last = value;
        value = Math.subtractExact(value, operand.intValue());
        listeners.firePropertyChange("value", last, value);
        return last;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPositive() {
        return Integer.signum(value) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNegative() {
        return Integer.signum(value) < 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isZero() {
        return Integer.signum(value) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqualTo(final Number number) {
        return value == number.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNotEqualTo(final Number number) {
        return value != number.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThanOrEqualTo(final Number number) {
        return value <= number.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThanOrEqualTo(final Number number) {
        return value >= number.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThan(final Number number) {
        return value < number.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThan(final Number number) {
        return value > number.intValue();
    }

    @Override
    public int compareTo(final @NotNull IntegerValue other) {
        return Integer.compare(value, other.value);
    }

    /**
     * Returns the value.
     * @return the stored value
     */
    @Override
    public Integer get() {
        return value;
    }

    /**
     * Sets the value.
     * @param value the value to store
     * @return this instance
     * @throws IllegalArgumentException if specified value is null
     */
    @Override
    public IntegerValue set(final Integer value) {
        checkArgumentNotNull(value, "Value cannot be null!");
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
    public IntegerValue set(final Number value) {
        checkArgumentNotNull(value, "Value cannot be null!");
        this.value = value.intValue();
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof IntegerValue && value == ((IntegerValue) obj).intValue();
    }

    @Override
    public int hashCode() {
        return value;
    }
}
