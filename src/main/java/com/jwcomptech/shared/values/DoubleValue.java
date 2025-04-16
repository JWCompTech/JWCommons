package com.jwcomptech.shared.values;

import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeSupport;
import java.io.Serial;

import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNullOrEmpty;

/**
 * Provides mutable access to an {@link Double}.
 * @since 0.0.1
 */
public class DoubleValue extends NumberValue<Double, DoubleValue> {
    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    @Serial
    private static final long serialVersionUID = 5626868775336965253L;

    private DoubleValue() {
        value = 0.0;
        listeners = new PropertyChangeSupport(this);
    }

    private DoubleValue(final int defaultValue) {
        value = (double) defaultValue;
        listeners = new PropertyChangeSupport(this);
    }

    private DoubleValue(final double defaultValue) {
        value = defaultValue;
        listeners = new PropertyChangeSupport(this);
    }


    private DoubleValue(final Number defaultValue) {
        checkArgumentNotNull(defaultValue, "Default value cannot be null!");
        value = defaultValue.doubleValue();
        listeners = new PropertyChangeSupport(this);
    }

    private DoubleValue(final String defaultValue) {
        checkArgumentNotNullOrEmpty(defaultValue, "Default value cannot be null or empty!");
        value = Double.parseDouble(defaultValue);
        listeners = new PropertyChangeSupport(this);
    }

    /** Creates a new DoubleValue instance with the default value of 0.0. */
    public static DoubleValue of() {
        return new DoubleValue();
    }

    /**
     * Creates a new DoubleValue instance with the specified default int value.
     * @param defaultValue the value to set
     */
    public static DoubleValue of(final int defaultValue) {
        return new DoubleValue(defaultValue);
    }

    /**
     * Creates a new DoubleValue instance with the specified default double value.
     * @param defaultValue the value to set
     */
    public static DoubleValue of(final double defaultValue) {
        return new DoubleValue(defaultValue);
    }

    /**
     * Creates a new DoubleValue instance with the specified number value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null
     */
    public static DoubleValue of(final Number defaultValue) {
        return new DoubleValue(defaultValue);
    }

    /**
     * Creates a new DoubleValue instance with the specified default string value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null or empty
     */
    public static DoubleValue of(final String defaultValue) {
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
    public DoubleValue add(final Number operand) {
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
    public Double addAndGet(final Number operand) {
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
    public Double getAndAdd(final Number operand) {
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
    public DoubleValue subtract(final Number operand) {
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
    public Double subtractAndGet(final Number operand) {
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
    public Double getAndSubtract(final Number operand) {
        final Double last = value;
        value = value - operand.intValue();
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
    public boolean isEqualTo(final Number number) {
        return value == number.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNotEqualTo(final Number number) {
        return value != number.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThanOrEqualTo(final Number number) {
        return value <= number.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThanOrEqualTo(final Number number) {
        return value >= number.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThan(final Number number) {
        return value < number.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThan(final Number number) {
        return value > number.doubleValue();
    }

    @Override
    public int compareTo(final @NotNull DoubleValue other) {
        return Double.compare(value, other.value);
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
    public DoubleValue set(final Number value) {
        checkArgumentNotNull(value, "Value cannot be null!");
        this.value = value.doubleValue();
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof DoubleValue && value == ((DoubleValue) obj).doubleValue();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
