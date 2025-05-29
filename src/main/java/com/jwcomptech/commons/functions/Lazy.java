/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import io.vavr.collection.Iterator;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNull;

/**
 * Represents a lazy evaluated value. Compared to a Supplier, Lazy is memoizing, i.e. it evaluates only once and
 * therefore is referential transparent.
 *
 * <pre>
 * <code>
 * final Lazy&lt;Double&gt; l = Lazy.of(Math::random);
 * l.isEvaluated(); // = false
 * l.get();         // = 0.123 (random generated)
 * l.isEvaluated(); // = true
 * l.get();         // = 0.123 (memoized)
 * </code>
 * </pre>
 *
 * Example of creating a <em>real</em> lazy value (works only with interfaces):
 *
 * <pre><code>final CharSequence chars = Lazy.val(() -&gt; "Yay!", CharSequence.class);</code></pre>
 *
 * @author Daniel Dietrich
 * @since 0.0.1
 */
// DEV-NOTE: No flatMap and orElse because this more like a Functor than a Monad.
//           It represents a value rather than capturing a specific state.
@SuppressWarnings({"ClassWithTooManyMethods", "unused"})
public final class Lazy<T> implements Value<T>, Supplier<T>, Serializable {

    @Serial
    private static final long serialVersionUID = 4567338503128116754L;
    @SuppressWarnings("FieldNotUsedInToString")
    private final ReentrantLock lock = new ReentrantLock();

    // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
    @SuppressWarnings("FieldNotUsedInToString")
    private transient volatile Supplier<? extends T> supplier;

    private T value; // will behave as a volatile in reality, because a supplier volatile read will update all fields (see https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#volatile)

    // should not be called directly
    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Narrows a widened {@code Lazy<? extends T>} to {@code Lazy<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param lazy A {@code Lazy}.
     * @param <T>  Component type of the {@code Lazy}.
     * @return the given {@code lazy} instance as narrowed type {@code Lazy<T>}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Lazy<T> narrow(Lazy<? extends T> lazy) {
        return (Lazy<T>) lazy;
    }

    /**
     * Creates a {@code Lazy} that requests its value from a given {@code Supplier}. The supplier is asked only once,
     * the value is memoized.
     *
     * @param <T>      type of the lazy value
     * @param supplier A supplier
     * @return A new instance of Lazy
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull Lazy<T> of(@NotNull Supplier<? extends T> supplier) {
        checkArgumentNotNull(supplier, cannotBeNull("supplier"));
        if (supplier instanceof Lazy) {
            return (Lazy<T>) supplier;
        } else {
            return new Lazy<>(supplier);
        }
    }

    /**
     * Reduces many {@code Lazy} values into a single {@code Lazy} by transforming an
     * {@code Iterable<Lazy<? extends T>>} into a {@code Lazy<Seq<T>>}.
     *
     * @param <T>    Type of the lazy values.
     * @param values An iterable of lazy values.
     * @return A lazy sequence of values.
     * @throws NullPointerException if values is null
     */
    @SuppressWarnings("Convert2MethodRef") // TODO should be fixed in JDK 9 and Idea
    public static <T> @NotNull Lazy<Seq<T>> sequence(Iterable<? extends Lazy<? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        return Lazy.of(() -> Vector.ofAll(values).map(lazy -> lazy.get()));
    }

    /**
     * Creates a real _lazy value_ of type {@code T}, backed by a {@linkplain Proxy} which delegates
     * to a {@code Lazy} instance.
     *
     * @param supplier A supplier
     * @param type     An interface
     * @param <T>      type of the lazy value
     * @return A new instance of T
     * @throws IllegalArgumentException if either {@code supplier} or {@code type} are null
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull T val(Supplier<? extends T> supplier, Class<T> type) {
        checkArgumentNotNull(supplier, cannotBeNull("supplier"));
        checkArgumentNotNull(type, cannotBeNull("type"));
        if (!type.isInterface()) {
            throw new IllegalArgumentException("type has to be an interface");
        }
        final Lazy<T> lazy = Lazy.of(supplier);
        final InvocationHandler handler = (proxy, method, args) -> method.invoke(lazy.get(), args);
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, handler);
    }

    public Option<T> filter(@NotNull Predicate<? super T> predicate) {
        final T v = get();
        return predicate.test(v) ? Option.some(v) : Option.none();
    }

    /**
     * Evaluates this lazy value and caches it, when called the first time.
     * On subsequent calls, returns the cached value.
     *
     * @return the lazy evaluated value
     */
    @Override
    public T get() {
        //noinspection VariableNotUsedInsideIf
        return (null == supplier) ? value : computeValue();
    }
    
    private T computeValue() {
        lock.lock();
        try {
            final Supplier<? extends T> s = supplier;
            if (null != s) {
                value = s.get();
                supplier = null;
            }
        } finally {
            lock.unlock();
        }
        return value;
    }

    /**
     * A {@code Lazy}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Checks, if this lazy value is evaluated.
     * <p>
     * Note: A value is internally evaluated (once) by calling {@link #get()}.
     *
     * @return true, if the value is evaluated, false otherwise.
     * @throws UnsupportedOperationException if this value is undefined
     */
    public boolean isEvaluated() {
        return null == supplier;
    }

    /**
     * A {@code Lazy}'s value is computed lazily.
     *
     * @return true
     */
    @Override
    public boolean isLazy() {
        return true;
    }

    @Override
    public boolean isSingleValued() {
        return true;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return Iterator.of(get());
    }

    @Override
    public <U> @NotNull Lazy<U> map(Function<? super T, ? extends U> mapper) {
        return Lazy.of(() -> mapper.apply(get()));
    }

    @Contract("_ -> this")
    @Override
    public Lazy<T> peek(@NotNull Consumer<? super T> action) {
        action.accept(get());
        return this;
    }

    /**
     * Transforms this {@code Lazy}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws IllegalArgumentException if {@code f} is null
     */
    public <U> U transform(Function<? super Lazy<T>, ? extends U> f) {
        checkArgumentNotNull(f, cannotBeNull("f"));
        return f.apply(this);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String stringPrefix() {
        return "Lazy";
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof Lazy && Objects.equals(((Lazy<?>) o).get(), get()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(get());
    }

    @Override
    public @NotNull String toString() {
        return stringPrefix() + "(" + (!isEvaluated() ? "?" : value) + ")";
    }

    /**
     * Ensures that the value is evaluated before serialization.
     *
     * @param s An object serialization stream.
     * @throws IOException If an error occurs writing to the stream.
     */
    @Serial
    private void writeObject(@NotNull ObjectOutputStream s) throws IOException {
        get(); // evaluates the lazy value if it isn't evaluated yet!
        s.defaultWriteObject();
    }
}
