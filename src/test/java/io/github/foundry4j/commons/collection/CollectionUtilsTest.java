package io.github.foundry4j.commons.collection;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionUtilsTest {

    @Test
    void isEmptyShouldReturnTrueForNull() {
        assertTrue(CollectionUtils.isEmpty(null));
    }

    @Test
    void isEmptyShouldReturnTrueForEmptyList() {
        assertTrue(CollectionUtils.isEmpty(List.of()));
    }

    @Test
    void isNotEmptyShouldReturnTrueForNonEmptyList() {
        assertTrue(CollectionUtils.isNotEmpty(List.of(1, 2, 3)));
    }

    @Test
    void toUnmodifiableListShouldReturnImmutableList() {
        List<Integer> list = CollectionUtils.toUnmodifiableList(List.of(1, 2, 3));

        assertEquals(3, list.size());
        assertThrows(UnsupportedOperationException.class, () -> list.add(4));
    }

    @Test
    void groupByShouldGroupByKeyExtractor() {
        List<String> values = List.of("apple", "banana", "avocado");

        Map<Character, List<String>> grouped = CollectionUtils.groupBy(values, value -> value.charAt(0));

        assertEquals(2, grouped.get('a').size());
        assertEquals(1, grouped.get('b').size());
    }

    @Test
    void partitionShouldSplitListBySize() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);

        List<List<Integer>> partitions = CollectionUtils.partition(list, 2);

        assertEquals(3, partitions.size());
        assertEquals(List.of(1, 2), partitions.get(0));
        assertEquals(List.of(3, 4), partitions.get(1));
        assertEquals(List.of(5), partitions.get(2));
    }

    @Test
    void partitionShouldThrowWhenSizeIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> CollectionUtils.partition(List.of(1), 0));
    }
}