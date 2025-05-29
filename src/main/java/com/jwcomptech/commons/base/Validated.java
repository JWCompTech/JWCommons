package com.jwcomptech.commons.base;

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

import com.jwcomptech.commons.Condition;
import lombok.Data;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNull;

/**
 * A base object to easily add validations to any class.
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Data
public class Validated implements Serializable {
    /**
     * The result of all validations.
     */
    private boolean valid;
    /**
     * Is true if the evaluation has been run.
     */
    private boolean alreadyValidated;
    /**
     * The list of true validations
     * -- GETTER --
     *  Returns the list of true validations.
     *
     */
    @Getter
    private final List<Condition> trueValidations;
    /**
     * The list of false validations
     * -- GETTER --
     *  Returns the list of false validations.
     *
     */
    @Getter
    private final List<Condition> falseValidations;

    /**
     * Creates a new empty instance.
     */
    public Validated() {
        valid = false;
        alreadyValidated = false;
        trueValidations = new ArrayList<>();
        falseValidations = new ArrayList<>();
    }

    /**
     * Required for serialization support.
     *
     * @see Serializable
     */
    @Serial
    private static final long serialVersionUID = 4520181131454855130L;

    /**
     * Evaluates all conditions and saves the result;
     * @return this instance
     */
    public Validated evaluateAll() {
        boolean failure = false;

        for(Condition condition : trueValidations) {
            if(condition.evaluate().isResultFalse()) {
                failure = true;
                break;
            }
        }

        if(!failure) {
            for (Condition condition : falseValidations) {
                if (condition.evaluate().isResultTrue()) {
                    failure = true;
                    break;
                }
            }
        }

        if(!failure) valid = true;

        alreadyValidated = true;

        return this;
    }

    /**
     * Returns the result of all validations and runs {@link #evaluateAll()} if
     * it has yet to be run.
     * @return the result of all validations
     */
    public boolean isValid() {
        if(!alreadyValidated) evaluateAll();
        return valid;
    }

    /**
     * Runs the specified runnable if the evaluation result is valid.
     * @param runnable the runnable to run
     * @return this instance
     * @throws IllegalArgumentException if runnable is null
     */
    public Validated ifValid(final Runnable runnable) {
        checkArgumentNotNull(runnable, "Runnable cannot be null or empty!");
        if(isValid()) runnable.run();
        return this;
    }

    /**
     * Runs the specified runnable if the evaluation result is invalid.
     * @param runnable the runnable to run
     * @return this instance
     * @throws IllegalArgumentException if runnable is null
     */
    public Validated ifInvalid(final Runnable runnable) {
        checkArgumentNotNull(runnable, "Runnable cannot be null or empty!");
        if(!isValid()) runnable.run();
        return this;
    }

    /**
     * Adds the specified condition(s) to the true list.
     * @param conditions one or more conditions to add
     * @return this instance
     */
    public Validated addToTrue(Condition... conditions) {
        this.trueValidations.addAll(List.of(conditions));
        return this;
    }

    /**
     * Adds the specified condition(s) to the false list.
     * @param conditions one or more conditions to add
     * @return this instance
     */
    public Validated addToFalse(Condition... conditions) {
        this.falseValidations.addAll(List.of(conditions));
        return this;
    }
}
