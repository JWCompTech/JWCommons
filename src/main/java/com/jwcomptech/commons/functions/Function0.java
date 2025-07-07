package com.jwcomptech.commons.functions;

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

import com.jwcomptech.commons.tuples.Tuple0;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * Represents a function with no arguments.
 *
 * @param <R> return type of the function
 * @author Daniel Dietrich
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Function0<R> extends Serializable, Supplier<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    @Serial
    long serialVersionUID = -6791645502091074801L;

    /**
     * Returns a function that always returns the constant
     * value that you give in parameter.
     *

     * @param <R> the result type
     * @param value the value to be returned
     * @return a function always returning the given value
     */
    @Contract(pure = true)
    static <R> @NotNull Function0<R> constant(final R value) {
        return () -> value;
    }

    /**
     * Creates a {@code Function0} based on
     * <ul>
     * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method reference</a></li>
     * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#syntax">lambda expression</a></li>
     * </ul>
     *
     * Examples (w.l.o.g. referring to Function1):
     * <pre><code>// using a lambda expression
     * Function1&lt;Integer, Integer&gt; add1 = Function1.of(i -&gt; i + 1);
     *
     * // using a method reference (, e.g. Integer method(Integer i) { return i + 1; })
     * Function1&lt;Integer, Integer&gt; add2 = Function1.of(this::method);
     *
     * // using a lambda reference
     * Function1&lt;Integer, Integer&gt; add3 = Function1.of(add1::apply);
     * </code></pre>
     * <p>
     * <strong>Caution:</strong> Reflection loses type information of lambda references.
     * <pre><code>// type of a lambda expression
     * Type&lt;?, ?&gt; type1 = add1.getType(); // (Integer) -&gt; Integer
     *
     * // type of a method reference
     * Type&lt;?, ?&gt; type2 = add2.getType(); // (Integer) -&gt; Integer
     *
     * // type of a lambda reference
     * Type&lt;?, ?&gt; type3 = add3.getType(); // (Object) -&gt; Object
     * </code></pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <R> return type
     * @return a {@code Function0}
     */
    static <R> Function0<R> of(final Function0<R> methodReference) {
        return methodReference;
    }

    /**
     * Lifts the given {@code partialFunction} into a total function that returns an {@code Option} result.
     *
     * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
     * @param <R> return type
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Some(result)}
     *         if the function is defined for the given arguments, and {@code None} otherwise.
     */
    @Contract(pure = true)
    static <R> @NotNull Function0<Option<R>> lift(final Supplier<? extends R> partialFunction) {
        return () -> Try.<R>of(partialFunction::get).toOption();
    }

    /**
     * Lifts the given {@code partialFunction} into a total function that returns an {@code Try} result.
     *
     * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
     * @param <R> return type
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Success(result)}
     *         if the function is defined for the given arguments, and {@code Failure(throwable)} otherwise.
     */
    @Contract(pure = true)
    static <R> @NotNull Function0<Try<R>> liftTry(final Supplier<? extends R> partialFunction) {
        return () -> Try.of(partialFunction::get);
    }

    /**
     * Narrows the given {@code Function0<? extends R>} to {@code Function0<R>}
     *
     * @param function A {@code Function0}
     * @param <R> return type
     * @return the given {@code f} instance as narrowed type {@code Function0<R>}
     */
    @SuppressWarnings("unchecked")
    static <R> Function0<R> narrow(final Function0<? extends R> function) {
        return (Function0<R>) function;
    }

    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     * 
     */
    R apply();

    /**
     * Implementation of {@linkplain Supplier#get()}, just calls {@linkplain #apply()}.
     *
     * @return the result of {@code apply()}
     */
    @SuppressWarnings("UnnecessaryJavaDocLink")
    @Override
    default R get() {
        return apply();
    }

    /**
     * Returns the number of function arguments.
     * @return an int value &gt;= 0
     * @see <a href="http://en.wikipedia.org/wiki/Arity">Arity</a>
     */
    @SuppressWarnings("SameReturnValue")
    default int arity() {
        return 0;
    }

    /**
     * Returns a curried version of this function.
     *
     * @return a curried function equivalent to this.
     */
    default Function0<R> curried() {
        return this;
    }

    /**
     * Returns a tupled version of this function.
     *
     * @return a tupled function equivalent to this.
     */
    default Function1<Tuple0, R> tupled() {
        return t -> apply();
    }

    /**
     * Returns a reversed version of this function. This may be useful in a recursive context.
     *
     * @return a reversed function equivalent to this.
     */
    default Function0<R> reversed() {
        return this;
    }

    /**
     * Returns a memoizing version of this function, which computes the return value for given arguments only one time.
     * On subsequent calls given the same arguments the memoized value is returned.
     * <p>
     * Please note that memoizing functions do not permit {@code null} as single argument or return value.
     *
     * @return a memoizing function equivalent to this.
     */
    default Function0<R> memoized() {
        if (isMemoized()) {
            return this;
        } else {
            return (Function0<R> & Memoized) Lazy.of(this)::get;
        }
    }

    /**
     * Checks if this function is memoizing (= caching) computed values.
     *
     * @return true, if this function is memoizing, false otherwise
     */
    default boolean isMemoized() {
        //noinspection InstanceofThis
        return this instanceof Memoized;
    }

    /**
     * Returns a composed function that first applies this Function0 to the given argument and then applies
     * {@linkplain Function} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws IllegalArgumentException if {@code after} is null
     */
    default <V> Function0<V> andThen(final Function<? super R, ? extends V> after) {
        checkArgumentNotNull(after, cannotBeNull("after"));
        return () -> after.apply(apply());
    }
}
