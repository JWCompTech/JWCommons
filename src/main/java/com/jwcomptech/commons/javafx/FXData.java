package com.jwcomptech.commons.javafx;

/*-
 * #%L
 * JWCommons
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

import com.jwcomptech.commons.annotations.FeatureComplete;
import com.jwcomptech.commons.javafx.controls.FXControls;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.BiFunction;

import static com.jwcomptech.commons.consts.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * A type-safe, multi-typed data container used for dialog-related key-value storage.
 * <p>
 * Supports any object value, indexed by {@code String} keys. Type access is enforced using {@code Class<T>} tokens.
 *
 * @since 1.0.0-alpha
 * @see java.util.Map
 */
@Data
@FeatureComplete(since = "1.0.0-alpha")
@SuppressWarnings("unused")
public class FXData {
    private final Map<Class<?>, Map<String, ?>> dataMap;
    private final FXControls controls;

    private FXData(final Map<Class<?>, Map<String, ?>> dataMap,
                   final FXControls controls) {
        this.dataMap = dataMap;
        this.controls = controls;
    }

    /**
     * Creates a new builder instance for {@code FXData}.
     *
     * @return the new builder
     */
    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Creates a mutable copy of this {@code FXData}.
     * The new instance has the same type and data, but is mutable.
     *
     * @return a new instance with the same values as the current instance
     */
    public @NotNull FXData copy() {
        final Map<Class<?>, Map<String, ?>> newDataMap = new LinkedHashMap<>();

        for (Map.Entry<Class<?>, Map<String, ?>> entry : dataMap.entrySet()) {
            Class<?> key = entry.getKey();
            Map<String, ?> valueMap = entry.getValue();

            // Create a shallow copy of the inner map
            Map<String, ?> copiedInnerMap = valueMap != null
                    ? new LinkedHashMap<>(valueMap)
                    : null;

            newDataMap.put(key, copiedInnerMap);
        }

        //controls must not be copied or can cause major JavaFX issues
        return new FXData(newDataMap, controls);
    }

    /**
     * Returns the base data map, that maps the object classes to their
     * values, as an {@code UnmodifiableMap}.
     *
     * @return the base data map, that maps the object classes to their
     * values, as an {@code UnmodifiableMap}
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public Map<Class<?>, Map<String, ?>> getDataMap() {
        return Collections.unmodifiableMap(dataMap);
    }

    /**
     * Gets the underlying map for the specified type.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return the modifiable map of data for the specified type
     */
    @SuppressWarnings("unchecked")
    private <T> @NotNull Map<String, T> getTypedMap(Class<T> type) {
        Map<String, ?> rawMap = dataMap.get(type);
        return (Map<String, T>) (
                rawMap == null
                ? dataMap.getOrDefault(type, new LinkedHashMap<>())
                : rawMap
        );
    }

    /**
     * Retrieves the value of a given type and key.
     *
     * @param type the class of the desired value type
     * @param key  the key for the value
     * @param <T>  the type parameter
     * @return the value cast to the specified type, or {@code null} if no mapping found
     */
    public @NotNull <T> Optional<T> getDataValue(final @NotNull Class<T> type, final String key) {
        return Optional.ofNullable(type.cast(getTypedMap(type).get(key)));
    }

    /**
     * Retrieves the value for a given key, or returns the default if not present.
     *
     * @param type         the class of the desired value type
     * @param key          the key for the value
     * @param defaultValue a fallback value if the key doesn't exist
     * @param <T>          the type parameter
     * @return the value or the default
     */
    public @NotNull <T> Optional<T> getDataValueOrDefault(final @NotNull Class<T> type,
                                                final String key,
                                                final T defaultValue) {
        return Optional.ofNullable(getTypedMap(type).getOrDefault(key, defaultValue));
    }

    /**
     * Checks if the given value exists in the map for the specified type.
     *
     * @param type  the value type
     * @param value the value to check
     * @param <T>   the type parameter
     * @return {@code true} if found, {@code false} otherwise
     */
    public <T> boolean containsDataValue(final @NotNull Class<T> type, final T value) {
        return getTypedMap(type).containsValue(value);
    }

    /**
     * Checks if the specified key exists for the given type.
     *
     * @param type the value type
     * @param key  the key to check
     * @param <T>  the type parameter
     * @return {@code true} if the key exists, {@code false} otherwise
     */
    public <T> boolean containsDataKey(final @NotNull Class<T> type, final String key) {
        return getTypedMap(type).containsKey(key);
    }

    /**
     * Checks if the data map for the given type is empty.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return {@code true} if empty, {@code false} otherwise
     */
    public <T> boolean isDataEmpty(final @NotNull Class<T> type) {
        return getTypedMap(type).isEmpty();
    }

    /**
     * Checks if the data map for the given type contains any entries.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return {@code true} if not empty, {@code false} otherwise
     */
    public <T> boolean isDataPresent(final @NotNull Class<T> type) {
        return !getTypedMap(type).isEmpty();
    }

    /**
     * Gets an unmodifiable map of all entries of the specified type.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return an unmodifiable map of key-value pairs
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public @NotNull @UnmodifiableView <T> Map<String, T> getData(final @NotNull Class<T> type) {
        return Collections.unmodifiableMap(getTypedMap(type));
    }

    /**
     * Gets an unmodifiable view of all keys for a given type.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return an unmodifiable set of keys
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    public @NotNull @UnmodifiableView <T> Set<String> getDataKeySet(final @NotNull Class<T> type) {
        return Collections.unmodifiableSet(getTypedMap(type).keySet());
    }

    /**
     * Gets an unmodifiable view of all values for a given type.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return an unmodifiable collection of values
     * @see java.util.Collections#unmodifiableCollection(Collection)
     */
    public @NotNull @UnmodifiableView <T> Collection<T> getDataValues(final @NotNull Class<T> type) {
        return Collections.unmodifiableCollection(getTypedMap(type).values());
    }

    /**
     * Returns a new {@code FXData} instance by merging this instance with another.
     * Entries from {@code other} will overwrite entries in this instance if keys collide.
     *
     * @param other the {@code FXData} instance to merge from
     * @return a new {@code FXData} containing merged data
     * @throws NullPointerException if {@code other} is null
     */
    public FXData mergeWith(@NotNull FXData other) {
        return merge(this, other);
    }

    /**
     * Returns a new FXData instance by merging this instance with another.
     * Entries from {@code other} will NOT overwrite entries in this instance if keys collide.
     *
     * @param other the {@code FXData} instance to merge from
     * @return a new {@code FXData} with merged data, preferring original values on conflict
     * @throws NullPointerException if either argument is null
     */
    public FXData mergeKeepOriginalWith(@NotNull FXData other) {
        return mergeKeepOriginal(this, other);
    }

    /**
     * Returns a new {@code FXData} instance by merging this instance with another,
     * using a custom conflict resolver function.
     *
     * @param other            the {@code FXData} instance to merge from
     * @param conflictResolver a function that takes (originalValue, otherValue) and returns the merged value
     * @return a new {@code FXData} with merged data, resolved by conflictResolver on conflicts
     * @throws NullPointerException if any argument is null
     */
    @SuppressWarnings("UnusedReturnValue")
    public FXData mergeWithConflictResolver(@NotNull FXData other,
                                            @NotNull BiFunction<Object, Object, Object> conflictResolver) {
        return mergeWithConflictResolver(this, other, conflictResolver);
    }

    /**
     * Gets an unmodifiable view of the entry set for the given type.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return an unmodifiable set of map entries
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    public @NotNull @UnmodifiableView <T> Set<Map.Entry<String, T>> getDataEntrySet(final @NotNull Class<T> type) {
        return Collections.unmodifiableSet(getTypedMap(type).entrySet());
    }

    /**
     * Returns a new {@code FXData} instance resulting from merging two FXData objects.
     * Entries from {@code other} will overwrite entries in {@code original} if keys collide.
     *
     * @param original the original {@code FXData} instance
     * @param other    the {@code FXData} instance to merge from
     * @return a new {@code FXData} containing merged data from both inputs
     * @throws NullPointerException if either argument is null
     */
    public static FXData merge(@NotNull FXData original, @NotNull FXData other) {
        return FXData.builder().mergeFrom(original).mergeFrom(other).build();
    }

    /**
     * Merges two {@code FXData} objects, keeping original values if keys collide.
     *
     * @param original the original {@code FXData} instance
     * @param other    the {@code FXData} instance to merge from
     * @return a new {@code FXData} with merged data, preferring original values on conflict
     * @throws NullPointerException if either argument is null
     */
    @SuppressWarnings("unchecked")
    public static FXData mergeKeepOriginal(@NotNull FXData original, @NotNull FXData other) {
        Builder builder = FXData.builder().mergeFrom(original);

        // Manually merge other, but skip keys already present
        for (Class<?> type : other.dataMap.keySet()) {
            Map<String, ?> sourceMap = other.getData(type);
            Map<String, Object> targetMap =
                    (Map<String, Object>) builder.dataMap.computeIfAbsent(
                            type, k -> new LinkedHashMap<>());

            sourceMap.forEach(targetMap::putIfAbsent);
        }

        return builder.build();
    }

    /**
     * Merges two {@code FXData} objects using a custom conflict resolver function.
     *
     * @param original         the original {@code FXData} instance
     * @param other            the {@code FXData} instance to merge from
     * @param conflictResolver a function that takes (originalValue, otherValue) and returns the merged value
     * @return a new {@code FXData} with merged data, resolved by conflictResolver on conflicts
     * @throws NullPointerException if any argument is null
     */
    @SuppressWarnings("unchecked")
    public static FXData mergeWithConflictResolver(
            @NotNull FXData original,
            @NotNull FXData other,
            @NotNull BiFunction<Object, Object, Object> conflictResolver) {

        Builder builder = FXData.builder().mergeFrom(original);

        for (Class<?> type : other.dataMap.keySet()) {
            Map<String, ?> sourceMap = other.getData(type);
            Map<String, Object> targetMap =
                    (Map<String, Object>) builder.dataMap.computeIfAbsent(
                            type, k -> new HashMap<>());

            sourceMap.forEach((key, value) -> {
                if (targetMap.containsKey(key)) {
                    Object originalValue = targetMap.get(key);
                    Object resolvedValue = conflictResolver.apply(originalValue, value);
                    targetMap.put(key, resolvedValue);
                } else {
                    targetMap.put(key, value);
                }
            });
        }

        return builder.build();
    }

    /**
     * Builder class for {@link FXData}.
     * Allows for incremental, type-safe construction of a {@code FXData} instance.
     */
    public static class Builder {
        private final Map<Class<?>, Map<String, ?>> dataMap = new LinkedHashMap<>();
        private final FXControls controls = new FXControls();

        /**
         * Adds a string value.
         *
         * @param key   the key
         * @param value the string value
         * @return this builder
         */
        public Builder putString(final String key, final String value) {
            return put(String.class, key, value);
        }

        /**
         * Adds multiple string values.
         *
         * @param values map of key-value pairs
         * @return this builder
         */
        public Builder putAllString(final Map<String, String> values) {
            return putAll(String.class, values);
        }

        /**
         * Adds an integer value.
         *
         * @param key   the key
         * @param value the integer value
         * @return this builder
         */
        public Builder putInteger(final String key, final Integer value) {
            return put(Integer.class, key, value);
        }

        /**
         * Adds multiple integer values.
         *
         * @param values map of key-value pairs
         * @return this builder
         */
        public Builder putAllInteger(final Map<String, Integer> values) {
            return putAll(Integer.class, values);
        }

        /**
         * Adds a float value.
         *
         * @param key   the key
         * @param value the float value
         * @return this builder
         */
        public Builder putFloat(final String key, final Float value) {
            return put(Float.class, key, value);
        }

        /**
         * Adds multiple float values.
         *
         * @param values map of key-value pairs
         * @return this builder
         */
        public Builder putAllFloat(final Map<String, Float> values) {
            return putAll(Float.class, values);
        }

        /**
         * Adds a double value.
         *
         * @param key   the key
         * @param value the double value
         * @return this builder
         * @since 1.0
         */
        public Builder putDouble(final String key, final Double value) {
            return put(Double.class, key, value);
        }

        /**
         * Adds multiple double values.
         *
         * @param values map of key-value pairs
         * @return this builder
         */
        public Builder putAllDouble(final Map<String, Double> values) {
            return putAll(Double.class, values);
        }

        /**
         * Adds a boolean value.
         *
         * @param key   the key
         * @param value the boolean value
         * @return this builder
         */
        public Builder putBoolean(final String key, final Boolean value) {
            return put(Boolean.class, key, value);
        }

        /**
         * Adds multiple boolean values.
         *
         * @param values map of key-value pairs
         * @return this builder
         */
        public Builder putAllBoolean(final Map<String, Boolean> values) {
            return putAll(Boolean.class, values);
        }

        /**
         * Adds a typed value to the data map.
         *
         * @param type  the type of the value
         * @param key   the key
         * @param value the value
         * @param <T>   the type parameter
         * @return this builder
         */
        @SuppressWarnings("unchecked")
        public <T> Builder put(final Class<T> type, final String key, T value) {
            checkArgumentNotNull(type, cannotBeNullOrEmpty("type"));
            Map<String, T> map = (Map<String, T>) dataMap.computeIfAbsent(
                    type, k -> new LinkedHashMap<String, T>());
            map.put(key, value);
            return this;
        }

        /**
         * Adds multiple values of a given type.
         *
         * @param type   the type of values
         * @param values the values to insert
         * @param <T>    the type parameter
         * @return this builder
         */
        @SuppressWarnings("unchecked")
        public <T> Builder putAll(final Class<T> type, final Map<String, T> values) {
            checkArgumentNotNull(type, cannotBeNullOrEmpty("type"));
            Map<String, T> map = (Map<String, T>) dataMap.computeIfAbsent(
                    type, k -> new LinkedHashMap<String, T>());
            map.putAll(values);
            return this;
        }

        /**
         * Adds multiple values of a given type, checking types for safety.
         *
         * @param type   the expected type
         * @param values the raw values to insert
         * @param <T>    the type parameter
         * @return this builder
         * @throws IllegalArgumentException if any value does not match the type
         */
        @SuppressWarnings("unchecked")
        public <T> Builder putAllSafe(final Class<T> type,
                                      final @NotNull Map<String, ?> values) {
            checkArgumentNotNull(type, cannotBeNullOrEmpty("type"));
            Map<String, T> map = (Map<String, T>) dataMap.computeIfAbsent(
                    type, k -> new LinkedHashMap<String, T>());

            for (Map.Entry<String, ?> entry : values.entrySet()) {
                Object val = entry.getValue();
                if (!type.isInstance(val)) {
                    throw new IllegalArgumentException("Value for key '" + entry.getKey() +
                            "' is not of type: " + type.getSimpleName());
                }
                map.put(entry.getKey(), type.cast(val));
            }

            return this;
        }

        /**
         * Merges values from another {@code FXData} into this builder.
         * Existing keys will be overwritten.
         *
         * @param other another {@code FXData} to merge from
         * @return this builder
         */
        @SuppressWarnings("unchecked")
        public Builder mergeFrom(final @NotNull FXData other) {
            for (Class<?> type : other.dataMap.keySet()) {
                Map<String, ?> sourceMap = other.getData(type);
                Map<String, Object> targetMap = (Map<String, Object>)
                        dataMap.computeIfAbsent(type, k -> new LinkedHashMap<>());

                targetMap.putAll(sourceMap);
            }
            return this;
        }

        public <T extends Node> void addControl(final Class<T> clazz, final String name, final T control) {
            controls.addControl(clazz, name, control);
        }

        public <T extends Node> void addControlIgnoreExisting(final Class<T> clazz, final String name, final T control) {
            controls.addControlIgnoreExisting(clazz, name, control);
        }

        public void addLabel(final String name, final Label value) {
            controls.addLabel(name, value);
        }

        public void addLabelIgnoreExisting(final String name, final Label label) {
            controls.addLabelIgnoreExisting(name, label);
        }

        public void addButton(final String name, final Button button) {
            controls.addButton(name, button);
        }

        public void addButtonIgnoreExisting(final String name, final Button button) {
            controls.addButtonIgnoreExisting(name, button);
        }

        public void addTextField(final String name, final TextField textField) {
            controls.addTextField(name, textField);
        }

        public void addTextFieldIgnoreExisting(final String name, final TextField textField) {
            controls.addTextFieldIgnoreExisting(name, textField);
        }

        public void addPasswordField(final String name, final PasswordField passwordField) {
            controls.addPasswordField(name, passwordField);
        }

        public void addPasswordFieldIgnoreExisting(final String name, final PasswordField passwordField) {
            controls.addPasswordFieldIgnoreExisting(name, passwordField);
        }

        public void addCheckBox(final String name, final CheckBox checkBox) {
            controls.addCheckBox(name, checkBox);
        }

        public void addCheckBoxIgnoreExisting(final String name, final CheckBox checkBox) {
            controls.addCheckBoxIgnoreExisting(name, checkBox);
        }

        /**
         * Builds the {@code FXData} instance.
         *
         * @return a new {@code FXData} object
         */
        public FXData build() {
            // Wrap all inner maps in unmodifiable maps
            Map<Class<?>, Map<String, ?>> finalized = new LinkedHashMap<>();

            for (Map.Entry<Class<?>, Map<String, ?>> entry : dataMap.entrySet()) {
                Map<String, ?> inner = Collections.unmodifiableMap(new LinkedHashMap<>(entry.getValue()));
                finalized.put(entry.getKey(), inner);
            }

            return new FXData(Collections.unmodifiableMap(finalized), controls);
        }
    }
}
