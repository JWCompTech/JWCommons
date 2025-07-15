package com.jwcomptech.commons.tuples;

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

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * A tuple of one element which can be seen as cartesian product of one component.
 *
 * @param <T1> type of the 1st element
 * @author Daniel Dietrich
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
public final class Tuple1<T1> implements Tuple, Comparable<Tuple1<T1>>, Serializable {

    @Serial
    private static final long serialVersionUID = 4967943714372852114L;

    /**
     * The 1st element of this tuple.
     */
    // Conditionally serializable
    private final T1 _1;

    /**
     * Constructs a tuple of one element.
     *
     * @param t1 the 1st element
     */
    public Tuple1(final T1 t1) {
        this._1 = t1;
    }

    @Contract(pure = true)
    public static <T1> @NotNull Comparator<Tuple1<T1>> comparator(final Comparator<? super T1> t1Comp) {
        return (Comparator<Tuple1<T1>> & Serializable) (t1, t2) -> {
            return t1Comp.compare(t1._1, t2._1);
            // all components are equal
        };
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>> int compareTo(final Tuple1<?> o1, final Tuple1<?> o2) {
        final Tuple1<U1> t1 = (Tuple1<U1>) o1;
        final Tuple1<U1> t2 = (Tuple1<U1>) o2;

        return t1._1.compareTo(t2._1);
        // all components are equal
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public int compareTo(@NotNull final Tuple1<T1> that) {
        return Tuple1.compareTo(this, that);
    }

    /**
     * Getter of the 1st element of this tuple.
     *
     * @return the 1st element of this Tuple.
     */
    public T1 _1() {
        return _1;
    }

    /**
     * Sets the 1st element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 1st element of this Tuple.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Tuple1<T1> update1(final T1 value) {
        return new Tuple1<>(value);
    }

    /**
     * Maps the components of this tuple using a mapper function.
     *
     * @param mapper the mapper function
     * @param <U1> new type of the 1st component
     * @return A new Tuple of same arity.
     * @throws IllegalArgumentException if {@code mapper} is null
     */
    public <U1> @NotNull Tuple1<U1> map(final Function<? super T1, ? extends U1> mapper) {
        checkArgumentNotNull(mapper, cannotBeNull("mapper"));
        return Tuple.of(mapper.apply(_1));
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param function Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws IllegalArgumentException if {@code f} is null
     */
    public <U> U apply(final Function<? super T1, ? extends U> function) {
        checkArgumentNotNull(function, cannotBeNull("function"));
        return function.apply(_1);
    }

    @Contract(" -> new")
    @Override
    public @NotNull Seq<?> toSeq() {
        return List.of(_1);
    }

    /**
     * Append a value to this tuple.
     *
     * @param <T2> type of the value to append
     * @param t2 the value to append
     * @return a new Tuple with the value appended
     */
    @Contract(value = "_ -> new", pure = true)
    public <T2> @NotNull Tuple2<T1, T2> append(final T2 t2) {
        return Tuple.of(_1, t2);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2> the type of the 2nd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws IllegalArgumentException if {@code tuple} is null
     */
    public <T2> @NotNull Tuple2<T1, T2> concat(final Tuple1<T2> tuple) {
        checkArgumentNotNull(tuple, cannotBeNull("tuple"));
        return Tuple.of(_1, tuple._1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws IllegalArgumentException if {@code tuple} is null
     */
    public <T2, T3> @NotNull Tuple3<T1, T2, T3> concat(final Tuple2<T2, T3> tuple) {
        checkArgumentNotNull(tuple, cannotBeNull("tuple"));
        return Tuple.of(_1, tuple._1(), tuple._2());
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param <T4> the type of the 4th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws IllegalArgumentException if {@code tuple} is null
     */
    public <T2, T3, T4> @NotNull Tuple4<T1, T2, T3, T4> concat(final Tuple3<T2, T3, T4> tuple) {
        checkArgumentNotNull(tuple, cannotBeNull("tuple"));
        return Tuple.of(_1, tuple._1(), tuple._2(), tuple._3());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof final Tuple1<?> that)) {
            return false;
        } else {
            return Objects.equals(this._1, that._1);
        }
    }

    @Override
    public int hashCode() {
        return Tuple.hash(_1);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "(" + _1 + ")";
    }
}
