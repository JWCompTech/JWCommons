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

import java.util.function.Predicate;

/**
 * A {@linkplain Predicate} which may throw.
 *
 * @param <T> the type of the input to the predicate
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface CheckedPredicate<T> {

    /**
     * Creates a {@code CheckedPredicate}.
     *
     * <pre>{@code
     * final CheckedPredicate<Boolean> checkedPredicate = CheckedPredicate.of(Boolean::booleanValue);
     * final Predicate<Boolean> predicate = checkedPredicate.unchecked();
     *
     * // = true
     * predicate.test(Boolean.TRUE);
     *
     * // throws
     * predicate.test(null);
     * }</pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <T> type of values that are tested by the predicate
     * @return a new {@code CheckedPredicate}
     * @see CheckedFunction1#of(CheckedFunction1)
     */
    static <T> CheckedPredicate<T> of(final CheckedPredicate<T> methodReference) {
        return methodReference;
    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
     * @throws Throwable if an error occurs
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean test(T t) throws Throwable;

    /**
     * Negates this predicate.
     *
     * @return A new CheckedPredicate.
     */
    default CheckedPredicate<T> negate() {
        return t -> !test(t);
    }

    /**
     * Returns an unchecked {@link Predicate} that will <em>sneaky throw</em> if an exceptions occurs when testing a value.
     *
     * @return a new {@link Predicate} that throws a {@code Throwable}.
     */
    default Predicate<T> unchecked() {
        return t -> {
            try {
                return test(t);
            } catch(final Throwable x) {
                return CheckedPredicateModule.sneakyThrow(x);
            }
        };
    }
}

@SuppressWarnings("ClassNameDiffersFromFileName")
interface CheckedPredicateModule {

    // DEV-NOTE: we do not plan to expose this as public API
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(final Throwable t) throws T {
        throw (T) t;
    }

}
