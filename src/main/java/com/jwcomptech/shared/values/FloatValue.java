package com.jwcomptech.shared.values;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

import static com.jwcomptech.shared.Literals.cannotBeNull;
import static com.jwcomptech.shared.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNullOrEmpty;

/**
 * Provides mutable access to an {@link Float}.
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

    /** Creates a new FloatValue instance with the default value of 0.0. */
    @Contract(" -> new")
    public static @NotNull FloatValue of() {
        return new FloatValue();
    }

    /**
     * Creates a new FloatValue instance with the specified default int value.
     * @param defaultValue the value to set
     */
    @Contract("_ -> new")
    public static @NotNull FloatValue of(final int defaultValue) {
        return new FloatValue(defaultValue);
    }

    /**
     * Creates a new FloatValue instance with the specified default float value.
     * @param defaultValue the value to set
     */
    @Contract("_ -> new")
    public static @NotNull FloatValue of(final float defaultValue) {
        return new FloatValue(defaultValue);
    }

    /**
     * Creates a new FloatValue instance with the specified number value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null
     */
    @Contract("_ -> new")
    public static @NotNull FloatValue of(final Number defaultValue) {
        return new FloatValue(defaultValue);
    }

    /**
     * Creates a new FloatValue instance with the specified default string value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default value is null or empty
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
     * {@inheritDoc}
     */
    @Override
    public boolean isPositive() {
        return 0 < Integer.signum(value.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNegative() {
        return 0 > Integer.signum(value.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isZero() {
        return 0.0F == value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqualTo(final @NotNull Number number) {
        return value == number.floatValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNotEqualTo(final @NotNull Number number) {
        return value != number.floatValue();
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
    public int compareTo(@NotNull Float other) {
        return Float.compare(value, other);
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
        return obj instanceof FloatValue && value == ((FloatValue) obj).floatValue();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
