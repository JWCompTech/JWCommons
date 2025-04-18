package com.jwcomptech.shared;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static com.jwcomptech.shared.Literals.cannotBeNull;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;

public final class Condition {
    private Supplier<Boolean> evaluation;
    private boolean result;
    private boolean beenEvaluated;

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Condition of(Supplier<Boolean> evaluation) {
        return new Condition(evaluation);
    }

    private Condition(Supplier<Boolean> evaluation) {
        this.evaluation = evaluation;
    }

    public Supplier<Boolean> getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Supplier<Boolean> evaluation) {
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
        return beenEvaluated;
    }

    public Condition evaluate() {
        checkArgumentNotNull(evaluation, cannotBeNull("evaluation"));
        if(!beenEvaluated) {
            beenEvaluated = true;
            result = evaluation.get();
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        Condition condition = (Condition) o;

        return new EqualsBuilder()
                .append(result, condition.result)
                .append(beenEvaluated, condition.beenEvaluated)
                .append(evaluation, condition.evaluation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(evaluation)
                .append(result)
                .append(beenEvaluated)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("evaluation", evaluation)
                .append("result", result)
                .append("beenEvaluated", beenEvaluated)
                .toString();
    }
}
