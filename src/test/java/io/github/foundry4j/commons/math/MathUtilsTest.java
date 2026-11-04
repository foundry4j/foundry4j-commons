package io.github.foundry4j.commons.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MathUtilsTest {

    @Test
    void sumShouldReturnTotal() {
        assertEquals(10L, MathUtils.sum(1, 2, 3, 4));
    }

    @Test
    void sumShouldReturnZeroForEmptyArray() {
        assertEquals(0L, MathUtils.sum());
    }

    @Test
    void averageShouldReturnCorrectAverage() {
        assertEquals(2.0, MathUtils.average(1, 2, 3));
    }

    @Test
    void averageShouldThrowWhenArrayIsEmpty() {
        assertThrows(IllegalArgumentException.class, MathUtils::average);
    }

    @Test
    void maxShouldReturnMaximum() {
        assertEquals(5, MathUtils.max(1, 5, 3));
    }

    @Test
    void minShouldReturnMinimum() {
        assertEquals(1, MathUtils.min(3, 1, 5));
    }

    @Test
    void maxShouldThrowWhenArrayIsEmpty() {
        assertThrows(IllegalArgumentException.class, MathUtils::max);
    }

    @Test
    void minShouldThrowWhenArrayIsEmpty() {
        assertThrows(IllegalArgumentException.class, MathUtils::min);
    }
}