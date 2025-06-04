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

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Optional;

/**
 * Provides immutable access to a {@link Enum}.
 *
 * @param <T> the enum type to wrap
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public class EnumValue<T extends Enum<T>> extends ObservableValue<T, EnumValue<T>> {

    /**
     * Required for serialization support.
     *
     * @see Serializable
     */
    @Serial
    private static final long serialVersionUID = -4700293867509243861L;

    public EnumValue(final T value) {
        super(value);
        super.setListenersTarget(this);
    }

    /**
     * Returns the name of this enum constant, exactly as declared in its
     * enum declaration.
     * <b>Most programmers should use the {@link #toString} method in
     * preference to this one, as the toString method may return
     * a more user-friendly name.</b>  This method is designed primarily for
     * use in specialized situations where correctness depends on getting the
     * exact name, which will not vary from release to release.
     *
     * @return the name of this enum constant
     */
    public String name() {
        return value.name();
    }

    /**
     * Returns the ordinal of this enumeration constant (its position
     * in its enum declaration, where the initial constant is assigned
     * an ordinal of zero).
     * <p>
     * Most programmers will have no use for this method.  It is
     * designed for use by sophisticated enum-based data structures, such
     * as {@link EnumSet} and {@link EnumMap}.
     *
     * @return the ordinal of this enumeration constant
     */
    public int ordinal() {
        return value.ordinal();
    }

    /**
     * Returns an enum descriptor {@code EnumDesc} for this instance, if one can be
     * constructed, or an empty {@link Optional} if one cannot be.
     *
     * @return An {@link Optional} containing the resulting nominal descriptor,
     * or an empty {@link Optional} if one cannot be constructed.
     */
    public Optional<Enum.EnumDesc<T>> describeConstable() {
        return value.describeConstable();
    }

    /**
     * Returns the Class object corresponding to this enum constant's
     * enum type.  Two enum constants e1 and  e2 are of the
     * same enum type if and only if
     * e1.getDeclaringClass() == e2.getDeclaringClass().
     * (The value returned by this method may differ from the one returned
     * by the {@link Object#getClass} method for enum constants with
     * constant-specific class bodies.)
     *
     * @return the Class object corresponding to this enum constant's
     * enum type
     */
    public Class<T> getDeclaringClass() {
        return value.getDeclaringClass();
    }

    /**
     * Compares this enum with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * Enum constants are only comparable to other enum constants of the
     * same enum type.  The natural order implemented by this
     * method is the order in which the constants are declared.
     *
     * @param other the value to compare
     */
    public int compareTo(@NotNull final T other) {
        return value.compareTo(other);
    }

    @Override
    public int compareTo(@NotNull final Value<T, EnumValue<T>> other) {
        return this.compareTo(other.get());
    }

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum class should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    public String toString() {
        return value.toString();
    }

    /**
     * Returns true if the specified object is equal to this
     * enum constant.
     *
     * @param obj the object to be compared for equality with this enum constant.
     * @return true if the specified object is equal to this
     * enum constant.
     */
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    public boolean equals(final Object obj) {
        return value == obj;
    }

    /**
     * Returns true if the specified enum is equal to this
     * enum constant.
     *
     * @param other the enum to be compared for equality with this enum constant.
     * @return true if the specified enum constant is equal to this
     * enum constant.
     */
    public boolean equals(final T other) {
        return value == other;
    }

    /**
     * Returns a hash code for this enum constant.
     *
     * @return a hash code for this enum constant.
     */
    public int hashCode() {
        return value.hashCode();
    }
}
