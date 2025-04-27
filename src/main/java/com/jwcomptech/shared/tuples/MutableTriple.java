package com.jwcomptech.shared.tuples;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.util.Objects;

@SuppressWarnings("unused")
public class MutableTriple<L, M, R> extends Triple<L, M, R> {
    public static final MutableTriple<?, ?, ?>[] EMPTY_ARRAY = new MutableTriple[0];
    @Serial
    private static final long serialVersionUID = 1L;
    public L left;
    public M middle;
    public R right;

    @SuppressWarnings("unchecked")
    public static <L, M, R> MutableTriple<L, M, R>[] emptyArray() {
        return (MutableTriple<L, M, R>[]) EMPTY_ARRAY;
    }

    public static <L, M, R> MutableTriple<L, M, R> of(L left, M middle, R right) {
        return new MutableTriple<>(left, middle, right);
    }

    public static <L, M, R> MutableTriple<L, M, R> ofNonNull(L left, M middle, R right) {
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(middle, "middle"),
                Objects.requireNonNull(right, "right"));
    }

    public MutableTriple() {
    }

    public MutableTriple(L left, M middle, R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    public L getLeft() {
        return this.left;
    }

    public M getMiddle() {
        return this.middle;
    }

    public R getRight() {
        return this.right;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public void setMiddle(M middle) {
        this.middle = middle;
    }

    public void setRight(R right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        MutableTriple<?, ?, ?> that = (MutableTriple<?, ?, ?>) o;

        return new EqualsBuilder().appendSuper(super.equals(o))
                .append(left, that.left)
                .append(middle, that.middle)
                .append(right, that.right)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(left)
                .append(middle)
                .append(right)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("left", left)
                .append("middle", middle)
                .append("right", right)
                .toString();
    }
}
