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
    private boolean validated;

    /**
     * Creates a new Condition instance with the specified evaluation.
     * @param evaluation a boolean supplier that provides the evaluation condition
     * @return a new Condition instance
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Condition of(Supplier<Boolean> evaluation) {
        return new Condition(evaluation);
    }

    private Condition(Supplier<Boolean> evaluation) {
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
    public boolean getResult() {
        return result;
    }

    /**
     * Evaluates the condition and stores the result.
     * @return this instance
     */
    public Condition evaluate() {
        checkArgumentNotNull(evaluation, cannotBeNull("evaluation"));
        if(!validated) {
            validated = true;
            result = evaluation.get();
        }

        return this;
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
    public void waitTillFalse() {
        while(isResultTrue()) {
            waitTime(TimeUnit.MILLISECONDS, 100);
            evaluate();
        }
    }
}
