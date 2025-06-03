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

import java.util.function.Consumer;

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNull;

/**
 * A consumer that may throw, equivalent to {@linkplain Consumer}.
 *
 * @param <T> the value type supplied to this consumer.
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface CheckedConsumer<T> {

    /**
     * Creates a {@code CheckedConsumer}.
     *
     * <pre>{@code
     * final CheckedConsumer<Value> checkedConsumer = CheckedConsumer.of(Value::stdout);
     * final Consumer<Value> consumer = checkedConsumer.unchecked();
     *
     * // prints "Hi" on the console
     * consumer.accept(CharSeq.of("Hi!"));
     *
     * // throws
     * consumer.accept(null);
     * }</pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <T> type of values that are accepted by the consumer
     * @return a new {@code CheckedConsumer}
     * @see CheckedFunction1#of(CheckedFunction1)
     */
    static <T> CheckedConsumer<T> of(final CheckedConsumer<T> methodReference) {
        return methodReference;
    }

    /**
     * Performs side-effects.
     *
     * @param t a value of type {@code T}
     * @throws Throwable if an error occurs
     */
    void accept(T t) throws Throwable;

    /**
     * Returns a chained {@code CheckedConsumer} that first executes {@code this.accept(t)}
     * and then {@code after.accept(t)}, for a given {@code t} of type {@code T}.
     *
     * @param after the action that will be executed after this action
     * @return a new {@code CheckedConsumer} that chains {@code this} and {@code after}
     * @throws IllegalArgumentException if {@code after} is null
     */
    default CheckedConsumer<T> andThen(final CheckedConsumer<? super T> after) {
        checkArgumentNotNull(after, cannotBeNull("after"));
        return (final T t) -> { accept(t); after.accept(t); };
    }

    /**
     * Returns an unchecked {@link Consumer} that will <em>sneaky throw</em> if an exceptions occurs when accepting a value.
     *
     * @return a new {@link Consumer} that throws a {@code Throwable}.
     */
    default Consumer<T> unchecked() {
        return t -> {
            try {
                accept(t);
            } catch(final Throwable x) {
                CheckedConsumerModule.sneakyThrow(x);
            }
        };
    }
}

@SuppressWarnings("ClassNameDiffersFromFileName")
interface CheckedConsumerModule {

    // DEV-NOTE: we do not plan to expose this as public API
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    static <T extends Throwable, R> R sneakyThrow(final Throwable t) throws T {
        throw (T) t;
    }

}
