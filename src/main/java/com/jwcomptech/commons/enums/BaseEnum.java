package com.jwcomptech.commons.enums;

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

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Base interface for enums that associate a value of type {@code T} with each constant.
 * <p>
 * This interface enables type-safe, reusable lookup and comparison logic
 * across enums with backing values.
 *
 * @param <T> the value type associated with the enum constant
 *
 * @apiNote When using Lombok's @{@link Getter} annotation with this interface,
 * the value field should be an object type (e.g. {@code Integer} instead of {@code int})
 * to comply with the generic type constraint.
 */
public interface BaseEnum<T> {
    T getValue();

    /**
     * Finds an enum constant of the given enum type by its associated value.
     *
     * @param enumType the class of the enum
     * @param value    the value to match
     * @param <E>      the enum type
     * @param <T>      the value type
     * @return an Optional containing the matching enum, or empty if none match
     */
    static <E extends Enum<E> & BaseEnum<T>, T> Optional<E> getByValue(@NotNull Class<E> enumType, T value) {
        for (E e : enumType.getEnumConstants()) {
            if (Objects.equals(e.getValue(), value)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
