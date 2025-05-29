package com.jwcomptech.commons.functions.tuples;

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
import java.util.function.Supplier;

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNull;

/**
 * A tuple of no elements which can be seen as cartesian product of no components.
 *
 * @author Daniel Dietrich
 * @since 0.0.1
 */
@SuppressWarnings("EqualsAndHashcode")
public final class Tuple0 implements Tuple, Comparable<Tuple0>, Serializable {

    @Serial
    private static final long serialVersionUID = -618549158980869821L;

    /**
     * The singleton instance of Tuple0.
     */
    private static final Tuple0 INSTANCE = new Tuple0 ();

    /**
     * The singleton Tuple0 comparator.
     */
    private static final Comparator<Tuple0> COMPARATOR = (Comparator<Tuple0> & Serializable) (t1, t2) -> 0;

    // hidden constructor, internally called
    private Tuple0 () {
    }

    /**
     * Returns the singleton instance of Tuple0.
     *
     * @return The singleton instance of Tuple0.
     */
    public static Tuple0 instance() {
        return INSTANCE;
    }

    public static  Comparator<Tuple0> comparator() {
        return COMPARATOR;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public int compareTo(@NotNull Tuple0 that) {
        return 0;
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws IllegalArgumentException if {@code f} is null
     */
    public <U> U apply(Supplier<? extends U> f) {
        checkArgumentNotNull(f, cannotBeNull("f"));
        return f.get();
    }

    @Override
    public Seq<?> toSeq() {
        return List.empty();
    }

    /**
     * Append a value to this tuple.
     *
     * @param <T1> type of the value to append
     * @param t1 the value to append
     * @return a new Tuple with the value appended
     */
    @Contract(value = "_ -> new", pure = true)
    public <T1> @NotNull Tuple1<T1> append(T1 t1) {
        return Tuple.of(t1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws IllegalArgumentException if {@code tuple} is null
     */
    public <T1> @NotNull Tuple1<T1> concat(Tuple1<T1> tuple) {
        checkArgumentNotNull(tuple, cannotBeNull("tuple"));
        return Tuple.of(tuple._1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws IllegalArgumentException if {@code tuple} is null
     */
    public <T1, T2> @NotNull Tuple2<T1, T2> concat(Tuple2<T1, T2> tuple) {
        checkArgumentNotNull(tuple, cannotBeNull("tuple"));
        return Tuple.of(tuple._1, tuple._2);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws IllegalArgumentException if {@code tuple} is null
     */
    public <T1, T2, T3> @NotNull Tuple3<T1, T2, T3> concat(Tuple3<T1, T2, T3> tuple) {
        checkArgumentNotNull(tuple, cannotBeNull("tuple"));
        return Tuple.of(tuple._1, tuple._2, tuple._3);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param <T4> the type of the 4th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws IllegalArgumentException if {@code tuple} is null
     */
    public <T1, T2, T3, T4> @NotNull Tuple4<T1, T2, T3, T4> concat(Tuple4<T1, T2, T3, T4> tuple) {
        checkArgumentNotNull(tuple, cannotBeNull("tuple"));
        return Tuple.of(tuple._1, tuple._2, tuple._3, tuple._4);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "()";
    }

    // -- Serializable implementation

    /**
     * Instance control for object serialization.
     *
     * @return The singleton instance of Tuple0.
     * @see Serializable
     */
    @Serial
    private Object readResolve() {
        return INSTANCE;
    }
}
