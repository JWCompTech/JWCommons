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
public class LongValue extends NumberValue<Long, LongValue> {
    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    @Serial
    private static final long serialVersionUID = -6266537770334178L;

    private LongValue() {
        value = 0L;
        listeners = new PropertyChangeSupport(this);
    }

    private LongValue(final int defaultValue) {
        value = (long) defaultValue;
        listeners = new PropertyChangeSupport(this);
    }

    private LongValue(final long defaultValue) {
        value = defaultValue;
        listeners = new PropertyChangeSupport(this);
    }

    private LongValue(final Number defaultValue) {
        checkArgumentNotNull(defaultValue, "Default value cannot be null!");
        value = defaultValue.longValue();
        listeners = new PropertyChangeSupport(this);
    }

    private LongValue(final String defaultValue) {
        checkArgumentNotNullOrEmpty(defaultValue, "Default value cannot be null or empty!");
        value = Long.parseLong(defaultValue);
        listeners = new PropertyChangeSupport(this);
    }

    /** Creates a new LongValue instance with the default value of 0. */
    public static LongValue of() {
        return new LongValue();
    }

    /**
     * Creates a new LongValue instance with the specified default int value.
     * @param defaultValue the value to set
     */
    public static LongValue of(final int defaultValue) {
        return new LongValue(defaultValue);
    }

    /**
     * Creates a new LongValue instance with the specified default long value.
     * @param defaultValue the value to set
     */
    public static LongValue of(final long defaultValue) {
        return new LongValue(defaultValue);
    }

    /**
     * Creates a new LongValue instance with the specified number value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null
     */
    public static LongValue of(final Number defaultValue) {
        return new LongValue(defaultValue);
    }

    /**
     * Creates a new LongValue instance with the specified default string value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null or empty
     */
    public static LongValue of(final String defaultValue) {
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
     * @throws ArithmeticException if the result overflows an long
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
     * @throws ArithmeticException if the result overflows an long
     * @return the value associated with this instance after adding the operand
     */
    @Override
    public Long addAndGet(final Number operand) {
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
     * @throws ArithmeticException if the result overflows an long
     * @return the value associated with this instance immediately before adding the operand
     */
    @Override
    public Long getAndAdd(final Number operand) {
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
     * @throws ArithmeticException if the result overflows an long
     * @return this instance
     */
    @Override
    public LongValue subtract(final Number operand) {
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
     * @throws ArithmeticException if the result overflows an long
     * @return the value associated with this instance after subtracting the operand
     */
    @Override
    public Long subtractAndGet(final Number operand) {
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
     * @throws ArithmeticException if the result overflows an long
     * @return the value associated with this instance immediately before subtracting the operand
     */
    @Override
    public Long getAndSubtract(final Number operand) {
        final Long last = value;
        value = Math.subtractExact(value, operand.longValue());
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
    public boolean isEqualTo(final Number number) {
        return value == number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNotEqualTo(final Number number) {
        return value != number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThanOrEqualTo(final Number number) {
        return value <= number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThanOrEqualTo(final Number number) {
        return value >= number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThan(final Number number) {
        return value < number.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThan(final Number number) {
        return value > number.longValue();
    }

    @Override
    public int compareTo(final @NotNull LongValue other) {
        return Long.compare(value, other.value);
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
    public LongValue set(final Number value) {
        checkArgumentNotNull(value, "Value cannot be null!");
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
