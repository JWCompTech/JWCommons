package com.jwcomptech.shared.base;

import com.jwcomptech.shared.Condition;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Validated {
    private boolean valid;
    private final List<Condition> trueValidations;
    private final List<Condition> falseValidations;

    public Validated() {
        trueValidations = new ArrayList<>();
        falseValidations = new ArrayList<>();
    }

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

        return this;
    }

    public boolean isValid() {
        return valid;
    }

    public List<Condition> getTrueValidations() {
        return trueValidations;
    }

    public List<Condition> getFalseValidations() {
        return falseValidations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        Validated validated = (Validated) o;

        return new EqualsBuilder()
                .append(valid, validated.valid)
                .append(trueValidations, validated.trueValidations)
                .append(falseValidations, validated.falseValidations)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(valid)
                .append(trueValidations)
                .append(falseValidations)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("valid", valid)
                .append("trueValidations", trueValidations)
                .append("falseValidations", falseValidations)
                .toString();
    }
}
