package com.jwcomptech.commons.javafx.dialogs;

import com.jwcomptech.commons.javafx.FXData;
import com.jwcomptech.commons.javafx.controls.FXButtonType;
import com.jwcomptech.commons.values.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.jwcomptech.commons.utils.DebugUtils.print;
import static org.junit.jupiter.api.Assertions.*;

public class DialogResultTest {
    private DialogResult result;

    @BeforeEach
    void setUp() {
        final FXData data = FXData.builder()
                .putString("name", "Alice")
                .putInteger("age", 30)
                .putFloat("height", 5.5f)
                .putDouble("weight", 135.5)
                .putBoolean("active", true)
                .put(StringValue.class, "desc", StringValue.of("This is a test"))
                .build();
        result = new DialogResult(FXButtonType.OK, data);
    }

    @Test
    void testGetDataValue() {
        assertEquals("Alice", result.getDataValue(String.class, "name").get());
        assertEquals(30, result.getDataValue(Integer.class, "age").get());
        assertEquals(5.5f, result.getDataValue(Float.class, "height").get());
        assertEquals(135.5, result.getDataValue(Double.class, "weight").get());
        assertTrue(result.getDataValue(Boolean.class, "active").get());
        assertTrue(result.getDataValue(StringValue.class, "desc").get().equalsValue("This is a test"));

        final FXData data = result.getData();
        assertEquals("Alice", data.getDataValue(String.class, "name").get());
        assertEquals(30, data.getDataValue(Integer.class, "age").get());
        assertEquals(5.5f, data.getDataValue(Float.class, "height").get());
        assertEquals(135.5, data.getDataValue(Double.class, "weight").get());
        assertTrue(data.getDataValue(Boolean.class, "active").get());
        assertTrue(data.getDataValue(StringValue.class, "desc").get().equalsValue("This is a test"));
    }

    @Test
    void testGetDataValueOrDefault() {
        assertEquals("Alice", result.getDataValueOrDefault(String.class,
                "name", "Default").get());
        assertEquals("Default", result.getDataValueOrDefault(String.class,
                "missing", "Default").get());

        final FXData data = result.getData();
        assertEquals("Alice", data.getDataValueOrDefault(String.class,
                "name", "Default").get());
        assertEquals("Default", data.getDataValueOrDefault(String.class,
                "missing", "Default").get());
    }

    @Test
    void testContainsDataValue() {
        assertTrue(result.containsDataValue(String.class, "Alice"));
        assertFalse(result.containsDataValue(String.class, "Bob"));

        final FXData data = result.getData();
        assertTrue(data.containsDataValue(String.class, "Alice"));
        assertFalse(data.containsDataValue(String.class, "Bob"));
    }

    @Test
    void testContainsDataKey() {
        assertTrue(result.containsDataKey(String.class, "name"));
        assertFalse(result.containsDataKey(String.class, "nickname"));

        final FXData data = result.getData();
        assertTrue(data.containsDataKey(String.class, "name"));
        assertFalse(data.containsDataKey(String.class, "nickname"));
    }

    @Test
    void testIsDataEmptyAndPresent() {
        assertFalse(result.isDataEmpty(String.class));
        assertTrue(result.isDataPresent(String.class));

        final FXData data = result.getData();
        assertFalse(data.isDataEmpty(String.class));
        assertTrue(data.isDataPresent(String.class));
    }

    @Test
    void testGetData() {
        Map<String, String> map = result.getData(String.class);
        assertEquals("Alice", map.get("name"));

        final FXData data = result.getData();
        Map<String, String> map2 = data.getData(String.class);
        assertEquals("Alice", map2.get("name"));
    }

    @Test
    void testGetDataKeySet() {
        Set<String> keys = result.getDataKeySet(String.class);
        assertTrue(keys.contains("name"));

        final FXData data = result.getData();
        Set<String> keys2 = data.getDataKeySet(String.class);
        assertTrue(keys2.contains("name"));
    }

    @Test
    void testGetDataValues() {
        Collection<String> values = result.getDataValues(String.class);
        assertTrue(values.contains("Alice"));

        final FXData data = result.getData();
        Collection<String> values2 = data.getDataValues(String.class);
        assertTrue(values2.contains("Alice"));
    }

    @Test
    void testGetDataEntrySet() {
        Set<Map.Entry<String, String>> entries = result.getDataEntrySet(String.class);
        assertTrue(entries.stream().anyMatch(e ->
                e.getKey().equals("name") && e.getValue().equals("Alice")));

        final FXData data = result.getData();
        Set<Map.Entry<String, String>> entries2 = data.getDataEntrySet(String.class);
        assertTrue(entries2.stream().anyMatch(e ->
                e.getKey().equals("name") && e.getValue().equals("Alice")));
    }

    //TODO fixme
//    @Test
//    void testInstanceMergeWith() {
//        FXData other = FXData.builder().putString("nickname", "Ali").build();
//        FXData mergeResult = result.mergeWith(other).getData();
//
//        assertEquals("Alice", mergeResult.getDataValue(String.class, "name").get()); // original retained
//        assertEquals("Ali", mergeResult.getDataValue(String.class, "nickname").get()); // merged in
//
//        final FXData data = result.getData();
//        FXData other2 = FXData.builder().putString("nickname", "Ali").build();
//        FXData mergeResult2 = data.mergeWith(other2);
//
//        assertEquals("Alice", mergeResult2.getDataValue(String.class, "name").get()); // original retained
//        assertEquals("Ali", mergeResult2.getDataValue(String.class, "nickname").get()); // merged in
//    }

    //TODO: fixme
//    @Test
//    void testInstanceMergeKeepOriginalWith() {
//        FXData other = FXData.builder().putString("name", "Bob").putString("nickname", "Bobby").build();
//        FXData mergeResult = result.mergeKeepOriginalWith(other).getData();
//
//        assertEquals("Alice", mergeResult.getDataValue(String.class, "name").get()); // original retained
//        assertEquals("Bobby", mergeResult.getDataValue(String.class, "nickname").get()); // merged in
//
//        final FXData data = result.getData();
//        FXData other2 = FXData.builder().putString("name", "Bob").putString("nickname", "Bobby").build();
//        FXData mergeResult2 = data.mergeKeepOriginalWith(other2);
//
//        assertEquals("Alice", mergeResult2.getDataValue(String.class, "name").get()); // original retained
//        assertEquals("Bobby", mergeResult2.getDataValue(String.class, "nickname").get()); // merged in
//    }

    @Test
    void testUnsupportedTypeThrowsException() {
        class Unsupported {}

        assertTrue(result.getDataValue(Unsupported.class, "someKey").isEmpty());

        FXData fxData = FXData.builder().build();

        assertTrue(fxData.getDataValue(Unsupported.class, "someKey").isEmpty());
    }

    @Test
    void testGetButtonType() {
        assertEquals(FXButtonType.OK, result.getButtonType());
    }

    @Test
    void testNoDataConstructor() {
        final DialogResult noDataResult = new DialogResult(FXButtonType.OK);
        assertEquals(FXButtonType.OK, noDataResult.getButtonType());
        assertTrue(noDataResult.isDataEmpty(String.class));
        assertTrue(noDataResult.isDataEmpty(Integer.class));
        assertTrue(noDataResult.isDataEmpty(Float.class));
        assertTrue(noDataResult.isDataEmpty(Double.class));
        assertTrue(noDataResult.isDataEmpty(Boolean.class));
    }
}
