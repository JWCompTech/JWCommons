package com.jwcomptech.commons.javafx.controls.locations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.*;

@Data
public abstract class NodeLocation<T> {
    @Getter(AccessLevel.PRIVATE)
    private final Map<String, T> locationValues = new LinkedHashMap<>(4);

    public int size() {
        return locationValues.size();
    }

    public boolean isEmpty() {
        return locationValues.isEmpty();
    }

    public boolean containsKey(final String key) {
        return locationValues.containsKey(key);
    }

    public boolean containsValue(final T value) {
        return locationValues.containsValue(value);
    }

    public T get(final String key) {
        return locationValues.get(key);
    }

    protected T put(final String key, final T value) {
        return locationValues.put(key, value);
    }

    public Set<String> keySet() {
        return Collections.unmodifiableSet(locationValues.keySet());
    }

    public Collection<T> values() {
        return Collections.unmodifiableCollection(locationValues.values());
    }

    public T getOrDefault(final String key, final T defaultValue) {
        return locationValues.getOrDefault(key, defaultValue);
    }
}
