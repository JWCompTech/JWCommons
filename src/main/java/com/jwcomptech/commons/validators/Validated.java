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

import lombok.Data;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * A base object to easily add validations to any class.
 * @since 1.0.0-alpha
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Data
public abstract class Validated implements Serializable {
    /**
     * The result of all validations.
     */
    private boolean valid;
    /**
     * Is true if the evaluation has been run.
     */
    private boolean validated;
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
     * The failure message of the first failed validation.
     */
    private String validationFailureMessage;

    /**
     * Creates a new empty instance.
     */
    public Validated() {
        valid = false;
        validated = false;
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
     * Evaluates all conditions and saves the result, but if already validated
     * does nothing. If there are no validations in either list then evaluation
     * will always return true.
     *
     * @return this instance
     */
    public Validated evaluateAllVals() {
        if(!validated) {
            boolean failure = false;

            for (final Condition condition : trueValidations) {
                if (condition.evaluate().hasEvaluatedFalse()) {
                    failure = true;
                    validationFailureMessage = condition.getFailureMessage();
                    break;
                }
            }

            if (!failure) {
                for (final Condition condition : falseValidations) {
                    if (condition.evaluate().hasEvaluatedTrue()) {
                        failure = true;
                        validationFailureMessage = condition.getFailureMessage();
                        break;
                    }
                }
            }

            if (!failure) {
                valid = true;
                validationFailureMessage = null;
            }

            validated = true;
        }

        return this;
    }

    /**
     * Evaluates all conditions and saves the result
     * even if validation has already been run.
     *
     * @return this instance
     */
    public Validated reevaluateAllVals() {
        validated = false;
        evaluateAllVals();
        return this;
    }

    /**
     * Returns the failure message of the first failed validation.
     *
     * @return the failure message of the first failed validation
     */
    public Optional<String> getFirstValFailMessage() {
        return Optional.ofNullable(validationFailureMessage);
    }

    /**
     * Returns the result of all validations and runs {@link #evaluateAllVals()} if
     * it has yet to be run.
     *
     * @return the result of all validations
     */
    public boolean isValid() {
        if(!validated) evaluateAllVals();
        return valid;
    }

    /**
     * Runs the specified runnable if the evaluation result is valid.
     *
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
     * Returns failure message of the first failed validation
     * if running reevaluateAll() results in a failure.
     *
     * @return failure message of the first failed validation
     * if running reevaluateAll() results in a failure
     */
    public Optional<String> reevaluateAllAndGetFailMessage() {
        reevaluateAllVals();
        return valid ? Optional.ofNullable(validationFailureMessage) : Optional.empty();
    }

    /**
     * Runs the specified runnable if the evaluation result is invalid.
     *
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
     *
     * @param conditions one or more conditions to add
     * @return this instance
     */
    public Validated addToTrueVals(final Condition... conditions) {
        this.trueValidations.addAll(List.of(conditions));
        return this;
    }

    /**
     * Adds the specified condition(s) to the false list.
     *
     * @param conditions one or more conditions to add
     * @return this instance
     */
    public Validated addToFalseVals(final Condition... conditions) {
        this.falseValidations.addAll(List.of(conditions));
        return this;
    }

    public Condition isValidCondition() {
        return Condition.of(this::isValid);
    }

    public Validated resetVal() {
        valid = false;
        validated = false;

        return this;
    }
}
