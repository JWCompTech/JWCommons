package com.jwcomptech.shared.tuples;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.util.Objects;

@SuppressWarnings("unused")
public class ImmutableTriple<L, M, R> extends Triple<L, M, R> {
    public static final ImmutableTriple<?, ?, ?>[] EMPTY_ARRAY = new ImmutableTriple[0];
    @SuppressWarnings("rawtypes")
    private static final ImmutableTriple NULL = new ImmutableTriple<>(null, null, null);
    @Serial
    private static final long serialVersionUID = 1L;
    public final L left;
    public final M middle;
    public final R right;

    @SuppressWarnings("unchecked")
    public static <L, M, R> ImmutableTriple<L, M, R>[] emptyArray() {
        return (ImmutableTriple<L, M, R>[]) EMPTY_ARRAY;
    }

    @SuppressWarnings("unchecked")
    public static <L, M, R> ImmutableTriple<L, M, R> nullTriple() {
        return NULL;
    }

    public static <L, M, R> ImmutableTriple<L, M, R> of(L left, M middle, R right) {
        return !(null != left || null != middle) && null == right
                ? nullTriple()
                : new ImmutableTriple<>(left, middle, right);
    }

    public static <L, M, R> ImmutableTriple<L, M, R> ofNonNull(L left, M middle, R right) {
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(middle, "middle"),
                Objects.requireNonNull(right, "right"));
    }

    public ImmutableTriple(L left, M middle, R right) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        ImmutableTriple<?, ?, ?> that = (ImmutableTriple<?, ?, ?>) o;

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
