package com.jwcomptech.commons.javafx.dialogs;

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
import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.javafx.controls.FXButtonType;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * The object returned from a Dialog that contains the {@link FXButtonType} of the
 * button pressed and the passed {@link FXData}.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Data
@FeatureComplete(since = "1.0.0-alpha")
public final class DialogResult {
    private final FXButtonType buttonType;
    private final FXData data;
    private final boolean immutable;


    public static DialogResult NONE = new DialogResult(FXButtonType.NONE, null, true);
    public static DialogResult OK = new DialogResult(FXButtonType.OK, null, true);
    public static DialogResult CANCEL = new DialogResult(FXButtonType.CANCEL, null, true);

    @Contract(pure = true)
    public DialogResult(final @NotNull FXButtonType buttonType) {
        this(buttonType, FXData.builder().build());
    }

    @Contract(pure = true)
    public DialogResult(final @NotNull FXButtonType buttonType,
                        final FXData data) {
        this(buttonType, data, false);
    }

    private DialogResult(final @NotNull FXButtonType buttonType,
                         final FXData data,
                         final boolean immutable) {
        this.buttonType = buttonType;
        this.data = data;
        this.immutable = immutable;
    }

    /**
     * Creates a mutable copy of this DialogResult.
     * The new instance has the same type and data, but is mutable.
     *
     * @return a new instance with the same values as the current instance
     */
    @Contract(value = " -> new", pure = true)
    public @NotNull DialogResult copy() {
        return new DialogResult(this.buttonType, this.data != null
                ? this.data.copy()
                : FXData.builder().build(), false);

    }

    /**
     * Retrieves the value of a given type and key.
     *
     * @param type the class of the desired value type
     * @param key  the key for the value
     * @param <T>  the type parameter
     * @return the value cast to the specified type, or {@code null} if no mapping found
     * @throws IllegalArgumentException if the type is not supported
     */
    public <T> @NotNull Optional<T> getDataValue(final @NotNull Class<T> type, final String key) {
        return data.getDataValue(type, key);
    }

    /**
     * Retrieves the value for a given key, or returns the default if not present.
     *
     * @param type         the class of the desired value type
     * @param key          the key for the value
     * @param defaultValue a fallback value if the key doesn't exist
     * @param <T>          the type parameter
     * @return the value or the default
     * @throws IllegalArgumentException if the type is not supported
     */
    public <T> @NotNull Optional<T> getDataValueOrDefault(final @NotNull Class<T> type, final String key, final T defaultValue) {
        return data.getDataValueOrDefault(type, key, defaultValue);
    }

    /**
     * Checks if the given value exists in the map for the specified type.
     *
     * @param type  the value type
     * @param value the value to check
     * @param <T>   the type parameter
     * @return {@code true} if found, {@code false} otherwise
     * @throws IllegalArgumentException if the type is not supported
     */
    public <T> boolean containsDataValue(final @NotNull Class<T> type, final T value) {
        return data.containsDataValue(type, value);
    }

    /**
     * Checks if the specified key exists for the given type.
     *
     * @param type the value type
     * @param key  the key to check
     * @param <T>  the type parameter
     * @return {@code true} if the key exists, {@code false} otherwise
     * @throws IllegalArgumentException if the type is not supported
     */
    public <T> boolean containsDataKey(final @NotNull Class<T> type, final String key) {
        return data.containsDataKey(type, key);
    }

    /**
     * Checks if the data map for the given type is empty.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return {@code true} if empty, {@code false} otherwise
     * @throws IllegalArgumentException if the type is not supported
     */
    public <T> boolean isDataEmpty(final @NotNull Class<T> type) {
        return data.isDataEmpty(type);
    }

    /**
     * Checks if the data map for the given type contains any entries.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return {@code true} if not empty, {@code false} otherwise
     * @throws IllegalArgumentException if the type is not supported
     */
    public <T> boolean isDataPresent(final @NotNull Class<T> type) {
        return data.isDataPresent(type);
    }

    /**
     * Gets an unmodifiable map of all entries of the specified type.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return an unmodifiable map of key-value pairs
     * @throws IllegalArgumentException if the type is not supported
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    @NotNull
    @UnmodifiableView
    public <T> Map<String, T> getData(final @NotNull Class<T> type) {
        return data.getData(type);
    }

    /**
     * Gets an unmodifiable view of all keys for a given type.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return an unmodifiable set of keys
     * @throws IllegalArgumentException if the type is not supported
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    @NotNull
    @UnmodifiableView
    public <T> Set<String> getDataKeySet(final @NotNull Class<T> type) {
        return data.getDataKeySet(type);
    }

    /**
     * Gets an unmodifiable view of all values for a given type.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return an unmodifiable collection of values
     * @throws IllegalArgumentException if the type is not supported
     * @see java.util.Collections#unmodifiableCollection(Collection)
     */
    @NotNull
    @UnmodifiableView
    public <T> Collection<T> getDataValues(final @NotNull Class<T> type) {
        return data.getDataValues(type);
    }

    /**
     * Gets an unmodifiable view of the entry set for the given type.
     *
     * @param type the value type
     * @param <T>  the type parameter
     * @return an unmodifiable set of map entries
     * @throws IllegalArgumentException if the type is not supported
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    @NotNull
    @UnmodifiableView
    public <T> Set<Map.Entry<String, T>> getDataEntrySet(final @NotNull Class<T> type) {
        return data.getDataEntrySet(type);
    }

    /**
     * Merges this instance with a {@link FXData} object.
     * Entries from {@code other} will overwrite entries in this instance if keys collide.
     *
     * @param other the DialogData instance to merge from
     * @return a modified version of this instance
     * @throws NullPointerException if {@code other} is null
     */
    public DialogResult mergeWith(@NotNull final FXData other) {
        if (!immutable) data.mergeWith(other);
        return this;
    }

    /**
     * Merges this instance with a {@link FXData} object.
     * Entries from {@code other} will NOT overwrite entries in this instance if keys collide.
     *
     * @param other the DialogData instance to merge from
     * @return a modified version of this instance
     * @throws NullPointerException if either argument is null
     */
    public DialogResult mergeKeepOriginalWith(@NotNull final FXData other) {
        if (!immutable) data.mergeKeepOriginalWith(other);
        return this;
    }

    /**
     * Merges this instance with a {@link FXData} object,
     * using a custom conflict resolver function.
     *
     * @param other            the DialogData instance to merge from
     * @param conflictResolver a function that takes (originalValue, otherValue) and returns the merged value
     * @return a new DialogData with merged data, resolved by conflictResolver on conflicts
     * @throws NullPointerException if any argument is null
     */
    public DialogResult mergeWithConflictResolver(@NotNull final FXData other, final @NotNull BiFunction<Object, Object, Object> conflictResolver) {
        if (!immutable) data.mergeWithConflictResolver(other, conflictResolver);
        return this;
    }
}
