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

import com.jwcomptech.commons.javafx.FXData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class FXDataTest {

    private FXData data;

    @BeforeEach
    void setUp() {
        data = FXData.builder()
                .putString("name", "Alice")
                .putInteger("age", 30)
                .putFloat("height", 5.5f)
                .putDouble("weight", 135.5)
                .putBoolean("active", true)
                .build();
    }

    @Test
    void testGetDataValue() {
        assertEquals("Alice", data.getDataValue(String.class, "name").get());
        assertEquals(30, data.getDataValue(Integer.class, "age").get());
        assertEquals(5.5f, data.getDataValue(Float.class, "height").get());
        assertEquals(135.5, data.getDataValue(Double.class, "weight").get());
        assertTrue(data.getDataValue(Boolean.class, "active").get());
    }

    @Test
    void testGetDataValueOrDefault() {
        assertEquals("Alice", data.getDataValueOrDefault(String.class, "name", "Default").get());
        assertEquals("Default", data.getDataValueOrDefault(String.class, "missing", "Default").get());
    }

    @Test
    void testContainsDataValue() {
        assertTrue(data.containsDataValue(String.class, "Alice"));
        assertFalse(data.containsDataValue(String.class, "Bob"));
    }

    @Test
    void testContainsDataKey() {
        assertTrue(data.containsDataKey(String.class, "name"));
        assertFalse(data.containsDataKey(String.class, "nickname"));
    }

    @Test
    void testIsDataEmptyAndPresent() {
        assertFalse(data.isDataEmpty(String.class));
        assertTrue(data.isDataPresent(String.class));
    }

    @Test
    void testGetData() {
        Map<String, String> map = data.getData(String.class);
        assertEquals("Alice", map.get("name"));
    }

    @Test
    void testGetDataKeySet() {
        Set<String> keys = data.getDataKeySet(String.class);
        assertTrue(keys.contains("name"));
    }

    @Test
    void testGetDataValues() {
        Collection<String> values = data.getDataValues(String.class);
        assertTrue(values.contains("Alice"));
    }

    @Test
    void testGetDataEntrySet() {
        Set<Map.Entry<String, String>> entries = data.getDataEntrySet(String.class);
        assertTrue(entries.stream().anyMatch(e -> e.getKey().equals("name") && e.getValue().equals("Alice")));
    }

    @Test
    void testStaticMergeOverwriteBehavior() {
        FXData other = FXData.builder().putString("name", "Bob").putString("nickname", "Bobby").build();
        FXData merged = FXData.merge(data, other);

        assertEquals("Bob", merged.getDataValue(String.class, "name").get()); // Overwritten
        assertEquals("Bobby", merged.getDataValue(String.class, "nickname").get()); // New entry
    }

    @Test
    void testStaticMergeKeepOriginalBehavior() {
        FXData other = FXData.builder().putString("name", "Bob").putString("nickname", "Bobby").build();
        FXData merged = FXData.mergeKeepOriginal(data, other);

        assertEquals("Alice", merged.getDataValue(String.class, "name").get()); // Original retained
        assertEquals("Bobby", merged.getDataValue(String.class, "nickname").get()); // New entry
    }

    @Test
    void testStaticMergeWithConflictResolver() {
        FXData d1 = FXData.builder().putInteger("score", 10).build();
        FXData d2 = FXData.builder().putInteger("score", 20).build();

        BiFunction<Object, Object, Object> resolver = (a, b) -> ((Integer) a + (Integer) b);
        FXData merged = FXData.mergeWithConflictResolver(d1, d2, resolver);

        assertEquals(30, merged.getDataValue(Integer.class, "score").get());
    }

    @Test
    void testInstanceMergeWith() {
        FXData other = FXData.builder().putString("nickname", "Ali").build();
        FXData result = data.mergeWith(other);

        assertEquals("Alice", result.getDataValue(String.class, "name").get()); // original retained
        assertEquals("Ali", result.getDataValue(String.class, "nickname").get()); // merged in
    }

    @Test
    void testInstanceMergeKeepOriginalWith() {
        FXData other = FXData.builder().putString("name", "Bob").putString("nickname", "Bobby").build();
        FXData result = data.mergeKeepOriginalWith(other);

        assertEquals("Alice", result.getDataValue(String.class, "name").get()); // original retained
        assertEquals("Bobby", result.getDataValue(String.class, "nickname").get()); // merged in
    }

    @Test
    void testUnsupportedTypeThrowsException() {
        class Unsupported {}

        FXData fxData = FXData.builder().build();

        assertTrue(fxData.getDataValue(Unsupported.class, "someKey").isEmpty());
    }

    @Test
    void testCopyCreatesDeepCopy() {
        FXData copy = data.copy();

        assertNotSame(data, copy);
        assertNotSame(data.getData(String.class), copy.getData(String.class));
        assertEquals(data.getDataValue(String.class, "name"), copy.getDataValue(String.class, "name"));

        // Replace the original with a new one with a modified value
        FXData modified = FXData.builder()
                .putString("name", "Changed")
                .putInteger("age", 30)
                .putFloat("height", 5.5f)
                .putDouble("weight", 135.5)
                .putBoolean("active", true)
                .build();

        assertEquals("Alice", copy.getDataValue(String.class, "name").get());
        assertEquals("Changed", modified.getDataValue(String.class, "name").get());
    }

    @Test
    void testMergeWithEmptyData() {
        FXData empty = FXData.builder().build();
        FXData merged = data.mergeWith(empty);

        assertEquals("Alice", merged.getDataValue(String.class, "name").get());
        assertTrue(merged.getDataValue(Integer.class, "age").isPresent());
    }

    @Test
    void testMergeKeepsTypesSeparate() {
        FXData withInt = FXData.builder().putInteger("score", 42).build();
        FXData withString = FXData.builder().putString("score", "forty-two").build();

        assertTrue(withInt.getDataValue(Integer.class, "score").isPresent());
        assertTrue(withString.getDataValue(String.class, "score").isPresent());
        assertTrue(withString.getDataValue(Integer.class, "score").isEmpty());
    }

    @Test
    void testIsDataEmptyReturnsTrueWhenNoDataPresent() {
        FXData empty = FXData.builder().build();
        assertTrue(empty.isDataEmpty(String.class));
        assertFalse(data.isDataEmpty(String.class));
    }

    @Test
    void testHandlesNullValuesGracefully() {
        FXData fxData = FXData.builder()
                .putString("nullable", null)
                .build();

        assertTrue(fxData.getDataValue(String.class, "nullable").isEmpty());
        assertTrue(fxData.getDataValues(String.class).contains(null));
    }
}

