package com.jwcomptech.shared.tuples;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class MutablePair<L, R> extends Pair<L, R> {
    public static final MutablePair<?, ?>[] EMPTY_ARRAY = new MutablePair[0];
    @Serial
    private static final long serialVersionUID = 4954918890077093841L;
    public L left;
    public R right;

    @SuppressWarnings("unchecked")
    public static <L, R> MutablePair<L, R>[] emptyArray() {
        return (MutablePair<L, R>[]) EMPTY_ARRAY;
    }

    public static <L, R> MutablePair<L, R> of(L left, R right) {
        return new MutablePair<>(left, right);
    }

    public static <L, R> MutablePair<L, R> of(Map.Entry<L, R> pair) {
        L left;
        R right;
        if (null != pair) {
            left = pair.getKey();
            right = pair.getValue();
        } else {
            left = null;
            right = null;
        }

        return new MutablePair<>(left, right);
    }

    public static <L, R> MutablePair<L, R> ofNonNull(L left, R right) {
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(right, "right"));
    }

    public MutablePair() {
    }

    public MutablePair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return this.left;
    }

    public R getRight() {
        return this.right;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public void setRight(R right) {
        this.right = right;
    }

    public R setValue(R value) {
        R result = this.right;
        this.right = value;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        MutablePair<?, ?> that = (MutablePair<?, ?>) o;

        return new EqualsBuilder().appendSuper(super.equals(o))
                .append(left, that.left)
                .append(right, that.right)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(left)
                .append(right)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("left", left)
                .append("right", right)
                .toString();
    }
}
