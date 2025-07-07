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

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class RangeTest {

    @Test
    void closedRange_shouldIncludeBounds() {
        final Range<Integer> range = Range.closed(5, 10);
        assertThat(range.contains(5)).isTrue();
        assertThat(range.contains(10)).isTrue();
        assertThat(range.contains(7)).isTrue();
        assertThat(range.contains(4)).isFalse();
        assertThat(range.contains(11)).isFalse();
    }

    @Test
    void openRange_shouldExcludeBounds() {
        final Range<Integer> range = Range.open(5, 10);
        assertThat(range.contains(5)).isFalse();
        assertThat(range.contains(10)).isFalse();
        assertThat(range.contains(6)).isTrue();
    }

    @Test
    void closedOpenRange_shouldIncludeLowerAndExcludeUpper() {
        final Range<Integer> range = Range.closedOpen(5, 10);
        assertThat(range.contains(5)).isTrue();
        assertThat(range.contains(10)).isFalse();
        assertThat(range.contains(7)).isTrue();
    }

    @Test
    void openClosedRange_shouldExcludeLowerAndIncludeUpper() {
        final Range<Integer> range = Range.openClosed(5, 10);
        assertThat(range.contains(5)).isFalse();
        assertThat(range.contains(10)).isTrue();
        assertThat(range.contains(7)).isTrue();
    }

    @Test
    void create_shouldThrowIfLowerGreaterThanUpper() {
        assertThatThrownBy(() -> Range.closed(10, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound cannot be greater than upper bound");
    }

    @Test
    void contains_shouldThrowOnNullValue() {
        final Range<Integer> range = Range.closed(1, 10);
        assertThatThrownBy(() -> range.contains(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Value");
    }

    @Test
    void containsAll_shouldReturnTrueWhenAllContained() {
        final Range<Integer> range = Range.closed(1, 10);
        final List<Integer> values = List.of(1, 5, 10);
        assertThat(range.containsAll(values)).isTrue();
    }

    @Test
    void containsAll_shouldReturnFalseWhenSomeNotContained() {
        final Range<Integer> range = Range.closed(1, 10);
        final List<Integer> values = List.of(1, 5, 15);
        assertThat(range.containsAll(values)).isFalse();
    }

    @Test
    void containsNone_shouldReturnTrueWhenNoneContained() {
        final Range<Integer> range = Range.closed(1, 10);
        final List<Integer> values = List.of(11, 20, -5);
        assertThat(range.containsNone(values)).isTrue();
    }

    @Test
    void containsNone_shouldReturnFalseWhenSomeContained() {
        final Range<Integer> range = Range.closed(1, 10);
        final List<Integer> values = List.of(1, 20, 30);
        assertThat(range.containsNone(values)).isFalse();
    }

    @Test
    void factoryMethodsShouldProduceCorrectInclusivity() {
        final Range<Integer> open = Range.open(1, 5);
        assertThat(open.isLowerInclusive()).isFalse();
        assertThat(open.isUpperInclusive()).isFalse();

        final Range<Integer> closed = Range.closed(1, 5);
        assertThat(closed.isLowerInclusive()).isTrue();
        assertThat(closed.isUpperInclusive()).isTrue();

        final Range<Integer> closedOpen = Range.closedOpen(1, 5);
        assertThat(closedOpen.isLowerInclusive()).isTrue();
        assertThat(closedOpen.isUpperInclusive()).isFalse();

        final Range<Integer> openClosed = Range.openClosed(1, 5);
        assertThat(openClosed.isLowerInclusive()).isFalse();
        assertThat(openClosed.isUpperInclusive()).isTrue();
    }
}

