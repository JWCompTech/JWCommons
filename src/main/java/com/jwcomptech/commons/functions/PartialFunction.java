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

import io.vavr.Value;
import io.vavr.control.Option;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.function.Function;

/**
 * Represents a partial function T -&gt; R that is not necessarily defined for all input values of type T.
 * The caller is responsible for calling the method isDefinedAt() before this function is applied to the value.
 * <p>
 * If the function <em>is not defined</em> for a specific value, apply() may produce an arbitrary result.
 * More specifically it is not guaranteed that the function will throw an exception.
 * <p>
 * If the function <em>is defined</em> for a specific value, apply() may still throw an exception.
 *
 * @param <T> type of the function input, called <em>domain</em> of the function
 * @param <R> type of the function output, called <em>codomain</em> of the function
 * @author Daniel Dietrich
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public interface PartialFunction<T, R> extends Function1<T, R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    @Serial
    long serialVersionUID = -3362244495967905580L;

    /**
     * Unlifts a {@code totalFunction} that returns an {@code Option} result into a partial function.
     * The total function should be side effect free because it might be invoked twice: when checking if the
     * unlifted partial function is defined at a value and when applying the partial function to a value.
     *
     * @param totalFunction the function returning an {@code Option} result.
     * @param <T> type of the function input, called <em>domain</em> of the function
     * @param <R> type of the function output, called <em>codomain</em> of the function
     * @return a partial function that is not necessarily defined for all input values of type T.
     */
    @Contract(value = "_ -> new", pure = true)
    static <T, R> @NotNull PartialFunction<T, R> unlift(Function<? super T, ? extends Option<? extends R>> totalFunction) {
        //noinspection AnonymousInnerClassWithTooManyMethods
        return new PartialFunction<>() {

            @Serial
            private static final long serialVersionUID = 3011903357621044756L;

            @Override
            public R apply(T t) {
                return totalFunction.apply(t).get();
            }

            @Override
            public boolean isDefinedAt(T value) {
                return totalFunction.apply(value).isDefined();
            }

        };
    }

    /**
     * Factory method for creating a partial function that maps a given {@code Value} to its underlying value.
     * The partial function is defined for an input {@code Value} if and only if the input {@code Value} is not
     * empty. If the input {@code Value} is not empty, the partial function will return the underlying value of
     * the input {@code Value}.
     *
     * @param <T> type of the underlying value of the input {@code Value}.
     * @param <V> type of the function input, called <em>domain</em> of the function
     * @return a partial function that maps a {@code Value} to its underlying value.
     */
    @Contract(value = " -> new", pure = true)
    static <T, V extends Value<T>> @NotNull PartialFunction<V, T> getIfDefined() {
        //noinspection AnonymousInnerClassWithTooManyMethods
        return new PartialFunction<>() {

            @Serial
            private static final long serialVersionUID = -558868863387532135L;

            @Override
            public T apply(V v) {
                return v.get();
            }

            @Override
            public boolean isDefinedAt(V v) {
                return !v.isEmpty();
            }

        };
    }

    /**
     * Applies this function to the given argument and returns the result.
     *
     * @param t the argument
     * @return the result of function application
     *
     */
    R apply(T t);

    /**
     * Tests if a value is contained in the function's domain.
     *
     * @param value a potential function argument
     * @return true, if the given value is contained in the function's domain, false otherwise
     */
    boolean isDefinedAt(T value);

    /**
     * Lifts this partial function into a total function that returns an {@code Option} result.
     *
     * @return a function that applies arguments to this function and returns {@code Some(result)}
     *         if the function is defined for the given arguments, and {@code None} otherwise.
     */
    default Function1<T, Option<R>> lift() {
        return t -> Option.when(isDefinedAt(t), () -> apply(t));
    }

}
