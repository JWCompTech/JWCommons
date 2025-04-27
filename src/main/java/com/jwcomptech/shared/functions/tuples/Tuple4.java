package com.jwcomptech.shared.functions.tuples;

import com.jwcomptech.shared.functions.Function4;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

import static com.jwcomptech.shared.Literals.cannotBeNull;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;

/**
 * A tuple of 4 elements which can be seen as cartesian product of 4 components.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @author Daniel Dietrich
 */
@SuppressWarnings({"ClassWithTooManyMethods", "unused"})
public final class Tuple4<T1, T2, T3, T4> implements Tuple, Comparable<Tuple4<T1, T2, T3, T4>>, Serializable {

    @Serial
    private static final long serialVersionUID = -1033984603908838481L;

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
     * The 3rd element of this tuple.
     */
    // Conditionally serializable
    public final T3 _3;

    /**
     * The 4th element of this tuple.
     */
    // Conditionally serializable
    public final T4 _4;

    /**
     * Constructs a tuple of 4 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     */
    public Tuple4(T1 t1, T2 t2, T3 t3, T4 t4) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
    }

    @Contract(pure = true)
    public static <T1, T2, T3, T4> @NotNull Comparator<Tuple4<T1, T2, T3, T4>> comparator(Comparator<? super T1> t1Comp,
                                                                                          Comparator<? super T2> t2Comp,
                                                                                          Comparator<? super T3> t3Comp,
                                                                                          Comparator<? super T4> t4Comp) {
        //noinspection OverlyLongLambda
        return (Comparator<Tuple4<T1, T2, T3, T4>> & Serializable) (t1, t2) -> {
            final int check1 = t1Comp.compare(t1._1, t2._1);
            if (0 != check1) {
                return check1;
            }

            final int check2 = t2Comp.compare(t1._2, t2._2);
            if (0 != check2) {
                return check2;
            }

            final int check3 = t3Comp.compare(t1._3, t2._3);
            if (0 != check3) {
                return check3;
            }

            return t4Comp.compare(t1._4, t2._4);

            // all components are equal
        };
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>,
            U2 extends Comparable<? super U2>,
            U3 extends Comparable<? super U3>,
            U4 extends Comparable<? super U4>> int compareTo(Tuple4<?, ?, ?, ?> o1, Tuple4<?, ?, ?, ?> o2) {
        final Tuple4<U1, U2, U3, U4> t1 = (Tuple4<U1, U2, U3, U4>) o1;
        final Tuple4<U1, U2, U3, U4> t2 = (Tuple4<U1, U2, U3, U4>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (0 != check1) {
            return check1;
        }

        final int check2 = t1._2.compareTo(t2._2);
        if (0 != check2) {
            return check2;
        }

        final int check3 = t1._3.compareTo(t2._3);
        if (0 != check3) {
            return check3;
        }

        return t1._4.compareTo(t2._4);

        // all components are equal
    }

    @Override
    public int arity() {
        return 4;
    }

    @Override
    public int compareTo(@NotNull Tuple4<T1, T2, T3, T4> that) {
        return Tuple4.compareTo(this, that);
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
    public @NotNull Tuple4<T1, T2, T3, T4> update1(T1 value) {
        return new Tuple4<>(value, _2, _3, _4);
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
    public @NotNull Tuple4<T1, T2, T3, T4> update2(T2 value) {
        return new Tuple4<>(_1, value, _3, _4);
    }

    /**
     * Getter of the 3rd element of this tuple.
     *
     * @return the 3rd element of this Tuple.
     */
    public T3 _3() {
        return _3;
    }

    /**
     * Sets the 3rd element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 3rd element of this Tuple.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Tuple4<T1, T2, T3, T4> update3(T3 value) {
        return new Tuple4<>(_1, _2, value, _4);
    }

    /**
     * Getter of the 4th element of this tuple.
     *
     * @return the 4th element of this Tuple.
     */
    public T4 _4() {
        return _4;
    }

    /**
     * Sets the 4th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 4th element of this Tuple.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Tuple4<T1, T2, T3, T4> update4(T4 value) {
        return new Tuple4<>(_1, _2, _3, value);
    }

    /**
     * Maps the components of this tuple using a mapper function.
     *
     * @param mapper the mapper function
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @param <U3> new type of the 3rd component
     * @param <U4> new type of the 4th component
     * @return A new Tuple of same arity.
     * @throws IllegalArgumentException if {@code mapper} is null
     */
    public <U1, U2, U3, U4> Tuple4<U1, U2, U3, U4> map(
            io.vavr.Function4<? super T1,? super T2, ? super T3, ? super T4, Tuple4<U1, U2, U3, U4>> mapper) {
        checkArgumentNotNull(mapper, cannotBeNull("mapper"));
        return mapper.apply(_1, _2, _3, _4);
    }

    /**
     * Maps the components of this tuple using a mapper function for each component.
     *
     * @param f1 the mapper function of the 1st component
     * @param f2 the mapper function of the 2nd component
     * @param f3 the mapper function of the 3rd component
     * @param f4 the mapper function of the 4th component
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @param <U3> new type of the 3rd component
     * @param <U4> new type of the 4th component
     * @return A new Tuple of same arity.
     * @throws IllegalArgumentException if one of the arguments is null
     */
    public <U1, U2, U3, U4> @NotNull Tuple4<U1, U2, U3, U4> map(Function<? super T1, ? extends U1> f1,
                                                                Function<? super T2, ? extends U2> f2,
                                                                Function<? super T3, ? extends U3> f3,
                                                                Function<? super T4, ? extends U4> f4) {
        checkArgumentNotNull(f1, cannotBeNull("f1"));
        checkArgumentNotNull(f2, cannotBeNull("f2"));
        checkArgumentNotNull(f3, cannotBeNull("f3"));
        checkArgumentNotNull(f4, cannotBeNull("f4"));
        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3), f4.apply(_4));
    }

    /**
     * Maps the 1st component of this tuple to a new value.
     *
     * @param <U> new type of the 1st component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 1st component
     */
    public <U> @NotNull Tuple4<U, T2, T3, T4> map1(Function<? super T1, ? extends U> mapper) {
        checkArgumentNotNull(mapper, cannotBeNull("mapper"));
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2, _3, _4);
    }

    /**
     * Maps the 2nd component of this tuple to a new value.
     *
     * @param <U> new type of the 2nd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 2nd component
     */
    public <U> @NotNull Tuple4<T1, U, T3, T4> map2(Function<? super T2, ? extends U> mapper) {
        checkArgumentNotNull(mapper, cannotBeNull("mapper"));
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u, _3, _4);
    }

    /**
     * Maps the 3rd component of this tuple to a new value.
     *
     * @param <U> new type of the 3rd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 3rd component
     */
    public <U> @NotNull Tuple4<T1, T2, U, T4> map3(Function<? super T3, ? extends U> mapper) {
        checkArgumentNotNull(mapper, cannotBeNull("mapper"));
        final U u = mapper.apply(_3);
        return Tuple.of(_1, _2, u, _4);
    }

    /**
     * Maps the 4th component of this tuple to a new value.
     *
     * @param <U> new type of the 4th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 4th component
     */
    public <U> @NotNull Tuple4<T1, T2, T3, U> map4(Function<? super T4, ? extends U> mapper) {
        checkArgumentNotNull(mapper, cannotBeNull("mapper"));
        final U u = mapper.apply(_4);
        return Tuple.of(_1, _2, _3, u);
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws IllegalArgumentException if {@code f} is null
     */
    public <U> U apply(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends U> f) {
        checkArgumentNotNull(f, cannotBeNull("f"));
        return f.apply(_1, _2, _3, _4);
    }

    @Override
    public Seq<?> toSeq() {
        return List.of(_1, _2, _3, _4);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple4<?, ?, ?, ?> that)) {
            return false;
        } else {
            return Objects.equals(this._1, that._1)
                  && Objects.equals(this._2, that._2)
                  && Objects.equals(this._3, that._3)
                  && Objects.equals(this._4, that._4);
        }
    }

    @Override
    public int hashCode() {
        return Tuple.hash(_1, _2, _3, _4);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
    }
}