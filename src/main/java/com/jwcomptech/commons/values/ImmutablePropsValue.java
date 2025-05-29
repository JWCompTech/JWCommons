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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.constant.ConstantDesc;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ImmutablePropsValue implements ImmutableValue<Properties> {
    private final Properties props;

    public ImmutablePropsValue(final InputStream inStream) throws IOException {
        this.props = new Properties();
        props.load(inStream);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns
     * {@code null} if the property is not found.
     *
     * @param key the property key.
     * @return the value in this property list with the specified key value.
     * @see Properties#setProperty
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns the
     * default value argument if the property is not found.
     *
     * @param key          the hashtable key.
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
     * @see Properties#setProperty
     */
    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    /**
     * Returns an enumeration of all the keys in this property list,
     * including distinct keys in the default property list if a key
     * of the same name has not already been found from the main
     * properties list.
     *
     * @return an enumeration of all the keys in this property list, including
     * the keys in the default property list.
     * @throws ClassCastException if any key in this property list
     *                            is not a string.
     * @see Enumeration
     * @see #stringPropertyNames
     */
    public Enumeration<?> propertyNames() {
        return props.propertyNames();
    }

    /**
     * Returns an unmodifiable set of keys from this property list
     * where the key and its corresponding value are strings,
     * including distinct keys in the default property list if a key
     * of the same name has not already been found from the main
     * properties list.  Properties whose key or value is not
     * of type {@code String} are omitted.
     * <p>
     * The returned set is not backed by this {@code Properties} object.
     * Changes to this {@code Properties} object are not reflected in the
     * returned set.
     *
     * @return an unmodifiable set of keys in this property list where
     * the key and its corresponding value are strings,
     * including the keys in the default property list.
     */
    public Set<String> stringPropertyNames() {
        return props.stringPropertyNames();
    }

    public boolean isEmpty() {
        return props.isEmpty();
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return props.entrySet().stream().map(e -> {
            //noinspection AnonymousInnerClassWithTooManyMethods
            return new Map.Entry<String, String>() {
                @Override
                public String getKey() {
                    return e.getKey().toString();
                }

                @Override
                public String getValue() {
                    return e.getValue().toString();
                }

                @Override
                public String setValue(String value) {
                    return (String) e.setValue(value);
                }
            };
        }).collect(Collectors.toSet());
    }

    public Collection<String> values() {
        return props.values().stream().map(Object::toString).collect(Collectors.toSet());
    }

    public boolean containsKey(String key) {
        return props.containsKey(key);
    }

    public boolean containsValue(String value) {
        return props.containsValue(value);
    }

    public boolean contains(String value) {
        return props.contains(value);
    }

    public String get(String key) {
        return (String) props.get(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        return (String) props.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super Object, ? super Object> action) {
        props.forEach(action);
    }

    /**
     * Returns the value.
     *
     * @return the stored value
     */
    @Override
    public Properties get() {
        return props;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(@NotNull Properties other) {
        throw new UnsupportedOperationException("Not Supported");
    }

    /**
     * Returns an {@link Optional} containing the nominal descriptor for this
     * instance, if one can be constructed, or an empty {@link Optional}
     * if one cannot be constructed.
     *
     * @return An {@link Optional} containing the resulting nominal descriptor,
     * or an empty {@link Optional} if one cannot be constructed.
     */
    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        ImmutablePropsValue that = (ImmutablePropsValue) o;

        return new EqualsBuilder()
                .append(props, that.props)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(props)
                .toHashCode();
    }

    @Override
    public String toString() {
        return props.toString();
    }
}
