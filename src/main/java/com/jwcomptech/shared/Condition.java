package com.jwcomptech.shared;

import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static com.jwcomptech.shared.Literals.cannotBeNull;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;

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
    private boolean alreadyValidated;

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Condition of(Supplier<Boolean> evaluation) {
        return new Condition(evaluation);
    }

    private Condition(Supplier<Boolean> evaluation) {
        this.evaluation = evaluation;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    public boolean isResultTrue() {
        return result;
    }

    public boolean isResultFalse() {
        return !result;
    }

    public boolean getResult() {
        return result;
    }

    public boolean beenEvaluated() {
        return alreadyValidated;
    }

    public Condition evaluate() {
        checkArgumentNotNull(evaluation, cannotBeNull("evaluation"));
        if(!alreadyValidated) {
            alreadyValidated = true;
            result = evaluation.get();
        }

        return this;
    }
}
