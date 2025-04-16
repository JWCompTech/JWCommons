package com.jwcomptech.shared.values;

import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Provides mutable access to a {@link Number}.
 *
 * @param <T> the type to set and get
 * @param <V> the value of the object that implements
 *            this class to allow for method chaining
 * @since 0.0.1
 */
public abstract class NumberValue<T extends Number, V extends NumberValue<T, V>>
        extends Number implements Comparable<V>, Value<T, V> {
    protected T value;
    protected PropertyChangeSupport listeners;


    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * The same listener object may be added more than once, and will be called
     * as many times as it is added.
     * If {@code listener} is null, no exception is thrown and no action
     * is taken.
     *
     * @param listener  The PropertyChangeListener to be added
     */
    public final void addPropertyChangeListener(final PropertyChangeListener listener) {
        listeners.addPropertyChangeListener("value", listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     * If {@code listener} was added more than once to the same event
     * source, it will be notified one less time after being removed.
     * If {@code listener} is null, or was never added, no exception is
     * thrown and no action is taken.
     *
     * @param listener  The PropertyChangeListener to be removed
     */
    public final void removePropertyChangeListener(final PropertyChangeListener listener) {
        listeners.removePropertyChangeListener("value", listener);
    }

    /**
     * Returns a List of all the listeners that were added to the
     * PropertyChangeSupport object with addPropertyChangeListener().
     * <p>
     * If some listeners have been added with a named property, then
     * the returned list will be a mixture of PropertyChangeListeners
     * and {@code PropertyChangeListenerProxy}s. If the calling
     * method is interested in distinguishing the listeners then it must
     * test each element to see if it's a
     * {@code PropertyChangeListenerProxy}, perform the cast, and examine
     * the parameter.
     *
     * <pre>{@code
     * List<PropertyChangeListener> listeners = bean.getPropertyChangeListeners();
     * for (final PropertyChangeListener listener : listeners) {
     *   if (listener instanceof PropertyChangeListenerProxy) {
     *     PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy)listener;
     *     if (proxy.getPropertyName().equals("foo")) {
     *       // proxy is a PropertyChangeListener which was associated
     *       // with the property named "foo"
     *     }
     *   }
     * }
     * }</pre>
     *
     * @see PropertyChangeListenerProxy
     * @return all of the {@code PropertyChangeListeners} added or an
     *         empty list if no listeners have been added
     */
    public final List<PropertyChangeListener> getListeners() {
        return List.of(listeners.getPropertyChangeListeners());
    }

    /**
     * Check if there are any listeners.
     * @return true if there are one or more listeners
     */
    public final boolean hasListeners() {
        return listeners.hasListeners("value");
    }


    /**
     * Increments the value.
     * @return this instance
     */
    public abstract V increment();

    /**
     * Increments this instance's value by 1; this method returns the value associated with the instance
     * immediately prior to the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was incremented
     */
    public abstract T incrementAndGet();

    /**
     * Increments this instance's value by 1; this method returns the value associated with the instance
     * immediately after the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is incremented
     */
    public abstract T getAndIncrement();

    /**
     * Decrements the value.
     * @return this instance
     */
    public abstract V decrement();

    /**
     * Decrements this instance's value by 1; this method returns the value associated with the instance
     * immediately prior to the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was decremented
     */
    public abstract T decrementAndGet();

    /**
     * Decrements this instance's value by 1; this method returns the value associated with the instance
     * immediately after the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is decremented
     */
    public abstract T getAndDecrement();

    /**
     * Adds a value to the value of this instance.
     *
     * @param operand the value to add, not null
     * @throws IllegalArgumentException if the object is null
     * @return this instance
     */
    public abstract V add(final Number operand);

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @return the value associated with this instance after adding the operand
     */
    public abstract T addAndGet(final Number operand);

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @return the value associated with this instance immediately before adding the operand
     */
    public abstract T getAndAdd(final Number operand);

    /**
     * Subtracts a value from the value of this instance.
     *
     * @param operand  the value to subtract, not null
     * @throws IllegalArgumentException if the object is null
     * @return this instance
     */
    public abstract V subtract(final Number operand);

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @return the value associated with this instance after subtracting the operand
     */
    public abstract T subtractAndGet(final Number operand);

    /**
     * Decrements this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the subtraction operation. This method is not thread safe.
     *
     * @param operand the quantity to subtract, not null
     * @throws IllegalArgumentException if {@code operand} is null
     * @return the value associated with this instance immediately before subtracting the operand
     */
    public abstract T getAndSubtract(final Number operand);

    /**
     * Returns {@code true} if the value is positive,
     * {@code false} otherwise.
     * @return  {@code true} if the value is positive;
     * {@code false} otherwise.
     */
    public abstract boolean isPositive();

    /**
     * Returns {@code true} if the value is negative,
     * {@code false} otherwise.
     * @return  {@code true} if the value is negative;
     * {@code false} otherwise.
     */
    public abstract boolean isNegative();

    /**
     * Returns {@code true} if the value is equal to zero,
     * {@code false} otherwise.
     * @return {@code true} if the value is equal to zero;
     * {@code false} otherwise.
     */
    public abstract boolean isZero();

    /**
     * Returns {@code true} if the value is not equal to zero,
     * {@code false} otherwise.
     * @return {@code true} if the value is not equal to zero;
     * {@code false} otherwise.
     */
    public final boolean isNotZero() { return !isZero(); }

    /**
     * Returns {@code true} if the value is equal to the specified number,
     * {@code false} otherwise.
     * @param number the number to check
     * @return {@code true} if the value is equal to the specified number;
     * {@code false} otherwise.
     * @throws IllegalArgumentException if {@code number} is null
     */
    public abstract boolean isEqualTo(final Number number);

    /**
     * Returns {@code true} if the value is not equal to the specified number,
     * {@code false} otherwise.
     * @param number the number to check
     * @return {@code true} if the value is not equal to the specified number;
     * {@code false} otherwise.
     * @throws IllegalArgumentException if {@code number} is null
     */
    public abstract boolean isNotEqualTo(final Number number);

    /**
     * Returns {@code true} if the value is less than
     * or equal to the specified number, {@code false} otherwise.
     * @param number the number to check
     * @return {@code true} if the value is less than
     * or equal to the specified number, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code number} is null
     */
    public abstract boolean isLessThanOrEqualTo(final Number number);

    /**
     * Returns {@code true} if the value is greater than
     * or equal to the specified number, {@code false} otherwise.
     * @param number the number to check
     * @return {@code true} if the value is greater than
     * or equal to the specified number, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code number} is null
     */
    public abstract boolean isGreaterThanOrEqualTo(final Number number);

    /**
     * Returns {@code true} if the value is less than
     * the specified number, {@code false} otherwise.
     * @param number the number to check
     * @return {@code true} if the value is less than
     * the specified number, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code number} is null
     */
    public abstract boolean isLessThan(final Number number);

    /**
     * Returns {@code true} if the value is greater than
     * the specified number, {@code false} otherwise.
     * @param number the number to check
     * @return {@code true} if the value is greater than
     * the specified number, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code number} is null
     */
    public abstract boolean isGreaterThan(final Number number);

    /**
     * {@inheritDoc}
     */
    @Override
    public int intValue() {
        return value.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long longValue() {
        return value.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float floatValue() {
        return value.floatValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public abstract boolean equals(final Object o);

    @Override
    public abstract int hashCode();

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public abstract int compareTo(final @NotNull V other);

    /**
     * Sets the value.
     * @param value the value to store
     * @return this instance
     */
    @Override
    public abstract V set(final Number value);
}
