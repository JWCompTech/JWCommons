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

import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNull;

/**
 * The base interface of all tuples.
 *
 * @author Daniel Dietrich
 * @since 0.0.1
 */
@SuppressWarnings({"unused", "ClassReferencesSubclass"})
public interface Tuple extends Serializable {

    @Serial
    long serialVersionUID = 1454100980309926685L;

    /**
     * The maximum arity of a Tuple.
     * <p>
     * Note: This value might be changed in a future versions.
     * So it is recommended to use this constant instead of hardcoding the current maximum arity.
     */
    int MAX_ARITY = 4;

    /**
     * Returns the number of elements of this tuple.
     *
     * @return the number of elements.
     */
    int arity();

    /**
     * Converts this tuple to a sequence.
     *
     * @return A new {@code Seq}.
     */
    Seq<?> toSeq();

    // -- factory methods

    /**
     * Creates the empty tuple.
     *
     * @return the empty tuple.
     */
    static Tuple0 empty() {
        return Tuple0.instance();
    }

    /**
     * Creates a {@code Tuple2} from a {@link Map.Entry}.
     *
     * @param <T1> Type of first component (entry key)
     * @param <T2> Type of second component (entry value)
     * @param      entry A {@link Map.Entry}
     * @return a new {@code Tuple2} containing key and value of the given {@code entry}
     * @throws IllegalArgumentException if {@code entry} is null
     */
    @Contract("_ -> new")
    static <T1, T2> @NotNull Tuple2<T1, T2> fromEntry(final Map.Entry<? extends T1, ? extends T2> entry) {
        checkArgumentNotNull(entry, cannotBeNull("entry"));
        return new Tuple2<>(entry.getKey(), entry.getValue());
    }

    /**
     * Creates a tuple of one element.
     *
     * @param <T1> type of the 1st element
     * @param t1 the 1st element
     * @return a tuple of one element.
     */
    @Contract(value = "_ -> new", pure = true)
    static <T1> @NotNull Tuple1<T1> of(final T1 t1) {
        return new Tuple1<>(t1);
    }

    /**
     * Creates a tuple of two elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @return a tuple of two elements.
     */
    @Contract(value = "_, _ -> new", pure = true)
    static <T1, T2> @NotNull Tuple2<T1, T2> of(final T1 t1, final T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    /**
     * Creates a tuple of three elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @return a tuple of three elements.
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static <T1, T2, T3> @NotNull Tuple3<T1, T2, T3> of(final T1 t1, final T2 t2, final T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }

    /**
     * Creates a tuple of 4 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @return a tuple of 4 elements.
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static <T1, T2, T3, T4> @NotNull Tuple4<T1, T2, T3, T4> of(final T1 t1, final T2 t2, final T3 t3, final T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }

    /**
     * Return the order-dependent hash of the one given value.
     *
     * @param o1 the 1st value to hash
     * @return the same result as {@link Objects#hashCode(Object)}
     */
    static int hash(final Object o1) {
        return Objects.hashCode(o1);
    }

    /**
     * Return the order-dependent hash of the two given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(final Object o1, final Object o2) {
        int result = 1;
        result = 31 * result + hash(o1);
        result = 31 * result + hash(o2);
        return result;
    }

    /**
     * Return the order-dependent hash of the three given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @param o3 the 3rd value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(final Object o1, final Object o2, final Object o3) {
        int result = hash(o1, o2);
        result = 31 * result + hash(o3);
        return result;
    }

    /**
     * Return the order-dependent hash of the 4 given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @param o3 the 3rd value to hash
     * @param o4 the 4th value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(final Object o1, final Object o2, final Object o3, final Object o4) {
        int result = hash(o1, o2, o3);
        result = 31 * result + hash(o4);
        return result;
    }

    /**
     * Narrows a widened {@code Tuple1<? extends T1>} to {@code Tuple1<T1>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple1}.
     * @param <T1> the 1st component type
     * @return the given {@code t} instance as narrowed type {@code Tuple1<T1>}.
     */
    @SuppressWarnings("unchecked")
    static <T1> Tuple1<T1> narrow(final Tuple1<? extends T1> t) {
        return (Tuple1<T1>) t;
    }

    /**
     * Narrows a widened {@code Tuple2<? extends T1, ? extends T2>} to {@code Tuple2<T1, T2>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple2}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @return the given {@code t} instance as narrowed type {@code Tuple2<T1, T2>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2> Tuple2<T1, T2> narrow(final Tuple2<? extends T1, ? extends T2> t) {
        return (Tuple2<T1, T2>) t;
    }

    /**
     * Narrows a widened {@code Tuple3<? extends T1, ? extends T2, ? extends T3>} to {@code Tuple3<T1, T2, T3>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple3}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @param <T3> the 3rd component type
     * @return the given {@code t} instance as narrowed type {@code Tuple3<T1, T2, T3>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3> Tuple3<T1, T2, T3> narrow(final Tuple3<? extends T1, ? extends T2, ? extends T3> t) {
        return (Tuple3<T1, T2, T3>) t;
    }

    /**
     * Narrows a widened {@code Tuple4<? extends T1, ? extends T2, ? extends T3, ? extends T4>} to {@code Tuple4<T1, T2, T3, T4>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple4}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @param <T3> the 3rd component type
     * @param <T4> the 4th component type
     * @return the given {@code t} instance as narrowed type {@code Tuple4<T1, T2, T3, T4>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> narrow(final Tuple4<? extends T1, ? extends T2, ? extends T3, ? extends T4> t) {
        return (Tuple4<T1, T2, T3, T4>) t;
    }

    /**
     * Turns a sequence of {@code Tuple1} into a Tuple1 of {@code Seq}.
     *
     * @param <T1> 1st component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of one {@link Seq}.
     */
    @Contract("_ -> new")
    static <T1> @NotNull Tuple1<Seq<T1>> sequence1(final Iterable<? extends Tuple1<? extends T1>> tuples) {
        checkArgumentNotNull(tuples, cannotBeNull("tuples"));
        final Stream<Tuple1<? extends T1>> stream = Stream.ofAll(tuples);
        return new Tuple1<>(stream.map(Tuple1::_1));
    }

    /**
     * Turns a sequence of {@code Tuple2} into a Tuple2 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of two {@link Seq}s.
     */
    @Contract("_ -> new")
    static <T1, T2> @NotNull Tuple2<Seq<T1>, Seq<T2>> sequence2(final Iterable<? extends Tuple2<? extends T1, ? extends T2>> tuples) {
        checkArgumentNotNull(tuples, cannotBeNull("tuples"));
        final Stream<Tuple2<? extends T1, ? extends T2>> stream = Stream.ofAll(tuples);
        return new Tuple2<>(stream.map(Tuple2::_1),
                            stream.map(Tuple2::_2));
    }

    /**
     * Turns a sequence of {@code Tuple3} into a Tuple3 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param <T3> 3rd component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of three {@link Seq}s.
     */
    @Contract("_ -> new")
    static <T1, T2, T3> @NotNull Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> sequence3(final Iterable<? extends Tuple3<? extends T1, ? extends T2, ? extends T3>> tuples) {
        checkArgumentNotNull(tuples, cannotBeNull("tuples"));
        final Stream<Tuple3<? extends T1, ? extends T2, ? extends T3>> stream = Stream.ofAll(tuples);
        return new Tuple3<>(stream.map(Tuple3::_1),
                            stream.map(Tuple3::_2),
                            stream.map(Tuple3::_3));
    }

    /**
     * Turns a sequence of {@code Tuple4} into a Tuple4 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param <T3> 3rd component type
     * @param <T4> 4th component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of 4 {@link Seq}s.
     */
    @Contract("_ -> new")
    static <T1, T2, T3, T4> @NotNull Tuple4<Seq<T1>, Seq<T2>, Seq<T3>, Seq<T4>> sequence4(final Iterable<? extends Tuple4<? extends T1, ? extends T2, ? extends T3, ? extends T4>> tuples) {
        checkArgumentNotNull(tuples, cannotBeNull("tuples"));
        final Stream<Tuple4<? extends T1, ? extends T2, ? extends T3, ? extends T4>> stream = Stream.ofAll(tuples);
        return new Tuple4<>(stream.map(Tuple4::_1),
                            stream.map(Tuple4::_2),
                            stream.map(Tuple4::_3),
                            stream.map(Tuple4::_4));
    }
}
