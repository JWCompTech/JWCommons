package com.jwcomptech.shared.values;

import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;

import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;

/**
 * Provides mutable access to a {@link Boolean}.
 * @since 0.0.1
 *
 * @apiNote DO NOT use everywhere or else things will get unnecessarily complicated.
 */
public class BooleanValue extends BasicValue<Boolean, BooleanValue>
        implements Comparable<BooleanValue>, Serializable {
    /**
     * Required for serialization support.
     *
     * @see Serializable
     */
    @Serial
    private static final long serialVersionUID = -2483549311860585978L;

    private BooleanValue() {
        value = Boolean.FALSE;
        listeners = new PropertyChangeSupport(this);
    }

    private BooleanValue(final boolean defaultValue) {
        value = defaultValue;
        listeners = new PropertyChangeSupport(this);
    }

    private BooleanValue(final Boolean defaultValue) {
        checkArgumentNotNull(defaultValue, "Value cannot be null or empty!");
        value = defaultValue;
        listeners = new PropertyChangeSupport(this);
    }

    /** Creates a new BooleanValue instance with the default value of false. */
    public static BooleanValue of() {
        return new BooleanValue();
    }

    /**
     * Creates a new BooleanValue instance with the specified default value.
     * @param defaultValue the value to set
     */
    public static BooleanValue of(final boolean defaultValue) {
        return new BooleanValue(defaultValue);
    }

    /**
     * Creates a new BooleanValue instance with the specified default value.
     * @param defaultValue the value to set
     * @throws IllegalArgumentException if specified default is null
     */
    public static BooleanValue of(final Boolean defaultValue) {
        return new BooleanValue(defaultValue);
    }

    public static final BooleanValue TRUE = TRUE();
    public static BooleanValue TRUE() { return new BooleanValue(Boolean.TRUE); }
    public static final BooleanValue FALSE = FALSE();
    public static BooleanValue FALSE() { return new BooleanValue(Boolean.FALSE); }

    /**
     * Returns the value of this BooleanValue as a boolean.
     *
     * @return the boolean value represented by this object.
     */
    public boolean booleanValue() {
        return value;
    }

    /**
     * Sets the value.
     * @param value the value to store
     * @throws IllegalArgumentException if specified value is null
     * @return this instance
     */
    @Override
    public BooleanValue set(final Boolean value) {
        checkArgumentNotNull(value, "Value cannot be null or empty!");
        Boolean last = this.value;
        this.value = value;
        listeners.firePropertyChange("value", last, value ? TRUE : FALSE);
        return this;
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
     * Sets the value to true.
     * @return this instance
     */
    public BooleanValue setTrue() {
        final Boolean last = value;
        value = Boolean.TRUE;
        listeners.firePropertyChange("value", last, TRUE);
        return this;
    }

    /**
     * Sets the value to false.
     * @return this instance
     */
    public BooleanValue setFalse() {
        final Boolean last = value;
        value = Boolean.FALSE;
        listeners.firePropertyChange("value", last, FALSE);
        return this;
    }

    /**
     * Runs the specified runnable if the value is true.
     * @param runnable the runnable to run
     * @return this instance
     * @throws IllegalArgumentException if runnable is null
     */
    public BooleanValue ifTrue(final Runnable runnable) {
        checkArgumentNotNull(runnable, "Runnable cannot be null or empty!");
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
        checkArgumentNotNull(runnable, "Runnable cannot be null or empty!");
        if(!value) runnable.run();
        return this;
    }

    /**
     * Sets the value to the opposite of the current value.
     * @return this instance
     */
    public BooleanValue flip() {
        final Boolean last = value;
        value = !value;
        listeners.firePropertyChange("value", last, value ? FALSE : TRUE);
        return this;
    }

    public BooleanValue and(final BooleanValue value) {
        return BooleanValue.of(this.value && value.get());
    }

    public BooleanValue and(final boolean value) {
        return BooleanValue.of(this.value && value);
    }

    public BooleanValue or(final BooleanValue value) {
        return BooleanValue.of(this.value || value.get());
    }

    public BooleanValue or(final boolean value) {
        return BooleanValue.of(this.value || value);
    }

    /**
     * Gets this mutable as an instance of Boolean.
     *
     * @return a Boolean instance containing the value from this mutable, never null
     */
    public Boolean toBoolean() {
        return booleanValue();
    }

    /**
     * Compares this BooleanValue's value to another in ascending order.
     *
     * @param other  the other boolean to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     *  where false is less than true
     */
    public int compareTo(final boolean other) {
        return value.compareTo(other);
    }

    /**
     * Compares this BooleanValue to another in ascending order.
     *
     * @param other  the other BooleanValue to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     *  where false is less than true
     */
    @Override
    public int compareTo(final @NotNull BooleanValue other) {
        return value.compareTo(other.value);
    }

    /**
     * Compares this object to the specified object. The result is {@code true} if and only if the argument is
     * not {@code null} and is an {@link BooleanValue} object that contains the same
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
     * Returns a suitable hash code for this mutable.
     *
     * @return the hash code returned by {@code Boolean.TRUE} or {@code Boolean.FALSE}
     */
    @Override
    public int hashCode() {
        return (value ? Boolean.TRUE : Boolean.FALSE).hashCode();
    }


    @Override
    public String toString() {
        return value.toString();
    }
}
