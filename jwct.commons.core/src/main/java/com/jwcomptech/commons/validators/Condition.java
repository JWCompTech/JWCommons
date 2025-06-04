package com.jwcomptech.commons.validators;

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

import com.jwcomptech.commons.functions.Function1;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.utils.DateTimeUtils.waitTime;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * An object that can be used to lazily evaluate boolean expressions.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Condition {
    /**
     * The evaluation to evaluate.
     */
    private Supplier<Boolean> evaluation;
    /**
     * The result of the evaluation.
     */
    private boolean result;
    /**
     * Is true if the evaluation has been run.
     */
    private boolean evaluated;

    /**
     * Creates a new Condition instance with the specified evaluation.
     *
     * @param evaluation a boolean supplier that provides the evaluation condition
     * @return a new Condition instance
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Condition of(final Supplier<Boolean> evaluation) {
        return new Condition(evaluation);
    }

    private Condition(final Supplier<Boolean> evaluation) {
        this.evaluation = evaluation;
    }

    /**
     * Returns true if the result evaluated to true.
     *
     * @return true if the result evaluated to true, false otherwise
     */
    public boolean hasEvaluatedTrue() {
        return result;
    }

    /**
     * Returns true if the result evaluated to false.
     *
     * @return true if the result evaluated to false, true otherwise
     */
    public boolean hasEvaluatedFalse() {
        return !result;
    }

    /**
     * Returns the result of the evaluation.
     *
     * @return the result of the evaluation
     */
    public boolean getResult() {
        return result;
    }

    /**
     * Returns the evaluation status.
     *
     * @return true if condition has been evaluated, false otherwise
     */
    public boolean hasBeenEvaluated() {
        return evaluated;
    }

    /**
     * Evaluates the condition and stores the result.
     *
     * @apiNote Subsequent calls do not reevaluate the condition.
     * To do this call {@link Condition#reevaluate()} instead.
     *
     * @return this instance
     */
    public Condition evaluate() {
        checkArgumentNotNull(evaluation, cannotBeNull("evaluation"));
        if(!evaluated) {
            evaluated = true;
            result = evaluation.get();
        }

        return this;
    }

    /**
     * Reevaluates the condition and stores the result.
     *
     * @return this instance
     */
    public Condition reevaluate() {
        checkArgumentNotNull(evaluation, cannotBeNull("evaluation"));
        evaluated = true;
        result = evaluation.get();

        return this;
    }

    /**
     * Does a full reset on the evaluation result.
     */
    public void reset() {
        result = false;
        evaluated = false;
    }

    /**
     * If this condition evaluated to true, runs the specified runnable.
     *
     * @param action the runnable to run if evaluation result is true
     * @throws IllegalStateException if the condition has not yet been evaluated
     * @throws IllegalArgumentException if action is null
     */
    public void ifEvalTrue(@NotNull final Runnable action) {
        checkArgumentNotNull(action, cannotBeNull("action"));
        if(!evaluated) throw new IllegalStateException("Evaluation not run");
        if(result) action.run();
    }

    /**
     * If this condition evaluated to true, applies the specified reference
     * to the specified transform function.
     *
     * @param <T> the type of the reference to transform
     * @param <R> the return type of the transform function
     * @param reference the object to transform if the condition evaluated to true
     * @param transformFunction the function to apply reference to
     *                          if condition evaluated to true
     * @return the result of the function if the condition evaluated to true,
     * otherwise the original reference
     * @throws IllegalStateException if the condition has not yet been evaluated
     * @throws IllegalArgumentException if reference or transformFunction is null
     */
    public <T, R> R ifEvalTrueTransform(final T reference,
                                        final Function1<T, R> transformFunction) {
        checkArgumentNotNull(reference, cannotBeNull("reference"));
        checkArgumentNotNull(transformFunction, cannotBeNull("transformFunction"));
        if(!evaluated) throw new IllegalStateException("Evaluation not run");
        if(result) return transformFunction.apply(reference);

        // T will always equal R because reference wasn't modified
        //noinspection unchecked
        return (R) reference;
    }

    /**
     * If this condition evaluated to false, runs the specified runnable.
     *
     * @param action the runnable to run if evaluation result is false
     * @throws IllegalStateException if the condition has not yet been evaluated
     * @throws IllegalArgumentException if action is null
     */
    public void ifEvalFalse(@NotNull final Runnable action) {
        checkArgumentNotNull(action, cannotBeNull("action"));
        if(!evaluated) throw new IllegalStateException("Evaluation not run");
        if(!result) action.run();
    }

    /**
     * If this condition evaluated to false, applies the specified reference
     * to the specified transform function.
     *
     * @param <T> the type of the reference to transform
     * @param <R> the return type of the transform function
     * @param reference the object to transform if the condition evaluated to false
     * @param transformFunction the function to apply reference to
     *                          if condition evaluated to false
     * @return the result of the function if the condition evaluated to false,
     * otherwise the original reference
     * @throws IllegalStateException if the condition has not yet been evaluated
     * @throws IllegalArgumentException if reference or transformFunction is null
     */
    public <T, R> R ifEvalFalseTransform(final T reference,
                                         final Function1<T, R> transformFunction) {
        checkArgumentNotNull(reference, cannotBeNull("reference"));
        checkArgumentNotNull(transformFunction, cannotBeNull("transformFunction"));
        if(!evaluated) throw new IllegalStateException("Evaluation not run");
        if(!result) return transformFunction.apply(reference);

        // T will always equal R because reference wasn't modified
        //noinspection unchecked
        return (R) reference;
    }

    /**
     * Blocks the current thread until the condition evaluates to true.
     */
    public void waitTillEvalTrue() {
        while(hasEvaluatedFalse()) {
            waitTime(TimeUnit.MILLISECONDS, 100);
            evaluate();
        }
    }

    /**
     * Blocks the current thread until the condition evaluates to true.
     *
     * @param interval the time to wait
     * @param unit the unit of time to wait
     */
    public void waitTillEvalTrue(final long interval, final TimeUnit unit) {
        checkArgumentNotNull(interval, cannotBeNull("interval"));
        checkArgumentNotNull(unit, cannotBeNull("unit"));
        while(hasEvaluatedFalse()) {
            waitTime(unit, interval);
            evaluate();
        }
    }

    /**
     * Blocks the current thread until the condition evaluates to true
     * and quits if the specified timeout is reached.
     *
     * @param timeout the timeout interval
     * @param unit the unit of time to wait
     * @return true if the timeout wasn't reached, false otherwise
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean waitTillEvalTrueWithTimeout(final long timeout, final TimeUnit unit) {
        checkArgumentNotNull(timeout, cannotBeNull("interval"));
        checkArgumentNotNull(unit, cannotBeNull("unit"));

        final long start = System.nanoTime();
        final long timeoutNs = unit.toNanos(timeout);
        while(hasEvaluatedTrue()) {
            if(System.nanoTime() - start >= timeoutNs) return false;
            waitTime(TimeUnit.MILLISECONDS, 100);
            evaluate();
        }

        return true;
    }

    /**
     * Blocks the current thread until the condition evaluates to false.
     */
    public void waitTillEvalFalse() {
        while(hasEvaluatedTrue()) {
            waitTime(TimeUnit.MILLISECONDS, 100);
            evaluate();
        }
    }

    /**
     * Blocks the current thread until the condition evaluates to false.
     *
     * @param interval the time to wait
     * @param unit the unit of time to wait
     */
    public void waitTillEvalFalse(final long interval, final TimeUnit unit) {
        checkArgumentNotNull(interval, cannotBeNull("interval"));
        checkArgumentNotNull(unit, cannotBeNull("unit"));
        while(hasEvaluatedTrue()) {
            waitTime(unit, interval);
            evaluate();
        }
    }

    /**
     * Blocks the current thread until the condition evaluates to false
     * and quits if the specified timeout is reached.
     *
     * @param timeout the timeout interval
     * @param unit the unit of time to wait
     * @return true if the timeout wasn't reached, false otherwise
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean waitTillEvalFalseWithTimeout(final long timeout, final TimeUnit unit) {
        checkArgumentNotNull(timeout, cannotBeNull("interval"));
        checkArgumentNotNull(unit, cannotBeNull("unit"));

        final long start = System.nanoTime();
        final long timeoutNs = unit.toNanos(timeout);
        while(hasEvaluatedFalse()) {
            if(System.nanoTime() - start >= timeoutNs) return false;
            waitTime(TimeUnit.MILLISECONDS, 100);
            evaluate();
        }

        return true;
    }
}
