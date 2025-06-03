package com.jwcomptech.commons;

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
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.utils.DateTimeUtils.waitTime;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNull;

/**
 * An object that can be used to lazily evaluate boolean expressions.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
     * @return true if the result evaluated to true
     */
    @SuppressWarnings("SuspiciousGetterSetter")
    public boolean isResultTrue() {
        return result;
    }

    /**
     * Returns true if the result evaluated to false.
     * @return true if the result evaluated to false
     */
    public boolean isResultFalse() {
        return !result;
    }

    /**
     * Returns the result of the evaluation.
     * @return the result of the evaluation
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean getResult() {
        return result;
    }

    /**
     * Evaluates the condition and stores the result.
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
     * @return this instance
     */
    public Condition reevaluate() {
        checkArgumentNotNull(evaluation, cannotBeNull("evaluation"));
        evaluated = true;
        result = evaluation.get();

        return this;
    }

    /**
     * If this condition evaluated to true, runs the specified runnable
     * @param action the runnable to run if evaluation result is true
     * @throws IllegalStateException if the condition has not yet been evaluated
     * @throws IllegalArgumentException if action is null
     */
    public void ifTrue(@NotNull final Runnable action) {
        checkArgumentNotNull(action, cannotBeNull("action"));
        if(!evaluated) throw new IllegalStateException("Evaluation not run");
        if(result) action.run();
    }

    /**
     * If this condition evaluated to true, applies the specified reference
     * to the specified transform function.
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
    public <T, R> R ifTrueTransform(final T reference,
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
     * If this condition evaluated to false, runs the specified runnable
     * @param action the runnable to run if evaluation result is false
     * @throws IllegalStateException if the condition has not yet been evaluated
     * @throws IllegalArgumentException if action is null
     */
    public void ifFalse(@NotNull final Runnable action) {
        checkArgumentNotNull(action, cannotBeNull("action"));
        if(!evaluated) throw new IllegalStateException("Evaluation not run");
        if(!result) action.run();
    }

    /**
     * If this condition evaluated to false, applies the specified reference
     * to the specified transform function.
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
    public <T, R> R ifFalseTransform(final T reference,
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
    public void waitTillTrue() {
        while(isResultFalse()) {
            waitTime(TimeUnit.MILLISECONDS, 100);
            evaluate();
        }
    }

    /**
     * Blocks the current thread until the condition evaluates to false.
     */
    @SuppressWarnings("CallToSimpleGetterFromWithinClass")
    public void waitTillFalse() {
        while(isResultTrue()) {
            waitTime(TimeUnit.MILLISECONDS, 100);
            evaluate();
        }
    }
}
