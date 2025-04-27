package com.jwcomptech.shared.values;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Optional;

@SuppressWarnings("unused")
public class EnumValue<T extends Enum<T>> extends ObservableValue<T, EnumValue<T>> {

    /**
     * Required for serialization support.
     *
     * @see Serializable
     */
    @Serial
    private static final long serialVersionUID = -4700293867509243861L;

    public EnumValue(T value) {
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
    @Override
    public int compareTo(@NotNull T other) {
        return value.compareTo(other);
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
     * @param other the object to be compared for equality with this object.
     * @return true if the specified object is equal to this
     * enum constant.
     */
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    public boolean equals(Object other) {
        return value.equals(other);
    }

    public boolean equals(T other) {
        return value.equals(other);
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
