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

import org.jetbrains.annotations.Contract;

/**
 * A {@linkplain Runnable} which may throw.
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface CheckedRunnable {

    /**
     * Creates a {@code CheckedRunnable}.
     *
     * <pre>{@code
     * // class Evil { static void sideEffect() { ... } }
     * final CheckedRunnable checkedRunnable = CheckedRunnable.of(Evil::sideEffect);
     * final Runnable runnable = checkedRunnable.unchecked();
     *
     * // may or may not perform a side-effect while not throwing
     * runnable.run();
     *
     * // may or may not perform a side-effect while throwing
     * runnable.run();
     * }</pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @return a new {@code CheckedRunnable}
     * @see CheckedFunction1#of(CheckedFunction1)
     */
    static CheckedRunnable of(CheckedRunnable methodReference) {
        return methodReference;
    }

    /**
     * Performs side-effects.
     *
     * @throws Throwable if an error occurs
     */
    void run() throws Throwable;

    /**
     * Returns an unchecked {@link Runnable} that will <em>sneaky throw</em> if an exceptions occurs when running the unit of work.
     *
     * @return a new {@link Runnable} that throws a {@code Throwable}.
     */
    default Runnable unchecked() {
        return () -> {
            try {
                run();
            } catch(Throwable x) {
                CheckedRunnableModule.sneakyThrow(x);
            }
        };
    }
}

@SuppressWarnings("ClassNameDiffersFromFileName")
interface CheckedRunnableModule {

    // DEV-NOTE: we do not plan to expose this as public API
    @Contract(value = "_ -> fail", pure = true)
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
