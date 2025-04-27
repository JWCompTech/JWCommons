package com.jwcomptech.shared.tuples;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;


@SuppressWarnings("unused")
public class ImmutablePair<L, R> extends Pair<L, R> {
    public static final ImmutablePair<?, ?>[] EMPTY_ARRAY = new ImmutablePair[0];
    @SuppressWarnings("rawtypes")
    private static final ImmutablePair NULL = new ImmutablePair<>(null, null);
    @Serial
    private static final long serialVersionUID = 4954918890077093841L;
    public final L left;
    public final R right;

    @SuppressWarnings("unchecked")
    public static <L, R> ImmutablePair<L, R>[] emptyArray() {
        return (ImmutablePair<L, R>[]) EMPTY_ARRAY;
    }

    public static <L, R> Pair<L, R> left(L left) {
        return of(left, null);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> ImmutablePair<L, R> nullPair() {
        return NULL;
    }

    public static <L, R> ImmutablePair<L, R> of(L left, R right) {
        return null == left && null == right ? nullPair() : new ImmutablePair<>(left, right);
    }

    public static <L, R> ImmutablePair<L, R> of(Map.Entry<L, R> pair) {
        return null != pair ? new ImmutablePair<>(pair.getKey(), pair.getValue()) : nullPair();
    }

    public static <L, R> ImmutablePair<L, R> ofNonNull(L left, R right) {
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(right, "right"));
    }

    public static <L, R> Pair<L, R> right(R right) {
        return of(null, right);
    }

    public ImmutablePair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return this.left;
    }

    public R getRight() {
        return this.right;
    }

    public R setValue(R value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        ImmutablePair<?, ?> that = (ImmutablePair<?, ?>) o;

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
