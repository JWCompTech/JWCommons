package com.jwcomptech.commons.utils;

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

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNull;

/**
 * Contains methods for dealing with collections.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public final class CollectionUtils {
    /**
     * Converts a map into a delimited string value.
     * A "{@literal =}" separates the keys and values and a "{@literal &}" separates the key pairs.
     * @param stringMap map to convert
     * @return string representation of map
     * @throws IllegalArgumentException if StringMap is null
     */
    public static String convertMapToString(final Map<String, String> stringMap) {
        checkArgumentNotNull(stringMap, cannotBeNull("stringMap"));
        final var sb = new StringBuilder();
        final var keySeparator = '=';
        final var pairSeparator = '&';
        //noinspection OverlyLongLambda
        stringMap.forEach((key, value) -> {
            sb.append(key);
            sb.append(keySeparator);
            sb.append(value);
            sb.append(pairSeparator);
        });
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Converts a delimited string value into a map.
     * <p>Expects that "{@literal =}" separates the keys and values and a "{@literal &}" separates the key pairs.</p>
     * @param value map to convert
     * @return string representation of map
     * @throws IllegalArgumentException if Value is null
     */
    public static Map<String, String> convertStringToMap(final String value) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        final Map<String, String> myMap;
        final var keySeparator = "=";
        final var pairSeparator = "&";
        final var pairs = value.split(pairSeparator);
        myMap = Stream.of(pairs)
                .map(p -> p.split(keySeparator))
                .collect(Collectors.toMap(keyValue -> keyValue[0], keyValue -> keyValue[1], (a, b) -> b));
        return myMap;
    }

    /**
     * Checks if an item exists in a Collection that matches the specified Predicate.
     * @param collection the Collection to check against
     * @param condition the Predicate to check
     * @param <T> the type of objects in the HashSet
     * @return true if condition is true
     * @throws IllegalArgumentException if Collection or Predicate is null
     */
    public static <T> boolean doesItemExistInCollection(final Collection<T> collection, final Predicate<T> condition) {
        checkArgumentNotNull(collection, cannotBeNull("collection"));
        checkArgumentNotNull(condition, cannotBeNull("condition"));
        return collection.parallelStream().anyMatch(condition);
    }

    /**
     * Checks if a value exists in a HashMap that matches the specified Predicate.
     * @param map the Map to check against
     * @param condition the Predicate to check
     * @param <K> the type of the Key in the Map
     * @param <V> the type of the Value in the Map
     * @return true if condition is true
     * @throws IllegalArgumentException if HashMap or Predicate is null
     */
    public static <K, V> boolean doesItemExistInMap(final Map<K, V> map, final Predicate<V> condition) {
        checkArgumentNotNull(map, cannotBeNull("map"));
        checkArgumentNotNull(condition, cannotBeNull("condition"));
        return map.values().parallelStream().anyMatch(condition);
    }

    /**
     * Returns the first item in a HashSet that matches the specified Predicate.
     * @param collection the Collection to check against
     * @param condition the Predicate to check
     * @param <T> the type of objects in the Collection
     * @return an Optional containing the matching object, Empty if not found
     * @throws IllegalArgumentException if Collection or Predicate is null
     */
    public static <T> @NotNull Optional<T> getItemInCollection(final Collection<T> collection, final Predicate<T> condition) {
        checkArgumentNotNull(collection, cannotBeNull("collection"));
        checkArgumentNotNull(condition, cannotBeNull("condition"));
        return collection.parallelStream().filter(condition).findFirst();
    }

    /**
     * Returns the items in a Collection that matches the specified Predicate.
     * @param collection the Collection to check against
     * @param condition the Predicate to evaluate
     * @param <T> the Object type that is stored in the Collection
     * @return a HashSet of items that match the Predicate
     */
    @NotNull
    public static <T> Set<T> getItemsInCollection(final Collection<T> collection,
                                                  final Predicate<T> condition) {
        checkArgumentNotNull(collection, cannotBeNull("collection"));
        checkArgumentNotNull(condition, cannotBeNull("condition"));
        return collection.parallelStream().filter(condition).collect(Collectors.toSet());
    }

    /**
     * Returns a Set of values in a Map that match the specified Predicate.
     * @param map the Map to check against
     * @param condition the Predicate to check
     * @param <K> the type of the Key in the Map
     * @param <V> the type of the Value in the Map
     * @return a Set of values matching the predicate, an empty Set if no results found
     * @throws IllegalArgumentException if Map or Predicate is null
     */
    public static <K, V> Set<V> getValuesInMap(final Map<K, V> map, final Predicate<V> condition) {
        checkArgumentNotNull(map, cannotBeNull("map"));
        checkArgumentNotNull(condition, cannotBeNull("condition"));
        return map.values().parallelStream().filter(condition).collect(Collectors.toSet());
    }

    /**
     * Returns the first item found in a Map that matches the specified Predicate.
     * @param map the Map to check against
     * @param condition the Predicate to check
     * @param <K> the type of the Key in the Map
     * @param <V> the type of the Value in the Map
     * @return an Optional containing the matching value, Empty if no results found
     * @throws IllegalArgumentException if Map or Predicate is null
     */
    public static <K, V> @NotNull Optional<V> getValueInMap(final Map<K, V> map, final Predicate<V> condition) {
        checkArgumentNotNull(map, cannotBeNull("map"));
        checkArgumentNotNull(condition, cannotBeNull("condition"));
        return map.values().parallelStream().filter(condition).findFirst();
    }

    /** Prevents instantiation of this utility class. */
    private CollectionUtils() { }
}
