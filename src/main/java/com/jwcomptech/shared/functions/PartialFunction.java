package com.jwcomptech.shared.functions;

import io.vavr.Value;
import io.vavr.control.Option;

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
 */
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
    static <T, R> PartialFunction<T, R> unlift(Function<? super T, ? extends Option<? extends R>> totalFunction) {
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
    static <T, V extends Value<T>> PartialFunction<V, T> getIfDefined() {
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
