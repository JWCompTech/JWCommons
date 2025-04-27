package com.jwcomptech.shared.functions.checked;

import org.jetbrains.annotations.Contract;

/**
 * A {@linkplain Runnable} which may throw.
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
