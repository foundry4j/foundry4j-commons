package io.github.tuyucheng777.commons.stream;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StreamUtilsTest {

    @Test
    void filterToListShouldKeepMatchedElements() {
        List<Integer> result = StreamUtils.filterToList(Stream.of(1, 2, 3, 4), value -> value % 2 == 0);

        assertEquals(List.of(2, 4), result);
    }

    @Test
    void mapToListShouldTransformElements() {
        List<String> result = StreamUtils.mapToList(Stream.of("a", "b"), String::toUpperCase);

        assertEquals(List.of("A", "B"), result);
    }

    @Test
    void distinctToListShouldRemoveDuplicates() {
        List<Integer> result = StreamUtils.distinctToList(Stream.of(1, 1, 2, 3, 3));

        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void distinctToSetShouldRemoveDuplicates() {
        Set<Integer> result = StreamUtils.distinctToSet(Stream.of(1, 1, 2, 3));

        assertEquals(Set.of(1, 2, 3), result);
    }

    @Test
    void filterToListShouldThrowWhenStreamIsNull() {
        assertThrows(NullPointerException.class, () -> StreamUtils.filterToList(null, value -> true));
    }
}