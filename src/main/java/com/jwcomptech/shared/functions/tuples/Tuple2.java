package com.jwcomptech.shared.functions.tuples;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jwcomptech.shared.Literals.cannotBeNull;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;

/**
 * A tuple of two elements which can be seen as cartesian product of two components.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @author Daniel Dietrich
 */
public final class Tuple2<T1, T2> implements Tuple, Comparable<Tuple2<T1, T2>>, Serializable {

    @Serial
    private static final long serialVersionUID = -2196122318421534117L;

    /**
     * The 1st element of this tuple.
     */
    // Conditionally serializable
    public final T1 _1;

    /**
     * The 2nd element of this tuple.
     */
    // Conditionally serializable
    public final T2 _2;

    /**
     * Constructs a tuple of two elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     */
    public Tuple2(T1 t1, T2 t2) {
        this._1 = t1;
        this._2 = t2;
    }

    @Contract(pure = true)
    public static <T1, T2> @NotNull Comparator<Tuple2<T1, T2>> comparator(Comparator<? super T1> t1Comp, Comparator<? super T2> t2Comp) {
        return (Comparator<Tuple2<T1, T2>> & Serializable) (t1, t2) -> {
            final int check1 = t1Comp.compare(t1._1, t2._1);
            if (check1 != 0) {
                return check1;
            }

            return t2Comp.compare(t1._2, t2._2);
            // all components are equal
        };
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>,
            U2 extends Comparable<? super U2>> int compareTo(Tuple2<?, ?> o1, Tuple2<?, ?> o2) {
        final Tuple2<U1, U2> t1 = (Tuple2<U1, U2>) o1;
        final Tuple2<U1, U2> t2 = (Tuple2<U1, U2>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        return t1._2.compareTo(t2._2);
        // all components are equal
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public int compareTo(@NotNull Tuple2<T1, T2> that) {
        return Tuple2.compareTo(this, that);
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
    public @NotNull Tuple2<T1, T2> update1(T1 value) {
        return new Tuple2<>(value, _2);
    }

    /**
     * Getter of the 2nd element of this tuple.
     *
     * @return the 2nd element of this Tuple.
     */
    public T2 _2() {
        return _2;
    }

    /**
     * Sets the 2nd element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 2nd element of this Tuple.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Tuple2<T1, T2> update2(T2 value) {
        return new Tuple2<>(_1, value);
    }

    /**
     * Swaps the elements of this {@code Tuple}.
     *
     * @return A new Tuple where the first element is the second element of this Tuple
     *   and the second element is the first element of this Tuple.
     */
    @Contract(value = " -> new", pure = true)
    public @NotNull Tuple2<T2, T1> swap() {
        return Tuple.of(_2, _1);
    }

    /**
     * Converts the tuple to java.util.Map.Entry {@code Tuple}.
     *
     * @return A  java.util.Map.Entry where the first element is the key and the second
     * element is the value.
     */
    @Contract(value = " -> new", pure = true)
    public Map.@NotNull Entry<T1, T2> toEntry() {
        return new AbstractMap.SimpleEntry<>(_1, _2);
    }

    /**
     * Maps the components of this tuple using a mapper function.
     *
     * @param mapper the mapper function
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @return A new Tuple of same arity.
     * @throws IllegalArgumentException if {@code mapper} is null
     */
    public <U1, U2> Tuple2<U1, U2> map(BiFunction<? super T1, ? super T2, Tuple2<U1, U2>> mapper) {
        checkArgumentNotNull(mapper, cannotBeNull("mapper"));
        return mapper.apply(_1, _2);
    }

    /**
     * Maps the components of this tuple using a mapper function for each component.
     *
     * @param f1 the mapper function of the 1st component
     * @param f2 the mapper function of the 2nd component
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @return A new Tuple of same arity.
     * @throws IllegalArgumentException if one of the arguments is null
     */
    public <U1, U2> @NotNull Tuple2<U1, U2> map(Function<? super T1, ? extends U1> f1,
                                                Function<? super T2, ? extends U2> f2) {
        checkArgumentNotNull(f1, cannotBeNull("f1"));
        checkArgumentNotNull(f2, cannotBeNull("f2"));
        return Tuple.of(f1.apply(_1), f2.apply(_2));
    }

    /**
     * Maps the 1st component of this tuple to a new value.
     *
     * @param <U> new type of the 1st component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 1st component
     */
    public <U> @NotNull Tuple2<U, T2> map1(Function<? super T1, ? extends U> mapper) {
        checkArgumentNotNull(mapper, cannotBeNull("mapper"));
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2);
    }

    /**
     * Maps the 2nd component of this tuple to a new value.
     *
     * @param <U> new type of the 2nd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 2nd component
     */
    public <U> @NotNull Tuple2<T1, U> map2(Function<? super T2, ? extends U> mapper) {
        checkArgumentNotNull(mapper, cannotBeNull("mapper"));
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u);
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U apply(BiFunction<? super T1, ? super T2, ? extends U> f) {
        checkArgumentNotNull(f, cannotBeNull("f"));
        return f.apply(_1, _2);
    }

    @Override
    public Seq<?> toSeq() {
        return List.of(_1, _2);
    }

    /**
     * Append a value to this tuple.
     *
     * @param <T3> type of the value to append
     * @param t3 the value to append
     * @return a new Tuple with the value appended
     */
    @Contract(value = "_ -> new", pure = true)
    public <T3> @NotNull Tuple3<T1, T2, T3> append(T3 t3) {
        return Tuple.of(_1, _2, t3);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T3> the type of the 3rd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws IllegalArgumentException if {@code tuple} is null
     */
    public <T3> @NotNull Tuple3<T1, T2, T3> concat(Tuple1<T3> tuple) {
        checkArgumentNotNull(tuple, cannotBeNull("tuple"));
        return Tuple.of(_1, _2, tuple._1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T3> the type of the 3rd value in the tuple
     * @param <T4> the type of the 4th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws IllegalArgumentException if {@code tuple} is null
     */
    public <T3, T4> @NotNull Tuple4<T1, T2, T3, T4> concat(Tuple2<T3, T4> tuple) {
        checkArgumentNotNull(tuple, cannotBeNull("tuple"));
        return Tuple.of(_1, _2, tuple._1, tuple._2);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple2<?, ?> that)) {
            return false;
        } else {
            return Objects.equals(this._1, that._1)
                  && Objects.equals(this._2, that._2);
        }
    }

    @Override
    public int hashCode() {
        return Tuple.hash(_1, _2);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }
}