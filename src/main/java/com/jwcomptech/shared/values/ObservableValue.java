package com.jwcomptech.shared.values;

import com.jwcomptech.shared.base.Validated;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Provides mutable access to a non-numeric value.
 *
 * @param <T> the type to set and get
 * @param <V> the value of the object that implements
 *            this class to allow for method chaining
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public abstract class ObservableValue<T, V extends ObservableValue<T, V>> extends Validated implements Value<T, V> {
    protected T value;
    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    /**
     * Required for serialization support.
     *
     * @see Serializable
     */
    @Serial
    private static final long serialVersionUID = 5183297117938121441L;

    public ObservableValue(T value) {
        this.value = value;
    }

    //TODO: Figure out if this is necessary
    protected void setListenersTarget(V target) {
        this.listeners = new PropertyChangeSupport(target);
    }

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
    public final @NotNull @Unmodifiable List<PropertyChangeListener> getListeners() {
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
     * Returns the value.
     * @return the stored value
     */
    public T get() {
        return value;
    }

    /**
     * Sets the value.
     * @param value the value to store
     * @return this instance
     */
    //TODO: Figure out if it is safe to not make this abstract
    public V set(T value) {
        this.value = value;
        //noinspection unchecked
        return (V) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        ObservableValue<?, ?> that = (ObservableValue<?, ?>) o;

        return new EqualsBuilder().append(value, that.value).append(listeners, that.listeners).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .append(listeners)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .append("listeners", listeners)
                .toString();
    }
}
