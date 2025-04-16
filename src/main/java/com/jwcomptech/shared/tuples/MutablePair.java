package com.jwcomptech.shared.tuples;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

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
        if (pair != null) {
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
        R result = this.getRight();
        this.setRight(value);
        return result;
    }
}
