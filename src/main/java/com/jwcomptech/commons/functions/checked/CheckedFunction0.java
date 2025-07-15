package com.jwcomptech.commons.functions.checked;

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

import com.jwcomptech.commons.functions.Function0;
import com.jwcomptech.commons.functions.Lazy;
import com.jwcomptech.commons.functions.Memoized;
import com.jwcomptech.commons.tuples.Tuple0;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * Represents a function with no arguments.
 *
 * @param <R> return type of the function
 * @author Daniel Dietrich
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface CheckedFunction0<R> extends Serializable {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    @Serial
    long serialVersionUID = -4706932315648107910L;

    /**
     * Returns a function that always returns the constant
     * value that you give in parameter.
     *

     * @param <R> the result type
     * @param value the value to be returned
     * @return a function always returning the given value
     */
    @Contract(pure = true)
    static <R> @NotNull CheckedFunction0<R> constant(final R value) {
        return () -> value;
    }

    /**
     * Creates a {@code CheckedFunction0} based on
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
     * @return a {@code CheckedFunction0}
     */
    static <R> CheckedFunction0<R> of(final CheckedFunction0<R> methodReference) {
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
    static <R> @NotNull Function0<Option<R>> lift(final CheckedFunction0<? extends R> partialFunction) {
        return () -> Try.<R>of(partialFunction::apply).toOption();
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
    static <R> @NotNull Function0<Try<R>> liftTry(final CheckedFunction0<? extends R> partialFunction) {
        return () -> Try.of(partialFunction::apply);
    }

    /**
     * Narrows the given {@code CheckedFunction0<? extends R>} to {@code CheckedFunction0<R>}
     *
     * @param function A {@code CheckedFunction0}
     * @param <R> return type
     * @return the given {@code f} instance as narrowed type {@code CheckedFunction0<R>}
     */
    @SuppressWarnings("unchecked")
    static <R> CheckedFunction0<R> narrow(final CheckedFunction0<? extends R> function) {
        return (CheckedFunction0<R>) function;
    }

    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply() throws Throwable;

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
    default CheckedFunction0<R> curried() {
        return this;
    }

    /**
     * Returns a tupled version of this function.
     *
     * @return a tupled function equivalent to this.
     */
    default CheckedFunction1<Tuple0, R> tupled() {
        return t -> apply();
    }

    /**
     * Returns a reversed version of this function. This may be useful in a recursive context.
     *
     * @return a reversed function equivalent to this.
     */
    default CheckedFunction0<R> reversed() {
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
    default CheckedFunction0<R> memoized() {
        if (isMemoized()) {
            return this;
        } else {
            final Lazy<R> lazy = Lazy.of(() -> {
                try {
                    return apply();
                } catch (final Throwable x) {
                    throw new RuntimeException(x);
                }
            });
            return (CheckedFunction0<R> & Memoized) () -> {
                try {
                    return lazy.get();
                } catch(final RuntimeException x) {
                    throw x.getCause();
                }
            };
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
     * Return a composed function that first applies this CheckedFunction0 to the given arguments and in case of throwable
     * try to get value from {@code recover} function with same arguments and throwable information.
     *
     * @param recover the function applied in case of throwable
     * @return a function composed of this and recover
     * @throws NullPointerException if recover is null
     */
    default Function0<R> recover(final Function<? super Throwable, ? extends Supplier<? extends R>> recover) {
        checkArgumentNotNull(recover, cannotBeNull("recover"));
        //noinspection OverlyLongLambda
        return () -> {
            try {
                return this.apply();
            } catch (final Throwable throwable) {
                final Supplier<? extends R> func = recover.apply(throwable);
                Objects.requireNonNull(func, () -> "recover return null for "
                        + throwable.getClass() + ": " + throwable.getMessage());
                return func.get();
            }
        };
    }

    /**
     * Returns an unchecked function that will <em>sneaky throw</em> if an exceptions occurs when applying the function.
     *
     * @return a new Function0 that throws a {@code Throwable}.
     */
    default Function0<R> unchecked() {
        return () -> {
            try {
                return apply();
            } catch(final Throwable t) {
                return CheckedFunction0Module.sneakyThrow(t);
            }
        };
    }

    /**
     * Returns a composed function that first applies this CheckedFunction0 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction0<V> andThen(final CheckedFunction1<? super R, ? extends V> after) {
        checkArgumentNotNull(after, cannotBeNull("after"));
        return () -> after.apply(apply());
    }

}

@SuppressWarnings("ClassNameDiffersFromFileName")
interface CheckedFunction0Module {

    // DEV-NOTE: we do not plan to expose this as public API
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(final Throwable t) throws T {
        throw (T) t;
    }
}
